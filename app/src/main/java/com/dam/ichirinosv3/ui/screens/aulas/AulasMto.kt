package com.dam.ichirinosv3.ui.screens.aulas

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.data.DataSource
import com.dam.ichirinosv3.ui.screens.dptos.DptosState
import com.dam.ichirinosv3.ui.screens.dptos.DptosVM
import com.dam.ichirinosv3.ui.screens.productos.MiDropdownMenu
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun AulasMto(
    dptosVM: DptosVM,
    aulasVM: AulasVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()

    AulasMto(
        uiDptosState = uiDptosState,
        uiMtoState = aulasVM.uiMtoState,
        onIdDptoValueChange = { aulasVM.setIdDpto(it) },
        onIdValueChange = { aulasVM.setId(it) },
        onNombreValueChange = { aulasVM.setNombre(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = { if (aulasVM.uiBusState.aulaSelected == null) aulasVM.alta() else aulasVM.editar() },
        modifier = modifier
    )

    when (aulasVM.uiInfoState) {
        is AulasInfoState.Loading -> {
        }

        is AulasInfoState.Success -> {
            aulasVM.resetInfoState()
            onShowSnackbar(
                if (aulasVM.uiBusState.aulaSelected == null) stringResource(R.string.msg_alta_ok)
                else stringResource(R.string.msg_editar_ok)
            )
            onNavUp()
        }

        is AulasInfoState.Error -> {
            aulasVM.resetInfoState()
            onShowSnackbar(
                if (aulasVM.uiBusState.aulaSelected == null) stringResource(R.string.msg_alta_ko)
                else stringResource(R.string.msg_editar_ko)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AulasMto(
    uiDptosState: DptosState,
    uiMtoState: AulasMtoState,
    onIdDptoValueChange: (String) -> Unit,
    onIdValueChange: (String) -> Unit,
    onNombreValueChange: (String) -> Unit,
    onCancelarClick: () -> Unit,
    onAceptarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedDptos by rememberSaveable { mutableStateOf(false) }

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
                value = uiDptosState.departamentos.find { it.id.toString() == uiMtoState.idDpto }?.nombre ?: "",
                onValueChange = { onIdDptoValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_aulas_iddpto),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = uiMtoState.id,
                onValueChange = { onIdValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_aulas_id),
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
        }
        AulasMtoAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun AulasMtoAcciones(
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
fun AulasMtoPreview() {
    IChirinosv3Theme {
        AulasMto(
            uiDptosState = DptosState(),
            uiMtoState = DataSource.aulasMock[0].toAulasMtoState(),
            onIdDptoValueChange = {},
            onIdValueChange = {},
            onNombreValueChange = {},
            onCancelarClick = {},
            onAceptarClick = {}
        )
    }
}
