package com.dam.ichirinosv3.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.screens.dptos.DptosState
import com.dam.ichirinosv3.ui.screens.dptos.DptosVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun LoginScreen(
    mainVM: MainVM,
    dptosVM: DptosVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LoginScreen(
        uiLoginState = mainVM.uiLoginState,
        uiDptosState = uiDptosState,
        onDptoClick = { mainVM.setIdDpto(it) },
        onClaveValueChange = { mainVM.setClave(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = {
            val dpto = if (mainVM.uiLoginState.idDpto != "") {
                uiDptosState.departamentos.find { it.id == mainVM.uiLoginState.idDpto.toInt() }
            } else {
                null
            }
            val ok = mainVM.setLogin(dpto)
            onShowSnackbar(context.resources.getString(if (ok) R.string.msg_login_ok else R.string.msg_login_ko))
            if (ok) {
                onNavUp()
            } else {
                keyboardController?.hide()
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiLoginState: LoginState,
    uiDptosState: DptosState,
    onDptoClick: (String) -> Unit,
    onClaveValueChange: (String) -> Unit,
    onCancelarClick: () -> Unit,
    onAceptarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedDptos by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
            ExposedDropdownMenuBox(
                expanded = expandedDptos,
                onExpandedChange = { expandedDptos = !expandedDptos }
            ) {
                OutlinedTextField(
                    value = if (uiLoginState.idDpto != "") {
                        uiDptosState.departamentos.find { it.id == uiLoginState.idDpto.toInt() }?.nombre
                            ?: ""
                    } else {
                        ""
                    },
                    onValueChange = { },
                    modifier = Modifier
                        .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    readOnly = true,
                    label = {
                        Text(
                            text = stringResource(R.string.txt_login_dptos),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDptos) },
                    isError = !uiLoginState.datosObligatorios,
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expandedDptos,
                    onDismissRequest = { expandedDptos = false }
                ) {
                    uiDptosState.departamentos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.nombre) },
                            onClick = {
                                onDptoClick(item.id.toString())
                                expandedDptos = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = uiLoginState.clave,
                onValueChange = { onClaveValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.txt_login_clave),
                        fontWeight = FontWeight.Bold
                    )
                },
                trailingIcon = {
                    val image =
                        if (passwordVisible) R.drawable.ic_visibility_32dp else R.drawable.ic_visibility_off_32dp
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(image),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                isError = !uiLoginState.datosObligatorios,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
        }
        LoginScreenAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun LoginScreenAcciones(
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
fun LoginScreenPreview() {
    IChirinosv3Theme {
        LoginScreen(
            uiLoginState = LoginState(),
            uiDptosState = DptosState(),
            onDptoClick = {},
            onClaveValueChange = {},
            onCancelarClick = { },
            onAceptarClick = { }
        )
    }
}
