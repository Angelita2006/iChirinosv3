package com.dam.ichirinosv3.ui.screens.productos

import com.dam.ichirinosv3.data.model.Producto
import com.dam.ichirinosv3.data.model.TipoProducto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class Estado {TODOS, ALTA, BAJA}

data class ProductosBusState(
    var productoSelected: Producto? = null,
    val showDlgBorrar: Boolean = false
)

data class ProductosMtoState(
    var idDpto: Int = 0,     // PK
    var id: Long = 0,        // PK
    var fecAlta: String = "",
    var fecBaja: String = "", // no obligatorio
    var descripcion: String = "",
    var cantidad: Int = 0,
    var tipo: TipoProducto = TipoProducto.HARD,
    var estado: Boolean = true, // True si está dado de alta, False si está dado de baja
    var idAula: Int? = null, // no obligatorio
    var idArmario: Int? = null, // no obligatorio
    var datosObligatorios: Boolean = false // no obligatorio
)

data class ProductosState(
    var productos: List<Producto> = listOf()
)

data class ProductosFiltroState(
    val idDpto: String = "",
    val fecAltaDesde: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    val estado: Estado = Estado.TODOS,
    val idAula: Int? = null,
    val idArmario: Int? = null
)

fun ProductosMtoState.toProducto(): Producto = Producto(
    idDpto = idDpto,
    id = id,
    fecAlta = fecAlta,
    fecBaja = fecBaja,
    descripcion = descripcion,
    cantidad = cantidad,
    tipo = tipo,
    estado = estado,
    idAula = idAula,
    idArmario = idArmario
)

fun Producto.toProductosMtoState(): ProductosMtoState = ProductosMtoState(
    idDpto = idDpto,
    id = id,
    fecAlta = fecAlta,
    fecBaja = fecBaja,
    descripcion = descripcion,
    cantidad = cantidad,
    tipo = tipo,
    estado = estado,
    idAula = idAula,
    idArmario = idArmario
)

sealed interface ProductosInfoState {
    data object Loading : ProductosInfoState
    data object Success : ProductosInfoState
    data object Error : ProductosInfoState
}