package ai.mlxdroid.selfmassage.data.model

data class Routine(
    val id: String,
    val name: String,
    val description: String,
    val iconName: String,
    val durationMinutes: Int,
    val techniqueIds: List<String>
)
