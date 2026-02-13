package com.dam.ichirinosv3.data

import com.dam.ichirinosv3.data.model.Armario
import com.dam.ichirinosv3.data.model.Aula
import com.dam.ichirinosv3.data.model.Departamento
import com.dam.ichirinosv3.data.model.Producto
import com.dam.ichirinosv3.data.model.ProductoNS
import com.dam.ichirinosv3.data.model.TipoProducto

object DataSource {

    val dptosMock = listOf(
        Departamento(id = 0, nombre = "admin", clave = "a"),
        Departamento(id = 1, nombre = "Informática", clave = "i"),
        Departamento(id = 2, nombre = "Sanitario", clave = "s")
    )
    var aulasMock = listOf(
        Aula(idDpto = 1, id = 1, nombre = "Taller1"),
        Aula(idDpto = 1, id = 2, nombre = "Taller2"),
        Aula(idDpto = 2, id = 1, nombre = "Aula1")
    )
    var armariosMock = listOf(
        Armario(idDpto = 1, idAula = 1, id = 111, nombre = "Armario111"),
        Armario(idDpto = 1, idAula = 2, id = 121, nombre = "Armario121"),
        Armario(idDpto = 1, idAula = 2, id = 122, nombre = "Armario122")
    )
    var productosMock = listOf(
        Producto(
            idDpto = 2,
            id = 0,
            fecAlta = "20260101",
            fecBaja = "",
            descripcion = "Producto",
            tipo = TipoProducto.HARD,
            estado = true,
            idAula = null,
            idArmario = null
        ),
        Producto(
            idDpto = 1, id = 111, fecAlta = "20260101", fecBaja = "", descripcion = "Producto111",
            tipo = TipoProducto.HARD, estado = true, idAula = null, idArmario = null
        ),
        Producto(
            idDpto = 1, id = 222, fecAlta = "20260101", fecBaja = "", descripcion = "Producto222",
            tipo = TipoProducto.SOFT, estado = true, idAula = 1, idArmario = null
        ),
        Producto(
            idDpto = 1,
            id = 333,
            fecAlta = "20260101",
            fecBaja = "20260202",
            descripcion = "Producto333",
            tipo = TipoProducto.OTRO,
            estado = false,
            idAula = 1,
            idArmario = 111
        )
    )
    var productosNSMock = listOf(
        ProductoNS(idDpto = 1, idProducto = 111, idProductoNS = 1111, numSerie = "111aaa"),
        ProductoNS(idDpto = 1, idProducto = 111, idProductoNS = 1112, numSerie = "111bbb"),
        ProductoNS(idDpto = 1, idProducto = 111, idProductoNS = 1113, numSerie = "111ccc")
    )
}
