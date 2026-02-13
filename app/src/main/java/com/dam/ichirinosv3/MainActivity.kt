package com.dam.ichirinosv3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dam.ichirinosv3.ui.main.MainApp
import com.dam.ichirinosv3.ui.main.MainVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IChirinosv3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val mainVM: MainVM = viewModel(factory = MainVM.Factory)
                    val windowSize = calculateWindowSizeClass(this)
                    MainApp(mainVM = mainVM, windowSize = windowSize.widthSizeClass)
                }
            }
        }
    }
}
