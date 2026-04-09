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
import ai.mlxdroid.selfmassage.data.model.BodyLocation
import ai.mlxdroid.selfmassage.ui.theme.SelfMassageTheme
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MassageAnimationCanvas(
    animationType: AnimationType,
    bodyLocation: BodyLocation,
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
        drawBodyLocator(progress, bodyLocation, surfaceVariant, primary, onPrimaryContainer)
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

// ─── Body location locator panel (top-left corner) ───────────────────────────

private fun DrawScope.drawBodyLocator(
    progress: Float,
    bodyLocation: BodyLocation,
    bgColor: Color,
    spotColor: Color,
    outlineColor: Color
) {
    val pad = 10.dp.toPx()
    val panelW = 76.dp.toPx()
    val panelH = 108.dp.toPx()
    val left = pad
    val top = pad

    // Panel background
    drawRoundRect(
        color = bgColor.copy(alpha = 0.80f),
        topLeft = Offset(left, top),
        size = Size(panelW, panelH),
        cornerRadius = CornerRadius(10.dp.toPx())
    )
    drawRoundRect(
        color = outlineColor.copy(alpha = 0.25f),
        topLeft = Offset(left, top),
        size = Size(panelW, panelH),
        cornerRadius = CornerRadius(10.dp.toPx()),
        style = Stroke(width = 1.dp.toPx())
    )

    val cx = left + panelW / 2f
    val pulse = 0.5f + 0.5f * sin(progress * 2f * PI.toFloat())

    when (bodyLocation) {
        BodyLocation.BASE_OF_SKULL,
        BodyLocation.LATERAL_NECK,
        BodyLocation.UPPER_TRAPEZIUS,
        BodyLocation.SHOULDER_BLADE,
        BodyLocation.UPPER_SHOULDER,
        BodyLocation.OUTER_SHOULDER -> drawUpperBodyLocator(
            bodyLocation, cx, top, panelH, outlineColor, spotColor, pulse
        )

        BodyLocation.FOREARM,
        BodyLocation.HAND_WEB -> drawArmLocator(
            bodyLocation, cx, top, panelH, outlineColor, spotColor, pulse
        )

        BodyLocation.LUMBAR_SPINE,
        BodyLocation.LATERAL_LOWER_BACK,
        BodyLocation.SACRUM -> drawLowerBackLocator(
            bodyLocation, cx, top, panelH, outlineColor, spotColor, pulse
        )

        BodyLocation.IT_BAND,
        BodyLocation.CALF,
        BodyLocation.PLANTAR_FOOT -> drawLegLocator(
            bodyLocation, cx, top, panelH, outlineColor, spotColor, pulse
        )
    }
}

// Single continuous bezier path — head + neck + shoulders + torso + upper arms
private fun DrawScope.drawUpperBodyLocator(
    location: BodyLocation,
    cx: Float, panelTop: Float, panelH: Float,
    outlineColor: Color, spotColor: Color, pulse: Float
) {
    val figTop    = panelTop + 6.dp.toPx()
    val headCy    = figTop + 14.dp.toPx()
    val headRx    = 10.dp.toPx()
    val headRy    = 12.dp.toPx()
    val neckTopY  = figTop + 26.dp.toPx()
    val neckBotY  = figTop + 38.dp.toPx()
    val neckHW    = 6.dp.toPx()
    val shoulderY = figTop + 44.dp.toPx()
    val sX        = 30.dp.toPx()   // shoulder half-spread from cx
    val armpitY   = figTop + 53.dp.toPx()
    val armBotY   = figTop + 80.dp.toPx()
    val armOutX   = 30.dp.toPx()   // outer arm x offset from cx
    val armInX    = 21.dp.toPx()   // inner arm x offset from cx
    val torsoBot  = panelTop + panelH - 10.dp.toPx()
    val torsoHW   = 13.dp.toPx()

    // Single closed body path (neck opening → shoulders → arms → torso → back)
    val bodyPath = Path().apply {
        // Start at top-left of neck opening
        moveTo(cx - neckHW, neckTopY)
        lineTo(cx + neckHW, neckTopY)
        lineTo(cx + neckHW, neckBotY)
        // Right trapezius → shoulder tip
        cubicTo(cx + 10.dp.toPx(), neckBotY + 2.dp.toPx(),
                cx + 26.dp.toPx(), shoulderY - 1.dp.toPx(),
                cx + sX, shoulderY)
        // Right outer arm down to elbow
        cubicTo(cx + sX + 2.dp.toPx(), shoulderY + 10.dp.toPx(),
                cx + armOutX + 1.dp.toPx(), armBotY - 10.dp.toPx(),
                cx + armOutX, armBotY)
        // Rounded elbow
        cubicTo(cx + armOutX - 1.dp.toPx(), armBotY + 7.dp.toPx(),
                cx + armInX + 1.dp.toPx(), armBotY + 7.dp.toPx(),
                cx + armInX, armBotY)
        // Right inner arm back up to armpit
        cubicTo(cx + armInX - 1.dp.toPx(), armBotY - 8.dp.toPx(),
                cx + 20.dp.toPx(), armpitY + 8.dp.toPx(),
                cx + 20.dp.toPx(), armpitY)
        // Right torso side down to bottom
        cubicTo(cx + 18.dp.toPx(), armpitY + 14.dp.toPx(),
                cx + torsoHW + 2.dp.toPx(), torsoBot - 8.dp.toPx(),
                cx + torsoHW, torsoBot)
        lineTo(cx - torsoHW, torsoBot)
        // Left torso up to armpit
        cubicTo(cx - torsoHW - 2.dp.toPx(), torsoBot - 8.dp.toPx(),
                cx - 18.dp.toPx(), armpitY + 14.dp.toPx(),
                cx - 20.dp.toPx(), armpitY)
        // Left inner arm down to elbow
        cubicTo(cx - 20.dp.toPx(), armpitY + 8.dp.toPx(),
                cx - armInX + 1.dp.toPx(), armBotY - 8.dp.toPx(),
                cx - armInX, armBotY)
        // Left rounded elbow
        cubicTo(cx - armInX - 1.dp.toPx(), armBotY + 7.dp.toPx(),
                cx - armOutX + 1.dp.toPx(), armBotY + 7.dp.toPx(),
                cx - armOutX, armBotY)
        // Left outer arm up to shoulder
        cubicTo(cx - armOutX - 1.dp.toPx(), armBotY - 10.dp.toPx(),
                cx - sX - 2.dp.toPx(), shoulderY + 10.dp.toPx(),
                cx - sX, shoulderY)
        // Left trapezius back to neck
        cubicTo(cx - 26.dp.toPx(), shoulderY - 1.dp.toPx(),
                cx - 10.dp.toPx(), neckBotY + 2.dp.toPx(),
                cx - neckHW, neckBotY)
        lineTo(cx - neckHW, neckTopY)
        close()
    }

    // Draw body silhouette
    drawPath(bodyPath, color = outlineColor.copy(alpha = 0.14f))
    drawPath(bodyPath, color = outlineColor.copy(alpha = 0.60f),
             style = Stroke(width = 1.5.dp.toPx(), join = StrokeJoin.Round, cap = StrokeCap.Round))

    // Head (on top of body path so neck junction is hidden)
    drawOval(
        color = outlineColor.copy(alpha = 0.14f),
        topLeft = Offset(cx - headRx, headCy - headRy),
        size = Size(headRx * 2, headRy * 2)
    )
    drawOval(
        color = outlineColor.copy(alpha = 0.60f),
        topLeft = Offset(cx - headRx, headCy - headRy),
        size = Size(headRx * 2, headRy * 2),
        style = Stroke(width = 1.5.dp.toPx())
    )

    // Pulsing spot at anatomically correct position
    val spot = when (location) {
        BodyLocation.BASE_OF_SKULL     -> Offset(cx, neckTopY)
        BodyLocation.LATERAL_NECK      -> Offset(cx + neckHW + 5.dp.toPx(), (neckTopY + neckBotY) / 2f)
        BodyLocation.UPPER_TRAPEZIUS   -> Offset(cx + 18.dp.toPx(), shoulderY - 2.dp.toPx())
        BodyLocation.SHOULDER_BLADE    -> Offset(cx + 9.dp.toPx(), armpitY + 14.dp.toPx())
        BodyLocation.UPPER_SHOULDER    -> Offset(cx + sX - 1.dp.toPx(), shoulderY + 2.dp.toPx())
        else                           -> Offset(cx + armOutX, armpitY - 1.dp.toPx()) // OUTER_SHOULDER
    }
    drawCircle(color = spotColor.copy(alpha = 0.20f + 0.20f * pulse), radius = 9.dp.toPx(), center = spot)
    drawCircle(color = spotColor, radius = 4.dp.toPx(), center = spot)
}

// Arm close-up: forearm tapering to wrist + palm + thumb + finger hints
private fun DrawScope.drawArmLocator(
    location: BodyLocation,
    cx: Float, panelTop: Float, panelH: Float,
    outlineColor: Color, spotColor: Color, pulse: Float
) {
    val elbowY    = panelTop + 12.dp.toPx()
    val wristY    = panelTop + 72.dp.toPx()
    val armHW     = 9.dp.toPx()
    val wristHW   = 7.dp.toPx()
    val palmBotY  = panelTop + 96.dp.toPx()
    val thumbTipX = cx + 19.dp.toPx()
    val thumbMidY = wristY + 13.dp.toPx()

    // Elbow hint (small arc at top)
    drawArc(
        color = outlineColor.copy(alpha = 0.30f),
        startAngle = 180f, sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(cx - armHW, elbowY - 5.dp.toPx()),
        size = Size(armHW * 2, 10.dp.toPx()),
        style = Stroke(width = 1.5.dp.toPx())
    )

    // Arm + hand single closed path
    val armPath = Path().apply {
        moveTo(cx - armHW, elbowY)
        // Left outer arm, tapering toward wrist
        cubicTo(cx - armHW - 1.dp.toPx(), elbowY + 20.dp.toPx(),
                cx - wristHW - 1.dp.toPx(), wristY - 10.dp.toPx(),
                cx - wristHW, wristY)
        // Palm left side to bottom-left
        cubicTo(cx - 11.dp.toPx(), wristY + 5.dp.toPx(),
                cx - 12.dp.toPx(), palmBotY - 6.dp.toPx(),
                cx - 8.dp.toPx(), palmBotY)
        // Across finger base (gentle rounded curve)
        cubicTo(cx - 3.dp.toPx(), palmBotY + 3.dp.toPx(),
                cx + 3.dp.toPx(), palmBotY + 3.dp.toPx(),
                cx + 8.dp.toPx(), palmBotY)
        // Up right side of palm to wrist
        cubicTo(cx + 10.dp.toPx(), palmBotY - 4.dp.toPx(),
                cx + wristHW + 2.dp.toPx(), wristY + 8.dp.toPx(),
                cx + wristHW, wristY)
        // Right outer arm back up to elbow
        cubicTo(cx + wristHW + 1.dp.toPx(), wristY - 10.dp.toPx(),
                cx + armHW + 1.dp.toPx(), elbowY + 20.dp.toPx(),
                cx + armHW, elbowY)
        close()
    }

    // Thumb blob
    val thumbPath = Path().apply {
        moveTo(cx + wristHW, wristY + 5.dp.toPx())
        cubicTo(cx + wristHW + 4.dp.toPx(), wristY - 1.dp.toPx(),
                thumbTipX + 2.dp.toPx(), thumbMidY - 6.dp.toPx(),
                thumbTipX, thumbMidY)
        cubicTo(thumbTipX - 2.dp.toPx(), thumbMidY + 6.dp.toPx(),
                cx + wristHW + 3.dp.toPx(), wristY + 15.dp.toPx(),
                cx + wristHW, wristY + 12.dp.toPx())
        close()
    }

    // Fill then stroke arm and thumb
    drawPath(armPath, color = outlineColor.copy(alpha = 0.14f))
    drawPath(thumbPath, color = outlineColor.copy(alpha = 0.14f))
    drawPath(armPath, color = outlineColor.copy(alpha = 0.60f),
             style = Stroke(width = 1.5.dp.toPx(), join = StrokeJoin.Round, cap = StrokeCap.Round))
    drawPath(thumbPath, color = outlineColor.copy(alpha = 0.60f),
             style = Stroke(width = 1.5.dp.toPx(), join = StrokeJoin.Round, cap = StrokeCap.Round))

    // Finger knuckle divider lines
    for (i in -1..1) {
        val fx = cx + i * 5.dp.toPx()
        drawLine(
            color = outlineColor.copy(alpha = 0.30f),
            start = Offset(fx, palmBotY - 2.dp.toPx()),
            end = Offset(fx, palmBotY - 8.dp.toPx()),
            strokeWidth = 1.dp.toPx(), cap = StrokeCap.Round
        )
    }

    // Pulsing spot
    val spot = when (location) {
        BodyLocation.FOREARM -> Offset(cx, (elbowY + wristY) / 2f)
        else                 -> Offset(cx + wristHW + 5.dp.toPx(), wristY + 10.dp.toPx()) // HAND_WEB
    }
    drawCircle(color = spotColor.copy(alpha = 0.20f + 0.20f * pulse), radius = 9.dp.toPx(), center = spot)
    drawCircle(color = spotColor, radius = 4.dp.toPx(), center = spot)
}

// Lower back close-up: torso rear view with waistline, spine hint, sacrum triangle
private fun DrawScope.drawLowerBackLocator(
    location: BodyLocation,
    cx: Float, panelTop: Float, panelH: Float,
    outlineColor: Color, spotColor: Color, pulse: Float
) {
    val figTop   = panelTop + 8.dp.toPx()
    val figBot   = panelTop + panelH - 8.dp.toPx()
    val torsoHW  = 28.dp.toPx()
    val waistHW  = 22.dp.toPx()
    val hipHW    = 26.dp.toPx()
    val midY     = (figTop + figBot) / 2f

    // Torso outline (trapezoid: wider shoulders → narrow waist → wider hips)
    val bodyPath = Path().apply {
        moveTo(cx - torsoHW, figTop)
        lineTo(cx + torsoHW, figTop)
        cubicTo(cx + torsoHW - 2.dp.toPx(), midY - 8.dp.toPx(),
                cx + waistHW + 2.dp.toPx(), midY - 4.dp.toPx(),
                cx + waistHW, midY)
        cubicTo(cx + waistHW + 2.dp.toPx(), midY + 8.dp.toPx(),
                cx + hipHW - 2.dp.toPx(), figBot - 8.dp.toPx(),
                cx + hipHW, figBot)
        lineTo(cx - hipHW, figBot)
        cubicTo(cx - hipHW + 2.dp.toPx(), figBot - 8.dp.toPx(),
                cx - waistHW - 2.dp.toPx(), midY + 8.dp.toPx(),
                cx - waistHW, midY)
        cubicTo(cx - waistHW - 2.dp.toPx(), midY - 4.dp.toPx(),
                cx - torsoHW + 2.dp.toPx(), midY - 8.dp.toPx(),
                cx - torsoHW, figTop)
        close()
    }

    drawPath(bodyPath, color = outlineColor.copy(alpha = 0.14f))
    drawPath(bodyPath, color = outlineColor.copy(alpha = 0.60f),
        style = Stroke(width = 1.5.dp.toPx(), join = StrokeJoin.Round, cap = StrokeCap.Round))

    // Spine line (dashed hint)
    val spineTop = figTop + 4.dp.toPx()
    val spineBot = figBot - 6.dp.toPx()
    for (y in generateSequence(spineTop) { it + 6.dp.toPx() }.takeWhile { it < spineBot }) {
        drawLine(
            color = outlineColor.copy(alpha = 0.30f),
            start = Offset(cx, y),
            end = Offset(cx, (y + 3.dp.toPx()).coerceAtMost(spineBot)),
            strokeWidth = 1.dp.toPx(), cap = StrokeCap.Round
        )
    }

    // Pulsing spot
    val spot = when (location) {
        BodyLocation.LUMBAR_SPINE      -> Offset(cx, midY - 4.dp.toPx())
        BodyLocation.LATERAL_LOWER_BACK -> Offset(cx + waistHW - 6.dp.toPx(), midY)
        else                            -> Offset(cx, figBot - 12.dp.toPx()) // SACRUM
    }
    drawCircle(color = spotColor.copy(alpha = 0.20f + 0.20f * pulse), radius = 9.dp.toPx(), center = spot)
    drawCircle(color = spotColor, radius = 4.dp.toPx(), center = spot)
}

// Leg close-up: thigh tapering to knee, calf tapering to ankle, foot stub
private fun DrawScope.drawLegLocator(
    location: BodyLocation,
    cx: Float, panelTop: Float, panelH: Float,
    outlineColor: Color, spotColor: Color, pulse: Float
) {
    val hipY     = panelTop + 8.dp.toPx()
    val kneeY    = panelTop + 42.dp.toPx()
    val ankleY   = panelTop + 78.dp.toPx()
    val footBotY = panelTop + panelH - 6.dp.toPx()
    val thighHW  = 14.dp.toPx()
    val kneeHW   = 10.dp.toPx()
    val calfHW   = 10.dp.toPx()
    val ankleHW  = 6.dp.toPx()
    val footLen  = 18.dp.toPx()

    // Leg path (single continuous outline)
    val legPath = Path().apply {
        moveTo(cx - thighHW, hipY)
        // Outer thigh down to knee
        cubicTo(cx - thighHW - 1.dp.toPx(), hipY + 10.dp.toPx(),
                cx - kneeHW - 2.dp.toPx(), kneeY - 8.dp.toPx(),
                cx - kneeHW, kneeY)
        // Outer calf down to ankle
        cubicTo(cx - calfHW - 1.dp.toPx(), kneeY + 10.dp.toPx(),
                cx - ankleHW - 1.dp.toPx(), ankleY - 8.dp.toPx(),
                cx - ankleHW, ankleY)
        // Foot (left side down, across bottom, back up right)
        lineTo(cx - ankleHW, footBotY - 4.dp.toPx())
        cubicTo(cx - ankleHW - 2.dp.toPx(), footBotY,
                cx + footLen - 4.dp.toPx(), footBotY,
                cx + footLen, footBotY - 6.dp.toPx())
        // Back up right ankle
        lineTo(cx + ankleHW, ankleY)
        // Inner calf up to knee
        cubicTo(cx + ankleHW + 1.dp.toPx(), ankleY - 8.dp.toPx(),
                cx + calfHW + 1.dp.toPx(), kneeY + 10.dp.toPx(),
                cx + kneeHW, kneeY)
        // Inner thigh up to hip
        cubicTo(cx + kneeHW + 2.dp.toPx(), kneeY - 8.dp.toPx(),
                cx + thighHW + 1.dp.toPx(), hipY + 10.dp.toPx(),
                cx + thighHW, hipY)
        close()
    }

    drawPath(legPath, color = outlineColor.copy(alpha = 0.14f))
    drawPath(legPath, color = outlineColor.copy(alpha = 0.60f),
        style = Stroke(width = 1.5.dp.toPx(), join = StrokeJoin.Round, cap = StrokeCap.Round))

    // Kneecap hint
    drawOval(
        color = outlineColor.copy(alpha = 0.25f),
        topLeft = Offset(cx - 5.dp.toPx(), kneeY - 4.dp.toPx()),
        size = Size(10.dp.toPx(), 8.dp.toPx()),
        style = Stroke(width = 1.dp.toPx())
    )

    // Pulsing spot
    val spot = when (location) {
        BodyLocation.IT_BAND      -> Offset(cx - thighHW + 2.dp.toPx(), (hipY + kneeY) / 2f)
        BodyLocation.CALF         -> Offset(cx, (kneeY + ankleY) / 2f)
        else                      -> Offset(cx + 2.dp.toPx(), footBotY - 10.dp.toPx()) // PLANTAR_FOOT
    }
    drawCircle(color = spotColor.copy(alpha = 0.20f + 0.20f * pulse), radius = 9.dp.toPx(), center = spot)
    drawCircle(color = spotColor, radius = 4.dp.toPx(), center = spot)
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

@Preview(showBackground = true, name = "Animation – Circular (Upper Trapezius)")
@Composable
private fun PreviewCircular() {
    SelfMassageTheme {
        MassageAnimationCanvas(
            animationType = AnimationType.CIRCULAR,
            bodyLocation = BodyLocation.UPPER_TRAPEZIUS
        )
    }
}

@Preview(showBackground = true, name = "Animation – Circular (Shoulder Blade)")
@Composable
private fun PreviewCircularShoulderBlade() {
    SelfMassageTheme {
        MassageAnimationCanvas(
            animationType = AnimationType.CIRCULAR,
            bodyLocation = BodyLocation.SHOULDER_BLADE
        )
    }
}

@Preview(showBackground = true, name = "Animation – Horizontal Sweep (Shoulder)")
@Composable
private fun PreviewHorizontalSweep() {
    SelfMassageTheme {
        MassageAnimationCanvas(
            animationType = AnimationType.HORIZONTAL_SWEEP,
            bodyLocation = BodyLocation.UPPER_SHOULDER
        )
    }
}

@Preview(showBackground = true, name = "Animation – Horizontal Sweep (Forearm)")
@Composable
private fun PreviewHorizontalSweepForearm() {
    SelfMassageTheme {
        MassageAnimationCanvas(
            animationType = AnimationType.HORIZONTAL_SWEEP,
            bodyLocation = BodyLocation.FOREARM
        )
    }
}

@Preview(showBackground = true, name = "Animation – Vertical Stroke (Lateral Neck)")
@Composable
private fun PreviewVerticalStroke() {
    SelfMassageTheme {
        MassageAnimationCanvas(
            animationType = AnimationType.VERTICAL_STROKE,
            bodyLocation = BodyLocation.LATERAL_NECK
        )
    }
}

@Preview(showBackground = true, name = "Animation – Vertical Stroke (Outer Shoulder)")
@Composable
private fun PreviewVerticalStrokeOuter() {
    SelfMassageTheme {
        MassageAnimationCanvas(
            animationType = AnimationType.VERTICAL_STROKE,
            bodyLocation = BodyLocation.OUTER_SHOULDER
        )
    }
}

@Preview(showBackground = true, name = "Animation – Pressure Pulse (Base of Skull)")
@Composable
private fun PreviewPressurePulse() {
    SelfMassageTheme {
        MassageAnimationCanvas(
            animationType = AnimationType.PRESSURE_PULSE,
            bodyLocation = BodyLocation.BASE_OF_SKULL
        )
    }
}

@Preview(showBackground = true, name = "Animation – Pressure Pulse (Hand Web)")
@Composable
private fun PreviewPressurePulseHand() {
    SelfMassageTheme {
        MassageAnimationCanvas(
            animationType = AnimationType.PRESSURE_PULSE,
            bodyLocation = BodyLocation.HAND_WEB
        )
    }
}
