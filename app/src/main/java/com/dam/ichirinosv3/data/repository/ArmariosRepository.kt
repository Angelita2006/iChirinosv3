package com.dam.ichirinosv3.data.repository

import com.dam.ichirinosv3.data.model.Armario
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ArmariosRepository(private val refFS: DocumentReference) {

    fun getArmario(idDpto: Int, idAula: Int, id: Int): Flow<Armario?> {
        return refFS.collection("armarios")
            .document("$idDpto-$idAula-$id")
            .dataObjects<Armario>()
    }

    fun getAllArmarios(): Flow<List<Armario>> {
        return refFS.collection("armarios")
            .orderBy("id")
            .dataObjects<Armario>()
    }

    suspend fun insertArmario(armario: Armario) {
        refFS.collection("armarios")
            .document(armario.idDpto.toString() + "-" + armario.idAula.toString() + "-" + armario.id.toString())
            .set(armario).await()
    }

    suspend fun updateArmario(armario: Armario) {
        refFS.collection("armarios")
            .document(armario.idDpto.toString() + "-" + armario.idAula.toString() + "-" + armario.id.toString())
            .set(armario, SetOptions.merge()).await()
    }

    suspend fun deleteArmario(armario: Armario) {
        refFS.collection("armarios")
            .document(armario.idDpto.toString() + "-" + armario.idAula.toString() + "-" + armario.id.toString())
            .delete().await()
    }
}
