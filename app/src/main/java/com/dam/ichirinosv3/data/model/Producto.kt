package com.dam.ichirinosv3.data.model

enum class TipoProducto { HARD, SOFT, OTRO }

data class Producto(
    var idDpto: Int = 0,    // PK
    var id: Long = 0,        // PK
    var fecAlta: String = "",
    var fecBaja: String = "",
    var descripcion: String = "",
    var cantidad: Int = 0,
    var tipo: TipoProducto = TipoProducto.HARD,
    var estado: Boolean = true, // True si está dado de alta, False si está dado de baja
    var idAula: Int? = null,
    var idArmario: Int? = null
)
