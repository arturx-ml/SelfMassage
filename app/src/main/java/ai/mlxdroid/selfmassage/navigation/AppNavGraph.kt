package ai.mlxdroid.selfmassage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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

        composable<MassageList> { backStackEntry ->
            val route: MassageList = backStackEntry.toRoute()
            MassageListScreen(
                zoneId = route.zoneId,
                onTechniqueClick = { technique ->
                    navController.navigate(
                        MassageDetail(zoneId = route.zoneId, techniqueId = technique.id)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<MassageDetail> { backStackEntry ->
            val route: MassageDetail = backStackEntry.toRoute()
            MassageDetailScreen(
                techniqueId = route.techniqueId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
