# BaHaAmazon

BaHaAmazon is a small Android app I made for Baltimore Hackerspace.

The app selects a random Amazon product ASIN from a local JSON list, builds an affiliate link, opens it through Android's normal `ACTION_VIEW` handling, then exits.

## v1.0 Beta

Current beta functionality:

- Kotlin Android app
- First-run affiliate disclosure and opt-in screen
- Opt-out exits without opening Amazon
- Opt-in choice is stored locally so the disclosure is only shown once
- Random product selection from `asins.json`
- Affiliate tag is applied when the Amazon URL is generated
- Amazon links are handed off through normal Android `ACTION_VIEW` handling
- App exits immediately after handing off the link
- No background service
- No special permissions
- No account or login required
- Tested on a Samsung Galaxy S24+

## How it works

On first launch, the app displays an affiliate disclosure and gives the user two choices:

- **Opt in and continue** - stores the opt-in locally and opens a randomly selected Amazon product.
- **Exit without opting in** - closes the app without opening Amazon.

After opting in, future launches skip the disclosure screen, select a random ASIN, generate the tagged Amazon URL, hand it off to Android, and exit.

## Product list

Product ASINs are stored locally in:

`app/src/main/assets/asins.json`

The list is intended to contain hackerspace-relevant products and can be refreshed as products become unavailable or new items are added.

The current version includes a fallback ASIN if the asset cannot be read or parsed.

## Privacy and permissions

BaHaAmazon does not run a background service and does not request special Android permissions.

The affiliate opt-in state is stored locally on the device using Android `SharedPreferences`.

## Current status

**v1.0 beta** is functional and has passed clean-install testing on physical hardware.
