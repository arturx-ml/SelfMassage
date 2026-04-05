package ai.mlxdroid.selfmassage.data.model

data class SessionStep(
    val techniqueName: String,
    val animationType: AnimationType,
    val bodyLocation: BodyLocation,
    val stepOrder: Int,
    val totalStepsInTechnique: Int,
    val instruction: String,
    val durationSeconds: Int
)
