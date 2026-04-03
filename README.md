[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/e4rOHRfR)

# UVA Campus Map

An Android app that displays an interactive map of the University of Virginia campus using Google Maps.

## Features

- **Interactive Google Map** centered on UVA's campus with markers for campus buildings and landmarks
- **Live Data from UVA API** - fetches placemark data (buildings, facilities, etc.) from the UVA API on startup
- **Offline Support** - stores fetched data locally in a Room/SQLite database so the map works without a network connection
- **Tag-Based Filtering** - dropdown menu to filter markers by category tags (e.g. "core", "housing", etc.), defaults to "core" on launch
- **Custom Info Windows** - tapping a marker shows the building name and full description in a multi-line info window

## Setup

1. Create a Google Maps API key in the [Google Cloud Console](https://console.cloud.google.com/)
2. Enable the Maps SDK for Android
3. Add your key to `secrets.properties` in the project root:
   ```
   MAPS_API_KEY=your_api_key_here
   ```
4. Build and run the app

## Tech Stack

- Kotlin
- Jetpack Compose
- Google Maps Compose SDK
- Room (SQLite)
- Retrofit
- MVVM architecture
