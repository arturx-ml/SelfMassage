package ai.mlxdroid.selfmassage.navigation

import kotlinx.serialization.Serializable

@Serializable
object ZoneList

@Serializable
data class MassageList(val zoneId: String)

@Serializable
data class MassageDetail(val zoneId: String, val techniqueId: String)
