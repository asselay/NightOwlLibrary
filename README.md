# NightOwl Library

Android app for a library that never closes. Students browse the catalog, reserve a study seat or pod for a time slot, and see which zones are quiet or busy.

## Group members
- Asselay Kalyk

## Main features
- Catalog of books with search
- Book Detail screen (receives `bookId` through Navigation)
- Seat Map / Zones with free seats and a quiet-level badge (Silent, Quiet, Moderate, Busy)
- Reserve Seat form with validation (name, seat, time, conflict check)
- My Reservations with cancel
- Night mode filter: shows only what is available between 00:00 and 06:00

## Tech
Kotlin, Jetpack Compose, Material 3, MVVM (ViewModel + StateFlow), Navigation Compose (arguments: bookId, seatId), Compose Previews (light and dark).

## Architecture
- `data/` models and in-memory repository
- `viewmodel/` UI state and logic
- `ui/` screens (stateful wrapper + stateless content), reusable components, theme, navigation