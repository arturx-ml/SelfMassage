package ai.mlxdroid.selfmassage.data

import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.data.model.Routine

interface MassageRepositoryInterface {
    val zones: List<BodyZone>
    val techniques: List<MassageTechnique>
    val routines: List<Routine>
    fun zoneById(id: String): BodyZone?
    fun techniqueById(id: String): MassageTechnique?
    fun techniquesForZone(zoneId: String): List<MassageTechnique>
    fun routineById(id: String): Routine?
}
