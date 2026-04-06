package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ai.mlxdroid.selfmassage.data.MassageRepository
import ai.mlxdroid.selfmassage.data.model.MassageStep
import ai.mlxdroid.selfmassage.data.model.MassageTechnique
import ai.mlxdroid.selfmassage.ui.components.AccentChip
import ai.mlxdroid.selfmassage.ui.components.ErrorPlaceholder
import ai.mlxdroid.selfmassage.ui.components.MassageAnimationCanvas
import ai.mlxdroid.selfmassage.ui.components.PrimaryButton
import ai.mlxdroid.selfmassage.ui.theme.GradientStart
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.theme.TrackGray
import ai.mlxdroid.selfmassage.ui.viewmodel.MassageDetailViewModel

@Composable
fun MassageDetailScreen(
    onBack: () -> Unit,
    onStartSession: (techniqueId: String) -> Unit = {},
    viewModel: MassageDetailViewModel = hiltViewModel()
) {
    val technique by viewModel.technique.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    when {
        error != null -> ErrorPlaceholder(message = error!!, onBack = onBack)
        technique != null -> MassageDetailContent(
            technique = technique!!,
            onBack = onBack,
            onStartSession = onStartSession
        )
    }
}

@Composable
fun MassageDetailContent(
    technique: MassageTechnique,
    onBack: () -> Unit,
    onStartSession: (techniqueId: String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        // Custom top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 40.dp, bottom = 8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                technique.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onStartSession(technique.id) }
            ) {
                Icon(
                    Icons.Outlined.PlayCircle,
                    contentDescription = "Start session",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Animation panel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(listOf(GradientStart, MaterialTheme.colorScheme.surfaceVariant)))
        ) {
            MassageAnimationCanvas(
                animationType = technique.animationType,
                bodyLocation = technique.bodyLocation,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Summary
        Text(
            technique.summary,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        // Steps header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("Steps", style = MaterialTheme.typography.titleMedium)
            Text(
                "${technique.steps.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        // Step timeline
        technique.steps.forEachIndexed { index, step ->
            StepRow(step = step, isLast = index == technique.steps.lastIndex)
        }

        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            text = "Start Session",
            onClick = { onStartSession(technique.id) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun StepRow(step: MassageStep, isLast: Boolean) {
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
    val lineColor = TrackGray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Number circle + dashed connector
        Box(modifier = Modifier.width(32.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    "${step.order}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = Color.White
                )
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp)
                        .offset(x = 15.dp, y = 36.dp)
                        .drawBehind {
                            drawLine(
                                color = lineColor,
                                start = Offset(size.width / 2, 0f),
                                end = Offset(size.width / 2, size.height),
                                strokeWidth = 2f,
                                pathEffect = dashEffect
                            )
                        }
                )
            }
        }

        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(step.instruction, style = MaterialTheme.typography.bodyLarge)
            AccentChip("${step.durationSeconds}s")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MassageDetailContentPreview() {
    SelfMassageTheme {
        MassageDetailContent(
            technique = MassageRepository.techniqueById("neck_suboccipital")!!,
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MassageDetailErrorPreview() {
    SelfMassageTheme {
        ErrorPlaceholder(message = "Technique not found", onBack = {})
    }
}
