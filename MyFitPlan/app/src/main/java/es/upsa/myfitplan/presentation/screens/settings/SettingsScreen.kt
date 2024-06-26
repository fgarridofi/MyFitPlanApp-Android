package es.upsa.myfitplan.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import es.upsa.myfitplan.BuildConfig
import es.upsa.myfitplan.domain.model.settings.WeightUnit
import es.upsa.myfitplan.presentation.navigation.MyFitPlanScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    var weightUnit by remember { mutableStateOf(WeightUnit.Kg) }
    val coroutineScope = rememberCoroutineScope()
    var showUpgradeAlert by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        settingsViewModel.weightUnitFlow.collect { unit ->
            weightUnit = unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack(MyFitPlanScreens.HomeRoutinesScreen.name, false)
                        navController.navigate(MyFitPlanScreens.HomeRoutinesScreen.name)
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary

                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Divider(modifier = Modifier.padding(vertical = 2.dp))
            SettingItem {
                WeightUnitSelector(
                    selectedUnit = weightUnit,
                    onUnitSelected = { unit ->
                        weightUnit = unit
                        coroutineScope.launch {
                            settingsViewModel.saveWeightUnit(unit)
                        }
                    }
                )
            }
            Divider(modifier = Modifier.padding(vertical = 2.dp))
            SettingItem {
                UpgradeToPremiumButton {
                    showUpgradeAlert = true
                }
            }
        }
    }

    if (showUpgradeAlert) {
        AlertDialog(
            onDismissRequest = { showUpgradeAlert = false },
            title = { Text("Upgrade Plan") },
            text = {
                Text(
                    if (BuildConfig.FLAVOR == "Free")
                        "Unlock all premium features and take your fitness journey to the next level. No more excuses!"
                    else
                        "You are already premium! Enjoy all the exclusive features and keep up the great work!"
                )
            },
            confirmButton = {
                Button(onClick = { showUpgradeAlert = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun SettingItem(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        content()
    }
}

@Composable
fun WeightUnitSelector(selectedUnit: WeightUnit, onUnitSelected: (WeightUnit) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Select Weight Unit", style = MaterialTheme.typography.bodyLarge)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedUnit.name, style = MaterialTheme.typography.bodyLarge)
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Kilograms") },
                        onClick = {
                            onUnitSelected(WeightUnit.Kg)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Pounds") },
                        onClick = {
                            onUnitSelected(WeightUnit.Lb)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UpgradeToPremiumButton(onUpgrade: () -> Unit) {
    val buttonText = if (BuildConfig.FLAVOR == "Free") "Go Premium" else "My Subscription"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Plan",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Button(
            onClick = onUpgrade,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = buttonText, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
