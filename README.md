# BaHaAmazon

BaHaAmazon is a small Android app I made for Baltimore Hackerspace.

V1 opens a hard-coded Amazon affiliate link using Android's normal `ACTION_VIEW` handling, then closes immediately.

## Current plan

The next step is to replace the single hard-coded link with either:

- a simple affiliate-link generator, or
- a list of predefined product links that the app can cycle through

That keeps the app from depending on one product staying available forever.

## V1

- Kotlin
- No UI
- No Tasker
- No background service
- No special permissions
- Tested on a Samsung Galaxy S24+
