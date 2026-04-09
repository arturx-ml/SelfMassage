package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.FitnessCenter
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.data.model.BodyLocation
import ai.mlxdroid.selfmassage.data.model.MassageStep
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.ui.components.AccentChip
import ai.mlxdroid.selfmassage.ui.components.EmptyPlaceholder
import ai.mlxdroid.selfmassage.ui.components.ErrorPlaceholder
import ai.mlxdroid.selfmassage.ui.components.GradientHeader
import ai.mlxdroid.selfmassage.ui.theme.PrimaryLight
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.MassageListViewModel

@Composable
fun MassageListScreen(
    onTechniqueClick: (zoneId: String, techniqueId: String) -> Unit,
    onBack: () -> Unit,
    viewModel: MassageListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.error != null) {
        ErrorPlaceholder(message = state.error!!, onBack = onBack)
    } else {
        MassageListContent(
            zoneName = state.zone?.name ?: "",
            techniques = state.techniques,
            onTechniqueClick = { techniqueId -> onTechniqueClick(viewModel.zoneId, techniqueId) },
            onBack = onBack
        )
    }
}

@Composable
fun MassageListContent(
    zoneName: String,
    techniques: List<MassageTechnique>,
    onTechniqueClick: (techniqueId: String) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GradientHeader(
            title = zoneName,
            subtitle = "${techniques.size} techniques available",
            onBack = onBack
        )

        if (techniques.isEmpty()) {
            EmptyPlaceholder("No techniques available")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 80.dp)
            ) {
                items(techniques, key = { it.id }) { technique ->
                    TechniqueCard(
                        technique = technique,
                        onClick = { onTechniqueClick(technique.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TechniqueCard(technique: MassageTechnique, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.10f))
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PrimaryLight.copy(alpha = 0.2f))
        ) {
            Icon(
                Icons.Outlined.FitnessCenter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                technique.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                technique.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        AccentChip("${technique.durationMinutes} min")
    }
}

@Preview(showBackground = true)
@Composable
private fun MassageListContentPreview() {
    SelfMassageTheme {
        MassageListContent(
            zoneName = "Neck",
            techniques = listOf(
                MassageTechnique("t1", "Suboccipital Release", "Releases tension at the base of the skull", 3, listOf(MassageStep(1, "Step one", 60)), AnimationType.PRESSURE_PULSE, BodyLocation.BASE_OF_SKULL),
                MassageTechnique("t2", "Lateral Neck Stretch", "Targets SCM and scalenes", 3, listOf(MassageStep(1, "Step one", 60)), AnimationType.VERTICAL_STROKE, BodyLocation.LATERAL_NECK)
            ),
            onTechniqueClick = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MassageListEmptyPreview() {
    SelfMassageTheme {
        MassageListContent(
            zoneName = "Neck",
            techniques = emptyList(),
            onTechniqueClick = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MassageListErrorPreview() {
    SelfMassageTheme {
        ErrorPlaceholder(message = "Zone not found", onBack = {})
    }
}
