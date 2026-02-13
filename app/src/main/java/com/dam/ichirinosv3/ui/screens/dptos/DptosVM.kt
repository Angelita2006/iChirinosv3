package com.dam.ichirinosv3.ui.screens.dptos

import android.database.sqlite.SQLiteException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dam.ichirinosv3.MainApplication
import com.dam.ichirinosv3.data.model.Departamento
import com.dam.ichirinosv3.data.repository.DptosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DptosVM(private val dptosRepository: DptosRepository) : ViewModel() {

    val uiDptosState: StateFlow<DptosState> =
        dptosRepository.getAllDptos().map { DptosState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DptosState()
            )

    var uiBusState by mutableStateOf(DptosBusState())
        private set

    var uiMtoState by mutableStateOf(DptosMtoState())
        private set

    var uiInfoState: DptosInfoState by mutableStateOf(DptosInfoState.Loading)
        private set

    /* Funciones Estado del Buscador **************************************************************/

    fun setDptoSelected(dpto: Departamento?) {
        uiBusState = uiBusState.copy(
            dptoSelected = dpto,
            showDlgBorrar = false
        )
        uiMtoState = if (dpto != null) {
            uiMtoState.copy(
                id = dpto.id.toString(),
                nombre = dpto.nombre,
                clave = dpto.clave,
                datosObligatorios = true
            )
        } else {
            uiMtoState.copy(
                id = "",
                nombre = "",
                clave = "",
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

    fun setId(id: String) {
        uiMtoState = uiMtoState.copy(
            id = if (id.isDigitsOnly()) id else "",
            datosObligatorios = (id != "" && uiMtoState.nombre != "" && uiMtoState.clave != "")
        )
    }

    fun setNombre(nombre: String) {
        uiMtoState = uiMtoState.copy(
            nombre = nombre,
            datosObligatorios = (uiMtoState.id != "" && nombre != "" && uiMtoState.clave != "")
        )
    }

    fun setClave(clave: String) {
        uiMtoState = uiMtoState.copy(
            clave = clave,
            datosObligatorios = (uiMtoState.id != "" && uiMtoState.nombre != "" && clave != "")
        )
    }

    /* Funciones Lógica ***************************************************************************/

    fun resetBusState() {
        setDptoSelected(null)
    }

    fun resetStates(id: String = "") {
        uiBusState = uiBusState.copy(
            dptoSelected = null,
            showDlgBorrar = false
        )
        uiMtoState = uiMtoState.copy(
            id = id,
            nombre = "",
            clave = "",
            datosObligatorios = false
        )
    }

    fun resetInfoState() {
        uiInfoState = DptosInfoState.Loading
    }

    fun alta() {
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = DptosInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                dptosRepository.insertDpto(uiMtoState.toDpto())
                DptosInfoState.Success
            } catch (e: SQLiteException) {
                DptosInfoState.Error
            }
        }
    }

    fun editar() {
        if (uiMtoState.id == "0" || !uiMtoState.datosObligatorios) {    // admin!!
            uiInfoState = DptosInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                dptosRepository.updateDpto(uiMtoState.toDpto())
                DptosInfoState.Success
            } catch (e: SQLiteException) {
                DptosInfoState.Error
            }
        }
    }

    fun baja() {
        if (uiMtoState.id == "0" || !uiMtoState.datosObligatorios) {    // admin!!
            uiInfoState = DptosInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                dptosRepository.deleteDpto(uiMtoState.toDpto())
                DptosInfoState.Success
            } catch (e: SQLiteException) {
                DptosInfoState.Error
            }
        }
    }

    /* Factory ************************************************************************************/

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val dptosRepository = application.container.dptosRepository
                DptosVM(dptosRepository = dptosRepository)
            }
        }
    }
}
