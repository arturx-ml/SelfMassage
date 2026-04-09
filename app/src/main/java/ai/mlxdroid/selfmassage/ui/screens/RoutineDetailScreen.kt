package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.data.model.BodyLocation
import ai.mlxdroid.selfmassage.data.model.MassageStep
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.data.model.Routine
import ai.mlxdroid.selfmassage.ui.components.AccentChip
import ai.mlxdroid.selfmassage.ui.components.ErrorPlaceholder
import ai.mlxdroid.selfmassage.ui.components.PrimaryButton
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.RoutineDetailViewModel

@Composable
fun RoutineDetailScreen(
    onBack: () -> Unit,
    onStartRoutine: (routineId: String) -> Unit,
    viewModel: RoutineDetailViewModel = hiltViewModel()
) {
    val routine by viewModel.routine.collectAsStateWithLifecycle()
    val techniques by viewModel.techniques.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    when {
        error != null -> ErrorPlaceholder(message = error!!, onBack = onBack)
        routine != null -> RoutineDetailContent(
            routine = routine!!,
            techniques = techniques,
            onBack = onBack,
            onStartRoutine = { onStartRoutine(routine!!.id) }
        )
    }
}

@Composable
fun RoutineDetailContent(
    routine: Routine,
    techniques: List<MassageTechnique>,
    onBack: () -> Unit,
    onStartRoutine: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Header with gradient
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.02f)
                            )
                        )
                    )
                    .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onBack)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        "Back",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    routine.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AccentChip("${routine.durationMinutes} min")
                    AccentChip("${techniques.size} techniques")
                }
            }
        }

        // Description
        item {
            Text(
                routine.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        // Techniques header
        item {
            Text(
                "Techniques",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Technique rows with green dots
        items(techniques, key = { it.id }) { technique ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .align(Alignment.Top)
                        .padding(top = 8.dp)
                )
                Column {
                    Text(technique.name, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        technique.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Start button
        item {
            Spacer(Modifier.height(24.dp))
            PrimaryButton(
                text = "Start Routine",
                onClick = onStartRoutine,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RoutineDetailContentPreview() {
    val sampleTechniques = listOf(
        MassageTechnique("t1", "Suboccipital Release", "Releases tension at the base of the skull", 3, listOf(MassageStep(1, "Step", 60)), AnimationType.PRESSURE_PULSE, BodyLocation.BASE_OF_SKULL),
        MassageTechnique("t2", "Upper Trapezius Kneading", "Classic knead for the neck-shoulder junction", 4, listOf(MassageStep(1, "Step", 60)), AnimationType.CIRCULAR, BodyLocation.UPPER_TRAPEZIUS)
    )
    val sampleRoutine = Routine("r1", "Morning Neck Reset", "Eases overnight stiffness.", "SelfImprovement", 7, listOf("t1", "t2"))
    SelfMassageTheme {
        RoutineDetailContent(
            routine = sampleRoutine,
            techniques = sampleTechniques,
            onBack = {},
            onStartRoutine = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RoutineDetailErrorPreview() {
    SelfMassageTheme {
        ErrorPlaceholder(message = "Routine not found", onBack = {})
    }
}
