package ai.mlxdroid.selfmassage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ai.mlxdroid.selfmassage.ui.screens.MassageDetailScreen
import ai.mlxdroid.selfmassage.ui.screens.MassageListScreen
import ai.mlxdroid.selfmassage.ui.screens.ZoneListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ZoneList) {
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
            MassageDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
