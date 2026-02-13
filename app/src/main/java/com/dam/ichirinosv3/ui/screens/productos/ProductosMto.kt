package com.dam.ichirinosv3.ui.screens.productos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.dam.ichirinosv3.data.model.TipoProducto
import com.dam.ichirinosv3.data.repository.DptosRepository
import com.dam.ichirinosv3.ui.components.DlgSeleccionFecha
import com.dam.ichirinosv3.ui.screens.armarios.ArmariosState
import com.dam.ichirinosv3.ui.screens.armarios.ArmariosVM
import com.dam.ichirinosv3.ui.screens.aulas.AulasState
import com.dam.ichirinosv3.ui.screens.aulas.AulasVM
import com.dam.ichirinosv3.ui.screens.dptos.DptosState
import com.dam.ichirinosv3.ui.screens.dptos.DptosVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProductosMto(
    dptosVM: DptosVM,
    aulasVM: AulasVM,
    armariosVM: ArmariosVM,
    productosVM: ProductosVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()
    val uiAulasState by aulasVM.uiAulasState.collectAsState()
    val uiArmariosState by armariosVM.uiArmariosState.collectAsState()

    ProductosMto(
        uiMtoState = productosVM.uiMtoState,
        uiDptosState = uiDptosState,
        uiAulasState = uiAulasState,
        uiArmariosState = uiArmariosState,
        onFecAltaValueChange = { productosVM.setFecAlta(it) },
        onFecBajaValueChange = { productosVM.setFecBaja(it) },
        onEstadoValueChange = { productosVM.setEstado(it) },
        onDescripcionValueChange = { productosVM.setDescripcion(it) },
        onCantidadValueChange = { productosVM.setCantidad(it) },
        onTipoValueChange = { productosVM.setTipo(it) },
        onIdAulaValueChange = { productosVM.setIdAula(it) },
        onIdArmarioValueChange = { productosVM.setIdArmario(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = { if (productosVM.uiBusState.productoSelected == null) { productosVM.alta() } else { productosVM.editar() } },
        modifier = modifier
    )

    when (productosVM.uiInfoState) {
        is ProductosInfoState.Loading -> {
        }

        is ProductosInfoState.Success -> {
            productosVM.resetInfoState()
            onShowSnackbar(
                if (productosVM.uiBusState.productoSelected == null) stringResource(R.string.msg_alta_ok)
                else stringResource(R.string.msg_editar_ok)
            )
            onNavUp()
        }

        is ProductosInfoState.Error -> {
            productosVM.resetInfoState()
            onShowSnackbar(
                if (productosVM.uiBusState.productoSelected == null) stringResource(R.string.msg_alta_ko)
                else stringResource(R.string.msg_editar_ko)
            )
        }
    }
}

@Composable
fun ProductosMto(
    uiMtoState: ProductosMtoState,
    uiDptosState: DptosState,
    uiAulasState: AulasState,
    uiArmariosState: ArmariosState,
    onFecAltaValueChange: (String) -> Unit,
    onFecBajaValueChange: (String) -> Unit,
    onEstadoValueChange: (Boolean) -> Unit,
    onDescripcionValueChange: (String) -> Unit,
    onCantidadValueChange: (String) -> Unit,
    onTipoValueChange: (TipoProducto) -> Unit,
    onIdAulaValueChange: (Int?) -> Unit,
    onIdArmarioValueChange: (Int?) -> Unit,
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
                value = uiDptosState.departamentos.find { it.id == uiMtoState.idDpto }?.nombre ?: "",
                onValueChange = {  },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_productos_iddpto),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = if (uiMtoState.id == 0L) "" else uiMtoState.id.toString(),
                onValueChange = {  },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_productos_id),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxWidth()
            ) {
                FechaSelector(
                    label = stringResource(R.string.txt_mto_productos_fecalta),
                    date = uiMtoState.fecAlta,
                    onFechaSelected = { onFecAltaValueChange(it) },
                    modifier = modifier.weight(1f)
                )
                FechaSelector(
                    label = stringResource(R.string.txt_mto_productos_fecbaja),
                    date = uiMtoState.fecBaja,
                    onFechaSelected = { onFecBajaValueChange(it) },
                    modifier = modifier.weight(1f)
                )
                EstadoCheck(
                    checked = uiMtoState.estado,
                    onCheckedChange = { checked ->
                        onEstadoValueChange(checked)
                        if (!checked) {
                            onFecBajaValueChange(
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(System.currentTimeMillis())
                            )
                        } else {
                            onFecBajaValueChange("")
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = uiMtoState.descripcion,
                onValueChange = { onDescripcionValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_productos_descripcion),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            OutlinedTextField(
                value = if (uiMtoState.cantidad == 0) "" else uiMtoState.cantidad.toString(),
                onValueChange = { onCantidadValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_productos_cantidad),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            TipoProductoSelector(
                tipo = uiMtoState.tipo,
                onTipoChange = { onTipoValueChange(it) }
            )
            MiDropdownMenu(
                enabled = true,
                label = stringResource(R.string.txt_mto_productos_idaula),
                selectedId = uiMtoState.idAula,
                options = uiAulasState.aulas.filter { it.idDpto == uiMtoState.idDpto },
                optionId = { it.id },
                optionLabel = { it.nombre },
                onSelectionChange = { onIdAulaValueChange(it) }
            )
            MiDropdownMenu(
                enabled = (uiMtoState.idAula != null),
                label = stringResource(R.string.txt_mto_productos_idarmario),
                selectedId = uiMtoState.idArmario,
                options = uiArmariosState.armarios.filter { it.idDpto == uiMtoState.idDpto && it.idAula == uiMtoState.idAula },
                optionId = { it.id },
                optionLabel = { it.nombre },
                onSelectionChange = { onIdArmarioValueChange(it) }
            )
        }
        ProductosMtoAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun ProductosMtoAcciones(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaSelector(
    label: String,
    date: String,
    onFechaSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var showDlgSeleccionFecha by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = { },
            label = { Text(label, modifier = Modifier.size(100.dp, 20.dp)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDlgSeleccionFecha = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_date_range_32dp),
                        contentDescription = "Seleccionar fecha",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDlgSeleccionFecha) {
            DlgSeleccionFecha(
                onFechaSelected = { onFechaSelected(it) },
                onCancelarClick = { showDlgSeleccionFecha = false },
                onAceptarClick = { showDlgSeleccionFecha = false }
            )
        }
    }
}

@Composable
fun EstadoCheck(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "Alta/Baja",
            fontWeight = FontWeight.Bold
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun TipoProductoSelector(
    tipo: TipoProducto,
    onTipoChange: (TipoProducto) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.txt_mto_productos_tipo) + ": ",
            fontWeight = FontWeight.Bold
        )
        TipoProducto.values().forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                RadioButton(
                    selected = tipo == it,
                    onClick = { onTipoChange(it) }
                )
                Text(it.name)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MiDropdownMenu(
    enabled: Boolean,
    label: String,
    selectedId: Int?,
    options: List<T>,
    optionId: (T) -> Int,
    optionLabel: (T) -> String,
    onSelectionChange: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedText = options
        .firstOrNull { optionId(it) == selectedId }
        ?.let { optionLabel(it) }
        ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            enabled = enabled,
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("—") },
                onClick = {
                    onSelectionChange(null)
                    expanded = false
                }
            )

            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
//                        if (enabled) {
                            onSelectionChange(optionId(option))
                            expanded = false
//                        }
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProductosMtoPreview() {
    IChirinosv3Theme {
        ProductosMto(
            uiMtoState = DataSource.productosMock[0].toProductosMtoState(),
            uiDptosState = DptosState(),
            uiAulasState = AulasState(),
            uiArmariosState = ArmariosState(),
            onFecAltaValueChange = {},
            onFecBajaValueChange = {},
            onEstadoValueChange = {},
            onDescripcionValueChange = {},
            onCantidadValueChange = {},
            onTipoValueChange = {},
            onIdAulaValueChange = {},
            onIdArmarioValueChange = {},
            onCancelarClick = {},
            onAceptarClick = {}
        )
    }
}
