# NightOwl Library

Android app for a library that never closes. Students browse the catalog, discover real books online, reserve a study seat for a time slot, keep a personal reading list and see which zones are quiet or busy.

## Group members
- Asselay Kalyk

## Main features
- Catalog of books with search and Night mode filter (00:00 - 06:00)
- Book Detail screen (receives `bookId` through Navigation)
- Discover: live search in the Open Library REST API with book covers
- Loading, Success, Error and Empty states with a Retry button
- Seat Map / Zones with free seats and a quiet-level badge (Silent, Quiet, Moderate, Busy)
- Reserve Seat form with validation and conflict check (receives `zoneId`, and `reservationId` when editing)
- My Reservations stored in Room: create, read, edit, cancel
- My reading list stored in Room: save from Discover, change status, rating and note, delete
- Settings stored in DataStore: app theme (System / Light / Dark) and reservation sort order
- Data and preferences stay after the app is closed and reopened

## Tech
Kotlin, Jetpack Compose, Material 3, MVVM (ViewModel + StateFlow), Navigation Compose, Retrofit + Gson, Kotlin Coroutines, Coil 3, Room (KSP), DataStore Preferences, Compose Previews (light and dark).

## External API
Open Library Search API: `https://openlibrary.org/search.json?q=...`
Covers: `https://covers.openlibrary.org/b/id/{cover_i}-M.jpg`

## Architecture
- `data/model` domain models
- `data/remote` Retrofit interface, JSON DTOs, Retrofit client
- `data/local` Room entities, DAOs, database
- `data/preferences` DataStore repository
- `data/repository` repositories for the API, Room and static catalog data
- `di` AppContainer that creates repositories once
- `viewmodel` UI state and logic exposed with StateFlow
- `ui` screens (stateful wrapper + stateless content), reusable components, theme, navigationss