package com.dam.ichirinosv3.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

@Composable
fun DlgConfirmacion(
    @StringRes mensaje: Int,
    onCancelarClick: () -> Unit,
    onAceptarClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(R.string.app_name)) },
        text = { Text(stringResource(mensaje, 0)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onCancelarClick
            ) {
                Text(text = stringResource(R.string.but_cancelar))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onAceptarClick
            ) {
                Text(text = stringResource(R.string.but_aceptar))
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DlgConfirmacionPreview() {
    IChirinosv3Theme {
        DlgConfirmacion(mensaje = R.string.msg_salir, onCancelarClick = {}, onAceptarClick = {})
    }
}
