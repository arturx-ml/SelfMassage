package ai.mlxdroid.selfmassage.ui.screens

import ai.mlxdroid.selfmassage.data.MassageRepository
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.ZoneListViewModel

@Composable
fun ZoneListScreen(
    onZoneClick: (BodyZone) -> Unit,
    viewModel: ZoneListViewModel = hiltViewModel()
) {
    ZoneListContent(zones = viewModel.zones, onZoneClick = onZoneClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoneListContent(
    zones: List<BodyZone>,
    onZoneClick: (BodyZone) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Self Massage") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(zones) { zone ->
                ZoneCard(
                    zone = zone,
                    techniqueCount = zone.techniqueIds.size,
                    onClick = { onZoneClick(zone) },
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun ZoneCard(
    zone: BodyZone,
    techniqueCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier.clickable(onClick = onClick)) {
        ListItem(
            headlineContent = { Text(zone.name, style = MaterialTheme.typography.titleMedium) },
            supportingContent = { Text("$techniqueCount techniques") },
            leadingContent = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = zoneIcon(zone.iconName),
                            contentDescription = zone.name,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

private fun zoneIcon(iconName: String): ImageVector = when (iconName) {
    "FitnessCenter" -> Icons.Outlined.FitnessCenter
    "Straighten" -> Icons.Outlined.Straighten
    else -> Icons.Outlined.SelfImprovement
}

@Preview(showBackground = true, name = "Zone List Screen")
@Composable
private fun ZoneListContentPreview() {
    SelfMassageTheme {
        ZoneListContent(zones = MassageRepository.zones, onZoneClick = {})
    }
}

@Preview(showBackground = true, name = "Zone Card")
@Composable
private fun ZoneCardPreview() {
    SelfMassageTheme {
        ZoneCard(
            zone = MassageRepository.zones.first(),
            techniqueCount = 3,
            onClick = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}
