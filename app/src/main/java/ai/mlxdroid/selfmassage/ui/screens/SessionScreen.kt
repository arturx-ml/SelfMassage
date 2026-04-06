package ai.mlxdroid.selfmassage.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
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
import ai.mlxdroid.selfmassage.ui.components.PrimaryButton
import ai.mlxdroid.selfmassage.ui.theme.GradientStart
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import ai.mlxdroid.selfmassage.ui.theme.TrackGray
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Minimal top bar
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
                currentStep?.techniqueName ?: "",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(48.dp))
        }

        // Animation panel
        if (currentStep != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(GradientStart, MaterialTheme.colorScheme.surfaceVariant)))
            ) {
                MassageAnimationCanvas(
                    animationType = currentStep.animationType,
                    bodyLocation = currentStep.bodyLocation,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(16.dp))

            // Step indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    "Step ${currentStep.stepOrder} of ${currentStep.totalStepsInTechnique}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "\u2022",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    currentStep.techniqueName,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(16.dp))

            // GET READY badge
            if (uiState.isPreparing) {
                val pulse = rememberInfiniteTransition(label = "prep")
                val alpha by pulse.animateFloat(
                    initialValue = 0.6f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
                    label = "prepAlpha"
                )
                Text(
                    "GET READY",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp
                    ),
                    color = Color.White,
                    modifier = Modifier
                        .alpha(alpha)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )
                Spacer(Modifier.height(16.dp))
            }

            // Instruction
            Text(
                currentStep.instruction,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(32.dp))

            // Countdown ring
            CountdownRing(
                secondsRemaining = if (uiState.isPreparing) uiState.prepSecondsRemaining else uiState.secondsRemaining,
                totalSeconds = if (uiState.isPreparing) 5 else currentStep.durationSeconds,
                isPreparing = uiState.isPreparing
            )

            Spacer(Modifier.height(32.dp))
        }

        // Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        ) {
            // Previous
            IconButton(
                onClick = onSkipPrevious,
                enabled = uiState.currentIndex > 0,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    Icons.Filled.SkipPrevious,
                    contentDescription = "Previous",
                    tint = if (uiState.currentIndex > 0)
                        MaterialTheme.colorScheme.primary
                    else TrackGray,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Play/Pause
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                IconButton(onClick = if (uiState.isPlaying) onPause else onPlay) {
                    Icon(
                        if (uiState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (uiState.isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Next
            IconButton(
                onClick = onSkipNext,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    Icons.Filled.SkipNext,
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun CountdownRing(secondsRemaining: Int, totalSeconds: Int, isPreparing: Boolean) {
    val progress = if (totalSeconds > 0) secondsRemaining.toFloat() / totalSeconds else 0f
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val arcColor = if (isPreparing) secondary else primary
    val glowColor = if (isPreparing) secondary else primary

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(152.dp)) {
        // Outer glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor.copy(alpha = 0.08f), Color.Transparent),
                    radius = size.minDimension / 2
                )
            )
        }

        // Ring
        Canvas(modifier = Modifier.size(120.dp)) {
            val stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            val arcSize = androidx.compose.ui.geometry.Size(size.width - stroke.width, size.height - stroke.width)
            val topLeft = androidx.compose.ui.geometry.Offset(stroke.width / 2, stroke.width / 2)
            drawArc(color = TrackGray, 0f, 360f, false, style = stroke, topLeft = topLeft, size = arcSize)
            drawArc(color = arcColor, -90f, 360f * progress, false, style = stroke, topLeft = topLeft, size = arcSize)
        }

        // Time
        Text(
            "$secondsRemaining",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 48.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SessionCompleteOverlay(onDone: () -> Unit) {
    // Dark scrim + centered card
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .padding(32.dp)
        ) {
            // Checkmark circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Well done!",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "You've completed your session. Keep up the great work on your wellness journey.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            PrimaryButton(text = "Done", onClick = onDone)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionContentPrepPreview() {
    SelfMassageTheme {
        SessionContent(
            uiState = SessionUiState(
                steps = listOf(
                    SessionStep("Suboccipital Release", AnimationType.PRESSURE_PULSE, BodyLocation.BASE_OF_SKULL, 2, 4, "Apply gentle upward pressure.", 30)
                ),
                currentIndex = 0, secondsRemaining = 30, prepSecondsRemaining = 4, isPlaying = true, isFinished = false
            ),
            onBack = {}, onPlay = {}, onPause = {}, onSkipNext = {}, onSkipPrevious = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionCompletePreview() {
    SelfMassageTheme {
        SessionCompleteOverlay(onDone = {})
    }
}
