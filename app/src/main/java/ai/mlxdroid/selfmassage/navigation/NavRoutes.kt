package ai.mlxdroid.selfmassage.navigation

import kotlinx.serialization.Serializable

@Serializable
object ZoneList

@Serializable
data class MassageList(val zoneId: String)

@Serializable
data class MassageDetail(val zoneId: String, val techniqueId: String)

@Serializable
object RoutineList

@Serializable
data class RoutineDetail(val routineId: String)

@Serializable
data class TechniqueSession(val techniqueId: String)

@Serializable
data class RoutineSession(val routineId: String)
