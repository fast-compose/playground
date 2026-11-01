package ma7moud3ly.fast.compose.playground.feature.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ma7moud3ly.nemo.NemoCodeEditor
import io.ma7moud3ly.nemo.model.CodeState
import io.ma7moud3ly.nemo.model.EditorSettings
import io.ma7moud3ly.nemo.model.EditorTheme
import io.ma7moud3ly.nemo.model.EditorThemes
import io.ma7moud3ly.nemo.model.Language
import io.ma7moud3ly.nemo.search.FindReplaceDialog
import ma7moud3ly.fast.compose.interpreter.Registry
import ma7moud3ly.fast.compose.playground.feature.playground.dialog.DetailsDialog
import ma7moud3ly.fast.compose.playground.feature.playground.components.PlaygroundEditorBar
import ma7moud3ly.fast.compose.playground.feature.playground.components.PlaygroundTopBar
import ma7moud3ly.fast.compose.playground.feature.playground.model.PlaygroundEvent
import ma7moud3ly.fast.compose.playground.model.PlaygroundConfig
import ma7moud3ly.fast.compose.playground.model.SAMPLE_CODE
import ma7moud3ly.fast.compose.playground.model.ViewMode
import ma7moud3ly.fast.compose.playground.resources.Res
import ma7moud3ly.fast.compose.playground.resources.logs_title
import ma7moud3ly.fast.compose.playground.resources.mode_split
import ma7moud3ly.fast.compose.playground.ui.components.SplitPane
import ma7moud3ly.fast.compose.playground.ui.theme.AppTheme
import ma7moud3ly.fast.compose.playground.ui.theme.AppThemes
import ma7moud3ly.fast.compose.ui.ComposeCode
import ma7moud3ly.fast.compose.ui.FastComposeLogs
import ma7moud3ly.fast.compose.ui.FastComposePreview
import ma7moud3ly.fast.compose.ui.rememberComposeCode
import ma7moud3ly.fast.compose.ui.rememberFullRegistry
import org.jetbrains.compose.resources.stringResource

/** A window narrower than this gets the compact page: stacked panes and smaller buttons. */
private val CompactWidth = 600.dp

/** The space between the dots behind the previews. */
private val PreviewCanvasDotsSpacing = 16.dp

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun PlaygroundContentPreview() {
    PlaygroundContentPreview(theme = AppThemes.DEFAULT, showLogs = false)
}

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun PlaygroundContentPreviewDarkLogs() {
    PlaygroundContentPreview(theme = EditorThemes.VS_CODE_DARK, showLogs = true)
}

@Preview(widthDp = 360, heightDp = 760)
@Composable
private fun PlaygroundContentPreviewCompact() {
    PlaygroundContentPreview(theme = AppThemes.DEFAULT, showLogs = false)
}

@Composable
private fun PlaygroundContentPreview(theme: EditorTheme, showLogs: Boolean) {
    AppTheme(theme = theme) {
        PlaygroundContent(
            codeState = remember { CodeState(SAMPLE_CODE, Language.KOTLIN, initialCursorPosition = 0) },
            settings = remember(theme) { EditorSettings(theme = theme) },
            code = rememberComposeCode(SAMPLE_CODE),
            registry = rememberFullRegistry(),
            theme = theme,
            mode = ViewMode.SPLIT,
            split = PlaygroundConfig.DEFAULT_SPLIT,
            fontSize = 14,
            canUndo = true,
            canRedo = false,
            showLogs = showLogs,
            runs = 0,
            problems = 0,
            snackBarHostState = remember { SnackbarHostState() },
            uiEvents = {}
        )
    }
}

/**
 * The whole page: the top bar, the editor bar, then the code and the preview, split by a bar that resizes them, and
 * the logs under both when they are open.
 *
 * The panes sit side by side, the code taking [split] of the width; below [CompactWidth] they are stacked, with the
 * code on top, and the buttons are smaller.
 *
 * @param code the code the preview runs, kept up to date with [codeState]
 * @param runs how many times the previews were restarted; a new value restarts them
 * @param problems the number of errors and warnings, shown on the logs button
 */
@Composable
internal fun PlaygroundContent(
    codeState: CodeState,
    settings: EditorSettings,
    code: ComposeCode,
    registry: Registry,
    theme: EditorTheme,
    mode: ViewMode,
    split: Float,
    fontSize: Int,
    canUndo: Boolean,
    canRedo: Boolean,
    showLogs: Boolean,
    runs: Int,
    problems: Int,
    snackBarHostState: SnackbarHostState,
    uiEvents: (PlaygroundEvent) -> Unit
) {
    var showDetails by remember { mutableStateOf(false) }
    if (showDetails) DetailsDialog(onDismiss = { showDetails = false })
    var showSearch by remember { mutableStateOf(false) }
    if (showSearch) FindReplaceDialog(
        state = codeState,
        showReplace = false,
        onDismiss = { showSearch = false }
    )

    // Each pane keeps its state, such as the editor's scroll and the previews' state, as the mode changes.
    val editor = remember(codeState, settings) {
        movableContentOf {
            NemoCodeEditor(
                state = codeState,
                settings = settings,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    val preview = remember(code, registry) {
        movableContentOf { runs: Int ->
            PreviewPane(code = code, registry = registry, runs = runs)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val compact = maxWidth < CompactWidth
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(snackBarHostState) },
            topBar = {
                Column {
                    PlaygroundTopBar(
                        theme = theme,
                        compact = compact,
                        uiEvents = uiEvents,
                        onShowDetails = { showDetails = true }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            },
            // below the top bar, keep clear of the navigation bar and the keyboard
            contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                PlaygroundEditorBar(
                    mode = mode,
                    vertical = compact,
                    compact = compact,
                    canUndo = canUndo,
                    canRedo = canRedo,
                    fontSize = fontSize,
                    problems = problems,
                    showLogs = showLogs,
                    uiEvents = {
                        if (it is PlaygroundEvent.Search) showSearch = true else uiEvents(it)
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                val panes = Modifier.fillMaxWidth().weight(1f)
                when (mode) {
                    ViewMode.EDIT -> Box(panes) { editor() }
                    ViewMode.PREVIEW -> Box(panes) { preview(runs) }
                    ViewMode.SPLIT -> SplitPane(
                        vertical = compact,
                        ratio = split,
                        onRatioChange = { uiEvents(PlaygroundEvent.ChangeSplit(it)) },
                        onReset = { uiEvents(PlaygroundEvent.ResetSplit) },
                        first = { editor() },
                        second = { preview(runs) },
                        minRatio = PlaygroundConfig.SPLIT_MIN,
                        maxRatio = PlaygroundConfig.SPLIT_MAX,
                        splitterLabel = stringResource(Res.string.mode_split),
                        modifier = panes
                    )
                }
                if (showLogs) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    FastComposeLogs(
                        code = code,
                        title = stringResource(Res.string.logs_title),
                        onLineClick = { uiEvents(PlaygroundEvent.GoToLine(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (compact) 120.dp else 160.dp)
                    )
                }
            }
        }
    }
}

/**
 * Every `@Preview` of [code], on the theme's background with a grid of dots. A new [runs] value restarts them,
 * resetting their state and animations.
 */
@Composable
private fun PreviewPane(
    code: ComposeCode,
    registry: Registry,
    runs: Int
) {
    val dots = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                val step = PreviewCanvasDotsSpacing.toPx()
                val radius = 1.dp.toPx()
                var y = step / 2
                while (y < size.height) {
                    var x = step / 2
                    while (x < size.width) {
                        drawCircle(dots, radius, Offset(x, y))
                        x += step
                    }
                    y += step
                }
            }
    ) {
        key(runs) {
            FastComposePreview(
                code = code,
                registry = registry,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
