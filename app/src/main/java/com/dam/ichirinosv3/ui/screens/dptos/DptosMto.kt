package com.dam.ichirinosv3.ui.screens.dptos

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.data.DataSource
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun DptosMto(
    dptosVM: DptosVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    DptosMto(
        uiMtoState = dptosVM.uiMtoState,
        onIdValueChange = { dptosVM.setId(it) },
        onNombreValueChange = { dptosVM.setNombre(it) },
        onClaveValueChange = { dptosVM.setClave(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = { if (dptosVM.uiBusState.dptoSelected == null) dptosVM.alta() else dptosVM.editar() },
        modifier = modifier
    )

    when (dptosVM.uiInfoState) {
        is DptosInfoState.Loading -> {
        }

        is DptosInfoState.Success -> {
            dptosVM.resetInfoState()
            onShowSnackbar(
                if (dptosVM.uiBusState.dptoSelected == null) stringResource(R.string.msg_alta_ok)
                else stringResource(R.string.msg_editar_ok)
            )
            onNavUp()
        }

        is DptosInfoState.Error -> {
            dptosVM.resetInfoState()
            onShowSnackbar(
                if (dptosVM.uiBusState.dptoSelected == null) stringResource(R.string.msg_alta_ko)
                else stringResource(R.string.msg_editar_ko)
            )
        }
    }
}

@Composable
fun DptosMto(
    uiMtoState: DptosMtoState,
    onIdValueChange: (String) -> Unit,
    onNombreValueChange: (String) -> Unit,
    onClaveValueChange: (String) -> Unit,
    onCancelarClick: () -> Unit,
    onAceptarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            OutlinedTextField(
                value = uiMtoState.id,
                onValueChange = { onIdValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_dptos_id),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = uiMtoState.nombre,
                onValueChange = { onNombreValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_dptos_nombre),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            OutlinedTextField(
                value = uiMtoState.clave,
                onValueChange = { onClaveValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_dptos_clave),
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
                isError = !uiMtoState.datosObligatorios,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
        }
        DptosMtoAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun DptosMtoAcciones(
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
fun DptosMtoPreview() {
    IChirinosv3Theme {
        DptosMto(
            uiMtoState = DataSource.dptosMock[0].toDptosMtoState(),
            onIdValueChange = {},
            onNombreValueChange = {},
            onClaveValueChange = {},
            onCancelarClick = {},
            onAceptarClick = {}
        )
    }
}
