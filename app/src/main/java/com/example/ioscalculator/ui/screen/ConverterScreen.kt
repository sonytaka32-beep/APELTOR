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
import androidx.compose.foundation.layout.spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ioscalculator.R
import com.example.ioscalculator.converter.Category
import com.example.ioscalculator.converter.ConverterEngine
import com.example.ioscalculator.ui.theme.iOSCalculatorTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalContentColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    onBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(ConverterEngine.categories[0]) }
    var fromUnit by remember { mutableStateOf(selectedCategory.units[0]) }
    var toUnit by remember { mutableStateOf(selectedCategory.units[1]) }
    var inputValue by remember { mutableStateOf("") }
    var showFromMenu by remember { mutableStateOf(false) }
    var showToMenu by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }

    val result = remember(inputValue, fromUnit, toUnit, selectedCategory) {
        if (inputValue.isNotBlank()) {
            val value = inputValue.toDoubleOrNull() ?: 0.0
            ConverterEngine.convert(value, fromUnit, toUnit, selectedCategory)
        } else 0.0
    }

    val keyboardController = LocalSoftwareKeyboardController.current

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
                        text = stringResource(R.string.unit_converter),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(modifier = Modifier.width(60.dp))
                }

                // Category selector
                CategorySelector(
                    category = selectedCategory,
                    onClick = { showCategoryMenu = true }
                )

                // Input area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // From unit
                        UnitInputRow(
                            label = stringResource(R.string.from_unit),
                            unit = fromUnit,
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            onUnitClick = { showFromMenu = true },
                            keyboardController = keyboardController
                        )

                        // Swap arrow
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp).padding(vertical = 8.dp)
                        )

                        // To unit
                        UnitOutputRow(
                            label = stringResource(R.string.to_unit),
                            unit = toUnit,
                            value = ConverterEngine.formatResult(result)
                        )
                    }
                }

                // Quick category buttons
                QuickCategories(
                    selectedCategory = selectedCategory,
                    onCategoryClick = { cat ->
                        selectedCategory = cat
                        fromUnit = cat.units[0]
                        toUnit = cat.units[1]
                        inputValue = ""
                    }
                )

                // Dropdown menus
                if (showFromMenu) UnitDropdown(
                    units = selectedCategory.units,
                    selected = fromUnit,
                    onSelect = { fromUnit = it; showFromMenu = false },
                    onDismiss = { showFromMenu = false }
                )
                if (showToMenu) UnitDropdown(
                    units = selectedCategory.units,
                    selected = toUnit,
                    onSelect = { toUnit = it; showToMenu = false },
                    onDismiss = { showToMenu = false }
                )
                if (showCategoryMenu) CategoryDropdown(
                    categories = ConverterEngine.categories,
                    selected = selectedCategory,
                    onSelect = { cat ->
                        selectedCategory = cat
                        fromUnit = cat.units[0]
                        toUnit = cat.units[1]
                        inputValue = ""
                        showCategoryMenu = false
                    },
                    onDismiss = { showCategoryMenu = false }
                )
            }
        }
    }
}

@Composable
fun CategorySelector(category: Category, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = category.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Expand",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun UnitInputRow(
    label: String,
    unit: com.example.ioscalculator.converter.Unit,
    value: String,
    onValueChange: (String) -> Unit,
    onUnitClick: () -> Unit,
    keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp)
                    .height(56.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Button(onClick = onUnitClick) {
                Text(text = unit.symbol, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun UnitOutputRow(label: String, unit: com.example.ioscalculator.converter.Unit, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            Text(text = unit.symbol, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun QuickCategories(
    selectedCategory: Category,
    onCategoryClick: (Category) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Categories",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
        )
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            items(ConverterEngine.categories) { cat ->
                val isSelected = cat.id == selectedCategory.id
                Button(
                    onClick = { onCategoryClick(cat) },
                    modifier = Modifier.width(100.dp).height(80.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = cat.name, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 2, textAlign = TextAlign.Center, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
fun UnitDropdown(
    units: List<com.example.ioscalculator.converter.Unit>,
    selected: com.example.ioscalculator.converter.Unit,
    onSelect: (com.example.ioscalculator.converter.Unit) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        units.forEach { unit ->
            DropdownMenuItem(
                text = { Text(text = "${unit.name} (${unit.symbol})") },
                onClick = { onSelect(unit) }
            )
        }
    }
}

@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selected: Category,
    onSelect: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        categories.forEach { cat ->
            DropdownMenuItem(
                text = { Text(text = cat.name) },
                onClick = { onSelect(cat) }
            )
        }
    }
}

object Icons {
    object Default {
        val SwapVert = androidx.compose.material.icons.filled.SwapVert
        val ExpandMore = androidx.compose.material.icons.filled.ExpandMore
    }
}