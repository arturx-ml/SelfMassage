package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.data.model.Routine
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.RoutineDetailViewModel

@Composable
fun RoutineDetailScreen(
    onBack: () -> Unit,
    onStartRoutine: (routineId: String) -> Unit,
    viewModel: RoutineDetailViewModel = hiltViewModel()
) {
    val routine = viewModel.routine ?: return
    RoutineDetailContent(
        routine = routine,
        techniques = viewModel.techniques,
        onBack = onBack,
        onStartRoutine = { onStartRoutine(routine.id) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailContent(
    routine: Routine,
    techniques: List<MassageTechnique>,
    onBack: () -> Unit,
    onStartRoutine: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(routine.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    routine.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(onClick = {}, label = { Text("${routine.durationMinutes} min") })
                    AssistChip(onClick = {}, label = { Text("${techniques.size} techniques") })
                }
            }

            item {
                Text("Techniques", style = MaterialTheme.typography.titleMedium)
                HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
            }

            items(techniques) { technique ->
                ListItem(
                    headlineContent = {
                        Text(technique.name, style = MaterialTheme.typography.bodyLarge)
                    },
                    supportingContent = {
                        Text(
                            "${technique.durationMinutes} min · ${technique.steps.size} steps",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onStartRoutine,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Routine")
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, name = "Routine Detail")
@Composable
private fun RoutineDetailContentPreview() {
    val routine = MassageRepository.routines.first()
    val techniques = routine.techniqueIds.mapNotNull { MassageRepository.techniqueById(it) }
    SelfMassageTheme {
        RoutineDetailContent(
            routine = routine,
            techniques = techniques,
            onBack = {},
            onStartRoutine = {}
        )
    }
}
