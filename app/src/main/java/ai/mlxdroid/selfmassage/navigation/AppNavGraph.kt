package ai.mlxdroid.selfmassage.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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

    val isOnTabRoot = listOf(
        ZoneList::class.qualifiedName,
        RoutineList::class.qualifiedName
    ).any { currentRoute?.startsWith(it ?: "") == true }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = ZoneList,
            modifier = Modifier.fillMaxSize()
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

        // Floating tab bar
        if (isOnTabRoot) {
            FloatingTabBar(
                selectedTab = if (currentRoute?.startsWith(RoutineList::class.qualifiedName ?: "") == true) 0 else 1,
                onTabSelected = { tab ->
                    val dest = if (tab == 0) RoutineList else ZoneList
                    navController.navigate(dest) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun FloatingTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.10f))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        TabItem(
            label = "Routines",
            selectedIcon = Icons.Filled.PlayCircle,
            unselectedIcon = Icons.Outlined.PlayCircle,
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        TabItem(
            label = "Zones",
            selectedIcon = Icons.Filled.Eco,
            unselectedIcon = Icons.Outlined.Eco,
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
    }
}

@Composable
private fun TabItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Icon(
            if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
