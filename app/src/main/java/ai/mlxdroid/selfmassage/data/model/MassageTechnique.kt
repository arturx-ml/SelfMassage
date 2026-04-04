package ai.mlxdroid.selfmassage.data.model

data class MassageTechnique(
    val id: String,
    val name: String,
    val summary: String,
    val durationMinutes: Int,
    val steps: List<MassageStep>,
    val animationType: AnimationType
)
