package com.dam.ichirinosv3.ui.screens.armarios

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
import com.dam.ichirinosv3.data.model.Armario
import com.dam.ichirinosv3.data.repository.ArmariosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArmariosVM(private val armariosRepository: ArmariosRepository) : ViewModel() {

    val uiArmariosState: StateFlow<ArmariosState> =
        armariosRepository.getAllArmarios().map { ArmariosState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(ArmariosVM.Companion.TIMEOUT_MILLIS),
                initialValue = ArmariosState()
            )

    var uiBusState by mutableStateOf(ArmariosBusState())
        private set

    var uiMtoState by mutableStateOf(ArmariosMtoState())
        private set

    var uiInfoState: ArmariosInfoState by mutableStateOf(ArmariosInfoState.Loading)
        private set

    /* Funciones Estado del Buscador **************************************************************/

    fun setArmarioSelected(armario: Armario?) {
        uiBusState = uiBusState.copy(
            armarioSelected = armario,
            showDlgBorrar = false
        )
        uiMtoState = if (armario != null) {
            uiMtoState.copy(
                idDpto = armario.idDpto.toString(),
                idAula = armario.idAula.toString(),
                id = armario.id.toString(),
                nombre = armario.nombre,
                datosObligatorios = true
            )
        } else {
            uiMtoState.copy(
                idDpto = "",
                idAula = "",
                id = "",
                nombre = "",
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
            datosObligatorios = (idDpto != "" && uiMtoState.idAula != "" && uiMtoState.id != "" && uiMtoState.nombre != "")
        )
    }

    fun setIdAula(idAula: String) {
        uiMtoState = uiMtoState.copy(
            idAula = idAula,
            datosObligatorios = (uiMtoState.idDpto != "" && idAula != "" && uiMtoState.id != "" && uiMtoState.nombre != "")
        )
    }

    fun setId(id: String) {
        uiMtoState = uiMtoState.copy(
            id = id,
            datosObligatorios = (uiMtoState.idDpto != "" && uiMtoState.idAula != "" && id != "" && uiMtoState.nombre != "")
        )
    }

    fun setNombre(nombre: String) {
        uiMtoState = uiMtoState.copy(
            nombre = nombre,
            datosObligatorios = (uiMtoState.idDpto != "" && uiMtoState.idAula != "" && uiMtoState.id != "" && nombre != "")
        )
    }

    /* Funciones Lógica Dptos *********************************************************************/

    fun resetBusState() {
        setArmarioSelected(null)
    }

    fun resetStates(idDpto: String, idAula: String, id: String) {
        uiBusState = uiBusState.copy(
            armarioSelected = null,
            showDlgBorrar = false
        )
        uiMtoState = uiMtoState.copy(
            idDpto = idDpto,
            idAula = idAula,
            id = id,
            nombre = "",
            datosObligatorios = false
        )
    }

    fun resetInfoState() {
        uiInfoState = ArmariosInfoState.Loading
    }

    fun alta() {
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = ArmariosInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                armariosRepository.insertArmario(uiMtoState.toArmario())
                ArmariosInfoState.Success
            } catch (e: SQLiteException) {
                ArmariosInfoState.Error
            }
        }
    }

    fun editar() {
        if (uiMtoState.id == "0") {
            uiInfoState = ArmariosInfoState.Error
            return
        }
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = ArmariosInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                armariosRepository.updateArmario(uiMtoState.toArmario())
                ArmariosInfoState.Success
            } catch (e: SQLiteException) {
                ArmariosInfoState.Error
            }
        }
    }

    fun baja() {
        if (uiMtoState.id == "0") {
            uiInfoState = ArmariosInfoState.Error
            return
        }
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = ArmariosInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                armariosRepository.deleteArmario(uiMtoState.toArmario())
                ArmariosInfoState.Success
            } catch (e: SQLiteException) {
                ArmariosInfoState.Error
            }
        }
    }

    /* Factory ************************************************************************************/

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val armariosRepository = application.container.armariosRepository
                ArmariosVM(armariosRepository = armariosRepository)
            }
        }
    }

}
