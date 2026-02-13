package com.dam.ichirinosv3.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DlgSeleccionFecha(
    onFechaSelected: (String) -> Unit,
    onCancelarClick: () -> Unit,
    onAceptarClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = { onCancelarClick() },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onFechaSelected(
                            SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            ).format(it)
                        )
                    }
                    onAceptarClick(false)
                }
            ) { Text(stringResource(R.string.but_aceptar)) }
        },
        dismissButton = {
            TextButton(onClick = { onAceptarClick(false) }) {
                Text(stringResource(R.string.but_cancelar))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DlgFechaPreview() {
    IChirinosv3Theme {
        DlgSeleccionFecha(onFechaSelected = {}, onCancelarClick = {}, onAceptarClick = {})
    }
}

