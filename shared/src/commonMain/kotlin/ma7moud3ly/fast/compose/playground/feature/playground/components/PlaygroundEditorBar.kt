package ma7moud3ly.fast.compose.playground.feature.playground.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Redo
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.HorizontalSplit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material.icons.outlined.TextDecrease
import androidx.compose.material.icons.outlined.TextIncrease
import androidx.compose.material.icons.outlined.VerticalSplit
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ma7moud3ly.nemo.model.EditorThemes
import ma7moud3ly.fast.compose.playground.feature.playground.model.PlaygroundEvent
import ma7moud3ly.fast.compose.playground.model.ViewMode
import ma7moud3ly.fast.compose.playground.resources.Res
import ma7moud3ly.fast.compose.playground.resources.editor_redo
import ma7moud3ly.fast.compose.playground.resources.editor_search
import ma7moud3ly.fast.compose.playground.resources.editor_undo
import ma7moud3ly.fast.compose.playground.resources.editor_zoom_in
import ma7moud3ly.fast.compose.playground.resources.editor_zoom_out
import ma7moud3ly.fast.compose.playground.resources.logs_hide
import ma7moud3ly.fast.compose.playground.resources.logs_show
import ma7moud3ly.fast.compose.playground.resources.mode_edit
import ma7moud3ly.fast.compose.playground.resources.mode_preview
import ma7moud3ly.fast.compose.playground.resources.mode_split
import ma7moud3ly.fast.compose.playground.resources.preview_restart
import ma7moud3ly.fast.compose.playground.ui.components.ToolbarButton
import ma7moud3ly.fast.compose.playground.ui.components.ToolbarDivider
import ma7moud3ly.fast.compose.playground.ui.theme.AppTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Preview
@Composable
private fun PlaygroundEditorBarPreview() {
    AppTheme {
        PlaygroundEditorBar(
            mode = ViewMode.SPLIT,
            vertical = false,
            compact = false,
            canUndo = true,
            canRedo = false,
            fontSize = 14,
            problems = 2,
            showLogs = false,
            uiEvents = {}
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun PlaygroundEditorBarPreviewCompact() {
    AppTheme(theme = EditorThemes.VS_CODE_DARK) {
        PlaygroundEditorBar(
            mode = ViewMode.SPLIT,
            vertical = true,
            compact = true,
            canUndo = true,
            canRedo = true,
            fontSize = 12,
            problems = 0,
            showLogs = true,
            uiEvents = {}
        )
    }
}

/**
 * The bar above the panes. On its left are the editor's actions: undo and redo, search, and the font size; they
 * are hidden while only the preview is shown. On its right are run again, the logs toggle with the number of
 * problems, and the switch between the code, both panes and the preview.
 *
 * @param vertical whether the panes are stacked; the split icon then turns to match
 * @param compact whether the window is narrow; the buttons are then smaller and the font size is not shown
 */
@Composable
internal fun PlaygroundEditorBar(
    mode: ViewMode,
    vertical: Boolean,
    compact: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    fontSize: Int,
    problems: Int,
    showLogs: Boolean,
    uiEvents: (PlaygroundEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .height(if (compact) 40.dp else 44.dp)
            .padding(horizontal = if (compact) 4.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // the editor's actions scroll sideways when the bar is too narrow for them
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(if (compact) 0.dp else 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mode != ViewMode.PREVIEW) EditorActions(
                compact = compact,
                canUndo = canUndo,
                canRedo = canRedo,
                fontSize = fontSize,
                uiEvents = uiEvents
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(if (compact) 0.dp else 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mode != ViewMode.EDIT) ToolbarButton(
                icon = Icons.Outlined.Refresh,
                label = stringResource(Res.string.preview_restart),
                compact = compact,
                onClick = { uiEvents(PlaygroundEvent.Restart) }
            )
            LogsButton(
                problems = problems,
                showLogs = showLogs,
                compact = compact,
                onClick = { uiEvents(PlaygroundEvent.ToggleLogs) }
            )
            ToolbarDivider(compact = compact)
            ModeSwitch(
                mode = mode,
                vertical = vertical,
                compact = compact,
                onSelect = { uiEvents(PlaygroundEvent.SelectMode(it)) }
            )
        }
    }
}

@Composable
private fun EditorActions(
    compact: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    fontSize: Int,
    uiEvents: (PlaygroundEvent) -> Unit
) {
    ToolbarButton(
        icon = Icons.AutoMirrored.Outlined.Undo,
        label = stringResource(Res.string.editor_undo),
        enabled = canUndo,
        compact = compact,
        onClick = { uiEvents(PlaygroundEvent.Undo) }
    )
    ToolbarButton(
        icon = Icons.AutoMirrored.Outlined.Redo,
        label = stringResource(Res.string.editor_redo),
        enabled = canRedo,
        compact = compact,
        onClick = { uiEvents(PlaygroundEvent.Redo) }
    )
    ToolbarDivider(compact = compact)
    ToolbarButton(
        icon = Icons.Outlined.Search,
        label = stringResource(Res.string.editor_search),
        compact = compact,
        onClick = { uiEvents(PlaygroundEvent.Search) }
    )
    ToolbarDivider(compact = compact)
    ToolbarButton(
        icon = Icons.Outlined.TextDecrease,
        label = stringResource(Res.string.editor_zoom_out),
        compact = compact,
        onClick = { uiEvents(PlaygroundEvent.ZoomOut) }
    )
    if (compact.not()) Text(
        text = fontSize.toString(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.width(24.dp)
    )
    ToolbarButton(
        icon = Icons.Outlined.TextIncrease,
        label = stringResource(Res.string.editor_zoom_in),
        compact = compact,
        onClick = { uiEvents(PlaygroundEvent.ZoomIn) }
    )
}

/** Opens and closes the logs; a badge shows the number of [problems]. */
@Composable
private fun LogsButton(
    problems: Int,
    showLogs: Boolean,
    compact: Boolean,
    onClick: () -> Unit
) {
    BadgedBox(
        badge = {
            if (problems > 0) Badge(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ) {
                Text(if (problems > 99) "99+" else problems.toString(), fontSize = 9.sp)
            }
        }
    ) {
        ToolbarButton(
            icon = Icons.Outlined.Terminal,
            label = stringResource(if (showLogs) Res.string.logs_hide else Res.string.logs_show),
            selected = showLogs,
            compact = compact,
            onClick = onClick
        )
    }
}

/** What each mode's tab shows. */
private fun ViewMode.icon(vertical: Boolean): ImageVector = when (this) {
    ViewMode.EDIT -> Icons.Outlined.Code
    ViewMode.SPLIT -> if (vertical) Icons.Outlined.HorizontalSplit else Icons.Outlined.VerticalSplit
    ViewMode.PREVIEW -> Icons.Outlined.Visibility
}

private val ViewMode.label: StringResource
    get() = when (this) {
        ViewMode.EDIT -> Res.string.mode_edit
        ViewMode.SPLIT -> Res.string.mode_split
        ViewMode.PREVIEW -> Res.string.mode_preview
    }

/** A pill of icon tabs that picks the [ViewMode]. */
@Composable
private fun ModeSwitch(
    mode: ViewMode,
    vertical: Boolean,
    compact: Boolean,
    onSelect: (ViewMode) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .selectableGroup()
            .clip(shape)
            .background(colors.surfaceVariant)
            .padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        ViewMode.entries.forEach { item ->
            val selected = mode == item
            Box(
                modifier = Modifier
                    .size(width = if (compact) 30.dp else 36.dp, height = if (compact) 26.dp else 30.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (selected) colors.primary else Color.Transparent)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .selectable(selected = selected, role = Role.Tab, onClick = { onSelect(item) }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon(vertical),
                    contentDescription = stringResource(item.label),
                    tint = if (selected) colors.onPrimary else colors.onSurfaceVariant,
                    modifier = Modifier.size(if (compact) 16.dp else 18.dp)
                )
            }
        }
    }
}
