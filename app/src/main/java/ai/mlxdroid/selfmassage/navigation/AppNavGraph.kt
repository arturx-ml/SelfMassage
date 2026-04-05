package ai.mlxdroid.selfmassage.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ai.mlxdroid.selfmassage.ui.screens.MassageDetailScreen
import ai.mlxdroid.selfmassage.ui.screens.MassageListScreen
import ai.mlxdroid.selfmassage.ui.screens.RoutineDetailScreen
import ai.mlxdroid.selfmassage.ui.screens.RoutineListScreen
import ai.mlxdroid.selfmassage.ui.screens.SessionScreen
import ai.mlxdroid.selfmassage.ui.screens.ZoneListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomNavRoutes = setOf(
        ZoneList::class.qualifiedName,
        RoutineList::class.qualifiedName
    )
    val showBottomNav = bottomNavRoutes.any { currentRoute?.startsWith(it ?: "") == true }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute?.startsWith(ZoneList::class.qualifiedName ?: "") == true,
                        onClick = {
                            navController.navigate(ZoneList) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Outlined.SelfImprovement, contentDescription = "Zones") },
                        label = { Text("Zones") }
                    )
                    NavigationBarItem(
                        selected = currentRoute?.startsWith(RoutineList::class.qualifiedName ?: "") == true,
                        onClick = {
                            navController.navigate(RoutineList) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Outlined.PlayCircle, contentDescription = "Routines") },
                        label = { Text("Routines") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ZoneList,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ZoneList> {
                ZoneListScreen(onZoneClick = { zone ->
                    navController.navigate(MassageList(zoneId = zone.id))
                })
            }

            composable<MassageList> {
                MassageListScreen(
                    onTechniqueClick = { zoneId, techniqueId ->
                        navController.navigate(MassageDetail(zoneId = zoneId, techniqueId = techniqueId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable<MassageDetail> {
                MassageDetailScreen(
                    onBack = { navController.popBackStack() },
                    onStartSession = { techniqueId ->
                        navController.navigate(TechniqueSession(techniqueId = techniqueId))
                    }
                )
            }

            composable<RoutineList> {
                RoutineListScreen(onRoutineClick = { routine ->
                    navController.navigate(RoutineDetail(routineId = routine.id))
                })
            }

            composable<RoutineDetail> {
                RoutineDetailScreen(
                    onBack = { navController.popBackStack() },
                    onStartRoutine = { routineId ->
                        navController.navigate(RoutineSession(routineId = routineId))
                    }
                )
            }

            composable<TechniqueSession> {
                SessionScreen(onBack = { navController.popBackStack() })
            }

            composable<RoutineSession> {
                SessionScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
