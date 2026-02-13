package com.dam.ichirinosv3.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun PrefScreen(
    mainVM: MainVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    PrefScreen(
        uiPrefState = mainVM.uiPrefState,
        onDSNameValueChange = { mainVM.setPrefDSName(it) },
        onDBNameValueChange = { mainVM.setPrefDBName(it) },
        onShowLoginCheckedChange = { mainVM.setPrefShowLoginOnStart(it) },
        onTimeSplashValueChange = { mainVM.setPrefDefaultTimeSplash(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = { mainVM.savePreferences() },
        modifier = modifier
    )

    when (mainVM.uiInfoState) {
        is MainInfoState.Loading -> {
        }

        is MainInfoState.Success -> {
            mainVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_pref_ok))
            onNavUp()
        }

        is MainInfoState.Error -> {
            mainVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_pref_ko))
            keyboardController?.hide()
        }
    }
}

@Composable
fun PrefScreen(
    uiPrefState: PrefState,
    onDSNameValueChange: (String) -> Unit,
    onDBNameValueChange: (String) -> Unit,
    onShowLoginCheckedChange: (Boolean) -> Unit,
    onTimeSplashValueChange: (String) -> Unit,
    onCancelarClick: () -> Unit,
    onAceptarClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = uiPrefState.dsName,
                onValueChange = { onDSNameValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_pref_dsname),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiPrefState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            OutlinedTextField(
                value = uiPrefState.dbName,
                onValueChange = { onDBNameValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_pref_dbname),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiPrefState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(0.5.dp, Color.Black), shape = RoundedCornerShape(10)
                    )
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_pref_showloginonstart),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = uiPrefState.showLoginOnStart,
                    onCheckedChange = { onShowLoginCheckedChange(it) }
                )
            }
            OutlinedTextField(
                value = uiPrefState.defaultTimeSplash,
                onValueChange = { onTimeSplashValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.txt_pref_defaulttimesplash),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiPrefState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
        PrefScreenAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun PrefScreenAcciones(
    onCancelarClick: () -> Unit,
    onAceptarClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        FloatingActionButton(
            onClick = onCancelarClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close_32dp),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(
            onClick = onAceptarClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check_32dp),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrefScreenPreview() {
    IChirinosv3Theme {
        PrefScreen(
            uiPrefState = PrefState(),
            onDSNameValueChange = {},
            onDBNameValueChange = {},
            onShowLoginCheckedChange = {},
            onTimeSplashValueChange = {},
            onCancelarClick = {},
            onAceptarClick = {}
        )
    }
}
