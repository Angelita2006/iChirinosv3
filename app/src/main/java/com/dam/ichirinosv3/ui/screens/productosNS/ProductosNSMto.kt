package com.dam.ichirinosv3.ui.screens.productosNS

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
import com.dam.ichirinosv3.ui.screens.dptos.DptosState
import com.dam.ichirinosv3.ui.screens.dptos.DptosVM
import com.dam.ichirinosv3.ui.screens.productos.ProductosState
import com.dam.ichirinosv3.ui.screens.productos.ProductosVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun ProductosNSMto(
    dptosVM: DptosVM,
    productosVM: ProductosVM,
    productosNSVM: ProductosNSVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()
    val uiProductosState by productosVM.uiProductosState.collectAsState()

    ProductosNSMto(
        uiDptosState = uiDptosState,
        uiProductosState = uiProductosState,
        uiMtoState = productosNSVM.uiMtoState,
        onIdDptoValueChange = { if (it.isDigitsOnly()) productosNSVM.setIdDpto(it) },
        onIdProductoValueChange = { if (it.isDigitsOnly()) productosNSVM.setIdProducto(it) },
        onIdProductoNSValueChange = { if (it.isDigitsOnly()) productosNSVM.setIdProductoNS(it) },
        onNumSerieValueChange = { productosNSVM.setNumSerie(it) },
        onCancelarClick = { onNavUp() },
        onAceptarClick = {
            if (productosNSVM.uiBusState.productoNSSelected?.numSerie?.isEmpty() ?: true) { productosNSVM.alta() } else { productosNSVM.editar() }
        },
        modifier = modifier
    )

    when (productosNSVM.uiInfoState) {
        is ProductosNSInfoState.Loading -> {
        }

        is ProductosNSInfoState.Success -> {
            productosNSVM.resetInfoState()
            onShowSnackbar(
                if (productosNSVM.uiBusState.productoNSSelected == null) stringResource(R.string.msg_alta_ok)
                else stringResource(R.string.msg_editar_ok)
            )
            onNavUp()
        }

        is ProductosNSInfoState.Error -> {
            productosNSVM.resetInfoState()
            onShowSnackbar(
                if (productosNSVM.uiBusState.productoNSSelected == null) stringResource(R.string.msg_alta_ko)
                else stringResource(R.string.msg_editar_ko)
            )
        }
    }
}

@Composable
fun ProductosNSMto(
    uiDptosState: DptosState,
    uiProductosState: ProductosState,
    uiMtoState: ProductosNSMtoState,
    onIdDptoValueChange: (String) -> Unit,
    onIdProductoValueChange: (String) -> Unit,
    onIdProductoNSValueChange: (String) -> Unit,
    onNumSerieValueChange: (String) -> Unit,
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
                        text = stringResource(R.string.txt_mto_productosns_iddpto),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = uiProductosState.productos.find { it.id.toString() == uiMtoState.idProducto }?.descripcion ?: "",
                onValueChange = { onIdProductoValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_productosns_idproducto),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = uiMtoState.idProductoNS,
                onValueChange = { onIdProductoNSValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_productosns_idproductons),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = uiMtoState.numSerie,
                onValueChange = { onNumSerieValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.txt_mto_productosns_numserie),
                        fontWeight = FontWeight.Bold
                    )
                },
                isError = !uiMtoState.datosObligatorios,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
        }
        ProductosNSMtoAcciones(
            onCancelarClick = { onCancelarClick() },
            onAceptarClick = { onAceptarClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun ProductosNSMtoAcciones(
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
fun ProductosNSMtoPreview() {
    IChirinosv3Theme {
        ProductosNSMto(
            uiDptosState = DptosState(),
            uiProductosState = ProductosState(),
            uiMtoState = DataSource.productosNSMock[0].toProductosNSMtoState(),
            onIdDptoValueChange = {},
            onIdProductoValueChange = {},
            onIdProductoNSValueChange = {},
            onNumSerieValueChange = {},
            onCancelarClick = {},
            onAceptarClick = {}
        )
    }
}
