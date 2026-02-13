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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.main.MainState
import com.dam.ichirinosv3.ui.main.MainVM
import com.dam.ichirinosv3.ui.screens.dptos.DptosState
import com.dam.ichirinosv3.ui.screens.dptos.DptosVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun AulasFiltro(
    mainVM: MainVM,
    dptosVM: DptosVM,
    aulasVM: AulasVM,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()

    AulasFiltro(
        uiMainState = mainVM.uiMainState,
        uiDptosState = uiDptosState,
        uiFiltroState = aulasVM.uiFiltroState,
        onDptoClick = { aulasVM.setIdDptoFiltro(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = {
            aulasVM.resetStates()
            aulasVM.filtrarAulas()
            onNavUp()
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AulasFiltro(
    uiMainState: MainState,
    uiDptosState: DptosState,
    uiFiltroState: AulasFiltroState,
    onDptoClick: (String) -> Unit,
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
            ExposedDropdownMenuBox(
                expanded = expandedDptos,
                onExpandedChange = {
                    if (uiMainState.login?.id == 0) {
                        expandedDptos = !expandedDptos
                    }
                }
            ) {
                OutlinedTextField(
                    value = if (uiFiltroState.idDpto != "" && uiFiltroState.idDpto != "0") {
                        uiDptosState.departamentos.find { it.id == uiFiltroState.idDpto.toInt() }?.nombre
                            ?: ""
                    } else {
                        ""
                    },
                    onValueChange = { },
                    modifier = Modifier
                        .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    enabled = uiMainState.login?.id == 0,
                    readOnly = true,
                    label = {
                        Text(
                            text = stringResource(R.string.txt_filtro_aulas_dptos),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDptos) },
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expandedDptos,
                    onDismissRequest = { expandedDptos = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "") },
                        onClick = {
                            onDptoClick("")
                            expandedDptos = false
                        }
                    )
                    uiDptosState.departamentos.forEach { item ->
                        if (item.id != 0) {
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
            }
        }
        AulasFiltroAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun AulasFiltroAcciones(
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
fun AulasFiltroPreview() {
    IChirinosv3Theme {
        AulasFiltro(
            uiMainState = MainState(),
            uiDptosState = DptosState(),
            uiFiltroState = AulasFiltroState(),
            onDptoClick = {},
            onCancelarClick = {},
            onAceptarClick = {}
        )
    }
}
