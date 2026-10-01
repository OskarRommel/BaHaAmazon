import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// Release signing values come only from the ignored local properties file.
val releaseProperties = Properties().apply {
    val propertiesFile = rootProject.file("keystore.properties")
    if (propertiesFile.isFile) propertiesFile.inputStream().use { load(it) }
}
fun releaseSetting(property: String): String? =
    releaseProperties.getProperty(property)?.takeIf {
        it.isNotBlank() && !it.startsWith("REPLACE_WITH_")
    }

val releaseStoreFile = releaseSetting("storeFile")
val releaseStorePassword = releaseSetting("storePassword")
val releaseKeyAlias = releaseSetting("keyAlias")
val releaseKeyPassword = releaseSetting("keyPassword")
val releaseSigningReady = listOf(
    releaseStoreFile, releaseStorePassword, releaseKeyAlias, releaseKeyPassword
).all { it != null }

android {
    namespace = "org.baltimorehackspace.bahaamazon"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "org.baltimorehackspace.bahaamazon"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (releaseSigningReady) {
            create("release") {
                storeFile = rootProject.file(releaseStoreFile!!)
                storeType = "PKCS12"
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }
    buildTypes {
        release {
            if (releaseSigningReady) signingConfig = signingConfigs.getByName("release")
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
// Prevent accidentally producing an unsigned release when local credentials are absent.
val signingCredentialsAvailable = releaseSigningReady
val signingKeystoreAvailable = releaseStoreFile?.let { rootProject.file(it).isFile } == true
tasks.configureEach {
    if (name.contains("release", ignoreCase = true)) {
        val credentialsAvailable = signingCredentialsAvailable
        val keystoreAvailable = signingKeystoreAvailable
        doFirst {
            check(credentialsAvailable) {
                "Release signing requires storeFile, storePassword, keyAlias and keyPassword in the local keystore.properties file; replace all placeholders."
            }
            check(keystoreAvailable) { "Release signing keystore is unavailable." }
        }
    }
}
