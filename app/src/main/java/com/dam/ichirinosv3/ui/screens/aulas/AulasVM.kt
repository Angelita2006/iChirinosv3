package com.dam.ichirinosv3.ui.screens.aulas

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
import com.dam.ichirinosv3.data.model.Aula
import com.dam.ichirinosv3.data.repository.AulasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AulasVM(private val aulasRepository: AulasRepository) : ViewModel() {

    lateinit var uiAulasState: StateFlow<AulasState>

    var uiBusState by mutableStateOf(AulasBusState())
        private set

    var uiMtoState by mutableStateOf(AulasMtoState())
        private set

    var uiFiltroState by mutableStateOf(AulasFiltroState())
        private set

    var uiInfoState: AulasInfoState by mutableStateOf(AulasInfoState.Loading)
        private set

    /* Funciones Estado del Buscador **************************************************************/

    fun setAulaSelected(aula: Aula?) {
        uiBusState = uiBusState.copy(
            aulaSelected = aula,
            showDlgBorrar = false
        )
        uiMtoState = if (aula != null) {
            uiMtoState.copy(
                idDpto = aula.idDpto.toString(),
                id = aula.id.toString(),
                nombre = aula.nombre,
                datosObligatorios = true
            )
        } else {
            uiMtoState.copy(
                idDpto = "",
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
            idDpto = if (idDpto.isDigitsOnly()) idDpto else "",
            datosObligatorios = (idDpto != "" && uiMtoState.id != "" && uiMtoState.nombre != "")
        )
    }

    fun setId(id: String) {
        uiMtoState = uiMtoState.copy(
            id = if (id.isDigitsOnly()) id else "",
            datosObligatorios = (uiMtoState.idDpto != "" && id != "" && uiMtoState.nombre != "")
        )
    }

    fun setNombre(nombre: String) {
        uiMtoState = uiMtoState.copy(
            nombre = nombre,
            datosObligatorios = (uiMtoState.idDpto != "" && uiMtoState.id != "" && nombre != "")
        )
    }

    /* Funciones Estado del Filtro ****************************************************************/

    fun setIdDptoFiltro(idDpto: String) {
        uiFiltroState = uiFiltroState.copy(
            idDpto = idDpto
        )
    }

    /* Funciones Lógica ***************************************************************************/

    fun resetBusState() {
        setAulaSelected(null)
    }

    fun filtrarAulas() {
        uiAulasState =
            aulasRepository.getAllAulasByFiltro(uiFiltroState.idDpto)
                .map { AulasState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = AulasState()
                )
    }

    fun resetStates(idDpto: String = "", id: String = "") {
        uiBusState = uiBusState.copy(
            aulaSelected = null,
            showDlgBorrar = false
        )
        uiMtoState = uiMtoState.copy(
            idDpto = idDpto,
            id = id,
            nombre = "",
            datosObligatorios = false
        )
    }

    fun resetInfoState() {
        uiInfoState = AulasInfoState.Loading
    }

    fun alta() {
        if (uiMtoState.idDpto == "0" || !uiMtoState.datosObligatorios) {    // admin!!
            uiInfoState = AulasInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                aulasRepository.insertAula(uiMtoState.toAula())
                AulasInfoState.Success
            } catch (e: SQLiteException) {
                AulasInfoState.Error
            }
        }
    }

    fun editar() {
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = AulasInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                aulasRepository.updateAula(uiMtoState.toAula())
                AulasInfoState.Success
            } catch (e: SQLiteException) {
                AulasInfoState.Error
            }
        }
    }

    fun baja() {
        if (!uiMtoState.datosObligatorios) {
            uiInfoState = AulasInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                aulasRepository.deleteAula(uiMtoState.toAula())
                AulasInfoState.Success
            } catch (e: SQLiteException) {
                AulasInfoState.Error
            }
        }
    }

    /* Factory ************************************************************************************/

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val aulasRepository = application.container.aulasRepository
                AulasVM(aulasRepository = aulasRepository)
            }
        }
    }
}
