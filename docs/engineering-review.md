# Engineering Review — SelfMassage Android App

**Date:** 2026-04-06
**Reviewer perspective:** Lead Android Engineer

---

## Critical

### 1. Missing loading/error states on detail screens
`MassageDetailScreen:59` and `RoutineDetailScreen:47` use `val technique = viewModel.technique ?: return`, which renders a blank screen if data is null. Should show a loading indicator or error message instead of a silent early return.

### 2. `checkNotNull` crashes on bad navigation args
`MassageListViewModel:22` calls `checkNotNull(savedStateHandle["zoneId"])` which throws if the nav arg is missing. All `SavedStateHandle` lookups should handle missing values gracefully.

### 3. R8/minification disabled in release
`app/build.gradle.kts` has `isMinifyEnabled = false`. Release APK ships unobfuscated, larger, and exposes internal class names. Enable R8 and configure keep rules for Hilt, kotlinx-serialization, and data models.

---

## High

### 4. Non-reactive ViewModels
`ZoneListViewModel`, `MassageListViewModel`, `MassageDetailViewModel`, `RoutineListViewModel`, `RoutineDetailViewModel` all initialize state synchronously as plain properties (e.g. `val zones = getZones()`). Should use `StateFlow` so the UI can react to changes and show loading states.

### 5. Hardcoded `Color.White` bypasses theme
Used ~15 times across screens (`ZoneListScreen:90`, `MassageDetailScreen:72`, `SessionScreen:99`, etc.) instead of `MaterialTheme.colorScheme.surface`. If dark mode is ever added, these won't adapt.

### 6. Fragile navigation route detection
`AppNavGraph:49-52` uses `currentRoute?.startsWith(ZoneList::class.qualifiedName ?: "")` for string matching. If `qualifiedName` is null, `startsWith("")` is always true. Consider a more robust approach like checking `destination.hierarchy`.

### 7. No empty state for lists
`ZoneListScreen`, `MassageListScreen`, `RoutineListScreen` show nothing if the list is empty. Add an empty state placeholder.

---

## Medium

### 8. Timer job lifecycle in SessionViewModel
`startTicking()` launches a `while(true)` coroutine. If called twice without cancelling the previous job first, two competing timers run. The `timerJob?.cancel()` at the top of `startTicking` handles this, but `play()` doesn't cancel before calling `startTicking()` — verify the cancel path is airtight.

### 9. No session state persistence
If the user rotates the device or the process is killed, all session progress (current step, timer) is lost. Consider saving session state to `SavedStateHandle`.

### 10. Accessibility gaps
Some interactive elements are below the 48dp minimum touch target (e.g. the 36dp play button in `MassageDetailScreen:96`). The countdown ring has no semantic description for screen readers. No dark mode support.

### 11. Inconsistent parameter naming in use cases
Some use `repository`, others use `repo`. Minor but worth standardizing.

### 12. Greeting logic duplicated
Time-of-day greeting appears in both `ZoneListScreen` and `RoutineListScreen`. Extract to a shared utility.

### 13. No deep link support
Navigation is purely in-app. If push notifications that open a specific technique/routine are needed, deep link routes will be required.

---

## Low

### 14. Compose BOM outdated
`2024.09.00` is nearly 2 years old. Update to latest stable BOM.

### 15. ProGuard rules file is empty
All rules are commented out. When R8 is enabled, proper keep rules will be needed.

### 16. Example test files still present
`ExampleUnitTest.kt` and `ExampleInstrumentedTest.kt` are template stubs. Remove them.

### 17. No CI/CD pipeline
No GitHub Actions or equivalent. Add at minimum: lint, unit tests, `assembleDebug` on PRs.

### 18. Card components not extracted
`ZoneCard`, `RoutineCard`, `TechniqueCard` are private to their screen files. If reuse is needed, extract to `ui/components/`.

### 19. No design token system for spacing
Values like `16.dp`, `12.dp`, `24.dp` are scattered everywhere. A `Spacing` object or `CompositionLocal` would centralize them.

### 20. Missing tests for newer features
No unit tests for `RoutineListViewModel`, `RoutineDetailViewModel`, or `SessionViewModel`. No UI tests for `RoutineListScreen`, `RoutineDetailScreen`, or `SessionScreen`.
