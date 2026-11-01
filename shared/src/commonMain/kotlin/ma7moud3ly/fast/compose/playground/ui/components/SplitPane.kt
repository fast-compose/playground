package ma7moud3ly.fast.compose.playground.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/** The thickness of the splitter between side by side panes; stacked panes get a thicker one, easier to touch. */
private val SplitterSize = 8.dp
private val SplitterSizeVertical = 12.dp

/**
 * Two panes split by a bar that can be dragged to resize them: [first] on the left and [second] on the right, or
 * [first] on top and [second] below when [vertical].
 *
 * [ratio] is the share of [first], kept between [minRatio] and [maxRatio]; dragging the bar calls [onRatioChange] and
 * a double click on it calls [onReset].
 *
 * @param splitterLabel what the bar does, for screen readers
 */
@Composable
fun SplitPane(
    vertical: Boolean,
    ratio: Float,
    onRatioChange: (Float) -> Unit,
    onReset: () -> Unit,
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    minRatio: Float = 0.15f,
    maxRatio: Float = 0.85f,
    splitterLabel: String = ""
) {
    var size by remember { mutableIntStateOf(0) }
    val currentRatio = ratio.coerceIn(minRatio, maxRatio)
    // the ratio while dragging; each move adds to it, even before the panes recompose
    var dragRatio by remember { mutableFloatStateOf(currentRatio) }
    val currentOnRatioChange by rememberUpdatedState(onRatioChange)
    val currentOnReset by rememberUpdatedState(onReset)
    val dragState = rememberDraggableState { delta ->
        if (size > 0) {
            dragRatio = (dragRatio + delta / size).coerceIn(minRatio, maxRatio)
            currentOnRatioChange(dragRatio)
        }
    }
    val interactionSource = remember { MutableInteractionSource() }
    val splitter = @Composable {
        Splitter(
            vertical = vertical,
            interactionSource = interactionSource,
            modifier = Modifier
                .draggable(
                    state = dragState,
                    orientation = if (vertical) Orientation.Vertical else Orientation.Horizontal,
                    interactionSource = interactionSource,
                    onDragStarted = { dragRatio = currentRatio }
                )
                .pointerInput(Unit) { detectTapGestures(onDoubleTap = { currentOnReset() }) }
                .semantics { contentDescription = splitterLabel }
        )
    }
    if (vertical) Column(modifier.onSizeChanged { size = it.height }) {
        Box(Modifier.fillMaxWidth().weight(currentRatio)) { first() }
        splitter()
        Box(Modifier.fillMaxWidth().weight(1f - currentRatio)) { second() }
    } else Row(modifier.onSizeChanged { size = it.width }) {
        Box(Modifier.fillMaxHeight().weight(currentRatio)) { first() }
        splitter()
        Box(Modifier.fillMaxHeight().weight(1f - currentRatio)) { second() }
    }
}

/**
 * The bar between the panes: a hairline with a grip in its middle. The grip takes the accent color while the pointer
 * is over the bar or drags it.
 */
@Composable
private fun Splitter(
    vertical: Boolean,
    interactionSource: MutableInteractionSource,
    modifier: Modifier
) {
    val colors = MaterialTheme.colorScheme
    val hovered by interactionSource.collectIsHoveredAsState()
    val dragged by interactionSource.collectIsDraggedAsState()
    val active = hovered || dragged
    Box(
        modifier = modifier
            .hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand)
            .background(colors.surface)
            .then(
                if (vertical) Modifier.fillMaxWidth().height(SplitterSizeVertical)
                else Modifier.fillMaxHeight().width(SplitterSize)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .background(colors.outlineVariant)
                .then(if (vertical) Modifier.fillMaxWidth().height(1.dp) else Modifier.fillMaxHeight().width(1.dp))
        )
        Box(
            Modifier
                .then(
                    if (vertical) Modifier.size(width = 36.dp, height = 4.dp)
                    else Modifier.size(width = 4.dp, height = 36.dp)
                )
                .clip(CircleShape)
                .background(if (active) colors.primary else colors.outline)
        )
    }
}
