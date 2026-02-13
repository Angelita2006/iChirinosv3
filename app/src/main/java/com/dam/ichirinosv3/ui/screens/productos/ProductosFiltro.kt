package com.dam.ichirinosv3.ui.screens.productos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.RadioButton
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
import com.dam.ichirinosv3.ui.screens.armarios.ArmariosState
import com.dam.ichirinosv3.ui.screens.armarios.ArmariosVM
import com.dam.ichirinosv3.ui.screens.aulas.AulasState
import com.dam.ichirinosv3.ui.screens.aulas.AulasVM
import com.dam.ichirinosv3.ui.screens.dptos.DptosState
import com.dam.ichirinosv3.ui.screens.dptos.DptosVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun ProductosFiltro(
    mainVM: MainVM,
    dptosVM: DptosVM,
    productosVM: ProductosVM,
    aulasVM: AulasVM,
    armariosVM: ArmariosVM,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()
    val uiAulasState by aulasVM.uiAulasState.collectAsState()
    val uiArmariosState by armariosVM.uiArmariosState.collectAsState()

    ProductosFiltro(
        uiMainState = mainVM.uiMainState,
        uiDptosState = uiDptosState,
        uiAulasState = uiAulasState,
        uiArmariosState = uiArmariosState,
        uiFiltroState = productosVM.uiFiltroState,
        onFecAltaDesdeValueChange = { productosVM.setFecAltaDesdeFiltro(it) },
        onEstadoChange = { productosVM.setEstadoFiltro(it) },
        onAulaChange = { productosVM.setIdAulaFiltro(it) },
        onArmarioChange = { productosVM.setIdArmarioFiltro(it) },
        onDptoClick = { productosVM.setIdDptoFiltro(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = {
            productosVM.filtrarProductos()
            onNavUp()
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosFiltro(
    uiMainState: MainState,
    uiDptosState: DptosState,
    uiAulasState: AulasState,
    uiArmariosState: ArmariosState,
    uiFiltroState: ProductosFiltroState,
    onFecAltaDesdeValueChange: (String) -> Unit,
    onEstadoChange: (Estado) -> Unit,
    onAulaChange: (Int?) -> Unit,
    onArmarioChange: (Int?) -> Unit,
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
                            text = stringResource(R.string.txt_filtro_productos_dptos),
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxWidth()
            ) {
                FechaSelector(
                    label = stringResource(R.string.txt_filtro_productos_fecaltadesde),
                    date = uiFiltroState.fecAltaDesde,
                    onFechaSelected = { onFecAltaDesdeValueChange(it) },
                    modifier = modifier.weight(1f)
                )
            }
            EstadoSelector(
                estado = uiFiltroState.estado,
                onEstadoChange = { onEstadoChange(it) }
            )
            MiDropdownMenu(
                enabled = true,
                label = stringResource(R.string.txt_mto_productos_idaula),
                selectedId = uiFiltroState.idAula,
                options = uiAulasState.aulas.filter { it.idDpto.toString() == uiFiltroState.idDpto },
                optionId = { it.id },
                optionLabel = { it.nombre },
                onSelectionChange = { onAulaChange(it) }
            )
            MiDropdownMenu(
                enabled = (uiFiltroState.idAula != null),
                label = stringResource(R.string.txt_mto_productos_idarmario),
                selectedId = uiFiltroState.idArmario,
                options = if (uiFiltroState.idAula != null) uiArmariosState.armarios.filter { it.idDpto.toString() == uiFiltroState.idDpto && it.idAula == uiFiltroState.idAula } else uiArmariosState.armarios.filter { it.idDpto.toString() == uiFiltroState.idDpto },
                optionId = { it.id },
                optionLabel = { it.nombre },
                onSelectionChange = { onArmarioChange(it) }
            )
        }
        ProductosFiltroAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
fun EstadoSelector(
    estado: Estado,
    onEstadoChange: (Estado) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.txt_mto_productos_tipo) + ": ",
            fontWeight = FontWeight.Bold
        )
        Estado.values().forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                RadioButton(
                    selected = estado == it,
                    onClick = { onEstadoChange(it) }
                )
                Text(it.name)
            }
        }
    }
}

@Composable
private fun ProductosFiltroAcciones(
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
fun ProductosFiltroPreview() {
    IChirinosv3Theme {
        ProductosFiltro(
            uiMainState = MainState(),
            uiAulasState = AulasState(),
            uiArmariosState = ArmariosState(),
            uiDptosState = DptosState(),
            uiFiltroState = ProductosFiltroState(),
            onFecAltaDesdeValueChange = {},
            onEstadoChange = {},
            onAulaChange = {},
            onArmarioChange = {},
            onDptoClick = {},
            onCancelarClick = {},
            onAceptarClick = {},
            modifier = Modifier
        )
    }
}
