package com.dam.ichirinosv3.ui.screens.aulas

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
import com.dam.ichirinosv3.data.model.Aula
import com.dam.ichirinosv3.data.model.Departamento
import com.dam.ichirinosv3.ui.components.DlgConfirmacion
import com.dam.ichirinosv3.ui.main.MainState
import com.dam.ichirinosv3.ui.main.MainVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun AulasBus(
    mainVM: MainVM,
    aulasVM: AulasVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    onNavDown: () -> Unit,
    onNavArmariosBus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiAulasState by aulasVM.uiAulasState.collectAsState()

    AulasBus(
        uiMainState = mainVM.uiMainState,
        uiAulasState = uiAulasState,
        uiBusState = aulasVM.uiBusState,
        onAulaSelectedChange = { aulasVM.setAulaSelected(it) },
        onDlgConfirmacionClick = { aulasVM.setShowDlgBorrar(it) },
        onArmariosBusClick = { onNavArmariosBus() },
        onCancelarClick = { onNavUp() },
        onAddEditClick = {
            if (aulasVM.uiBusState.aulaSelected == null) {    // Nueva Aula
                aulasVM.resetStates(
                    idDpto = mainVM.uiMainState.login?.id.toString(),
                    id = (uiAulasState.aulas
                        .filter { it.idDpto == mainVM.uiMainState.login?.id }
                        .maxOfOrNull { it.id }
                        ?.plus(1)
                        ?: 1).toString())
            }
            onNavDown()
        },
        modifier = modifier
    )

    if (aulasVM.uiBusState.showDlgBorrar) {
        DlgConfirmacion(
            mensaje = R.string.msg_borrar,
            onCancelarClick = { aulasVM.setShowDlgBorrar(false) },
            onAceptarClick = {
                aulasVM.setShowDlgBorrar(false)
                aulasVM.baja()
            }
        )
    }

    when (aulasVM.uiInfoState) {
        is AulasInfoState.Loading -> {
        }

        is AulasInfoState.Success -> {
            aulasVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ok))
            aulasVM.setAulaSelected(null)
        }

        is AulasInfoState.Error -> {
            aulasVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ko))
        }
    }
}

@Composable
fun AulasBus(
    uiMainState: MainState,
    uiAulasState: AulasState,
    uiBusState: AulasBusState,
    onAulaSelectedChange: (Aula?) -> Unit,
    onDlgConfirmacionClick: (Boolean) -> Unit,
    onArmariosBusClick: () -> Unit,
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
            items(uiAulasState.aulas) {
                AulaCard(
                    aula = it,
                    itemSelected = uiBusState.aulaSelected == it,
                    onItemSelectedChange = {
                        onAulaSelectedChange(if (uiBusState.aulaSelected != it) it else null)
                    },
                    modifier = modifier
                )
            }
        }
        AulasBusAcciones(
            uiMainState = uiMainState,
            aulaSelected = (uiBusState.aulaSelected != null),
            onArmariosBusClick = { onArmariosBusClick() },
            onCancelarClick = { onCancelarClick() },
            onDeleteClick = { onDlgConfirmacionClick(true) },
            onAddEditClick = { onAddEditClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun AulaCard(
    aula: Aula,
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
                painter = painterResource(R.drawable.ic_store_32dp),
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
                    text = "IDDpto: " + aula.idDpto.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID: " + aula.id.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = aula.nombre, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
private fun AulasBusAcciones(
    uiMainState: MainState,
    aulaSelected: Boolean,
    onArmariosBusClick: () -> Unit,
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
        if (aulaSelected) {
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = onArmariosBusClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pallet_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        if (uiMainState.login?.id != 0) {
            if (aulaSelected) {
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
            }
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = onAddEditClick,
            ) {
                Icon(
                    painter = painterResource(if (!aulaSelected) R.drawable.ic_add_32dp else R.drawable.ic_edit_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AulasBusPreview() {
    IChirinosv3Theme {
        AulasBus(
            uiMainState = MainState(login = Departamento()),
            uiAulasState = AulasState(DataSource.aulasMock),
            uiBusState = AulasBusState(),
            onAulaSelectedChange = {},
            onDlgConfirmacionClick = {},
            onArmariosBusClick = {},
            onCancelarClick = {},
            onAddEditClick = {}
        )
    }
}
