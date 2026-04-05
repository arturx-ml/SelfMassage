# Self Massage

An Android app that guides you through self-massage techniques organized by body zone, with step-by-step instructions, animated movement illustrations, curated routines, and a guided session timer.

## Features

- **Zone list** — browse massage areas: Neck, Shoulders, Arms
- **Technique list** — per-zone catalog of massage techniques with duration info
- **Technique detail** — step-by-step guide with animated movement illustration and body location indicator; tap the play button in the top bar to launch a guided session
- **Routines** — curated multi-technique sequences (Morning Neck Reset, Office Tension Relief, Arms & Hands Recovery)
- **Session timer** — guided step-through player with per-step 5-second prep countdown, auto-advance, and skip/previous controls

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
app/src/main/java/ai/mlxdroid/selfmassage/
├── data/
│   ├── model/          # BodyZone, MassageTechnique, MassageStep,
│   │                   # AnimationType, BodyLocation, Routine, SessionStep
│   ├── MassageRepositoryInterface.kt
│   └── MassageRepository.kt   # Hardcoded catalog (3 zones, 8 techniques, 3 routines)
├── di/
│   └── DataModule.kt   # Hilt module binding MassageRepositoryInterface
├── domain/
│   ├── GetZonesUseCase.kt
│   ├── GetTechniquesForZoneUseCase.kt
│   ├── GetTechniqueDetailUseCase.kt
│   ├── GetRoutinesUseCase.kt
│   ├── GetRoutineDetailUseCase.kt
│   └── GetSessionStepsUseCase.kt  # Builds flat SessionStep list for technique or routine
├── navigation/
│   ├── NavRoutes.kt    # @Serializable routes: ZoneList, MassageList, MassageDetail,
│   │                   # RoutineList, RoutineDetail, TechniqueSession, RoutineSession
│   └── AppNavGraph.kt  # Scaffold with bottom nav (Zones / Routines tabs) + NavHost
└── ui/
    ├── viewmodel/
    │   ├── ZoneListViewModel.kt
    │   ├── MassageListViewModel.kt
    │   ├── MassageDetailViewModel.kt
    │   ├── RoutineListViewModel.kt
    │   ├── RoutineDetailViewModel.kt
    │   └── SessionViewModel.kt     # StateFlow timer with prep countdown + auto-advance
    ├── screens/
    │   ├── ZoneListScreen.kt
    │   ├── MassageListScreen.kt
    │   ├── MassageDetailScreen.kt
    │   ├── RoutineListScreen.kt
    │   ├── RoutineDetailScreen.kt
    │   └── SessionScreen.kt
    └── components/
        └── MassageAnimationCanvas.kt   # Canvas animations + body location indicator
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

### Neck (3 techniques)
- **Suboccipital Release** — pressure hold at base of skull
- **Lateral Neck Stretch & Friction** — downward strokes along sternocleidomastoid
- **Upper Trapezius Kneading** — circular knead at neck-shoulder junction

### Shoulders (3 techniques)
- **Cross-Fiber Friction** — transverse strokes across the deltoid
- **Periscapular Release** — circular friction along the shoulder blade edge
- **Deltoid Stripping** — long gliding strokes down the outer arm

### Arms (2 techniques)
- **Forearm Muscle Rolling** — transverse sweeps along wrist extensors and flexors
- **Hand Web & Thumb Base Massage** — thenar eminence circles + LI4 acupressure hold

### Routines (3 sequences)
- **Morning Neck Reset** (~6 min) — Suboccipital Release → Upper Trapezius Kneading
- **Office Tension Relief** (~11 min) — Lateral Neck Stretch → Cross-Fiber Friction → Periscapular Release
- **Arms & Hands Recovery** (~7 min) — Forearm Rolling → Hand Web Massage

## Getting started

1. Clone the repo
2. Open in Android Studio Meerkat or later
3. Sync Gradle (`File → Sync Project with Gradle Files`)
4. Run on a device or emulator running Android 9+

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
