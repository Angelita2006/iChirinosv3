package com.dam.ichirinosv3.data.repository

import com.dam.ichirinosv3.data.model.Producto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ProductosRepository(private val refFS: DocumentReference) {

    fun getProducto(idDpto: Int, id: Int): Flow<Producto?> {
        return refFS.collection("productos")
            .document("$idDpto-$id")
            .dataObjects<Producto>()
    }

    fun getAllProductos(): Flow<List<Producto>> {
        return refFS.collection("productos")
            .orderBy("idDpto")
            .orderBy("id")
            .dataObjects<Producto>()
    }

    fun getAllProductosByFiltro(idDpto: String, fecAltaDesde: String, estado: Int, idAula: Int?, idArmario: Int?): Flow<List<Producto>> {
        var query: Query = refFS.collection("productos")
        query = query.orderBy("idDpto").orderBy("id").orderBy("fecAlta")
        if (idDpto != "") query = query.whereEqualTo("idDpto", idDpto.toInt())
        if (fecAltaDesde != "") query = query.whereGreaterThanOrEqualTo("fecAlta", fecAltaDesde)
        if (estado == 1) query = query.whereEqualTo("estado", true)
        if (estado == 2) query = query.whereEqualTo("estado", false)
        if (idAula != null) query = query.whereEqualTo("idAula", idAula)
        if (idArmario != null) query = query.whereEqualTo("idArmario", idArmario)
        return query.dataObjects<Producto>()
    }

    suspend fun insertProducto(producto: Producto) {
        refFS.collection("productos")
            .document(producto.idDpto.toString() + "-" + producto.id.toString())
            .set(producto).await()
    }

    suspend fun updateProducto(producto: Producto) {
        refFS.collection("productos")
            .document(producto.idDpto.toString() + "-" + producto.id.toString())
            .set(producto, SetOptions.merge()).await()
    }

    suspend fun deleteProducto(producto: Producto) {
        refFS.collection("productos")
            .document(producto.idDpto.toString() + "-" + producto.id.toString())
            .delete().await()
    }
}
