package com.dam.ichirinosv3.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    mainVM: MainVM,
    onNavHome: () -> Unit,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(key1 = true) {
        mainVM.getPreferences()
        delay(mainVM.uiPrefState.defaultTimeSplash.toInt() * 1000L)
        onNavHome()
    }
    SplashScreen(modifier)
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        color = Color.White
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_chirinos),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Text(
                text = stringResource(R.string.app_name),
                color = Color.Red,
                fontSize = 32.sp
            )
            Text(
                text = stringResource(R.string.app_version),
                color = Color.Red,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SplashPreview() {
    IChirinosv3Theme {
        SplashScreen()
    }
}
