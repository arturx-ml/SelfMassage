package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.MassageListViewModel

@Composable
fun MassageListScreen(
    onTechniqueClick: (zoneId: String, techniqueId: String) -> Unit,
    onBack: () -> Unit,
    viewModel: MassageListViewModel = viewModel(factory = MassageListViewModel.factory())
) {
    val state = viewModel.uiState
    MassageListContent(
        zoneName = state.zone?.name ?: "",
        techniques = state.techniques,
        onTechniqueClick = { techniqueId -> onTechniqueClick(viewModel.zoneId, techniqueId) },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MassageListContent(
    zoneName: String,
    techniques: List<MassageTechnique>,
    onTechniqueClick: (techniqueId: String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(zoneName) },
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(techniques) { technique ->
                TechniqueCard(
                    technique = technique,
                    onClick = { onTechniqueClick(technique.id) }
                )
            }
        }
    }
}

@Composable
private fun TechniqueCard(technique: MassageTechnique, onClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(technique.name, style = MaterialTheme.typography.titleMedium)
            Text(
                technique.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SuggestionChip(
                    onClick = {},
                    label = { Text("${technique.durationMinutes} min") },
                    icon = {
                        Icon(
                            Icons.Outlined.Timer,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Massage List – Neck")
@Composable
private fun MassageListContentPreview() {
    SelfMassageTheme {
        MassageListContent(
            zoneName = "Neck",
            techniques = MassageRepository.techniquesForZone("neck"),
            onTechniqueClick = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Massage List – Empty")
@Composable
private fun MassageListContentEmptyPreview() {
    SelfMassageTheme {
        MassageListContent(
            zoneName = "Unknown",
            techniques = emptyList(),
            onTechniqueClick = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Technique Card")
@Composable
private fun TechniqueCardPreview() {
    SelfMassageTheme {
        TechniqueCard(technique = MassageRepository.techniques.first(), onClick = {})
    }
}
