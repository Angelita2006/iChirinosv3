package com.dam.ichirinosv3.ui.screens.productosNS

import android.database.sqlite.SQLiteException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dam.ichirinosv3.MainApplication
import com.dam.ichirinosv3.data.model.ProductoNS
import com.dam.ichirinosv3.data.repository.ProductosNSRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.String

class ProductosNSVM(private val productosNSRepository: ProductosNSRepository): ViewModel() {

    val uiProductosNSState: StateFlow<ProductosNSState> =
        productosNSRepository.getAllProductosNS().map { ProductosNSState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(ProductosNSVM.Companion.TIMEOUT_MILLIS),
                initialValue = ProductosNSState()
            )

    var uiBusState by mutableStateOf(ProductosNSBusState())
        private set

    var uiMtoState by mutableStateOf(ProductosNSMtoState())
        private set

    var uiInfoState: ProductosNSInfoState by mutableStateOf(ProductosNSInfoState.Loading)
        private set

    /* Funciones Estado del Buscador **************************************************************/

    fun setProductoNSSelected(productoNS: ProductoNS?) {
        uiBusState = uiBusState.copy(
            productoNSSelected = productoNS,
            showDlgBorrar = false
        )
        uiMtoState = if (productoNS != null) {
            uiMtoState.copy(
                idDpto = productoNS.idDpto.toString(),
                idProducto = productoNS.idProducto.toString(),
                idProductoNS = productoNS.idProductoNS.toString(),
                numSerie = productoNS.numSerie,
                datosObligatorios = true
            )
        } else {
            uiMtoState.copy(
                idDpto = "",
                idProducto = "",
                idProductoNS = "",
                numSerie = "",
                datosObligatorios = false
            )
        }
    }

    fun setShowDlgBorrar(show: Boolean) {
        uiBusState = uiBusState.copy(
            showDlgBorrar = show
        )
    }

    /* Funciones Estado del Mantenimiento *********************************************************/

    fun setIdDpto(idDpto: String) {
        uiMtoState = uiMtoState.copy(
            idDpto = idDpto,
            datosObligatorios = (idDpto != "" && uiMtoState.idProducto != "" && uiMtoState.idProductoNS != "" && uiMtoState.numSerie != "")
        )
    }

    fun setIdProducto(idProducto: String) {
        uiMtoState = uiMtoState.copy(
            idProducto = idProducto,
            datosObligatorios = (uiMtoState.idDpto != "" && idProducto != "" && uiMtoState.idProductoNS != "" && uiMtoState.numSerie != "")
        )
    }

    fun setIdProductoNS(idProductoNS: String) {
        uiMtoState = uiMtoState.copy(
            idProductoNS = idProductoNS,
            datosObligatorios = (uiMtoState.idDpto != "" && uiMtoState.idProducto != "" && idProductoNS != "" && uiMtoState.numSerie != "")
        )
    }

    fun setNumSerie(numSerie: String) {
        uiMtoState = uiMtoState.copy(
            numSerie = numSerie,
            datosObligatorios = (uiMtoState.idDpto != "" && uiMtoState.idProducto != "" && uiMtoState.idProductoNS != "" && numSerie != "")
        )
    }

    /* Funciones Lógica Dptos *********************************************************************/

    fun resetBusState() {
        setProductoNSSelected(null)
    }

    fun resetStates(idDpto: String, idProducto: String, idProductoNS: String) {
        uiBusState = uiBusState.copy(
            productoNSSelected = null,
            showDlgBorrar = false
        )
        uiMtoState = uiMtoState.copy(
            idDpto = idDpto,
            idProducto = idProducto,
            idProductoNS = idProductoNS,
            numSerie = "",
            datosObligatorios = false
        )
    }

    fun resetInfoState() {
        uiInfoState = ProductosNSInfoState.Loading
    }

    fun alta() {
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = ProductosNSInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                productosNSRepository.insertProductoNS(uiMtoState.toProductoNS())
                ProductosNSInfoState.Success
            } catch (e: SQLiteException) {
                ProductosNSInfoState.Error
            }
        }
    }

    fun editar() {
        if (uiMtoState.idProductoNS == "0") {
            uiInfoState = ProductosNSInfoState.Error
            return
        }
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = ProductosNSInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                productosNSRepository.updateProductoNS(uiMtoState.toProductoNS())
                ProductosNSInfoState.Success
            } catch (e: SQLiteException) {
                ProductosNSInfoState.Error
            }
        }
    }

    fun baja() {
        if (uiMtoState.idProductoNS == "0") {
            uiInfoState = ProductosNSInfoState.Error
            return
        }
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = ProductosNSInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                productosNSRepository.deleteProductoNS(uiMtoState.toProductoNS())
                ProductosNSInfoState.Success
            } catch (e: SQLiteException) {
                ProductosNSInfoState.Error
            }
        }
    }

    /* Factory ************************************************************************************/

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val productosNSRepository = application.container.productosNSRepository
                ProductosNSVM(productosNSRepository = productosNSRepository)
            }
        }
    }
}
