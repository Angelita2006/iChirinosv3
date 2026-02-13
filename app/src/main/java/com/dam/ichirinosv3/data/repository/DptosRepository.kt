package com.dam.ichirinosv3.data.repository

import com.dam.ichirinosv3.data.model.Departamento
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class DptosRepository(private val refFS: DocumentReference) {

    fun getDpto(id: Int): Flow<Departamento?> {
        return refFS.collection("dptos")
            .document(id.toString())
            .dataObjects<Departamento>()
    }

    fun getAllDptos(): Flow<List<Departamento>> {
        return refFS.collection("dptos")
            .orderBy("id")
            .dataObjects<Departamento>()
    }

    suspend fun insertDpto(dpto: Departamento) {
        refFS.collection("dptos")
            .document(dpto.id.toString())
            .set(dpto).await()
    }

    suspend fun updateDpto(dpto: Departamento) {
        refFS.collection("dptos")
            .document(dpto.id.toString())
            .set(dpto, SetOptions.merge()).await()
    }

    suspend fun deleteDpto(dpto: Departamento) {
        refFS.collection("dptos")
            .document(dpto.id.toString())
            .delete().await()
    }
}
