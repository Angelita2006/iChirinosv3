package com.dam.ichirinosv3.data.repository

import com.dam.ichirinosv3.data.model.Aula
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AulasRepository(private val refFS: DocumentReference) {

    fun getAula(idDpto: Int, id: Int): Flow<Aula?> {
        return refFS.collection("aulas")
            .document("$idDpto-$id")
            .dataObjects<Aula>()
    }

    fun getAllAulas(): Flow<List<Aula>> {
        return refFS.collection("aulas")
            .orderBy("idDpto")
            .orderBy("id")
            .dataObjects<Aula>()
    }

    fun getAllAulasByFiltro(idDpto: String): Flow<List<Aula>> {
        var query: Query = refFS.collection("aulas")
        query = query.orderBy("idDpto").orderBy("id")
        if (idDpto != "") query = query.whereEqualTo("idDpto", idDpto.toInt())
        return query.dataObjects<Aula>()
    }

    suspend fun insertAula(aula: Aula) {
        refFS.collection("aulas")
            .document(aula.idDpto.toString() + "-" + aula.id.toString())
            .set(aula).await()
    }

    suspend fun updateAula(aula: Aula) {
        refFS.collection("aulas")
            .document(aula.idDpto.toString() + "-" + aula.id.toString())
            .set(aula, SetOptions.merge()).await()
    }

    suspend fun deleteAula(aula: Aula) {
        refFS.collection("aulas")
            .document(aula.idDpto.toString() + "-" + aula.id.toString())
            .delete().await()
    }
}
