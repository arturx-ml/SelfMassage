package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Straighten
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ai.mlxdroid.selfmassage.data.model.BodyZone
import ai.mlxdroid.selfmassage.ui.components.EmptyPlaceholder
import ai.mlxdroid.selfmassage.ui.components.GradientHeader
import ai.mlxdroid.selfmassage.ui.theme.PrimaryLight
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.util.timeOfDayGreeting
import ai.mlxdroid.selfmassage.ui.viewmodel.ZoneListViewModel

@Composable
fun ZoneListScreen(
    onZoneClick: (BodyZone) -> Unit,
    viewModel: ZoneListViewModel = hiltViewModel()
) {
    val zones by viewModel.zones.collectAsStateWithLifecycle()
    ZoneListContent(zones = zones, onZoneClick = onZoneClick)
}

@Composable
fun ZoneListContent(
    zones: List<BodyZone>,
    onZoneClick: (BodyZone) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GradientHeader(title = timeOfDayGreeting(), subtitle = "Choose a zone to begin your session")

        if (zones.isEmpty()) {
            EmptyPlaceholder("No zones available")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 4.dp, bottom = 80.dp)
            ) {
                items(zones, key = { it.id }) { zone ->
                    ZoneCard(zone = zone, onClick = { onZoneClick(zone) })
                }
            }
        }
    }
}

@Composable
private fun ZoneCard(zone: BodyZone, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.10f))
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
    ) {
        // Left accent bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(MaterialTheme.colorScheme.primary)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            // Icon container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PrimaryLight.copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = zoneIcon(zone.iconName),
                    contentDescription = zone.name,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(zone.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                Text(
                    "${zone.techniqueIds.size} techniques",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

fun zoneIcon(iconName: String): ImageVector = when (iconName) {
    "FitnessCenter" -> Icons.Outlined.FitnessCenter
    "Straighten" -> Icons.Outlined.Straighten
    "DirectionsWalk" -> Icons.Outlined.DirectionsWalk
    else -> Icons.Outlined.SelfImprovement
}

@Preview(showBackground = true)
@Composable
private fun ZoneListContentPreview() {
    SelfMassageTheme {
        ZoneListContent(
            zones = listOf(
                BodyZone("neck", "Neck", "SelfImprovement", listOf("t1", "t2")),
                BodyZone("shoulders", "Shoulders", "FitnessCenter", listOf("t3")),
                BodyZone("arms", "Arms", "Straighten", listOf("t4"))
            ),
            onZoneClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ZoneListEmptyPreview() {
    SelfMassageTheme {
        ZoneListContent(zones = emptyList(), onZoneClick = {})
    }
}
