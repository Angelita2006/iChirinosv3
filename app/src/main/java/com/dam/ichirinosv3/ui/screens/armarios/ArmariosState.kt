package com.dam.ichirinosv3.ui.screens.armarios

import com.dam.ichirinosv3.data.model.Armario

data class ArmariosBusState(
    var armarioSelected: Armario? = null,
    val showDlgBorrar: Boolean = false
)

data class ArmariosMtoState(
    val idDpto: String = "",
    val idAula: String = "",
    val id: String = "",
    val nombre: String = "",
    val datosObligatorios: Boolean = false
)

data class ArmariosState(
    var armarios: List<Armario> = listOf()
)

fun ArmariosMtoState.toArmario(): Armario = Armario(
    idDpto = idDpto.toInt(),
    idAula = idAula.toInt(),
    id = id.toInt(),
    nombre = nombre
)

fun Armario.toArmariosMtoState(): ArmariosMtoState = ArmariosMtoState(
    idDpto = idDpto.toString(),
    idAula = idAula.toString(),
    id = id.toString(),
    nombre = nombre
)

sealed interface ArmariosInfoState {
    data object Loading : ArmariosInfoState
    data object Success : ArmariosInfoState
    data object Error : ArmariosInfoState
}
