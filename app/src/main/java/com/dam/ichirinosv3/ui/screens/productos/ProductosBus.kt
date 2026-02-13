package com.dam.ichirinosv3.ui.screens.productos

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.dam.ichirinosv3.data.model.Producto
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.components.DlgConfirmacion
import com.dam.ichirinosv3.ui.main.MainState
import com.dam.ichirinosv3.ui.main.MainVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.filter

@Composable
fun ProductosBus(
    mainVM: MainVM,
    productosVM: ProductosVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    onNavDown: () -> Unit,
    onNavProductosNS: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiProductosState by productosVM.uiProductosState.collectAsState()

    ProductosBus(
        uiMainState = mainVM.uiMainState,
        uiProductosState = uiProductosState,
        uiBusState = productosVM.uiBusState,
        onProductoSelectedChange = { productosVM.setProductoSelected(it) },
        onDlgConfirmacionClick = { productosVM.setShowDlgBorrar(it) },
        onCancelarClick = { onNavUp() },
        onProductosNSClick = { onNavProductosNS() },
        onAddEditClick = {
            if (productosVM.uiBusState.productoSelected == null) {
                productosVM.resetStates(
                    idDpto = mainVM.uiMainState.login?.id!!,
                    id = (uiProductosState.productos
                        .filter { it.idDpto == mainVM.uiMainState.login?.id }
                        .maxOfOrNull { it.id }
                        ?.plus(1)
                        ?: 1),
                    fecAlta = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            }
            onNavDown()
        },
        modifier = modifier
    )

    if (productosVM.uiBusState.showDlgBorrar) {
        DlgConfirmacion(
            mensaje = R.string.msg_borrar,
            onCancelarClick = { productosVM.setShowDlgBorrar(false) },
            onAceptarClick = {
                productosVM.setShowDlgBorrar(false)
                productosVM.baja()
            }
        )
    }

    when (productosVM.uiInfoState) {
        is ProductosInfoState.Loading -> {
        }

        is ProductosInfoState.Success -> {
            productosVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ok))
            productosVM.setProductoSelected(null)
        }

        is ProductosInfoState.Error -> {
            productosVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ko))
        }
    }
}

@Composable
fun ProductosBus(
    uiMainState: MainState,
    uiProductosState: ProductosState,
    uiBusState: ProductosBusState,
    onProductoSelectedChange: (Producto?) -> Unit,
    onDlgConfirmacionClick: (Boolean) -> Unit,
    onCancelarClick: () -> Unit,
    onProductosNSClick: () -> Unit,
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
            items(uiProductosState.productos) {
                if (uiMainState.login?.id == 0 || uiMainState.login?.id == it.idDpto) {
                    ProductoCard(
                        producto = it,
                        itemSelected = uiBusState.productoSelected == it,
                        onItemSelectedChange = {
                            onProductoSelectedChange(if (uiBusState.productoSelected != it) it else null)
                        },
                        modifier = modifier
                    )
                }
            }
        }
        ProductosBusAcciones(
            uiMainState = uiMainState,
            productoSelected = (uiBusState.productoSelected != null),
            onCancelarClick = { onCancelarClick() },
            onProductosNSClick = { onProductosNSClick() },
            onDeleteClick = { onDlgConfirmacionClick(true) },
            onAddEditClick = { onAddEditClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun ProductoCard(
    producto: Producto,
    itemSelected: Boolean,
    onItemSelectedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onItemSelectedChange() },
        border = if (itemSelected) BorderStroke(1.dp, Color.Black) else null
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(R.drawable.ic_shoppingmode_32dp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "ID: ${producto.id}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Fec. Alta: ${producto.fecAlta}",
                        fontSize = 13.sp
                    )
                    Text(
                        text = producto.descripcion,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic
                    )
                }

                Icon(
                    painter = painterResource(
                        if (expanded) R.drawable.ic_arrow_drop_up_32dp
                        else R.drawable.ic_arrow_drop_down_32dp
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            expanded = !expanded
                        }
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "Tipo: ${producto.tipo}",
                        fontSize = 13.sp
                    )

                    if (producto.fecBaja.isNotBlank()) {
                        Text(
                            text = "Fec. Baja: ${producto.fecBaja}",
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = "Aula: ${producto.idAula ?: "No asignada"}",
                        fontSize = 13.sp
                    )

                    Text(
                        text = "Armario: ${producto.idArmario ?: "No asignado"}",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductosBusAcciones(
    uiMainState: MainState,
    productoSelected: Boolean,
    onCancelarClick: () -> Unit,
    onProductosNSClick: () -> Unit,
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

        Spacer(modifier = Modifier.width(8.dp))
        if (productoSelected) {
            FloatingActionButton(
                onClick = onProductosNSClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_tag_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (uiMainState.login?.id != 0) {
                Spacer(modifier = Modifier.width(8.dp))
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
        }

        if (uiMainState.login?.id != 0) {
            FloatingActionButton(
                onClick = onAddEditClick,
            ) {
                Icon(
                    painter = painterResource(if (!productoSelected) R.drawable.ic_add_32dp else R.drawable.ic_edit_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProductosBusPreview() {
    IChirinosv3Theme {
        ProductosBus(
            uiMainState = MainState(),
            uiProductosState = ProductosState(),
            uiBusState = ProductosBusState(),
            onProductoSelectedChange = {},
            onDlgConfirmacionClick = {},
            onCancelarClick = {},
            onProductosNSClick = {},
            onAddEditClick = {}
        )
    }
}
