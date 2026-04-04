package ai.mlxdroid.selfmassage.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MassageAnimationCanvas(
    animationType: AnimationType,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "massage_anim")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val error = MaterialTheme.colorScheme.error
    val errorContainer = MaterialTheme.colorScheme.errorContainer

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        when (animationType) {
            AnimationType.CIRCULAR -> drawCircularAnimation(progress, primary, primaryContainer)
            AnimationType.HORIZONTAL_SWEEP -> drawSweepAnimation(progress, tertiary)
            AnimationType.VERTICAL_STROKE -> drawStrokeAnimation(progress, secondary)
            AnimationType.PRESSURE_PULSE -> drawPulseAnimation(progress, error, errorContainer)
        }
    }
}

private fun DrawScope.drawCircularAnimation(
    progress: Float,
    dotColor: Color,
    arcColor: Color
) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val radius = minOf(size.width, size.height) * 0.30f

    // Draw arc guide
    drawCircle(
        color = arcColor,
        radius = radius,
        center = Offset(centerX, centerY),
        style = Stroke(width = 6.dp.toPx())
    )

    // Compute dot position on circle
    val angle = progress * 2f * PI.toFloat() - PI.toFloat() / 2f
    val dotX = centerX + radius * cos(angle)
    val dotY = centerY + radius * sin(angle)

    // Draw trailing arc (ghost)
    val sweepAngle = 90f
    val startAngle = progress * 360f - 90f - sweepAngle
    drawArc(
        color = dotColor.copy(alpha = 0.2f),
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(centerX - radius, centerY - radius),
        size = Size(radius * 2, radius * 2),
        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
    )

    // Draw arrowhead tangent to circle
    val tangentAngle = angle + PI.toFloat() / 2f
    val arrowSize = 18.dp.toPx()
    rotate(
        degrees = Math.toDegrees(tangentAngle.toDouble()).toFloat(),
        pivot = Offset(dotX, dotY)
    ) {
        val arrowPath = Path().apply {
            moveTo(dotX, dotY - arrowSize)
            lineTo(dotX + arrowSize * 0.6f, dotY + arrowSize * 0.4f)
            lineTo(dotX - arrowSize * 0.6f, dotY + arrowSize * 0.4f)
            close()
        }
        drawPath(arrowPath, color = dotColor)
    }

    // Draw dot
    drawCircle(
        color = dotColor,
        radius = 10.dp.toPx(),
        center = Offset(dotX, dotY)
    )
}

private fun DrawScope.drawSweepAnimation(
    progress: Float,
    color: Color
) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val sweepRadius = size.width * 0.30f
    val arrowWidth = 14.dp.toPx()
    val arrowHeadSize = 20.dp.toPx()

    // Oscillate: sin gives -1..1, map to position
    val sinVal = sin(progress * 2f * PI.toFloat())
    val x = centerX + sinVal * sweepRadius

    // Draw ghost trail at 3 prior positions
    for (i in 1..3) {
        val ghostProgress = (progress - i * 0.08f).let { if (it < 0) it + 1f else it }
        val ghostSin = sin(ghostProgress * 2f * PI.toFloat())
        val ghostX = centerX + ghostSin * sweepRadius
        drawLine(
            color = color.copy(alpha = 0.08f * (4 - i)),
            start = Offset(ghostX, centerY - 40.dp.toPx()),
            end = Offset(ghostX, centerY + 40.dp.toPx()),
            strokeWidth = arrowWidth * 0.6f,
            cap = StrokeCap.Round
        )
    }

    // Arrow shaft
    drawLine(
        color = color,
        start = Offset(x, centerY - 40.dp.toPx()),
        end = Offset(x, centerY + 40.dp.toPx()),
        strokeWidth = arrowWidth,
        cap = StrokeCap.Round
    )

    // Arrowhead direction based on movement direction (derivative of sin = cos)
    val cosVal = cos(progress * 2f * PI.toFloat())
    val headX = if (cosVal >= 0) x + arrowHeadSize else x - arrowHeadSize
    val arrowPath = Path().apply {
        moveTo(headX, centerY)
        if (cosVal >= 0) {
            lineTo(x + arrowHeadSize * 0.3f, centerY - arrowHeadSize * 0.7f)
            lineTo(x + arrowHeadSize * 0.3f, centerY + arrowHeadSize * 0.7f)
        } else {
            lineTo(x - arrowHeadSize * 0.3f, centerY - arrowHeadSize * 0.7f)
            lineTo(x - arrowHeadSize * 0.3f, centerY + arrowHeadSize * 0.7f)
        }
        close()
    }
    drawPath(arrowPath, color = color)
}

private fun DrawScope.drawStrokeAnimation(
    progress: Float,
    color: Color
) {
    val centerX = size.width / 2f
    val topY = size.height * 0.15f
    val bottomY = size.height * 0.85f
    val arrowWidth = 16.dp.toPx()
    val arrowHeadSize = 22.dp.toPx()

    // Fade out near end of stroke
    val alpha = if (progress > 0.85f) (1f - progress) / 0.15f else 1f

    val currentY = topY + progress * (bottomY - topY)

    // Arrow shaft from top to current position
    if (currentY > topY + arrowHeadSize) {
        drawLine(
            color = color.copy(alpha = alpha),
            start = Offset(centerX, topY),
            end = Offset(centerX, currentY - arrowHeadSize),
            strokeWidth = arrowWidth,
            cap = StrokeCap.Round
        )
    }

    // Arrowhead pointing down
    val arrowPath = Path().apply {
        moveTo(centerX, currentY)
        lineTo(centerX - arrowHeadSize * 0.7f, currentY - arrowHeadSize)
        lineTo(centerX + arrowHeadSize * 0.7f, currentY - arrowHeadSize)
        close()
    }
    drawPath(arrowPath, color = color.copy(alpha = alpha))
}

private fun DrawScope.drawPulseAnimation(
    progress: Float,
    dotColor: Color,
    ringColor: Color
) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val baseRadius = 28.dp.toPx()
    val pulseAmount = 8.dp.toPx()
    val maxRingRadius = 80.dp.toPx()

    // Expanding ripple rings
    for (i in 0..1) {
        val ringProgress = (progress + i * 0.5f) % 1f
        val ringRadius = baseRadius + ringProgress * (maxRingRadius - baseRadius)
        val ringAlpha = (1f - ringProgress) * 0.5f
        drawCircle(
            color = ringColor.copy(alpha = ringAlpha),
            radius = ringRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 4.dp.toPx())
        )
    }

    // Pulsing center dot
    val pulseRadius = baseRadius + sin(progress * 2f * PI.toFloat()).toFloat() * pulseAmount
    drawCircle(
        color = dotColor,
        radius = pulseRadius,
        center = Offset(centerX, centerY)
    )

    // Inner highlight
    drawCircle(
        color = dotColor.copy(alpha = 0.4f),
        radius = pulseRadius * 0.5f,
        center = Offset(centerX - pulseRadius * 0.2f, centerY - pulseRadius * 0.2f)
    )
}

@Preview(showBackground = true, name = "Animation – Circular (Kneading)")
@Composable
private fun PreviewCircular() {
    SelfMassageTheme {
        MassageAnimationCanvas(animationType = AnimationType.CIRCULAR)
    }
}

@Preview(showBackground = true, name = "Animation – Horizontal Sweep (Cross-Fiber)")
@Composable
private fun PreviewHorizontalSweep() {
    SelfMassageTheme {
        MassageAnimationCanvas(animationType = AnimationType.HORIZONTAL_SWEEP)
    }
}

@Preview(showBackground = true, name = "Animation – Vertical Stroke (Effleurage)")
@Composable
private fun PreviewVerticalStroke() {
    SelfMassageTheme {
        MassageAnimationCanvas(animationType = AnimationType.VERTICAL_STROKE)
    }
}

@Preview(showBackground = true, name = "Animation – Pressure Pulse (Trigger Point)")
@Composable
private fun PreviewPressurePulse() {
    SelfMassageTheme {
        MassageAnimationCanvas(animationType = AnimationType.PRESSURE_PULSE)
    }
}
