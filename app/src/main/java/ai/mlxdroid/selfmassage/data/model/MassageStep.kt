package ai.mlxdroid.selfmassage.data.model

data class MassageStep(
    val order: Int,
    val instruction: String,
    val durationSeconds: Int = 30
)
