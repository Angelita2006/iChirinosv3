package com.dam.ichirinosv3.ui.screens.aulas

import com.dam.ichirinosv3.data.model.Aula

data class AulasBusState(
    val aulaSelected: Aula? = null,
    val showDlgBorrar: Boolean = false
)

data class AulasMtoState(
    val idDpto: String = "",
    val id: String = "",
    val nombre: String = "",
    val datosObligatorios: Boolean = false
)

data class AulasState(
    var aulas: List<Aula> = listOf()
)

data class AulasFiltroState(
    val idDpto: String = ""
)

fun AulasMtoState.toAula(): Aula = Aula(
    idDpto = idDpto.toInt(),
    id = id.toInt(),
    nombre = nombre
)

fun Aula.toAulasMtoState(): AulasMtoState = AulasMtoState(
    idDpto = idDpto.toString(),
    id = id.toString(),
    nombre = nombre
)

sealed interface AulasInfoState {
    data object Loading : AulasInfoState
    data object Success : AulasInfoState
    data object Error : AulasInfoState
}
