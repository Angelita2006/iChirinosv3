package com.dam.ichirinosv3.ui.screens.dptos

import com.dam.ichirinosv3.data.model.Departamento

data class DptosBusState(
    val dptoSelected: Departamento? = null,
    val showDlgBorrar: Boolean = false
)

data class DptosMtoState(
    val id: String = "",
    val nombre: String = "",
    val clave: String = "",
    val datosObligatorios: Boolean = false
)

data class DptosState(
    var departamentos: List<Departamento> = listOf()
)

fun DptosMtoState.toDpto(): Departamento = Departamento(
    id = id.toInt(),
    nombre = nombre,
    clave = clave
)

fun Departamento.toDptosMtoState(): DptosMtoState = DptosMtoState(
    id = id.toString(),
    nombre = nombre,
    clave = clave,
    datosObligatorios = false
)

sealed interface DptosInfoState {
    data object Loading : DptosInfoState
    data object Success : DptosInfoState
    data object Error : DptosInfoState
}
