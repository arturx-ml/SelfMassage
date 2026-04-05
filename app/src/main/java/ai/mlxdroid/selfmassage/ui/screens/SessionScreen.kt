package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.data.model.BodyLocation
import ai.mlxdroid.selfmassage.data.model.SessionStep
import ai.mlxdroid.selfmassage.ui.components.MassageAnimationCanvas
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.viewmodel.SessionUiState
import ai.mlxdroid.selfmassage.ui.viewmodel.SessionViewModel

@Composable
fun SessionScreen(
    onBack: () -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SessionContent(
        uiState = uiState,
        onBack = onBack,
        onPlay = viewModel::play,
        onPause = viewModel::pause,
        onSkipNext = viewModel::skipNext,
        onSkipPrevious = viewModel::skipPrevious
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionContent(
    uiState: SessionUiState,
    onBack: () -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit
) {
    if (uiState.isFinished) {
        SessionCompleteOverlay(onDone = onBack)
        return
    }

    val currentStep = uiState.steps.getOrNull(uiState.currentIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentStep?.techniqueName ?: "") },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentStep != null) {
                MassageAnimationCanvas(
                    animationType = currentStep.animationType,
                    bodyLocation = currentStep.bodyLocation,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Step ${currentStep.stepOrder} of ${currentStep.totalStepsInTechnique}  ·  ${currentStep.techniqueName}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(8.dp))

                if (uiState.isPreparing) {
                    Text(
                        text = "Get Ready",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(4.dp))
                }

                Text(
                    text = currentStep.instruction,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(Modifier.height(24.dp))

                if (uiState.isPreparing) {
                    CountdownRing(
                        secondsRemaining = uiState.prepSecondsRemaining,
                        totalSeconds = 5,
                        arcColor = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    CountdownRing(
                        secondsRemaining = uiState.secondsRemaining,
                        totalSeconds = currentStep.durationSeconds
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                IconButton(onClick = onSkipPrevious, enabled = uiState.currentIndex > 0) {
                    Icon(Icons.Outlined.SkipPrevious, contentDescription = "Previous step")
                }

                FilledIconButton(
                    onClick = if (uiState.isPlaying) onPause else onPlay,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                        contentDescription = if (uiState.isPlaying) "Pause" else "Play"
                    )
                }

                IconButton(onClick = onSkipNext) {
                    Icon(Icons.Outlined.SkipNext, contentDescription = "Next step")
                }
            }
        }
    }
}

@Composable
private fun CountdownRing(
    secondsRemaining: Int,
    totalSeconds: Int,
    arcColor: Color = MaterialTheme.colorScheme.primary
) {
    val progress = if (totalSeconds > 0) secondsRemaining.toFloat() / totalSeconds else 0f
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 8.dp.toPx())
            val inset = stroke.width / 2
            val arcSize = androidx.compose.ui.geometry.Size(size.width - stroke.width, size.height - stroke.width)
            val topLeft = androidx.compose.ui.geometry.Offset(inset, inset)
            drawArc(color = trackColor, startAngle = 0f, sweepAngle = 360f, useCenter = false, style = stroke, topLeft = topLeft, size = arcSize)
            drawArc(color = arcColor, startAngle = -90f, sweepAngle = 360f * progress, useCenter = false, style = stroke, topLeft = topLeft, size = arcSize)
        }
        Text(
            text = "$secondsRemaining",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 36.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SessionCompleteOverlay(onDone: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Session Complete!", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Great work! Take a moment to breathe and relax.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(32.dp))
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
                Text("Done")
            }
        }
    }
}

@Preview(showBackground = true, name = "Session Screen – Get Ready")
@Composable
private fun SessionContentPrepPreview() {
    SelfMassageTheme {
        SessionContent(
            uiState = SessionUiState(
                steps = listOf(
                    SessionStep(
                        techniqueName = "Suboccipital Release",
                        animationType = AnimationType.PRESSURE_PULSE,
                        bodyLocation = BodyLocation.BASE_OF_SKULL,
                        stepOrder = 2,
                        totalStepsInTechnique = 4,
                        instruction = "Apply gentle upward pressure with both middle fingers into the two small hollows on either side of the spine.",
                        durationSeconds = 30
                    )
                ),
                currentIndex = 0,
                secondsRemaining = 30,
                prepSecondsRemaining = 4,
                isPlaying = true,
                isFinished = false
            ),
            onBack = {},
            onPlay = {},
            onPause = {},
            onSkipNext = {},
            onSkipPrevious = {}
        )
    }
}

@Preview(showBackground = true, name = "Session Screen – Playing")
@Composable
private fun SessionContentPlayingPreview() {
    SelfMassageTheme {
        SessionContent(
            uiState = SessionUiState(
                steps = listOf(
                    SessionStep(
                        techniqueName = "Suboccipital Release",
                        animationType = AnimationType.PRESSURE_PULSE,
                        bodyLocation = BodyLocation.BASE_OF_SKULL,
                        stepOrder = 2,
                        totalStepsInTechnique = 4,
                        instruction = "Apply gentle upward pressure with both middle fingers into the two small hollows on either side of the spine.",
                        durationSeconds = 30
                    )
                ),
                currentIndex = 0,
                secondsRemaining = 20,
                prepSecondsRemaining = 0,
                isPlaying = true,
                isFinished = false
            ),
            onBack = {},
            onPlay = {},
            onPause = {},
            onSkipNext = {},
            onSkipPrevious = {}
        )
    }
}

@Preview(showBackground = true, name = "Session Complete")
@Composable
private fun SessionCompletePreview() {
    SelfMassageTheme {
        SessionCompleteOverlay(onDone = {})
    }
}
