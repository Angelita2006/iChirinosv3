package com.dam.ichirinosv3.ui.screens.dptos

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
import androidx.compose.material3.CardDefaults
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
import com.dam.ichirinosv3.data.DataSource
import com.dam.ichirinosv3.data.model.Departamento
import com.dam.ichirinosv3.ui.components.DlgConfirmacion
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun DptosBus(
    dptosVM: DptosVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    onNavDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiDptosState by dptosVM.uiDptosState.collectAsState()

    DptosBus(
        uiDptosState = uiDptosState,
        uiBusState = dptosVM.uiBusState,
        onDptoSelectedChange = { dptosVM.setDptoSelected(it) },
        onDlgConfirmacionClick = { dptosVM.setShowDlgBorrar(it) },
        onCancelarClick = { onNavUp() },
        onAddEditClick = {
            if (dptosVM.uiBusState.dptoSelected == null) {    // Nuevo Departamento
                dptosVM.resetStates(
                    id = (uiDptosState.departamentos
                        .maxOfOrNull { it.id }
                        ?.plus(1)
                        ?: 1).toString()
                )
            }
            onNavDown()
        },
        modifier = modifier
    )

    if (dptosVM.uiBusState.showDlgBorrar) {
        DlgConfirmacion(
            mensaje = R.string.msg_borrar,
            onCancelarClick = { dptosVM.setShowDlgBorrar(false) },
            onAceptarClick = {
                dptosVM.setShowDlgBorrar(false)
                dptosVM.baja()
            }
        )
    }

    when (dptosVM.uiInfoState) {
        is DptosInfoState.Loading -> {
        }

        is DptosInfoState.Success -> {
            dptosVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ok))
            dptosVM.setDptoSelected(null)
        }

        is DptosInfoState.Error -> {
            dptosVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ko))
        }
    }
}

@Composable
fun DptosBus(
    uiDptosState: DptosState,
    uiBusState: DptosBusState,
    onDptoSelectedChange: (Departamento?) -> Unit,
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
            items(uiDptosState.departamentos) {
                DptoCard(
                    dpto = it,
                    itemSelected = uiBusState.dptoSelected == it,
                    onItemSelectedChange = {
                        onDptoSelectedChange(if (uiBusState.dptoSelected != it) it else null)
                    },
                    modifier = modifier
                )
            }
        }
        DptosBusAcciones(
            dptoSelected = (uiBusState.dptoSelected != null),
            onCancelarClick = { onCancelarClick() },
            onDeleteClick = { onDlgConfirmacionClick(true) },
            onAddEditClick = { onAddEditClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun DptoCard(
    dpto: Departamento,
    itemSelected: Boolean,
    onItemSelectedChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onItemSelectedChange() },
        elevation = CardDefaults.cardElevation(8.dp),
        border = if (itemSelected) BorderStroke(1.dp, Color.Black) else null
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_storefront_32dp),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = "ID: " + dpto.id.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = dpto.nombre, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
private fun DptosBusAcciones(
    dptoSelected: Boolean,
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
        Spacer(modifier = Modifier.width(8.dp))
        if (dptoSelected) {
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
            onClick = onAddEditClick
        ) {
            Icon(
                painter = painterResource(if (!dptoSelected) R.drawable.ic_add_32dp else R.drawable.ic_edit_32dp),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DptosBusPreview() {
    IChirinosv3Theme {
        DptosBus(
            uiDptosState = DptosState(DataSource.dptosMock),
            uiBusState = DptosBusState(),
            onDptoSelectedChange = {},
            onDlgConfirmacionClick = {},
            onCancelarClick = {},
            onAddEditClick = {}
        )
    }
}
