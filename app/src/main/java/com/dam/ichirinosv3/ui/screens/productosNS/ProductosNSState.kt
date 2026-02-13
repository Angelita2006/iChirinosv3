package com.dam.ichirinosv3.ui.screens.productosNS

import com.dam.ichirinosv3.data.model.ProductoNS
import kotlin.String

data class ProductosNSBusState(
    var productoNSSelected: ProductoNS? = null,
    val showDlgBorrar: Boolean = false
)

data class ProductosNSMtoState(
    val idDpto: String = "",
    val idProducto: String = "",
    val idProductoNS: String = "",
    val numSerie: String = "",
    val datosObligatorios: Boolean = false
)

data class ProductosNSState(
    var productosNS: List<ProductoNS> = listOf()
)

fun ProductosNSMtoState.toProductoNS(): ProductoNS = ProductoNS(
    idDpto = idDpto.toInt(),
    idProducto = idProducto.toLong(),
    idProductoNS = idProductoNS.toLong(),
    numSerie = numSerie
)

fun ProductoNS.toProductosNSMtoState(): ProductosNSMtoState = ProductosNSMtoState(
    idDpto = idDpto.toString(),
    idProducto = idProducto.toString(),
    idProductoNS = idProductoNS.toString(),
    numSerie = numSerie
)

sealed interface ProductosNSInfoState {
    data object Loading : ProductosNSInfoState
    data object Success : ProductosNSInfoState
    data object Error : ProductosNSInfoState
}
