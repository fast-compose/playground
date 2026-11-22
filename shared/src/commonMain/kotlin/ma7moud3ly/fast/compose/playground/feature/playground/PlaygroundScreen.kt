package ma7moud3ly.fast.compose.playground.feature.playground

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import ma7moud3ly.fast.compose.playground.feature.playground.dialog.ShareDialog
import ma7moud3ly.fast.compose.playground.feature.playground.model.PlaygroundEvent
import ma7moud3ly.fast.compose.playground.platform.rememberCopyText
import ma7moud3ly.fast.compose.playground.ui.theme.codeFontFamily
import ma7moud3ly.fast.compose.ui.ComposeCode
import ma7moud3ly.fast.compose.ui.rememberFullRegistry

/** The background of a line with an error, in the editor. */
private const val ERROR_LINE_COLOR = 0x40E53935L

/**
 * The playground: keeps [code] up to date with the editor, marks the lines with errors, opens the logs when there are
 * problems, and carries out the events that need the screen, such as opening the share dialog.
 */
@Composable
fun PlaygroundScreen(
    viewModel: PlaygroundViewModel,
    code: ComposeCode
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val copyText = rememberCopyText()
    val registry = rememberFullRegistry()
    val codeState = viewModel.codeState
    val codeFont = codeFontFamily()
    val problems = code.diagnostics.size + code.logs.count { it.isError || it.isWarning }

    // The font arrives once its files are loaded; pass each new value to the editor.
    LaunchedEffect(codeFont) {
        viewModel.setCodeFont(codeFont)
    }
    LaunchedEffect(codeState.code) {
        code.update(codeState.code)
    }
    LaunchedEffect(code.diagnostics, code.errorLines) {
        codeState.clearHighlights()
        (code.diagnostics.map { it.span.line } + code.errorLines).distinct().forEach {
            codeState.toggleHighlight(it, ERROR_LINE_COLOR)
        }
    }
    LaunchedEffect(problems > 0) {
        viewModel.onProblemsChange(problems > 0)
    }

    // the link the share dialog shows; it opens once the code is compressed into it
    var shareLink by remember { mutableStateOf<String?>(null) }
    shareLink?.let { link ->
        ShareDialog(
            link = link,
            onCopy = copyText,
            onDismiss = { shareLink = null }
        )
    }

    PlaygroundContent(
        codeState = codeState,
        settings = viewModel.editorSettings,
        code = code,
        registry = registry,
        theme = viewModel.theme,
        mode = viewModel.mode,
        split = viewModel.split,
        fontSize = viewModel.fontSize,
        canUndo = viewModel.canUndo,
        canRedo = viewModel.canRedo,
        showLogs = viewModel.showLogs,
        runs = viewModel.runs,
        problems = problems,
        snackBarHostState = snackBarHostState,
        uiEvents = { event ->
            when (event) {
                is PlaygroundEvent.Share -> scope.launch { shareLink = viewModel.link() }
                is PlaygroundEvent.Restart, is PlaygroundEvent.OpenExample -> {
                    code.clearLogs()
                    viewModel.onEvent(event)
                }

                else -> viewModel.onEvent(event)
            }
        }
    )
}
