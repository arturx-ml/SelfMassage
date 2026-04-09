# Self Massage

An Android app that guides you through self-massage techniques organized by body zone, with step-by-step instructions, animated movement illustrations, curated routines, and a guided session timer.

## Features

- **Zone list** — browse massage areas by body zone
- **Technique list** — per-zone catalog of massage techniques with duration info
- **Technique detail** — step-by-step guide with animated movement illustration and body location indicator; tap the play button in the top bar to launch a guided session
- **Routines** — curated multi-technique sequences
- **Session timer** — guided step-through player with per-step 5-second prep countdown, auto-advance, and skip/previous controls
- **Free / Paid flavors** — two app variants with different content catalogs (see [docs/builds.md](docs/builds.md))

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose 2.8 (type-safe routes) + bottom NavigationBar |
| Animation | Compose Canvas + `rememberInfiniteTransition` |
| DI | Hilt 2.56.2 + KSP 2.2.10-2.0.2 |
| Min SDK | 28 (Android 9) |
| Target SDK | 36 (Android 15) |
| Build | Gradle 9.3.1 · AGP 9.1.0 |

## Project structure

```
app/src/
├── main/java/ai/mlxdroid/selfmassage/   # Shared code (both flavors)
│   ├── data/
│   │   ├── model/          # BodyZone, MassageTechnique, MassageStep,
│   │   │                   # AnimationType, BodyLocation, Routine, SessionStep
│   │   └── MassageRepositoryInterface.kt
│   ├── di/
│   │   └── DataModule.kt   # Hilt module binding MassageRepositoryInterface
│   ├── domain/
│   │   ├── GetZonesUseCase.kt
│   │   ├── GetTechniquesForZoneUseCase.kt
│   │   ├── GetTechniqueDetailUseCase.kt
│   │   ├── GetRoutinesUseCase.kt
│   │   ├── GetRoutineDetailUseCase.kt
│   │   └── GetSessionStepsUseCase.kt
│   ├── navigation/
│   │   ├── NavRoutes.kt
│   │   └── AppNavGraph.kt
│   └── ui/
│       ├── viewmodel/      # ZoneList, MassageList, MassageDetail,
│       │                   # RoutineList, RoutineDetail, Session ViewModels
│       ├── screens/        # Compose screens for each route
│       └── components/     # MassageAnimationCanvas, shared UI components
├── free/java/.../data/
│   └── MassageRepository.kt   # Free catalog (3 zones, 8 techniques, 3 routines)
├── paid/java/.../data/
│   └── MassageRepository.kt   # Paid catalog (5 zones, 14 techniques, 6 routines)
├── test/                       # Shared unit tests
├── testFree/                   # Free-flavor-specific tests
└── testPaid/                   # Paid-flavor-specific tests
```

## Screens

### Zone List
Home screen (Zones tab) showing all body zones. Each card displays the zone name, icon, and technique count.

### Massage List
Techniques for the selected zone. Each card shows the technique name, a one-line summary, and estimated duration.

### Massage Detail
Full detail view with:
- Animated canvas illustration of the movement with a body silhouette highlighting where to apply the technique
- Summary line
- Numbered step-by-step guide with duration cues per step
- **Start Session** button in the top bar and at the bottom of the page — launches the guided session player for this technique only

### Routine List
Routines tab showing all curated multi-technique sequences. Each card shows name, description (1 line), and total duration.

### Routine Detail
- Routine description
- Duration and technique-count chips
- Ordered list of techniques with duration
- **Start Routine** button — launches the session player for the full sequence

### Session Screen
Full-screen guided step player:
- Animation canvas for the current step's technique and body location
- Step progress label (`Step N of M · Technique Name`)
- Step instruction text
- **5-second "Get Ready" prep countdown** (secondary color ring) before each step's main timer begins
- Main countdown ring (primary color) counting down the step duration
- Controls: Previous / Play·Pause / Next
- Auto-advances to the next step (including prep) when the countdown reaches zero
- "Session Complete" overlay with Done button when all steps finish

## Animations

Each technique is tagged with one of four animation types, rendered with Compose Canvas:

| Type | Visual | Used for |
|---|---|---|
| `CIRCULAR` | Dot orbits a guide ring with trailing arc | Kneading, circular friction |
| `HORIZONTAL_SWEEP` | Arrow sweeps left ↔ right with ghost trail | Cross-fiber strokes |
| `VERTICAL_STROKE` | Arrow glides top → bottom, fades and resets | Effleurage, stripping |
| `PRESSURE_PULSE` | Center dot pulses with expanding ripple rings | Trigger point holds |

The canvas also renders a continuous bezier body silhouette (neck → shoulders → arms → torso) with a pulsing dot pinpointing the body location for each technique.

## Content

### Free (3 zones, 8 techniques, 3 routines)

| Zone | Techniques |
|---|---|
| Neck | Suboccipital Release, Lateral Neck Stretch & Friction, Upper Trapezius Kneading |
| Shoulders | Cross-Fiber Friction, Periscapular Release, Deltoid Stripping |
| Arms | Forearm Muscle Rolling, Hand Web & Thumb Base Massage |

Routines: Morning Neck Reset (7 min), Office Tension Relief (11 min), Arms & Hands Recovery (7 min)

### Paid (5 zones, 14 techniques, 6 routines)

Everything in Free, plus:

| Zone | Techniques |
|---|---|
| Lower Back | Lumbar Pressure Points, QL Side Release, Sacral Circles |
| Legs | IT Band Foam Roll, Calf Kneading, Plantar Fascia Release |

Additional routines: Lower Back Relief (10 min), Runner's Recovery (11 min), Full Body Reset (17 min)

## Getting started

1. Clone the repo
2. Open in Android Studio Meerkat or later
3. Sync Gradle (`File → Sync Project with Gradle Files`)
4. Select a build variant: `freeDebug` or `paidDebug` (`Build → Select Build Variant`)
5. Run on a device or emulator running Android 9+

For detailed build commands, signing, and CI setup, see [docs/builds.md](docs/builds.md).

## Compose Previews

Previews are co-located in each source file. Open any of the following in Android Studio's Split / Design view:

| File | Previews |
|---|---|
| `ZoneListScreen.kt` | Full zone list · Single zone card |
| `MassageListScreen.kt` | Full technique list (Neck) · Single technique card |
| `MassageDetailScreen.kt` | Detail (pressure-pulse) · Detail (circular) · Detail (sweep) · Single step row |
| `MassageAnimationCanvas.kt` | All 4 animation types (static initial frame) |
| `RoutineListScreen.kt` | Full routine list |
| `RoutineDetailScreen.kt` | Routine detail (Morning Neck Reset) |
| `SessionScreen.kt` | Get Ready state · Playing state · Session Complete |
