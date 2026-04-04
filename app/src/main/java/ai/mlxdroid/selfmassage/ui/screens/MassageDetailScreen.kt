package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.model.MassageStep
import ai.mlxdroid.selfmassage.domain.GetTechniqueDetailUseCase
import ai.mlxdroid.selfmassage.ui.components.MassageAnimationCanvas
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.MassageDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MassageDetailScreen(
    onBack: () -> Unit,
    viewModel: MassageDetailViewModel = viewModel(factory = MassageDetailViewModel.factory())
) {
    val technique = viewModel.technique ?: return
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(technique.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                MassageAnimationCanvas(
                    animationType = technique.animationType,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                technique.summary,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text("Steps", style = MaterialTheme.typography.titleMedium)

            technique.steps.forEach { step ->
                StepRow(step = step)
            }
        }
    }
}

@Composable
private fun StepRow(step: MassageStep) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    "${step.order}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(step.instruction, style = MaterialTheme.typography.bodyLarge)
            AssistChip(
                onClick = {},
                label = { Text("${step.durationSeconds}s") }
            )
        }
    }
}

@Preview(showBackground = true, name = "Massage Detail – Suboccipital Release")
@Composable
private fun MassageDetailScreenPreview() {
    SelfMassageTheme {
        MassageDetailScreen(
            onBack = {},
            viewModel = MassageDetailViewModel(
                savedStateHandle = SavedStateHandle(mapOf("zoneId" to "neck", "techniqueId" to "neck_suboccipital")),
                getTechniqueDetail = GetTechniqueDetailUseCase(MassageRepository)
            )
        )
    }
}

@Preview(showBackground = true, name = "Massage Detail – Kneading (Circular)")
@Composable
private fun MassageDetailCircularPreview() {
    SelfMassageTheme {
        MassageDetailScreen(
            onBack = {},
            viewModel = MassageDetailViewModel(
                savedStateHandle = SavedStateHandle(mapOf("zoneId" to "neck", "techniqueId" to "neck_trapezius_knead")),
                getTechniqueDetail = GetTechniqueDetailUseCase(MassageRepository)
            )
        )
    }
}

@Preview(showBackground = true, name = "Step Row")
@Composable
private fun StepRowPreview() {
    SelfMassageTheme {
        StepRow(
            step = MassageStep(
                order = 1,
                instruction = "Place your fingertips at the base of your skull where it meets the neck.",
                durationSeconds = 30
            )
        )
    }
}
