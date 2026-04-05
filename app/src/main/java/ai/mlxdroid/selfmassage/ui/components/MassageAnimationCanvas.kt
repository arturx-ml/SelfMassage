package ai.mlxdroid.selfmassage.ui.components

import android.graphics.Paint
import android.graphics.Typeface
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.mlxdroid.selfmassage.data.model.AnimationType
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import kotlin.math.PI
import kotlin.math.abs
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
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    // Capture all theme colors before entering Canvas (Canvas is not a composable scope)
    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val secondary = MaterialTheme.colorScheme.secondary
    val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
    val tertiary = MaterialTheme.colorScheme.tertiary
    val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    val error = MaterialTheme.colorScheme.error
    val errorContainer = MaterialTheme.colorScheme.errorContainer
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurface = MaterialTheme.colorScheme.onSurface

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        when (animationType) {
            AnimationType.CIRCULAR ->
                drawCircularKneading(progress, primary, primaryContainer, onPrimaryContainer, surfaceVariant)
            AnimationType.HORIZONTAL_SWEEP ->
                drawCrossFiberSweep(progress, tertiary, tertiaryContainer, surfaceVariant)
            AnimationType.VERTICAL_STROKE ->
                drawGlidingStroke(progress, secondary, secondaryContainer, surfaceVariant)
            AnimationType.PRESSURE_PULSE ->
                drawTriggerPointPulse(progress, error, errorContainer, surfaceVariant, onSurface)
        }
    }
}

// ─── CIRCULAR: kneading circles over muscle belly ───────────────────────────

private fun DrawScope.drawCircularKneading(
    progress: Float,
    fingerColor: Color,
    orbitColor: Color,
    labelColor: Color,
    muscleColor: Color
) {
    val cx = size.width / 2f
    val cy = size.height / 2f

    // Muscle belly (soft oval)
    drawOval(
        color = muscleColor.copy(alpha = 0.35f),
        topLeft = Offset(cx - 90.dp.toPx(), cy - 52.dp.toPx()),
        size = Size(180.dp.toPx(), 104.dp.toPx())
    )
    drawOval(
        color = muscleColor,
        topLeft = Offset(cx - 90.dp.toPx(), cy - 52.dp.toPx()),
        size = Size(180.dp.toPx(), 104.dp.toPx()),
        style = Stroke(width = 2.dp.toPx())
    )

    // Three overlapping dashed orbit circles showing kneading pattern
    val orbitR = 28.dp.toPx()
    val orbitSpread = 20.dp.toPx()
    val orbitCenters = listOf(
        Offset(cx - orbitSpread, cy),
        Offset(cx + orbitSpread * 0.5f, cy - orbitSpread * 0.8f),
        Offset(cx + orbitSpread * 0.5f, cy + orbitSpread * 0.8f)
    )
    orbitCenters.forEach { center ->
        drawCircle(
            color = orbitColor.copy(alpha = 0.6f),
            radius = orbitR,
            center = center,
            style = Stroke(
                width = 2.dp.toPx(),
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                    floatArrayOf(10f, 7f), 0f
                )
            )
        )
    }

    // Current orbit center cycles through the 3 orbits
    val orbitPhase = (progress * 3f) % 3f
    val orbitIdx = orbitPhase.toInt().coerceIn(0, 2)
    val nextIdx = (orbitIdx + 1) % 3
    val blend = orbitPhase - orbitIdx
    val currentOrbitCenter = Offset(
        orbitCenters[orbitIdx].x + (orbitCenters[nextIdx].x - orbitCenters[orbitIdx].x) * blend,
        orbitCenters[orbitIdx].y + (orbitCenters[nextIdx].y - orbitCenters[orbitIdx].y) * blend
    )

    // Angle within current orbit
    val angle = progress * 3f * 2f * PI.toFloat()
    val fingerX = currentOrbitCenter.x + orbitR * cos(angle)
    val fingerY = currentOrbitCenter.y + orbitR * sin(angle)

    // Motion trail (5 ghost finger pads)
    for (i in 5 downTo 1) {
        val t = (progress - i * 0.025f + 1f) % 1f
        val ta = t * 3f * 2f * PI.toFloat()
        val tPhase = (t * 3f) % 3f
        val tIdx = tPhase.toInt().coerceIn(0, 2)
        val tNext = (tIdx + 1) % 3
        val tBlend = tPhase - tIdx
        val tCenter = Offset(
            orbitCenters[tIdx].x + (orbitCenters[tNext].x - orbitCenters[tIdx].x) * tBlend,
            orbitCenters[tIdx].y + (orbitCenters[tNext].y - orbitCenters[tIdx].y) * tBlend
        )
        drawCircle(
            color = fingerColor.copy(alpha = 0.06f * (6 - i)),
            radius = 13.dp.toPx(),
            center = Offset(tCenter.x + orbitR * cos(ta), tCenter.y + orbitR * sin(ta))
        )
    }

    // Finger pad with shadow-like second circle
    drawCircle(
        color = fingerColor.copy(alpha = 0.25f),
        radius = 18.dp.toPx(),
        center = Offset(fingerX + 2.dp.toPx(), fingerY + 3.dp.toPx())
    )
    drawCircle(
        color = fingerColor,
        radius = 14.dp.toPx(),
        center = Offset(fingerX, fingerY)
    )
    // Specular highlight
    drawCircle(
        color = Color.White.copy(alpha = 0.35f),
        radius = 5.dp.toPx(),
        center = Offset(fingerX - 4.dp.toPx(), fingerY - 4.dp.toPx())
    )

    drawLabel("Circular kneading", cx, size.height - 12.dp.toPx(), labelColor)
}

// ─── HORIZONTAL_SWEEP: cross-fiber friction ──────────────────────────────────

private fun DrawScope.drawCrossFiberSweep(
    progress: Float,
    thumbColor: Color,
    trailColor: Color,
    muscleColor: Color
) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val fiberAreaH = 80.dp.toPx()
    val fiberAreaW = 200.dp.toPx()

    // Forearm body outline
    val armTop = cy - fiberAreaH / 2f - 12.dp.toPx()
    val armBottom = cy + fiberAreaH / 2f + 12.dp.toPx()
    val armLeft = cx - fiberAreaW / 2f - 10.dp.toPx()
    val armRight = cx + fiberAreaW / 2f + 10.dp.toPx()
    drawRoundRect(
        color = muscleColor.copy(alpha = 0.3f),
        topLeft = Offset(armLeft, armTop),
        size = Size(armRight - armLeft, armBottom - armTop),
        cornerRadius = CornerRadius(20.dp.toPx())
    )
    drawRoundRect(
        color = muscleColor,
        topLeft = Offset(armLeft, armTop),
        size = Size(armRight - armLeft, armBottom - armTop),
        cornerRadius = CornerRadius(20.dp.toPx()),
        style = Stroke(width = 2.dp.toPx())
    )

    // Muscle fiber lines (running horizontally along the arm)
    val fiberCount = 7
    for (i in 0..fiberCount) {
        val y = armTop + 16.dp.toPx() + i * ((armBottom - armTop - 32.dp.toPx()) / fiberCount)
        drawLine(
            color = muscleColor.copy(alpha = 0.5f),
            start = Offset(armLeft + 16.dp.toPx(), y),
            end = Offset(armRight - 16.dp.toPx(), y),
            strokeWidth = 1.dp.toPx()
        )
    }

    // Oscillate thumb left↔right with eased sin motion
    val sinVal = sin(progress * 2f * PI.toFloat())
    val thumbX = cx + sinVal * (fiberAreaW / 2f - 20.dp.toPx())

    // Direction of motion for arrowhead
    val cosVal = cos(progress * 2f * PI.toFloat())
    val movingRight = cosVal >= 0f

    // Ghost trail (3 copies)
    for (i in 1..3) {
        val ghostP = (progress - i * 0.06f + 1f) % 1f
        val ghostSin = sin(ghostP * 2f * PI.toFloat())
        val ghostX = cx + ghostSin * (fiberAreaW / 2f - 20.dp.toPx())
        drawRoundRect(
            color = trailColor.copy(alpha = 0.08f * (4 - i)),
            topLeft = Offset(ghostX - 10.dp.toPx(), cy - 26.dp.toPx()),
            size = Size(20.dp.toPx(), 52.dp.toPx()),
            cornerRadius = CornerRadius(10.dp.toPx())
        )
    }

    // Thumb shape (tall rounded rectangle, perpendicular to fibers)
    drawRoundRect(
        color = thumbColor.copy(alpha = 0.3f),
        topLeft = Offset(thumbX - 13.dp.toPx(), cy - 30.dp.toPx()),
        size = Size(26.dp.toPx(), 60.dp.toPx()),
        cornerRadius = CornerRadius(13.dp.toPx())
    )
    drawRoundRect(
        color = thumbColor,
        topLeft = Offset(thumbX - 11.dp.toPx(), cy - 28.dp.toPx()),
        size = Size(22.dp.toPx(), 56.dp.toPx()),
        cornerRadius = CornerRadius(11.dp.toPx())
    )
    // Knuckle line
    drawLine(
        color = thumbColor.copy(alpha = 0.4f),
        start = Offset(thumbX - 8.dp.toPx(), cy - 6.dp.toPx()),
        end = Offset(thumbX + 8.dp.toPx(), cy - 6.dp.toPx()),
        strokeWidth = 1.5.dp.toPx(),
        cap = StrokeCap.Round
    )
    // Highlight
    drawRoundRect(
        color = Color.White.copy(alpha = 0.25f),
        topLeft = Offset(thumbX - 8.dp.toPx(), cy - 26.dp.toPx()),
        size = Size(8.dp.toPx(), 20.dp.toPx()),
        cornerRadius = CornerRadius(4.dp.toPx())
    )

    // Direction arrow at thumb tip
    val arrowY = cy - 42.dp.toPx()
    val arrowSize = 8.dp.toPx()
    val arrowPath = Path().apply {
        if (movingRight) {
            moveTo(thumbX + arrowSize * 1.5f, arrowY)
            lineTo(thumbX, arrowY - arrowSize)
            lineTo(thumbX, arrowY + arrowSize)
        } else {
            moveTo(thumbX - arrowSize * 1.5f, arrowY)
            lineTo(thumbX, arrowY - arrowSize)
            lineTo(thumbX, arrowY + arrowSize)
        }
        close()
    }
    drawPath(arrowPath, color = thumbColor.copy(alpha = 0.85f))

    drawLabel("Cross-fiber strokes", cx, size.height - 12.dp.toPx(), thumbColor)
}

// ─── VERTICAL_STROKE: long gliding stroke down the muscle ────────────────────

private fun DrawScope.drawGlidingStroke(
    progress: Float,
    strokeColor: Color,
    handColor: Color,
    muscleColor: Color
) {
    val cx = size.width / 2f
    val bodyW = 70.dp.toPx()
    val topY = 18.dp.toPx()
    val bottomY = size.height - 34.dp.toPx()

    // Body part outline (arm/neck silhouette — two parallel curved lines)
    val bodyPath = Path().apply {
        moveTo(cx - bodyW / 2f, topY + 10.dp.toPx())
        cubicTo(
            cx - bodyW / 2f - 8.dp.toPx(), topY + (bottomY - topY) * 0.3f,
            cx - bodyW / 2f + 4.dp.toPx(), topY + (bottomY - topY) * 0.7f,
            cx - bodyW / 2f, bottomY - 10.dp.toPx()
        )
        lineTo(cx + bodyW / 2f, bottomY - 10.dp.toPx())
        cubicTo(
            cx + bodyW / 2f + 4.dp.toPx(), topY + (bottomY - topY) * 0.7f,
            cx + bodyW / 2f - 8.dp.toPx(), topY + (bottomY - topY) * 0.3f,
            cx + bodyW / 2f, topY + 10.dp.toPx()
        )
        close()
    }
    drawPath(bodyPath, color = muscleColor.copy(alpha = 0.3f))
    drawPath(bodyPath, color = muscleColor, style = Stroke(width = 2.dp.toPx()))

    // Ease in-out using smooth step: 0.5 * (1 - cos(progress * PI))
    val eased = if (progress < 0.85f) {
        0.5f * (1f - cos(progress / 0.85f * PI.toFloat()))
    } else {
        // fade-out phase: maintain position, alpha fades
        1f
    }
    val alpha = if (progress > 0.82f) maxOf(0f, 1f - (progress - 0.82f) / 0.18f) else 1f

    val handY = topY + eased * (bottomY - topY - 40.dp.toPx())

    // Stroke trail (fading line behind the hand)
    if (handY > topY + 8.dp.toPx()) {
        val trailPath = Path().apply {
            moveTo(cx, topY + 8.dp.toPx())
            lineTo(cx, handY)
        }
        drawPath(
            trailPath,
            color = strokeColor.copy(alpha = alpha * 0.25f),
            style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Round)
        )
    }

    // Palm/hand shape (wide rounded rectangle)
    val handW = 58.dp.toPx()
    val handH = 28.dp.toPx()
    drawRoundRect(
        color = handColor.copy(alpha = alpha * 0.4f),
        topLeft = Offset(cx - handW / 2f - 3.dp.toPx(), handY - 2.dp.toPx()),
        size = Size(handW + 6.dp.toPx(), handH + 4.dp.toPx()),
        cornerRadius = CornerRadius(16.dp.toPx())
    )
    drawRoundRect(
        color = strokeColor.copy(alpha = alpha),
        topLeft = Offset(cx - handW / 2f, handY),
        size = Size(handW, handH),
        cornerRadius = CornerRadius(14.dp.toPx())
    )
    // Finger lines
    for (i in -1..1) {
        drawLine(
            color = strokeColor.copy(alpha = alpha * 0.45f),
            start = Offset(cx + i * 16.dp.toPx(), handY + 6.dp.toPx()),
            end = Offset(cx + i * 16.dp.toPx(), handY + handH - 6.dp.toPx()),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
    // Highlight
    drawRoundRect(
        color = Color.White.copy(alpha = alpha * 0.2f),
        topLeft = Offset(cx - handW / 2f + 6.dp.toPx(), handY + 4.dp.toPx()),
        size = Size(handW - 16.dp.toPx(), 8.dp.toPx()),
        cornerRadius = CornerRadius(4.dp.toPx())
    )

    // Downward arrow below hand (shows direction)
    val arrowY = handY + handH + 10.dp.toPx()
    val arrowPath = Path().apply {
        moveTo(cx, arrowY + 12.dp.toPx())
        lineTo(cx - 10.dp.toPx(), arrowY)
        lineTo(cx + 10.dp.toPx(), arrowY)
        close()
    }
    drawPath(arrowPath, color = strokeColor.copy(alpha = alpha * 0.8f))

    drawLabel("Long gliding stroke ↓", cx, size.height - 12.dp.toPx(), strokeColor)
}

// ─── PRESSURE_PULSE: sustained trigger point hold ────────────────────────────

private fun DrawScope.drawTriggerPointPulse(
    progress: Float,
    pressColor: Color,
    ringColor: Color,
    surfaceColor: Color,
    labelColor: Color
) {
    val cx = size.width / 2f
    val targetY = size.height * 0.58f
    val baseR = 18.dp.toPx()

    // Phase: 0-0.35 = approach, 0.35-0.72 = hold+ripple, 0.72-1.0 = lift
    val (fingerOffsetY, pressIntensity) = when {
        progress < 0.35f -> {
            val t = progress / 0.35f
            val eased = t * t * (3f - 2f * t) // smoothstep
            (-50.dp.toPx() * (1f - eased)) to eased
        }
        progress < 0.72f -> 0f to 1f
        else -> {
            val t = (progress - 0.72f) / 0.28f
            val eased = t * t * (3f - 2f * t)
            (-50.dp.toPx() * eased) to (1f - eased)
        }
    }
    val fingerY = targetY + fingerOffsetY - baseR - 8.dp.toPx()

    // Target area: concentric rings (bullseye)
    val maxTargetR = 62.dp.toPx()
    for (i in 3 downTo 1) {
        val r = maxTargetR * i / 3f
        drawCircle(
            color = ringColor.copy(alpha = 0.15f * (4 - i) + pressIntensity * 0.05f),
            radius = r,
            center = Offset(cx, targetY)
        )
        drawCircle(
            color = surfaceColor.copy(alpha = 0.7f),
            radius = r,
            center = Offset(cx, targetY),
            style = Stroke(width = 1.5.dp.toPx())
        )
    }
    // Center dot of target
    drawCircle(
        color = pressColor.copy(alpha = 0.5f + pressIntensity * 0.4f),
        radius = baseR,
        center = Offset(cx, targetY)
    )
    drawCircle(
        color = pressColor,
        radius = baseR * 0.5f,
        center = Offset(cx, targetY)
    )

    // Expanding ripple rings (only visible during hold phase)
    if (pressIntensity > 0.5f) {
        val rippleAlphaBase = (pressIntensity - 0.5f) * 2f
        for (i in 0..1) {
            val rippleP = (progress * 1.5f + i * 0.5f) % 1f
            val rippleR = baseR + rippleP * (maxTargetR - baseR) * 0.9f
            val rippleAlpha = rippleAlphaBase * (1f - rippleP) * 0.55f
            drawCircle(
                color = pressColor.copy(alpha = rippleAlpha),
                radius = rippleR,
                center = Offset(cx, targetY),
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }

    // Finger pad descending / ascending
    val fingerAlpha = if (progress < 0.1f) progress / 0.1f
                      else if (progress > 0.9f) (1f - progress) / 0.1f
                      else 1f

    // Shadow under finger
    if (fingerOffsetY > -30.dp.toPx()) {
        val shadowAlpha = (1f - abs(fingerOffsetY) / 30.dp.toPx()) * 0.15f
        drawCircle(
            color = pressColor.copy(alpha = shadowAlpha),
            radius = baseR + 8.dp.toPx(),
            center = Offset(cx + 3.dp.toPx(), targetY + 4.dp.toPx())
        )
    }

    // Finger pad
    drawCircle(
        color = pressColor.copy(alpha = fingerAlpha * 0.3f),
        radius = baseR + 5.dp.toPx(),
        center = Offset(cx, fingerY)
    )
    drawCircle(
        color = pressColor.copy(alpha = fingerAlpha),
        radius = baseR,
        center = Offset(cx, fingerY)
    )
    // Specular
    drawCircle(
        color = Color.White.copy(alpha = fingerAlpha * 0.35f),
        radius = 6.dp.toPx(),
        center = Offset(cx - 5.dp.toPx(), fingerY - 5.dp.toPx())
    )

    // "Hold" label appears during hold phase
    if (pressIntensity > 0.6f) {
        val holdAlpha = ((pressIntensity - 0.6f) / 0.4f).coerceIn(0f, 1f)
        drawLabel("Hold", cx, targetY + 88.dp.toPx(), pressColor.copy(alpha = holdAlpha))
    } else {
        drawLabel("Sustained pressure", cx, size.height - 12.dp.toPx(), pressColor)
    }
}

// ─── Helper: draw a centered text label ──────────────────────────────────────

private fun DrawScope.drawLabel(text: String, x: Float, y: Float, color: Color) {
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            textSize = 11.sp.toPx()
            this.color = color.toArgb()
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            alpha = (color.alpha * 255).toInt().coerceIn(0, 255)
        }
        canvas.nativeCanvas.drawText(text, x, y, paint)
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Animation – Circular (Kneading)")
@Composable
private fun PreviewCircular() {
    SelfMassageTheme { MassageAnimationCanvas(animationType = AnimationType.CIRCULAR) }
}

@Preview(showBackground = true, name = "Animation – Horizontal Sweep (Cross-Fiber)")
@Composable
private fun PreviewHorizontalSweep() {
    SelfMassageTheme { MassageAnimationCanvas(animationType = AnimationType.HORIZONTAL_SWEEP) }
}

@Preview(showBackground = true, name = "Animation – Vertical Stroke (Effleurage)")
@Composable
private fun PreviewVerticalStroke() {
    SelfMassageTheme { MassageAnimationCanvas(animationType = AnimationType.VERTICAL_STROKE) }
}

@Preview(showBackground = true, name = "Animation – Pressure Pulse (Trigger Point)")
@Composable
private fun PreviewPressurePulse() {
    SelfMassageTheme { MassageAnimationCanvas(animationType = AnimationType.PRESSURE_PULSE) }
}
