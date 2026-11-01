package ma7moud3ly.fast.compose.playground.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

/**
 * A button of a bar: an [icon], with its [label] beside it when [showLabel] is set.
 *
 * A [filled] button is painted in the accent color, a [selected] one in a lighter fill. A disabled button is faded and
 * ignores clicks. A [compact] button is smaller, for phones.
 */
@Composable
fun ToolbarButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false,
    filled: Boolean = false,
    selected: Boolean = false,
    enabled: Boolean = true,
    compact: Boolean = false,
    trailing: @Composable (() -> Unit)? = null
) {
    val colors = MaterialTheme.colorScheme
    val content = when {
        filled -> colors.onPrimary
        enabled -> colors.onSurface
        else -> colors.onSurface.copy(alpha = 0.35f)
    }
    val background = when {
        filled -> colors.primary
        selected -> colors.surfaceVariant
        else -> Color.Transparent
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .pointerHoverIcon(if (enabled) PointerIcon.Hand else PointerIcon.Default)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(
                horizontal = if (showLabel) 10.dp else if (compact) 6.dp else 8.dp,
                vertical = if (compact) 6.dp else 8.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = content,
            modifier = Modifier.size(if (compact) 18.dp else 20.dp)
        )
        if (showLabel) Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = content,
            maxLines = 1
        )
        trailing?.invoke()
    }
}

/** A short vertical line between groups of bar buttons. */
@Composable
fun ToolbarDivider(compact: Boolean = false) {
    VerticalDivider(
        color = MaterialTheme.colorScheme.outlineVariant,
        modifier = Modifier
            .padding(horizontal = if (compact) 2.dp else 4.dp)
            .height(if (compact) 18.dp else 22.dp)
    )
}
