# Self Massage

An Android app that guides you through self-massage techniques organized by body zone, with step-by-step instructions and animated movement illustrations.

## Features

- **Zone list** — browse massage areas: Neck, Shoulders, Arms
- **Technique list** — per-zone catalog of massage techniques with duration info
- **Technique detail** — step-by-step guide with a live animated illustration of the movement

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose 2.8 (type-safe routes) |
| Animation | Compose Canvas + `rememberInfiniteTransition` |
| Min SDK | 28 (Android 9) |
| Target SDK | 36 (Android 15) |
| Build | Gradle 9.3.1 · AGP 9.1.0 |

## Project structure

```
app/src/main/java/ai/mlxdroid/selfmassage/
├── data/
│   ├── model/          # Data classes: BodyZone, MassageTechnique, MassageStep, AnimationType
│   └── MassageRepository.kt   # Hardcoded catalog (3 zones, 8 techniques)
├── navigation/
│   ├── NavRoutes.kt    # @Serializable route objects (ZoneList, MassageList, MassageDetail)
│   └── AppNavGraph.kt  # NavHost wiring all screens
└── ui/
    ├── screens/
    │   ├── ZoneListScreen.kt
    │   ├── MassageListScreen.kt
    │   └── MassageDetailScreen.kt
    └── components/
        └── MassageAnimationCanvas.kt   # Canvas animations per technique type
```

## Screens

### Zone List
Home screen showing all body zones. Each card displays the zone name, icon, and technique count.

### Massage List
Techniques for the selected zone. Each card shows the technique name, a one-line summary, and estimated duration.

### Massage Detail
Full-screen detail view with:
- Animated canvas illustration of the movement
- Summary line
- Numbered step-by-step guide with duration cues per step

## Animations

Each technique is tagged with one of four animation types, rendered with Compose Canvas:

| Type | Visual | Used for |
|---|---|---|
| `CIRCULAR` | Dot orbits a guide ring with trailing arc | Kneading, circular friction |
| `HORIZONTAL_SWEEP` | Arrow sweeps left ↔ right with ghost trail | Cross-fiber strokes |
| `VERTICAL_STROKE` | Arrow glides top → bottom, fades and resets | Effleurage, stripping |
| `PRESSURE_PULSE` | Center dot pulses with expanding ripple rings | Trigger point holds |

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
| `MassageDetailScreen.kt` | Detail with pressure-pulse animation · Detail with circular animation · Single step row |
| `MassageAnimationCanvas.kt` | All 4 animation types (static initial frame) |
