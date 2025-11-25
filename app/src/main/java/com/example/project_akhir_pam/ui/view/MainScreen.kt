package com.example.project_akhir_pam.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.project_akhir_pam.HistoryScreen
import com.example.project_akhir_pam.data.preferences.UserPreferenceRepositoryImpl
import com.example.project_akhir_pam.ui.settings.SettingsScreen
import com.example.project_akhir_pam.ui.settings.SettingsViewModel
import com.example.project_akhir_pam.ui.settings.SettingsViewModelFactory
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object History : Screen("history", "History", Icons.Filled.List)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

@Composable
fun MainScreen(
    viewModel: WaterViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.History,
        Screen.Settings
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(viewModel = viewModel)
            }

            composable(Screen.History.route) {
                HistoryScreen(viewModel = viewModel)
            }

            composable(Screen.Settings.route) {
                val context = LocalContext.current
                val repo = UserPreferenceRepositoryImpl(context)
                val viewModel: SettingsViewModel = viewModel(
                    factory = SettingsViewModelFactory(repo)
                )

                SettingsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

        }
    }
}

@Composable
fun HistoryScreenPlaceholder() {
    Surface(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "History Screen\n(Akan diisi oleh Orang 2)",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}


