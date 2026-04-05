package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.AssistChip
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
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.model.Routine
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.RoutineListViewModel

@Composable
fun RoutineListScreen(
    onRoutineClick: (Routine) -> Unit,
    viewModel: RoutineListViewModel = hiltViewModel()
) {
    RoutineListContent(routines = viewModel.routines, onRoutineClick = onRoutineClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListContent(
    routines: List<Routine>,
    onRoutineClick: (Routine) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Routines") },
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
            items(routines) { routine ->
                RoutineCard(
                    routine = routine,
                    onClick = { onRoutineClick(routine) },
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun RoutineCard(
    routine: Routine,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier.clickable(onClick = onClick)) {
        ListItem(
            headlineContent = { Text(routine.name, style = MaterialTheme.typography.titleMedium) },
            supportingContent = {
                Text(
                    routine.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            },
            leadingContent = {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = routineIcon(routine.iconName),
                            contentDescription = routine.name,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            },
            trailingContent = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(onClick = {}, label = { Text("${routine.durationMinutes} min") })
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

private fun routineIcon(iconName: String): ImageVector = when (iconName) {
    "FitnessCenter" -> Icons.Outlined.FitnessCenter
    "Straighten" -> Icons.Outlined.Straighten
    else -> Icons.Outlined.SelfImprovement
}

@Preview(showBackground = true, name = "Routine List Screen")
@Composable
private fun RoutineListContentPreview() {
    SelfMassageTheme {
        RoutineListContent(routines = MassageRepository.routines, onRoutineClick = {})
    }
}
