# Figma Design Prompt — Self Massage App

Wellness Android app, 390×844pt. Modern, editorial, high-end feel — like a luxury spa brand's digital product. Dark-mode-first with light mode variant. Generous whitespace, bold typography, glassmorphism accents. References: Apple Fitness+, Headspace 2024 rebrand, Arc browser.

## Colors
| Token | Hex |
|---|---|
| Background | #0F0F0F |
| Surface | #1A1A1A |
| Card | #242424 (glass: white 6% opacity + 12px blur) |
| Primary | #A8E6CF |
| Primary muted | #5B8A72 |
| Accent | #FFD6A5 |
| Text primary | #F5F5F5 |
| Text secondary | #8A8A8A |
| Destructive | #FF8A80 |
| Light mode bg | #FAFAF8, surface #FFFFFF, card #F3F3F0 |

## Typography & Shape
- Font: Satoshi (headings Black/Bold, body Medium/Regular) — fallback: Plus Jakarta Sans
- Display: 32sp Black, tracked -0.02em. Body: 15sp Regular, 1.6 line-height
- Radius: cards 24dp, buttons 16dp, chips fully rounded, bottom sheet 32dp
- No hard shadows — use layered surfaces, subtle borders (white 8%), and backdrop blur

## Navigation
Bottom tab bar: full-width, transparent bg with top blur edge. 2 tabs: Zones (body icon) | Routines (play icon). Active = Primary tint icon + dot indicator below. No labels — icon-only with 28dp touch targets in 56dp row.

## Screens

**Zone List (Home):**
- Large display heading "Self Massage" at top (32sp Black), subtle tagline below (15sp, secondary text)
- 3 zone cards in vertical stack, 16dp gap, full-bleed horizontal (8dp screen margin)
- Each card: 140pt tall, 24dp radius, background image (abstract body-part illustration, dark overlay 60%), zone name in Bold 20sp white bottom-left, technique count pill top-right (glass chip, Primary text)

**Routine List:**
- Same heading pattern: "Routines" + subtitle "Curated sequences"
- Cards: 120pt, horizontal scroll row (peek next card), 24dp radius, glass bg, icon left (40dp circle, Primary bg), name Bold 17sp + description Regular 13sp secondary, duration pill bottom-right (Accent bg, dark text)

**Technique List:**
- Minimal top bar: back arrow + zone name (17sp Bold)
- List items (no cards): 72pt rows, thin bottom divider (white 6%), technique name (17sp Medium) + summary (13sp secondary) left, duration text right (Accent color, 13sp Bold). Tap state: row bg → white 4%

**Technique Detail:**
- Edge-to-edge animation panel top half (280pt, no radius — bleeds to screen edges), dark gradient overlay bottom 40% fading up, technique name overlaid bottom-left (24sp Bold white), Play FAB bottom-right of panel (56dp circle, Primary bg, dark play icon)
- Below: summary (15sp, secondary, 16dp padding), then step timeline — vertical line (Primary muted, 2dp) left side, step circles on the line (24dp, filled Primary for current, outline for others), instruction right of each node (15sp), duration below each (13sp Accent). Full-width "Start Session" button at bottom (56pt, Primary bg, dark text, 16dp radius, Bold)

**Routine Detail:**
- Hero: routine name (28sp Bold) + description (15sp secondary) over gradient bg (Primary muted 20% → transparent), 160pt
- Chips row: duration + count (glass chips)
- Technique list: numbered rows, name + duration, left Primary accent dot
- Sticky bottom: "Start Routine" button (same style as above)

**Session Screen** (immersive, no nav bar, no status bar feel):
- Full-screen dark bg, animation panel top (200pt, edge-to-edge)
- Step label centered below: "Step 2 of 4" (13sp secondary) · technique name (13sp Primary)
- GET READY state: large "GET READY" text (24sp Bold, Primary, pulsing opacity 0.6↔1.0)
- Instruction: centered, 17sp Medium, max 3 lines, 24dp h-padding
- Countdown: 160pt ring, 6dp stroke, track white 8%, arc Primary (prep: Accent), center number 52sp Satoshi Black + "sec" 12sp below. Subtle outer glow (Primary, 20dp blur, 10% opacity)
- Controls: row at bottom — Skip prev (44dp ghost, white 12% border) | Play/Pause (72dp, Primary bg, dark icon) | Skip next (44dp ghost). 40dp bottom margin
- Complete: centered stack — animated checkmark (Lottie-style, 80dp), "Well done" 28sp Bold, subtitle 15sp secondary, "Done" button full-width

## Components
ZoneCard (image bg) · RoutineCard (glass, horizontal) · TechniqueRow · StepTimelineNode · CountdownRing (prep/active/done) · SessionControls · IconTabBar · GlassChip · PrimaryButton · AnimationPanel

## Deliverables
Design tokens (color, type, radius, spacing: 4/8/12/16/24/32/48) · Component lib with states · 6 screens dark mode · 6 screens light mode · Session prototype flow
