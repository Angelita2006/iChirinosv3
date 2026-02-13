package com.dam.ichirinosv3.data.repository

import com.dam.ichirinosv3.data.model.ProductoNS
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ProductosNSRepository(private val refFS: DocumentReference) {

    fun getProductoNS(idDpto: Int, idProducto: Int, id: Int): Flow<ProductoNS?> {
        return refFS.collection("productosNS")
            .document("$idDpto-$idProducto-$id")
            .dataObjects<ProductoNS>()
    }

    fun getAllProductosNS(): Flow<List<ProductoNS>> {
        return refFS.collection("productosNS")
            .orderBy("idProductoNS")
            .dataObjects<ProductoNS>()
    }

    suspend fun insertProductoNS(productoNS: ProductoNS) {
        refFS.collection("productosNS")
            .document(productoNS.idDpto.toString() + "-" + productoNS.idProducto.toString() + "-" + productoNS.idProductoNS.toString())
            .set(productoNS).await()
    }

    suspend fun updateProductoNS(productoNS: ProductoNS) {
        refFS.collection("productosNS")
            .document(productoNS.idDpto.toString() + "-" + productoNS.idProducto.toString() + "-" + productoNS.idProductoNS.toString())
            .set(productoNS, SetOptions.merge()).await()
    }

    suspend fun deleteProductoNS(productoNS: ProductoNS) {
        refFS.collection("productosNS")
            .document(productoNS.idDpto.toString() + "-" + productoNS.idProducto.toString() + "-" + productoNS.idProductoNS.toString())
            .delete().await()
    }
}
