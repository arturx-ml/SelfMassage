# Figma Design Prompt — Self Massage App

Wellness Android app, 390×844pt, Material 3. Premium calm aesthetic — think Calm app meets health tech. Warm, focused, not clinical.

## Colors
| Token | Hex |
|---|---|
| Primary | #2D6A4F |
| Primary light | #52B788 |
| Secondary | #1B4332 |
| Surface | #F8F9F4 |
| Card fill | #F1F5EE |
| Subtle text | #6B7280 |
| Accent warm | #D4A373 |
| Pulse/error | #C45C3A |

## Typography & Shape
- Fonts: Plus Jakarta Sans (headings SemiBold, body Regular), Inter (labels/numbers)
- Corner radius: cards 20dp, buttons 14dp, chips pill, overlays 28dp top
- Shadows: soft only — 0 4 12 rgba(0,0,0,0.10), no heavy elevation

## Navigation
Floating pill bottom nav bar (20dp radius, 16dp side margins, white bg, soft shadow). 2 tabs: Zones (leaf icon) | Routines (play circle). Active = filled icon + Primary label.

## Screens

**Zone List & Routine List** share the same layout:
- 140pt gradient header (#EAF4EC → #FFFFFF) with greeting text + subtitle
- Scrollable card list below (16pt padding, 12pt gap)
- Zone cards: 88pt tall, left accent bar (4pt, Primary), 52dp rounded-square icon badge (Primary light bg), name + count, right chevron
- Routine cards: 108pt tall, name + 2-line description, bottom row with warm accent chips (duration, technique count) + 32dp filled arrow button, subtle leaf illustration in corner at 8% opacity

**Technique List:** compact 72pt cards, 40dp circle icon badge left, technique name + summary, duration chip right (accent warm)

**Technique Detail:**
- Top bar: name + back + 36dp Play circle button (Primary)
- 220pt animation panel card (gradient bg, canvas animation, small body silhouette with pulsing dot in corner)
- Summary text, "Steps" label with pill badge
- Step rows: 32dp Primary circle with step number, instruction text, duration chip below, dashed divider between steps
- Full-width "Start Session" button (56pt, Primary, 14dp radius)

**Routine Detail:**
- 120pt gradient hero (Primary tint): routine name white + 2 chips (duration, count)
- Scrollable: description, techniques list with left accent dots, "Start Routine" button

**Session Screen** (immersive, no bottom nav):
- Minimal top bar (back + centered technique name)
- 200pt animation panel (gradient bg)
- Step strip below panel: "Step N of M" subtle + bullet + technique name Primary
- GET READY badge (pill, Secondary bg, white uppercase text, pulsing) during 5s prep
- Instruction text centered (bodyLarge, 24pt padding)
- 140pt countdown ring: 8pt stroke track (surfaceVariant) + arc (Primary active / Secondary prep) + 152pt outer glow ring at 4% opacity; center shows seconds in 48sp Inter SemiBold
- Controls: 44dp ghost Prev | 64dp filled Play/Pause (Primary) | 44dp ghost Next
- Complete overlay: 80dp Primary checkmark circle, "Well done!" headline, bodyLarge message, full-width Done button

## Components to build
ZoneCard · RoutineCard · TechniqueCard · StepRow · CountdownRing (prep/active/complete states) · SessionControls (playing/paused) · FloatingTabBar · AnimationPanel · SessionCompleteOverlay

## Deliverables
Color + type styles · Component library with all states · 6 screens light mode · Session screen dark mode · Prototype flow: Zones → Techniques → Detail → Session → Complete
