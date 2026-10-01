# BaHaAmazon

BaHaAmazon is a small Android app I made for Baltimore Hackerspace.

The app selects a random Amazon product ASIN from a local JSON list, builds an affiliate link, opens it using Android's normal `ACTION_VIEW` handling, then exits.

## v1.0 Beta

Current beta functionality:

- Kotlin Android app
- First-run affiliate disclosure and opt-in screen
- Opt-out exits without opening Amazon
- Opt-in choice is stored locally so the disclosure is only shown once
- Random product selection from `asins.json`
- Affiliate tag is applied when the Amazon URL is generated
- Amazon links are handed off through normal Android `ACTION_VIEW` handling
- App exits after handing off the link
- No background service
- No special permissions
- Tested on a Samsung Galaxy S24+

## Product list

Product ASINs are stored locally in:

`app/src/main/assets/asins.json`

The current version selects a random entry from that list each time the app runs.

The product list can be updated independently as products become unavailable or new hackerspace-relevant items are added.

## Disclosure

On first launch, the app displays an affiliate disclosure and requires the user to either:

- opt in and continue, or
- exit without opting in

After opting in, that choice is stored locally and future launches skip the disclosure screen.

## Current status

v1.0 beta is functional and has been tested on physical hardware.
