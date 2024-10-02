package com.example.task1_kotelevskyi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            val uiFactory: UIFactory = if (isDarkTheme) {
                DarkThemeFactory()
            } else {
                LightThemeFactory()
            }

            MaterialTheme(
                colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        uiFactory = uiFactory,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        isDarkTheme = !isDarkTheme
                    }
                }
            }
        }
    }
}

// Інтерфейси елементів інтерфейсу
interface ThemeButton {
    @Composable
    fun Render(text: String, onClick: () -> Unit, modifier: Modifier)
}

interface ThemeText {
    @Composable
    fun Render(text: String, modifier: Modifier)
}

// Інтерфейс абстрактної фабрики
interface UIFactory {
    fun createButton(): ThemeButton
    fun createText(): ThemeText
}

// Конкретні фабрики
class LightThemeFactory : UIFactory {
    override fun createButton(): ThemeButton = LightButton()
    override fun createText(): ThemeText = LightText()
}

class DarkThemeFactory : UIFactory {
    override fun createButton(): ThemeButton = DarkButton()
    override fun createText(): ThemeText = DarkText()
}

//Реалізації кнопок
class LightButton : ThemeButton {
    @Composable
    override fun Render(text: String, onClick: () -> Unit, modifier: Modifier) {
        Button(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(text = text)
        }
    }
}

class DarkButton : ThemeButton {
    @Composable
    override fun Render(text: String, onClick: () -> Unit, modifier: Modifier) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            modifier = modifier
        ) {
            Text(text = text, color = Color.White)
        }
    }
}

// Реалізації тексту
class LightText : ThemeText {
    @Composable
    override fun Render(text: String, modifier: Modifier) {
        Text(
            text = text,
            modifier = modifier
        )
    }
}

class DarkText : ThemeText {
    @Composable
    override fun Render(text: String, modifier: Modifier) {
        Text(
            text = text,
            color = Color.White,
            modifier = modifier
        )
    }
}

// Компонент Greeting
@Composable
fun Greeting(
    name: String,
    uiFactory: UIFactory,
    modifier: Modifier = Modifier,
    onThemeSwitch: () -> Unit
) {
    val button = uiFactory.createButton()
    val text = uiFactory.createText()

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        text.Render(text = "Hello $name!", modifier = Modifier)
        Spacer(modifier = Modifier.height(16.dp))
        button.Render(
            text = "Switch Theme",
            onClick = onThemeSwitch,
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val uiFactory = LightThemeFactory()
    MaterialTheme {
        Greeting(
            name = "Android",
            uiFactory = uiFactory,
            onThemeSwitch = {}
        )
    }
}