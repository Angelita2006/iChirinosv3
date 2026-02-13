package com.dam.ichirinosv3.ui.screens.productosNS

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.data.model.ProductoNS
import com.dam.ichirinosv3.ui.components.DlgConfirmacion
import com.dam.ichirinosv3.ui.main.MainState
import com.dam.ichirinosv3.ui.main.MainVM
import com.dam.ichirinosv3.ui.screens.productos.ProductosBusState
import com.dam.ichirinosv3.ui.screens.productos.ProductosVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun ProductosNSBus(
    mainVM: MainVM,
    productosVM: ProductosVM,
    productosNSVM: ProductosNSVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    onNavDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiProductosNSState by productosNSVM.uiProductosNSState.collectAsState()

    ProductosNSBus(
        uiMainState = mainVM.uiMainState,
        uiProductosBusState = productosVM.uiBusState,
        uiProductosNSState = uiProductosNSState,
        uiBusState = productosNSVM.uiBusState,
        onProductoNSSelectedChange = { productosNSVM.setProductoNSSelected(it) },
        onDlgConfirmacionClick = { productosNSVM.setShowDlgBorrar(it) },
        onCancelarClick = { onNavUp() },
        onAddEditClick = {
            if (productosNSVM.uiBusState.productoNSSelected == null) {
                productosNSVM.resetStates(
                    idDpto = mainVM.uiMainState.login?.id.toString(),
                    idProducto = productosVM.uiBusState.productoSelected?.id.toString(),
                    idProductoNS = (uiProductosNSState.productosNS
                        .filter { it.idDpto == mainVM.uiMainState.login?.id }
                        .maxOfOrNull { it.idProductoNS }
                        ?.plus(1)
                        ?: 1).toString()
                )
            }
            onNavDown()
        },
        modifier = modifier
    )

    if (productosNSVM.uiBusState.showDlgBorrar) {
        DlgConfirmacion(
            mensaje = R.string.msg_borrar,
            onCancelarClick = { productosNSVM.setShowDlgBorrar(false) },
            onAceptarClick = {
                productosNSVM.setShowDlgBorrar(false)
                productosNSVM.baja()
            }
        )
    }

    when (productosNSVM.uiInfoState) {
        is ProductosNSInfoState.Loading -> {
        }

        is ProductosNSInfoState.Success -> {
            productosNSVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ok))
            productosNSVM.setProductoNSSelected(null)
        }

        is ProductosNSInfoState.Error -> {
            productosNSVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ko))
        }
    }
}

@Composable
fun ProductosNSBus(
    uiMainState: MainState,
    uiProductosBusState: ProductosBusState,
    uiProductosNSState: ProductosNSState,
    uiBusState: ProductosNSBusState,
    onProductoNSSelectedChange: (ProductoNS?) -> Unit,
    onDlgConfirmacionClick: (Boolean) -> Unit,
    onCancelarClick: () -> Unit,
    onAddEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiProductosNSState.productosNS) {
                val producto = uiProductosBusState.productoSelected
                if (producto != null && producto.idDpto == it.idDpto && producto.id == it.idProducto) {
                    ProductoNSCard(
                        productoNS = it,
                        itemSelected = uiBusState.productoNSSelected == it,
                        onItemSelectedChange = {
                            onProductoNSSelectedChange(if (uiBusState.productoNSSelected != it) it else null)
                        },
                        modifier = modifier
                    )
                }
            }
        }
        ProductosNSBusAcciones(
            uiMainState = uiMainState,
            productoNSSelected = (uiBusState.productoNSSelected != null),
            onCancelarClick = { onCancelarClick() },
            onDeleteClick = { onDlgConfirmacionClick(true) },
            onAddEditClick = { onAddEditClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun ProductoNSCard(
    productoNS: ProductoNS,
    itemSelected: Boolean,
    onItemSelectedChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onItemSelectedChange() },
        border = if (itemSelected) BorderStroke(1.dp, Color.Black) else null
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                Image(
                    painter = painterResource(R.drawable.ic_tag_32dp),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = "IDDpto: " + productoNS.idDpto.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "IDProducto: " + productoNS.idProducto.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "IDProductoNS: " + productoNS.idProductoNS.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "NumSerie: " + productoNS.numSerie, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
private fun ProductosNSBusAcciones(
    uiMainState: MainState,
    productoNSSelected: Boolean,
    onCancelarClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddEditClick: () -> Unit,
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
        if (uiMainState.login?.id != 0) {
            Spacer(modifier = Modifier.width(8.dp))
            if (productoNSSelected) {
                FloatingActionButton(
                    onClick = onDeleteClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete_32dp),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            FloatingActionButton(
                onClick = onAddEditClick,
            ) {
                Icon(
                    painter = painterResource(if (!productoNSSelected) R.drawable.ic_add_32dp else R.drawable.ic_edit_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProductosNSBusPreview() {
    IChirinosv3Theme {
        ProductosNSBus(
            uiMainState = MainState(),
            uiProductosBusState = ProductosBusState(),
            uiProductosNSState = ProductosNSState(),
            uiBusState = ProductosNSBusState(),
            onProductoNSSelectedChange = {},
            onDlgConfirmacionClick = {},
            onCancelarClick = {},
            onAddEditClick = {}
        )
    }
}
