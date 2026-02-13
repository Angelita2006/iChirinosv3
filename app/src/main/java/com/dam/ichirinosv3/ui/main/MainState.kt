package com.dam.ichirinosv3.ui.main

import androidx.annotation.StringRes
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.data.Preferencias
import com.dam.ichirinosv3.data.model.Departamento

enum class AppScreens(@param:StringRes val title: Int) {
    Splash(title = R.string.title_splash),
    Home(title = R.string.app_name),
    Login(title = R.string.title_login),
    Prefs(title = R.string.title_prefs),
    DptosBus(title = R.string.title_dptosbus),
    DptosMto(title = R.string.title_dptosmto),
    AulasBus(title = R.string.title_aulasbus),
    AulasMto(title = R.string.title_aulasmto),
    AulasFiltro(title = R.string.title_aulasfiltro),
    ArmariosBus(title = R.string.title_armariosbus),
    ArmariosMto(title = R.string.title_armariosmto),
    ProductosBus(title = R.string.title_productosbus),
    ProductosFiltro(title = R.string.title_productosfiltro),
    ProductosMto(title = R.string.title_productosmto),
    ProductosNSBus(title = R.string.title_productosnsbus),
    ProductosNSMto(title = R.string.title_productosnsmto)
}

data class MainState(
    val login: Departamento? = null,
    val showLogin: Boolean = true,
    val showPrefs: Boolean = false,
    val showDlgSalir: Boolean = false
)

data class LoginState(
    val idDpto: String = "",
    val clave: String = "",
    val datosObligatorios: Boolean = false
)

data class PrefState(
    val dsName: String = "",
    val dbName: String = "",
    val showLoginOnStart: Boolean = false,
    val defaultTimeSplash: String = "",
    val datosObligatorios: Boolean = false
)

fun PrefState.toPreferencias(): Preferencias = Preferencias(
    dsName = dsName,
    dbName = dbName,
    showLoginOnStart = showLoginOnStart,
    defaultTimeSplash = defaultTimeSplash.toInt()
)

fun Preferencias.toPrefState(): PrefState = PrefState(
    dsName = dsName,
    dbName = dbName,
    showLoginOnStart = showLoginOnStart,
    defaultTimeSplash = defaultTimeSplash.toString()
)

sealed interface MainInfoState {
    data object Loading : MainInfoState
    data object Success : MainInfoState
    data object Error : MainInfoState
}
