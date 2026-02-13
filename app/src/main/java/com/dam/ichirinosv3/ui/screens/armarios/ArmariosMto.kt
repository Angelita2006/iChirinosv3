package com.dam.ichirinosv3.ui.screens.armarios

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.data.DataSource
import com.dam.ichirinosv3.ui.screens.aulas.AulasState
import com.dam.ichirinosv3.ui.screens.aulas.AulasVM
import com.dam.ichirinosv3.ui.screens.dptos.DptosState
import com.dam.ichirinosv3.ui.screens.dptos.DptosVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun ArmariosMto(
    dptosVM: DptosVM,
    aulasVM: AulasVM,
    armariosVM: ArmariosVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()
    val uiAulasState by aulasVM.uiAulasState.collectAsState()

    ArmariosMto(
        uiDptosState = uiDptosState,
        uiAulasState = uiAulasState,
        uiMtoState = armariosVM.uiMtoState,
        onIdDptoValueChange = { if (it.isDigitsOnly()) armariosVM.setIdDpto(it) },
        onIdAulaValueChange = { if (it.isDigitsOnly()) armariosVM.setIdAula(it) },
        onIdValueChange = { if (it.isDigitsOnly()) armariosVM.setId(it) },
        onNombreValueChange = { armariosVM.setNombre(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = { if (armariosVM.uiBusState.armarioSelected == null) {  armariosVM.alta() } else {  armariosVM.editar() } },
        modifier = modifier
    )

    when (armariosVM.uiInfoState) {
        is ArmariosInfoState.Loading -> {
        }

        is ArmariosInfoState.Success -> {
            armariosVM.resetInfoState()
            onShowSnackbar(
                if (armariosVM.uiBusState.armarioSelected == null) stringResource(R.string.msg_alta_ok)
                else stringResource(R.string.msg_editar_ok)
            )
            onNavUp()
        }

        is ArmariosInfoState.Error -> {
            armariosVM.resetInfoState()
            onShowSnackbar(
                if (armariosVM.uiBusState.armarioSelected == null) stringResource(R.string.msg_alta_ko)
                else stringResource(R.string.msg_editar_ko)
            )
        }
    }
}

@Composable
fun ArmariosMto(
    uiDptosState: DptosState,
    uiAulasState: AulasState,
    uiMtoState: ArmariosMtoState,
    onIdDptoValueChange: (String) -> Unit,
    onIdAulaValueChange: (String) -> Unit,
    onIdValueChange: (String) -> Unit,
    onNombreValueChange: (String) -> Unit,
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
                value = uiDptosState.departamentos.find { it.id.toString() == uiMtoState.idDpto }?.nombre ?: "",
                onValueChange = { onIdDptoValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_armarios_iddpto),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = uiAulasState.aulas.find { it.id.toString() == uiMtoState.idAula }?.nombre ?: "",
                onValueChange = { onIdAulaValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_armarios_idaula),
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
                        text = stringResource(R.string.txt_mto_armarios_id),
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
                        text = stringResource(R.string.txt_mto_armarios_nombre),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
        }
        ArmariosMtoAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun ArmariosMtoAcciones(
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
fun ArmariosMtoPreview() {
    IChirinosv3Theme {
        ArmariosMto(
            uiDptosState = DptosState(),
            uiAulasState = AulasState(),
            uiMtoState = DataSource.armariosMock[0].toArmariosMtoState(),
            onIdDptoValueChange = {},
            onIdAulaValueChange = {},
            onIdValueChange = {},
            onNombreValueChange = {},
            onCancelarClick = {},
            onAceptarClick = {}
        )
    }
}
