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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.ioscalculator.currency.CurrencyEngine
import com.example.ioscalculator.currency.CurrencyRate
import com.example.ioscalculator.ui.theme.iOSCalculatorTheme
import kotlinx.coroutines.Delay
import kotlinx.coroutines.launch

@Composable
fun CurrencyScreen(
    onBack: () -> Unit
) {
    var fromCurrency by remember { mutableStateOf(CurrencyEngine.allCurrencies.find { it.code == "USD" }!!) }
    var toCurrency by remember { mutableStateOf(CurrencyEngine.allCurrencies.find { it.code == "EUR" }!!) }
    var inputValue by remember { mutableStateOf("") }
    var showFromMenu by remember { mutableStateOf(false) }
    var showToMenu by remember { mutableStateOf(false) }
    var rates by remember { mutableStateOf<Map<String, Double>>(CurrencyEngine.DEFAULT_RATES.mapValues { it.value.rate }) }
    var isUpdating by remember { mutableStateOf(false) }
    var lastUpdated by remember { mutableStateOf<String?>(null) }
    var isOffline by remember { mutableStateOf(false) }

    val result = remember(inputValue, fromCurrency, toCurrency, rates) {
        if (inputValue.isNotBlank()) {
            val value = inputValue.toDoubleOrNull() ?: 0.0
            CurrencyEngine.convert(value, fromCurrency.code, toCurrency.code, rates)
        } else 0.0
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    // Load cached rates on start
    LaunchedEffect(Unit) {
        // In real app, load from DataStore
    }

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
                        text = stringResource(R.string.currency_converter),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(onClick = {
                        isUpdating = true
                        // In real app, fetch from API
                        // Simulate update
                        Delay(1000)
                        lastUpdated = "Just now"
                        isUpdating = false
                        isOffline = false
                    }) {
                        if (isUpdating) {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier.size(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Update rates",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Status
                if (isOffline) {
                    Text(
                        text = stringResource(R.string.offline_mode),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, bottom = 8.dp)
                    )
                }
                lastUpdated?.let {
                    Text(
                        text = "Last updated: $it",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, bottom = 8.dp)
                    )
                }

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
                        // From currency
                        CurrencyInputRow(
                            label = stringResource(R.string.from_unit),
                            currency = fromCurrency,
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            onCurrencyClick = { showFromMenu = true },
                            keyboardController = keyboardController
                        )

                        // Swap arrow
                        Button(onClick = {
                            val temp = fromCurrency
                            fromCurrency = toCurrency
                            toCurrency = temp
                        }) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap currencies",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp).padding(vertical = 8.dp)
                            )
                        }

                        // To currency
                        CurrencyOutputRow(
                            label = stringResource(R.string.to_unit),
                            currency = toCurrency,
                            value = CurrencyEngine.formatResult(result)
                        )
                    }
                }

                // Currency list
                Text(
                    text = "Popular Currencies",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, top = 16.dp, bottom = 8.dp)
                )

                CurrencyList(
                    currencies = CurrencyEngine.allCurrencies,
                    selectedFrom = fromCurrency,
                    selectedTo = toCurrency,
                    onFromSelect = { c -> fromCurrency = c; showFromMenu = false },
                    onToSelect = { c -> toCurrency = c; showToMenu = false }
                )

                // Dropdown menus
                if (showFromMenu) CurrencyDropdown(
                    currencies = CurrencyEngine.allCurrencies,
                    selected = fromCurrency,
                    onSelect = { c -> fromCurrency = c; showFromMenu = false },
                    onDismiss = { showFromMenu = false }
                )
                if (showToMenu) CurrencyDropdown(
                    currencies = CurrencyEngine.allCurrencies,
                    selected = toCurrency,
                    onSelect = { c -> toCurrency = c; showToMenu = false },
                    onDismiss = { showToMenu = false }
                )
            }
        }
    }
}

@Composable
fun CurrencyInputRow(
    label: String,
    currency: CurrencyRate,
    value: String,
    onValueChange: (String) -> Unit,
    onCurrencyClick: () -> Unit,
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
            Button(onClick = onCurrencyClick) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currency.code, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = " ${currency.country.flag()}", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun CurrencyOutputRow(label: String, currency: CurrencyRate, value: String) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = currency.code, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = " ${currency.country.flag()}", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun CurrencyList(
    currencies: List<CurrencyRate>,
    selectedFrom: CurrencyRate,
    selectedTo: CurrencyRate,
    onFromSelect: (CurrencyRate) -> Unit,
    onToSelect: (CurrencyRate) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(currencies) { currency ->
            CurrencyListItem(
                currency = currency,
                isSelectedFrom = currency.code == selectedFrom.code,
                isSelectedTo = currency.code == selectedTo.code,
                onFromClick = { onFromSelect(currency) },
                onToClick = { onToSelect(currency) }
            )
        }
    }
}

@Composable
fun CurrencyListItem(
    currency: CurrencyRate,
    isSelectedFrom: Boolean,
    isSelectedTo: Boolean,
    onFromClick: () -> Unit,
    onToClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currency.code, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = " ${currency.country.flag()}", fontSize = 16.sp)
                    if (isSelectedFrom) {
                        Text(text = " FROM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 8.dp))
                    }
                    if (isSelectedTo) {
                        Text(text = " TO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 8.dp))
                    }
                }
                Text(text = currency.name, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row {
                Button(onClick = onFromClick, modifier = Modifier.padding(end = 8.dp)) {
                    Text("Set as FROM", fontSize = 12.sp)
                }
                Button(onClick = onToClick) {
                    Text("Set as TO", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun CurrencyDropdown(
    currencies: List<CurrencyRate>,
    selected: CurrencyRate,
    onSelect: (CurrencyRate) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        currencies.forEach { c ->
            DropdownMenuItem(
                text = { Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${c.code} ${c.country.flag()}", fontWeight = FontWeight.Bold)
                    Text(text = " - ${c.name}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } },
                onClick = { onSelect(c) }
            )
        }
    }
}

object Icons {
    object Default {
        val SwapVert = androidx.compose.material.icons.filled.SwapVert
        val Refresh = androidx.compose.material.icons.filled.Refresh
    }
}

fun String.flag(): String {
    // Convert country code to flag emoji
    return this.toUpperCase().map { c ->
        Character.toString(0x1F1E6 + c - 'A')
    }.joinToString("")
}