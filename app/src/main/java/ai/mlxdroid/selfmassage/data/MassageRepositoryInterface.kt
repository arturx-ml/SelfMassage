package ai.mlxdroid.selfmassage.data

import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique

interface MassageRepositoryInterface {
    val zones: List<BodyZone>
    val techniques: List<MassageTechnique>
    fun zoneById(id: String): BodyZone?
    fun techniqueById(id: String): MassageTechnique?
    fun techniquesForZone(zoneId: String): List<MassageTechnique>
}
