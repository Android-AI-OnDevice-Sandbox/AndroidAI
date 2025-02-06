package com.sandbox.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sandbox.ai.ui.main.PickPhotoScreen
import com.sandbox.ai.ui.main.MainViewModel
import com.sandbox.ai.ui.theme.AndroidAITheme

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PickPhotoScreen(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
