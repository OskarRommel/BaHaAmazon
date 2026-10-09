# BaHaAmazon

BaHaAmazon is a small Android app I made for Baltimore Hackerspace.

Open it, tap **SEND IT!**, and check out a random Amazon product from the gear list. Purchases through the link may support the space at no extra cost to you.

As an Amazon Associate, BaHa earns from qualifying purchases.

## v1.2 Beta

The big change: after opting in, opening the app brings you to **Hackerspace Gear Shuffle**. Amazon only opens when you tap **SEND IT!**.

The button says what it does: **Open a random Amazon product**. The affiliate disclosure stays visible on that screen every time.

## How it works

First launch still gives you two choices:

- **Opt in and continue** — saves your choice and takes you to Gear Shuffle.
- **Exit without opting in** — closes the app without opening Amazon.

You only need to opt in once. That choice stays saved locally.

After that:

1. Open the app.
2. See the disclosure and the **SEND IT!** button.
3. Tap it when you're ready.
4. The app picks a random ASIN from the local JSON list, adds the Baltimore Hackerspace affiliate tag, and opens Amazon through Android's normal `ACTION_VIEW` handling.
5. BaHaAmazon closes after the handoff.

No product gets picked and no tagged link gets built until you tap. Incoming intents don't skip that step or decide which link opens.

This is a gear shuffle. No prizes, contests, or sweepstakes.

## If Amazon won't open

The app stays open and lets you try again.

The first two failed taps show a brief error. On the third failure, and every failure after that, you'll see **Amazon launch was unsuccessful**. Dismiss it and hit **SEND IT!** again if you want. There's no retry limit and no automatic retry.

The failure count survives rotation and resets on a fresh launch. This catches a missing app or Android blocking the launch; it can't tell whether a product page loads after Amazon or a browser accepts the link.

## Product list

The gear list lives here:

`app/src/main/assets/asins.json`

The app uses the existing random ASIN selection and affiliate tag. There's still a fallback ASIN if the file is empty or can't be read or parsed.

The list can be refreshed as products disappear or new gear gets added.

## Privacy and permissions

- No account or login required by BaHaAmazon.
- No background service.
- No special Android permissions.
- Your affiliate opt-in stays on the device in `SharedPreferences`.

## Current status

**v1.2 beta builds and runs on a Samsung Galaxy S24+.** Gear Shuffle waits for **SEND IT!**, then opens Amazon as expected. That flow has been confirmed on the phone.

Error handling is included in v1.2 and will be verified during beta testing. The existing unit test and Android lint checks pass. Device regression tests compile, but haven't been run on the phone yet. Clean-install opt-in and upgrade behavior still need verification for v1.2.
