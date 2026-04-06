package ai.mlxdroid.selfmassage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ai.mlxdroid.selfmassage.ui.theme.Accent

@Composable
fun AccentChip(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = Accent,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Accent.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}
