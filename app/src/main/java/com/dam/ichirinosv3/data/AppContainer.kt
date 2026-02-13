package com.dam.ichirinosv3.data

import android.content.Context
import com.dam.ichirinosv3.data.repository.ArmariosRepository
import com.dam.ichirinosv3.data.repository.AulasRepository
import com.dam.ichirinosv3.data.repository.DptosRepository
import com.dam.ichirinosv3.data.repository.MainRepository
import com.dam.ichirinosv3.data.repository.ProductosNSRepository
import com.dam.ichirinosv3.data.repository.ProductosRepository

interface AppContainer {
    val mainRepository: MainRepository
    val dptosRepository: DptosRepository
    val aulasRepository: AulasRepository
    val armariosRepository: ArmariosRepository
    val productosRepository: ProductosRepository
    val productosNSRepository: ProductosNSRepository
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {

    override val mainRepository: MainRepository by lazy {
        MainRepository(context, AppDatastore(context).getDataStore())
    }
    override val dptosRepository: DptosRepository by lazy {
        DptosRepository(AppDatabase.getDatabase(context).getRefFS())
    }
    override val aulasRepository: AulasRepository by lazy {
        AulasRepository(AppDatabase.getDatabase(context).getRefFS())
    }
    override val armariosRepository: ArmariosRepository by lazy {
        ArmariosRepository(AppDatabase.getDatabase(context).getRefFS())
    }
    override val productosRepository: ProductosRepository by lazy {
        ProductosRepository(AppDatabase.getDatabase(context).getRefFS())
    }
    override val productosNSRepository: ProductosNSRepository by lazy {
        ProductosNSRepository(AppDatabase.getDatabase(context).getRefFS())
    }

}
