package com.dam.ichirinosv3.ui.screens.productos

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
import com.dam.ichirinosv3.data.model.Producto
import com.dam.ichirinosv3.data.model.TipoProducto
import com.dam.ichirinosv3.data.repository.ProductosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.String

class ProductosVM(private val productosRepository: ProductosRepository) : ViewModel() {

    lateinit var uiProductosState: StateFlow<ProductosState>

    var uiBusState by mutableStateOf(ProductosBusState())
        private set

    var uiMtoState by mutableStateOf(ProductosMtoState())
        private set

    var uiFiltroState by mutableStateOf(ProductosFiltroState())
        private set

    var uiInfoState: ProductosInfoState by mutableStateOf(ProductosInfoState.Loading)
        private set

    /* Funciones Estado del Buscador **************************************************************/

    fun setProductoSelected(producto: Producto?) {
        uiBusState = uiBusState.copy(
            productoSelected = producto,
            showDlgBorrar = false
        )

        uiMtoState = if (producto != null) {
//            val producto = DataSource.productos[pos]
            uiMtoState.copy(
                idDpto = producto.idDpto,
                id = producto.id,
                fecAlta = producto.fecAlta,
                fecBaja = producto.fecBaja,
                descripcion = producto.descripcion,
                cantidad = producto.cantidad,
                tipo = producto.tipo,
                estado = producto.estado,
                idAula = producto.idAula,
                idArmario = producto.idArmario,
                datosObligatorios = true
            )
        } else {
            uiMtoState.copy(
                idDpto = 0,
                id = 0,
                fecAlta = "",
                fecBaja = "",
                descripcion = "",
                cantidad = 0,
                tipo = TipoProducto.HARD,
                estado = true,
                idAula = null,
                idArmario = null,
                datosObligatorios = false
            )
        }
    }

    fun setShowDlgBorrar(show: Boolean) {
        uiBusState = uiBusState.copy(showDlgBorrar = show)
    }

    /* Funciones Estado del Mantenimiento *********************************************************/

    private fun validarDatos(state: ProductosMtoState): Boolean {
        return state.idDpto != 0 &&
                state.id != 0L &&
                state.fecAlta.isNotBlank() &&
                state.descripcion.isNotBlank() &&
                state.cantidad > 0
    }

    fun setIdDpto(idDpto: String) {
        val nuevo = uiMtoState.copy(
            idDpto = idDpto.toIntOrNull() ?: 0
        )
        uiMtoState = nuevo.copy(
            datosObligatorios = validarDatos(nuevo)
        )
    }

    fun setId(id: String) {
        val nuevo = uiMtoState.copy(
            id = id.toLongOrNull() ?: 0L
        )
        uiMtoState = nuevo.copy(
            datosObligatorios = validarDatos(nuevo)
        )
    }

    fun setFecAlta(fecAlta: String) {
        val nuevo = uiMtoState.copy(fecAlta = fecAlta)
        uiMtoState = nuevo.copy(
            datosObligatorios = validarDatos(nuevo)
        )
    }

    fun setFecBaja(fecBaja: String) {
        uiMtoState = uiMtoState.copy(fecBaja = fecBaja)
    }

    fun setDescripcion(descripcion: String) {
        val nuevo = uiMtoState.copy(descripcion = descripcion)
        uiMtoState = nuevo.copy(
            datosObligatorios = validarDatos(nuevo)
        )
    }

    fun setCantidad(cantidad: String) {
        val nuevo = uiMtoState.copy(
            cantidad = cantidad.toIntOrNull() ?: 0
        )
        uiMtoState = nuevo.copy(
            datosObligatorios = validarDatos(nuevo)
        )
    }

    fun setTipo(tipo: TipoProducto) {
        uiMtoState = uiMtoState.copy(tipo = tipo)
    }

    fun setEstado(estado: Boolean) {
        uiMtoState = uiMtoState.copy(estado = estado)
    }

    fun setIdAula(idAula: Int?) {
        uiMtoState = uiMtoState.copy(idAula = idAula)
    }

    fun setIdArmario(idArmario: Int?) {
        uiMtoState = uiMtoState.copy(idArmario = idArmario)
    }

    fun setIdDptoFiltro(idDpto: String) {
        uiFiltroState = uiFiltroState.copy(
            idDpto = idDpto
        )
    }

    fun setFecAltaDesdeFiltro(fecAltaDesde: String) {
        uiFiltroState = uiFiltroState.copy(
            fecAltaDesde = fecAltaDesde
        )
    }

    fun setEstadoFiltro(estado: Estado) {
        uiFiltroState = uiFiltroState.copy(
            estado = estado
        )
    }

    fun setIdAulaFiltro(idAula: Int?) {
        uiFiltroState = uiFiltroState.copy(
            idAula = idAula
        )
    }

    fun setIdArmarioFiltro(idArmario: Int?) {
        uiFiltroState = uiFiltroState.copy(
            idArmario = idArmario
        )
    }

    /* Funciones Lógica Productos *****************************************************************/
    fun resetBusState() {
        setProductoSelected(null)
    }

    fun resetStates(idDpto: Int = 0, id: Long = 0, fecAlta: String = "") {
        uiBusState = uiBusState.copy(
            productoSelected = null,
            showDlgBorrar = false
        )

        uiMtoState = uiMtoState.copy(
            idDpto = idDpto,
            id = id,
            fecAlta = fecAlta,
            fecBaja = "",
            descripcion = "",
            cantidad = 0,
            tipo = TipoProducto.HARD,
            estado = true,
            idAula = null,
            idArmario = null,
            datosObligatorios = false
        )

    }

    fun resetInfoState() {
        uiInfoState = ProductosInfoState.Loading
    }

    fun filtrarProductos() {
        val filtro = if (uiFiltroState.idDpto == "0") "" else uiFiltroState.idDpto
        val filtro1 = uiFiltroState.estado.ordinal
        val filtro2 = if (uiFiltroState.idAula == 0) null else uiFiltroState.idAula
        val filtro3 = if (uiFiltroState.idArmario == 0) null else uiFiltroState.idArmario
        val filtro4 = if (uiFiltroState.fecAltaDesde == "") null else uiFiltroState.fecAltaDesde
        uiProductosState =
            productosRepository.getAllProductosByFiltro(
                idDpto = filtro,
                fecAltaDesde = filtro4 ?: "",
                idAula = filtro2,
                idArmario = filtro3,
                estado = filtro1)
                .map { ProductosState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = ProductosState()
                )
    }

    fun alta() {
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = ProductosInfoState.Error
            return
        }

        viewModelScope.launch {
            uiInfoState = try {
                productosRepository.insertProducto(uiMtoState.toProducto())
                ProductosInfoState.Success
            } catch (e: SQLiteException) {
                ProductosInfoState.Error
            }
        }
    }

    fun editar() {
        if (uiMtoState.id == 0L) {
            uiInfoState = ProductosInfoState.Error
            return
        }
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = ProductosInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                productosRepository.updateProducto(uiMtoState.toProducto())
                ProductosInfoState.Success
            } catch (e: SQLiteException) {
                ProductosInfoState.Error
            }
        }
    }

    fun baja() {
        if (uiMtoState.id == 0L) {
            uiInfoState = ProductosInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                productosRepository.deleteProducto(uiMtoState.toProducto())
                ProductosInfoState.Success
            } catch (e: SQLiteException) {
                ProductosInfoState.Error
            }
        }
    }

    /* Factory ************************************************************************************/

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val productosRepository = application.container.productosRepository
                ProductosVM(productosRepository)
            }
        }
    }
}
