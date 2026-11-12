package com.example.ioscalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ioscalculator.R
import com.example.ioscalculator.engine.CalculatorEngine
import com.example.ioscalculator.preferences.PreferencesRepository
import com.example.ioscalculator.ui.theme.iOSCalculatorTheme

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    preferences: PreferencesRepository
) {
    var themeMode by remember { mutableStateOf(0) }
    var angleMode by remember { mutableStateOf(CalculatorEngine.AngleMode.DEG) }

    iOSCalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                // Top bar
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onBack) {
                        Text("← Back", fontSize = 16.sp)
                    }
                    Text(
                        text = stringResource(R.string.settings),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(modifier = Modifier.width(60.dp))
                }

                // Settings list
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingSection(title = "Appearance") {
                        SettingRow(
                            title = "Theme",
                            subtitle = when (themeMode) { 0 -> "System"; 1 -> "Light"; 2 -> "Dark"; else -> "" },
                            onClick = { themeMode = (themeMode + 1) % 3 }
                        )
                    }

                    SettingSection(title = "Calculator") {
                        SettingRow(
                            title = "Angle Mode",
                            subtitle = angleMode.name,
                            onClick = { angleMode = if (angleMode == CalculatorEngine.AngleMode.DEG) CalculatorEngine.AngleMode.RAD else CalculatorEngine.AngleMode.DEG }
                        )
                    }

                    SettingSection(title = "About") {
                        SettingRow(
                            title = "Version",
                            subtitle = "1.0.0",
                            onClick = {}
                        )
                        SettingRow(
                            title = "Open Source Licenses",
                            subtitle = "",
                            onClick = { /* open licenses */ }
                        )
                        SettingRow(
                            title = "Privacy Policy",
                            subtitle = "",
                            onClick = { /* open privacy */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            content()
        }
    }
}

@Composable
fun SettingRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 16.sp)
            Text(text = subtitle, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

object Icons {
    object Default {
        val ChevronRight = androidx.compose.material.icons.filled.ChevronRight
    }
}