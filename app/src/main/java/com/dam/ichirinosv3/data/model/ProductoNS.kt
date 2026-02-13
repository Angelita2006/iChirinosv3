package com.dam.ichirinosv3.data.model

data class ProductoNS(
    var idDpto: Int = 0, // PK
    var idProducto: Long = 0, // PK yyyyMMddHHmmss
    var idProductoNS: Long = 0, // PK yyyyMMddHHmmss
    var numSerie: String = ""
)
