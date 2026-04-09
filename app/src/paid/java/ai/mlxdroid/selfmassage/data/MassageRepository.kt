package ai.mlxdroid.selfmassage.data

import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.data.model.BodyLocation
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageStep
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.data.model.Routine

object MassageRepository : MassageRepositoryInterface {

    override val techniques: List<MassageTechnique> = listOf(
        // --- NECK ---
        MassageTechnique(
            id = "neck_suboccipital",
            name = "Suboccipital Release",
            summary = "Releases tension at the base of the skull",
            durationMinutes = 3,
            animationType = AnimationType.PRESSURE_PULSE,
            bodyLocation = BodyLocation.BASE_OF_SKULL,
            steps = listOf(
                MassageStep(1, "Sit upright in a chair. Place your fingertips at the base of your skull where it meets the neck.", 30),
                MassageStep(2, "Apply gentle upward pressure with both middle fingers into the two small hollows on either side of the spine.", 30),
                MassageStep(3, "Slowly tilt your head back slightly to increase pressure. Hold, breathing slowly.", 60),
                MassageStep(4, "Tilt your head forward to release. Repeat 3 times.", 30)
            )
        ),
        MassageTechnique(
            id = "neck_lateral_flex",
            name = "Lateral Neck Stretch & Friction",
            summary = "Targets sternocleidomastoid and scalene muscles",
            durationMinutes = 3,
            animationType = AnimationType.VERTICAL_STROKE,
            bodyLocation = BodyLocation.LATERAL_NECK,
            steps = listOf(
                MassageStep(1, "Drop your right ear toward your right shoulder until you feel a stretch on the left side of your neck.", 20),
                MassageStep(2, "Use your left hand's fingertips to apply short downward strokes along the left side of your neck from just behind the ear to the collarbone.", 45),
                MassageStep(3, "Pause on any tender spots and hold steady pressure for 10 seconds before continuing.", 30),
                MassageStep(4, "Return head to center. Repeat on the opposite side.", 60)
            )
        ),
        MassageTechnique(
            id = "neck_trapezius_knead",
            name = "Upper Trapezius Kneading",
            summary = "Classic knead for the neck-shoulder junction",
            durationMinutes = 4,
            animationType = AnimationType.CIRCULAR,
            bodyLocation = BodyLocation.UPPER_TRAPEZIUS,
            steps = listOf(
                MassageStep(1, "Reach your right hand across to your left shoulder-neck junction (the ridge of the trapezius).", 10),
                MassageStep(2, "Grasp the muscle belly between your fingers and thumb and squeeze firmly without digging in.", 20),
                MassageStep(3, "Roll the muscle in slow, circular motions — 10 circles clockwise, then 10 counter-clockwise.", 60),
                MassageStep(4, "Work your way along the trapezius from the neck base toward the shoulder tip.", 60),
                MassageStep(5, "Switch hands and repeat on the opposite side.", 60)
            )
        ),

        // --- SHOULDERS ---
        MassageTechnique(
            id = "shoulder_cross_friction",
            name = "Cross-Fiber Friction",
            summary = "Breaks up adhesions in the rotator cuff area",
            durationMinutes = 4,
            animationType = AnimationType.HORIZONTAL_SWEEP,
            bodyLocation = BodyLocation.UPPER_SHOULDER,
            steps = listOf(
                MassageStep(1, "Using the opposite hand, locate the front edge of the shoulder (anterior deltoid).", 15),
                MassageStep(2, "Place 2–3 fingers across the muscle fibers — perpendicular to how they run.", 10),
                MassageStep(3, "Apply firm pressure and sweep your fingers back and forth across the muscle belly with short strokes.", 60),
                MassageStep(4, "Move 1 cm lower and repeat. Cover the full deltoid front-to-back over 3 minutes.", 120)
            )
        ),
        MassageTechnique(
            id = "shoulder_blade_squeeze",
            name = "Periscapular Release",
            summary = "Relieves tension around the shoulder blade edges",
            durationMinutes = 4,
            animationType = AnimationType.CIRCULAR,
            bodyLocation = BodyLocation.SHOULDER_BLADE,
            steps = listOf(
                MassageStep(1, "Reach your right hand over your left shoulder, fingers pointing down your back toward your left shoulder blade.", 10),
                MassageStep(2, "With your elbow pointing upward, use fingertips to find the inner edge of the shoulder blade.", 20),
                MassageStep(3, "Apply circular friction motions along the medial border of the scapula, working top to bottom.", 60),
                MassageStep(4, "Press inward on tender points and hold for 20 seconds each before resuming circles.", 40),
                MassageStep(5, "Switch sides.", 60)
            )
        ),
        MassageTechnique(
            id = "shoulder_deltoid_strip",
            name = "Deltoid Stripping",
            summary = "Long gliding strokes to flush the deltoid muscle",
            durationMinutes = 3,
            animationType = AnimationType.VERTICAL_STROKE,
            bodyLocation = BodyLocation.OUTER_SHOULDER,
            steps = listOf(
                MassageStep(1, "Using the opposite hand's thumb, place it at the top of the shoulder cap (acromion).", 10),
                MassageStep(2, "Apply firm downward pressure and glide the thumb slowly down the outer arm to just above the elbow. Take 5 seconds per stroke.", 60),
                MassageStep(3, "Slightly reposition to cover anterior, lateral, and posterior portions of the deltoid.", 60),
                MassageStep(4, "Finish with 3 full-length lighter strokes to soothe the area.", 30)
            )
        ),

        // --- ARMS ---
        MassageTechnique(
            id = "arm_forearm_roll",
            name = "Forearm Muscle Rolling",
            summary = "Relieves tension in the wrist extensors and flexors",
            durationMinutes = 4,
            animationType = AnimationType.HORIZONTAL_SWEEP,
            bodyLocation = BodyLocation.FOREARM,
            steps = listOf(
                MassageStep(1, "Rest your left forearm on your thigh, palm up. Use your right thumb to locate the fleshy muscle bulk just below the elbow on the inner forearm.", 15),
                MassageStep(2, "Apply moderate pressure with the thumb and make short transverse sweeps across the muscle fibers.", 60),
                MassageStep(3, "Slowly work your way down the forearm toward the wrist, spending extra time on any tight bands.", 60),
                MassageStep(4, "Flip the arm over (palm down) and repeat on the outer forearm (extensors).", 60),
                MassageStep(5, "Finish with long light strokes from elbow to wrist. Switch arms.", 30)
            )
        ),
        MassageTechnique(
            id = "arm_hand_web_press",
            name = "Hand Web & Thumb Base Massage",
            summary = "Targets the thenar eminence and first dorsal interosseous",
            durationMinutes = 3,
            animationType = AnimationType.PRESSURE_PULSE,
            bodyLocation = BodyLocation.HAND_WEB,
            steps = listOf(
                MassageStep(1, "Hold your left hand palm-up in your right hand. Use your right thumb to find the fleshy mound at the base of your left thumb (thenar eminence).", 10),
                MassageStep(2, "Apply firm circular pressure in small circles over the entire thenar area.", 45),
                MassageStep(3, "Move to the web between thumb and index finger. Pinch this web between your right thumb and index finger.", 10),
                MassageStep(4, "Apply and hold firm pressure on the thickest point of the web — this is a strong acupressure point (LI4). Hold for 30 seconds.", 30),
                MassageStep(5, "Release, shake the hand out, then switch to the other hand.", 60)
            )
        ),

        // --- LOWER BACK ---
        MassageTechnique(
            id = "back_lumbar_press",
            name = "Lumbar Pressure Points",
            summary = "Targets deep pressure points along the lumbar spine",
            durationMinutes = 4,
            animationType = AnimationType.PRESSURE_PULSE,
            bodyLocation = BodyLocation.LUMBAR_SPINE,
            steps = listOf(
                MassageStep(1, "Stand with your back against a wall. Place two tennis balls (or fists) on either side of the lumbar spine at belt level.", 20),
                MassageStep(2, "Lean into the wall, letting body weight create firm pressure on the erector spinae muscles.", 60),
                MassageStep(3, "Hold each point for 15 seconds, then shift the balls up one vertebra. Cover the full lumbar region (L1–L5).", 90),
                MassageStep(4, "Finish by gently bending your knees to roll the balls up and down the lumbar area 5 times.", 50)
            )
        ),
        MassageTechnique(
            id = "back_ql_release",
            name = "QL Side Release",
            summary = "Releases the quadratus lumborum for side-body tension",
            durationMinutes = 3,
            animationType = AnimationType.HORIZONTAL_SWEEP,
            bodyLocation = BodyLocation.LATERAL_LOWER_BACK,
            steps = listOf(
                MassageStep(1, "Lie on your right side with a tennis ball under your left flank, just above the hip bone.", 15),
                MassageStep(2, "Use your body weight to apply moderate pressure. Make slow horizontal sweeps forward and backward across the QL muscle.", 60),
                MassageStep(3, "Pause on any tender spots and hold for 15 seconds before continuing the sweeps.", 45),
                MassageStep(4, "Roll onto your left side and repeat for the right QL.", 60)
            )
        ),
        MassageTechnique(
            id = "back_sacral_circles",
            name = "Sacral Circles",
            summary = "Gentle circular massage over the sacrum for lower back relief",
            durationMinutes = 3,
            animationType = AnimationType.CIRCULAR,
            bodyLocation = BodyLocation.SACRUM,
            steps = listOf(
                MassageStep(1, "Place both palms over the sacrum (the flat triangular bone at the base of the spine). Press gently.", 20),
                MassageStep(2, "Make slow, wide clockwise circles with both hands, warming the area. Complete 15 circles.", 60),
                MassageStep(3, "Reverse direction for 15 counter-clockwise circles, gradually increasing pressure on the upward sweep.", 60)
            )
        ),

        // --- LEGS ---
        MassageTechnique(
            id = "leg_it_band_roll",
            name = "IT Band Foam Roll",
            summary = "Releases tension along the iliotibial band from hip to knee",
            durationMinutes = 5,
            animationType = AnimationType.VERTICAL_STROKE,
            bodyLocation = BodyLocation.IT_BAND,
            steps = listOf(
                MassageStep(1, "Lie on your right side with a foam roller under the outer thigh, just below the hip bone. Support yourself on your right forearm.", 15),
                MassageStep(2, "Slowly roll down the outer thigh toward the knee, taking 5 seconds per inch.", 60),
                MassageStep(3, "When you reach a tender spot, pause and hold for 20 seconds until the tension releases.", 45),
                MassageStep(4, "Continue rolling down to just above the knee. Roll back up to the hip.", 60),
                MassageStep(5, "Switch to the left side and repeat.", 90)
            )
        ),
        MassageTechnique(
            id = "leg_calf_knead",
            name = "Calf Kneading",
            summary = "Deep kneading for the gastrocnemius and soleus muscles",
            durationMinutes = 3,
            animationType = AnimationType.CIRCULAR,
            bodyLocation = BodyLocation.CALF,
            steps = listOf(
                MassageStep(1, "Sit with your left ankle resting on your right knee. Wrap both hands around the left calf.", 10),
                MassageStep(2, "Use both thumbs to make firm circular motions along the midline of the calf, starting just below the knee.", 50),
                MassageStep(3, "Work your way down to the Achilles tendon, spending extra time on tight knots.", 50),
                MassageStep(4, "Switch legs and repeat.", 60)
            )
        ),
        MassageTechnique(
            id = "leg_plantar_release",
            name = "Plantar Fascia Release",
            summary = "Relieves tension in the sole of the foot using targeted pressure",
            durationMinutes = 3,
            animationType = AnimationType.PRESSURE_PULSE,
            bodyLocation = BodyLocation.PLANTAR_FOOT,
            steps = listOf(
                MassageStep(1, "While seated, place a tennis ball or lacrosse ball under the arch of your right foot.", 10),
                MassageStep(2, "Apply body weight gradually and roll the ball from the heel toward the ball of the foot in slow strokes.", 50),
                MassageStep(3, "Pause on the most tender point (usually mid-arch) and press firmly for 20 seconds.", 40),
                MassageStep(4, "Switch feet and repeat.", 60)
            )
        )
    )

    override val routines: List<Routine> = listOf(
        Routine(
            id = "morning_neck_reset",
            name = "Morning Neck Reset",
            description = "A gentle wake-up sequence targeting the base of the skull and neck-shoulder junction to ease overnight stiffness.",
            iconName = "SelfImprovement",
            durationMinutes = 7,
            techniqueIds = listOf("neck_suboccipital", "neck_trapezius_knead")
        ),
        Routine(
            id = "office_tension_relief",
            name = "Office Tension Relief",
            description = "Targets the common desk-work tension triangle: side of neck, shoulder joint, and shoulder blade edges.",
            iconName = "FitnessCenter",
            durationMinutes = 11,
            techniqueIds = listOf("neck_lateral_flex", "shoulder_cross_friction", "shoulder_blade_squeeze")
        ),
        Routine(
            id = "arms_recovery",
            name = "Arms & Hands Recovery",
            description = "Flushes forearm fatigue and relieves hand tension — ideal after typing, sport, or manual work.",
            iconName = "Straighten",
            durationMinutes = 7,
            techniqueIds = listOf("arm_forearm_roll", "arm_hand_web_press")
        ),
        Routine(
            id = "lower_back_relief",
            name = "Lower Back Relief",
            description = "A comprehensive lower back sequence targeting lumbar pressure points, the QL, and sacrum for deep relief.",
            iconName = "SelfImprovement",
            durationMinutes = 10,
            techniqueIds = listOf("back_lumbar_press", "back_ql_release", "back_sacral_circles")
        ),
        Routine(
            id = "runners_recovery",
            name = "Runner's Recovery",
            description = "Targets the IT band, calves, and plantar fascia — essential post-run recovery for tired legs.",
            iconName = "DirectionsWalk",
            durationMinutes = 11,
            techniqueIds = listOf("leg_it_band_roll", "leg_calf_knead", "leg_plantar_release")
        ),
        Routine(
            id = "full_body_reset",
            name = "Full Body Reset",
            description = "A top-to-bottom reset hitting neck, shoulders, lower back, legs, and hands for total-body relief.",
            iconName = "FitnessCenter",
            durationMinutes = 17,
            techniqueIds = listOf("neck_suboccipital", "shoulder_cross_friction", "back_lumbar_press", "leg_calf_knead", "arm_hand_web_press")
        )
    )

    override val zones: List<BodyZone> = listOf(
        BodyZone(
            id = "neck",
            name = "Neck",
            iconName = "SelfImprovement",
            techniqueIds = listOf("neck_suboccipital", "neck_lateral_flex", "neck_trapezius_knead")
        ),
        BodyZone(
            id = "shoulders",
            name = "Shoulders",
            iconName = "FitnessCenter",
            techniqueIds = listOf("shoulder_cross_friction", "shoulder_blade_squeeze", "shoulder_deltoid_strip")
        ),
        BodyZone(
            id = "arms",
            name = "Arms",
            iconName = "Straighten",
            techniqueIds = listOf("arm_forearm_roll", "arm_hand_web_press")
        ),
        BodyZone(
            id = "lower_back",
            name = "Lower Back",
            iconName = "SelfImprovement",
            techniqueIds = listOf("back_lumbar_press", "back_ql_release", "back_sacral_circles")
        ),
        BodyZone(
            id = "legs",
            name = "Legs",
            iconName = "DirectionsWalk",
            techniqueIds = listOf("leg_it_band_roll", "leg_calf_knead", "leg_plantar_release")
        )
    )

    override fun zoneById(id: String): BodyZone? = zones.find { it.id == id }

    override fun techniqueById(id: String): MassageTechnique? = techniques.find { it.id == id }

    override fun techniquesForZone(zoneId: String): List<MassageTechnique> {
        val zone = zoneById(zoneId) ?: return emptyList()
        return zone.techniqueIds.mapNotNull { techniqueById(it) }
    }

    override fun routineById(id: String): Routine? = routines.find { it.id == id }
}
