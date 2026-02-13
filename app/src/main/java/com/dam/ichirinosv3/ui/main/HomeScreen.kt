package com.dam.ichirinosv3.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.components.DlgConfirmacion
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@SuppressLint("ContextCastToActivity")
@Composable
fun HomeScreen(
    mainVM: MainVM,
    onNavLogin: () -> Unit,
    onNavPrefs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiMainState = mainVM.uiMainState
    val activity = (LocalContext.current as? Activity)

    HomeScreen(
        uiMainState = uiMainState,
        modifier = modifier
    )

    if (uiMainState.showLogin) {
        mainVM.resetStates()
        onNavLogin()
        mainVM.setShowLogin(false)
    }

    if (uiMainState.showPrefs) {
        mainVM.resetStates()
        onNavPrefs()
        mainVM.setShowPrefs(false)
    }

    if (uiMainState.showDlgSalir) {
        DlgConfirmacion(
            mensaje = R.string.msg_salir,
            onCancelarClick = { mainVM.setShowDlgSalir(false) },
            onAceptarClick = {
                mainVM.setShowDlgSalir(false)
                activity?.finish()
            },
            modifier = modifier
        )
    }
}

@Composable
private fun HomeScreen(
    uiMainState: MainState,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = uiMainState.login?.nombre ?: stringResource(R.string.msg_nologin),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    IChirinosv3Theme {
        HomeScreen(uiMainState = MainState())
    }
}
