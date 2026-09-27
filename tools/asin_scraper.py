import requests
import re
from urllib.parse import quote_plus
import time
import json


# Pull ASINs from Amazon searches and save a clean list to JSON.

search_terms = {
    "soldering",
    "soldering iron",
    "soldering station",
    "multimeter",
    "oscilloscope",
    "bench power supply",
    "logic analyzer",
    "function generator",
    "esp32",
    "arduino",
    "raspberry pi",
    "microcontroller",
    "breadboard",
    "jumper wires",
    "dupont connectors",
    "heat shrink tubing",
    "wire connectors",
    "crimping tool",
    "wire stripper",
    "electronics kit",
    "resistor kit",
    "capacitor kit",
    "led assortment",
    "transistor kit",
    "relay module",
    "stepper motor",
    "servo motor",
    "dc motor",
    "motor driver",
    "buck converter",
    "boost converter",
    "usb cable",
    "usb c cable",
    "usb hub",
    "ethernet cable",
    "network switch",
    "wifi adapter",
    "bluetooth adapter",
    "rtl sdr",
    "sdr antenna",
    "ham radio",
    "coax cable",
    "bnc connector",
    "sma connector",
    "antenna analyzer",
    "3d printer filament",
    "pla filament",
    "petg filament",
    "3d printer nozzle",
    "calipers",
    "digital caliper",
    "precision screwdriver",
    "hex key set",
    "torx set",
    "drill bit set",
    "step drill bit",
    "rotary tool",
    "dremel accessories",
    "hot glue gun",
    "epoxy",
    "super glue",
    "double sided tape",
    "velcro straps",
    "cable ties",
    "label maker",
    "small parts organizer",
    "project box",
    "electronics enclosure",
    "magnifying lamp",
    "helping hands",
    "fume extractor",
    "esd mat",
    "esd wrist strap",
    "safety glasses",
    "hearing protection",
    "work gloves",
}

# A set keeps duplicates out while we collect results.
all_asins = set()

# Amazon was returning 503s without normal browser headers.
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
    "Accept-Language": "en-US,en;q=0.9",
}

for search_term in search_terms:
    encoded_term = quote_plus(search_term)
    url = f"https://www.amazon.com/s?k={encoded_term}"

    try:
        response = requests.get(url, headers=headers, timeout=10)
        print(response.status_code)

    except requests.exceptions.Timeout:
        print(f"Request timed out for search term: {search_term}")
        time.sleep(1)
        continue

    except requests.exceptions.RequestException as exc:
        print(f"Request failed for search term: {search_term}: {exc}")
        time.sleep(1)
        continue

    if response.status_code == 200:
        # ASINs show up in /dp/ product links.
        page_asins = re.findall(r"/dp/([A-Z0-9]{10})", response.text)
        all_asins.update(page_asins)

    elif response.status_code == 403:
        print(f"Access denied for search term: {search_term}")

    elif response.status_code == 429:
        print(f"Rate limited for search term: {search_term}")

    elif response.status_code == 503:
        print(f"Service unavailable or anti-bot measures for search term: {search_term}")
        print(response.text[:500])

    else:
        print(f"Unexpected status code {response.status_code} for search term: {search_term}")

    time.sleep(4)  # Don't hammer Amazon or they will tell you to fuck off.

# Final cleanup before writing the file.
sorted_asins = sorted(all_asins)

with open("tools/asins.json", "w") as file:
    json.dump(sorted_asins, file, indent=2)

print("Collected ASINs:")
for asin in sorted_asins:
    print(asin)