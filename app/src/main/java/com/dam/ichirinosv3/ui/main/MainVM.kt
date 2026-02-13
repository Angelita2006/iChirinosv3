package com.dam.ichirinosv3.ui.main

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
import com.dam.ichirinosv3.data.model.Departamento
import com.dam.ichirinosv3.data.repository.MainRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.io.IOException

class MainVM(private val mainRepository: MainRepository) : ViewModel() {

    var uiMainState by mutableStateOf(MainState())
        private set

    var uiLoginState by mutableStateOf(LoginState())
        private set

    var uiPrefState by mutableStateOf(PrefState())
        private set

    var uiInfoState: MainInfoState by mutableStateOf(MainInfoState.Loading)
        private set

    /* Funciones Estado del Main ******************************************************************/

    fun setLogin(dpto: Departamento?): Boolean {
        if (!uiLoginState.datosObligatorios) return false
        if (dpto == null) {
            return false
        } else if (dpto.clave == uiLoginState.clave) {
            uiMainState = uiMainState.copy(
                login = dpto
            )
            return true
        }
        return false
    }

    fun setLogout(): Boolean {
        uiMainState = uiMainState.copy(
            login = null
        )
        return true
    }

    fun setShowLogin(show: Boolean) {
        uiMainState = uiMainState.copy(
            showLogin = show
        )
    }

    fun setShowPrefs(show: Boolean) {
        uiMainState = uiMainState.copy(
            showPrefs = show
        )
    }

    fun setShowDlgSalir(show: Boolean) {
        uiMainState = uiMainState.copy(
            showDlgSalir = show
        )
    }

    /* Funciones Estado del Login ******************************************************************/

    fun setIdDpto(idDpto: String) {
        uiLoginState = uiLoginState.copy(
            idDpto = idDpto,
            datosObligatorios = (idDpto != "" && uiLoginState.clave != "")
        )
    }

    fun setClave(clave: String) {
        uiLoginState = uiLoginState.copy(
            clave = clave,
            datosObligatorios = (uiLoginState.idDpto != "" && clave != "")
        )
    }

    /* Funciones Estado de Preferencias ***********************************************************/

    fun setPrefDSName(dsName: String) {
        uiPrefState = uiPrefState.copy(
            dsName = dsName,
            datosObligatorios = (dsName != "" && uiPrefState.dbName != "" && uiPrefState.defaultTimeSplash != "")
        )
    }

    fun setPrefDBName(dbName: String) {
        uiPrefState = uiPrefState.copy(
            dbName = dbName,
            datosObligatorios = (uiPrefState.dsName != "" && dbName != "" && uiPrefState.defaultTimeSplash != "")
        )
    }

    fun setPrefShowLoginOnStart(showLoginOnStart: Boolean) {
        uiPrefState = uiPrefState.copy(
            showLoginOnStart = showLoginOnStart,
            datosObligatorios = (uiPrefState.dsName != "" && uiPrefState.dbName != "" && uiPrefState.defaultTimeSplash != "")
        )
    }

    fun setPrefDefaultTimeSplash(defaultTimeSplash: String) {
        uiPrefState = uiPrefState.copy(
            defaultTimeSplash = defaultTimeSplash,
            datosObligatorios = (uiPrefState.dsName != "" && uiPrefState.dbName != "" && defaultTimeSplash != "")
        )
    }

    /* Funciones Lógica Main **********************************************************************/

    fun resetStates() {
        uiLoginState = uiLoginState.copy(
            idDpto = "",
            clave = "",
            datosObligatorios = false
        )
    }

    fun resetInfoState() {
        uiInfoState = MainInfoState.Loading
    }

    suspend fun getPreferences() {
        viewModelScope.async {
            mainRepository.getPreferences().take(1).collect {
                uiPrefState = uiPrefState.copy(
                    dsName = it.dsName,
                    dbName = it.dbName,
                    showLoginOnStart = it.showLoginOnStart,
                    defaultTimeSplash = it.defaultTimeSplash.toString(),
                    datosObligatorios = true
                )
                uiMainState = uiMainState.copy(
                    showLogin = uiPrefState.showLoginOnStart
                )
            }
        }.await()
    }

    fun savePreferences() {
        if (!uiPrefState.datosObligatorios) {
            uiInfoState = MainInfoState.Error
            return
        }
        viewModelScope.launch {
            uiInfoState = try {
                mainRepository.savePreferences(uiPrefState.toPreferencias())
                MainInfoState.Success
            } catch (e: IOException) {
                MainInfoState.Error
            }
        }
    }

    /* Factory ************************************************************************************/

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                MainVM(mainRepository = mainRepository)
            }
        }
    }
}
