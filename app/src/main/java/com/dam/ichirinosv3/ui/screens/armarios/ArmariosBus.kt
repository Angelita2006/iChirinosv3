package com.dam.ichirinosv3.ui.screens.armarios

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
import com.dam.ichirinosv3.data.model.Armario
import com.dam.ichirinosv3.ui.components.DlgConfirmacion
import com.dam.ichirinosv3.ui.main.MainState
import com.dam.ichirinosv3.ui.main.MainVM
import com.dam.ichirinosv3.ui.screens.aulas.AulasBusState
import com.dam.ichirinosv3.ui.screens.aulas.AulasVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme
import kotlin.collections.filter

@Composable
fun ArmariosBus(
    mainVM: MainVM,
    aulasVM: AulasVM,
    armariosVM: ArmariosVM,
    onShowSnackbar: (String) -> Unit,
    onNavUp: () -> Unit,
    onNavDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiArmariosState by armariosVM.uiArmariosState.collectAsState()

    ArmariosBus(
        uiMainState = mainVM.uiMainState,
        uiAulasBusState = aulasVM.uiBusState,
        uiArmariosState = uiArmariosState,
        uiBusState = armariosVM.uiBusState,
        onArmarioSelectedChange = { armariosVM.setArmarioSelected(it) },
        onDlgConfirmacionClick = { armariosVM.setShowDlgBorrar(it) },
        onCancelarClick = { onNavUp() },
        onAddEditClick = {
            if (armariosVM.uiBusState.armarioSelected == null) {
                armariosVM.resetStates(
                    idDpto = mainVM.uiMainState.login?.id.toString(),
                    idAula = aulasVM.uiBusState.aulaSelected?.id.toString(),
                    id = (uiArmariosState.armarios
                        .filter { it.idDpto == mainVM.uiMainState.login?.id && it.idAula == aulasVM.uiBusState.aulaSelected?.id }
                        .maxOfOrNull { it.id }
                        ?.plus(1)
                        ?: 1).toString()
                )
            }
            onNavDown()
        },
        modifier = modifier
    )

    if (armariosVM.uiBusState.showDlgBorrar) {
        DlgConfirmacion(
            mensaje = R.string.msg_borrar,
            onCancelarClick = { armariosVM.setShowDlgBorrar(false) },
            onAceptarClick = {
                armariosVM.setShowDlgBorrar(false)
                armariosVM.baja()
            }
        )
    }

    when (armariosVM.uiInfoState) {
        is ArmariosInfoState.Loading -> {
        }

        is ArmariosInfoState.Success -> {
            armariosVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ok))
            armariosVM.setArmarioSelected(null)
        }

        is ArmariosInfoState.Error -> {
            armariosVM.resetInfoState()
            onShowSnackbar(stringResource(R.string.msg_baja_ko))
        }
    }

}

@Composable
fun ArmariosBus(
    uiMainState: MainState,
    uiAulasBusState: AulasBusState,
    uiArmariosState: ArmariosState,
    uiBusState: ArmariosBusState,
    onArmarioSelectedChange: (Armario?) -> Unit,
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
            items(uiArmariosState.armarios) {
                val aula = uiAulasBusState.aulaSelected
                if (aula != null && aula.id == it.idAula) {
                    ArmarioCard(
                        armario = it,
                        itemSelected = uiBusState.armarioSelected == it,
                        onItemSelectedChange = {
                            onArmarioSelectedChange(if (uiBusState.armarioSelected != it) it else null)
                        },
                        modifier = modifier
                    )
                }
            }
        }
        ArmariosBusAcciones(
            uiMainState = uiMainState,
            armarioSelected = (uiBusState.armarioSelected != null),
            onCancelarClick = { onCancelarClick() },
            onDeleteClick = { onDlgConfirmacionClick(true) },
            onAddEditClick = { onAddEditClick() },
            modifier = modifier
        )
    }
}

@Composable
private fun ArmarioCard(
    armario: Armario,
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
                    painter = painterResource(R.drawable.ic_pallet_32dp),
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
                    text = "ID: " + armario.id.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Departamento: " + armario.idDpto.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Aula: " + armario.idAula,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = armario.nombre, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
private fun ArmariosBusAcciones(
    uiMainState: MainState,
    armarioSelected: Boolean,
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
            if (armarioSelected) {
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
                    painter = painterResource(if (!armarioSelected) R.drawable.ic_add_32dp else R.drawable.ic_edit_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ArmariosBusPreview() {
    IChirinosv3Theme {
        ArmariosBus(
            uiMainState = MainState(),
            uiAulasBusState = AulasBusState(),
            uiArmariosState = ArmariosState(),
            uiBusState = ArmariosBusState(),
            onArmarioSelectedChange = {},
            onDlgConfirmacionClick = {},
            onCancelarClick = {},
            onAddEditClick = {}
        )
    }
}
