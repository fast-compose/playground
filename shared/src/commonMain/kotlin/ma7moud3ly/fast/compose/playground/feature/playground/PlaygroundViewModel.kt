package ma7moud3ly.fast.compose.playground.feature.playground

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ma7moud3ly.nemo.model.CodeState
import io.ma7moud3ly.nemo.model.EditorSettings
import io.ma7moud3ly.nemo.model.EditorTheme
import io.ma7moud3ly.nemo.model.FindReplaceAction
import io.ma7moud3ly.nemo.model.Language
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ma7moud3ly.fast.compose.playground.feature.playground.model.PlaygroundEvent
import ma7moud3ly.fast.compose.playground.managers.StorageManager
import ma7moud3ly.fast.compose.playground.managers.UrlCodec
import ma7moud3ly.fast.compose.playground.model.Example
import ma7moud3ly.fast.compose.playground.model.PlaygroundConfig
import ma7moud3ly.fast.compose.playground.model.SAMPLE_CODE
import ma7moud3ly.fast.compose.playground.model.ViewMode
import ma7moud3ly.fast.compose.playground.platform.onUrlHashChange
import ma7moud3ly.fast.compose.playground.platform.readUrlHash
import ma7moud3ly.fast.compose.playground.platform.shareBaseUrl
import ma7moud3ly.fast.compose.playground.platform.writeUrlHash
import ma7moud3ly.fast.compose.playground.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

/** How long the playground has to stay unchanged before it is saved, in milliseconds. */
private const val SAVE_DELAY_MS = 300L

/** Space between the code and the edges of the editor. */
private val EditorPadding = PaddingValues(start = 4.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)

/**
 * Holds the playground and keeps it in the page address and in storage.
 *
 * On start the playground is read from the page address, then from storage, then from the defaults. Every later
 * change is written back to both. On the web, a link pasted into the address bar of an open playground is loaded.
 */
class PlaygroundViewModel(
    private val storageManager: StorageManager = StorageManager()
) : ViewModel() {

    private val initial = restore()

    /** The code in the editor. */
    val codeState = CodeState(
        initialCode = initial.code,
        language = Language.KOTLIN,
        initialCursorPosition = 0
    )

    /** The editor's options; it owns the theme and the font size. */
    val editorSettings = EditorSettings(
        theme = initial.theme,
        fontSize = initial.fontSize,
        enableAutocomplete = true,
        contentPadding = EditorPadding
    )

    /** The theme of the whole app. */
    val theme: EditorTheme get() = editorSettings.themeState.value

    val fontSize: Int get() = editorSettings.fontSizeState.value

    var mode by mutableStateOf(initial.mode)
        private set

    /** The code pane's share of the workspace. */
    var split by mutableFloatStateOf(initial.split)
        private set

    /** Whether the logs pane is open. Not saved. */
    var showLogs by mutableStateOf(false)
        private set

    /** How many times the previews were restarted; a new value restarts them. */
    var runs by mutableIntStateOf(0)
        private set

    // reading the code makes these update after each edit
    val canUndo: Boolean by derivedStateOf {
        codeState.code
        codeState.canUndo()
    }

    val canRedo: Boolean by derivedStateOf {
        codeState.code
        codeState.canRedo()
    }

    /** The text this playground last wrote to the page address. */
    private var savedHash = ""

    init {
        save()
        onUrlHashChange { hash ->
            if (hash != savedHash) UrlCodec.decode(hash)?.let(::load)
        }
    }

    fun onEvent(event: PlaygroundEvent) {
        when (event) {
            is PlaygroundEvent.Undo -> codeState.undo()
            is PlaygroundEvent.Redo -> codeState.redo()
            is PlaygroundEvent.ZoomIn -> setFontSize(fontSize + 1)
            is PlaygroundEvent.ZoomOut -> setFontSize(fontSize - 1)
            is PlaygroundEvent.SelectMode -> mode = event.mode
            is PlaygroundEvent.SelectTheme -> editorSettings.themeState.value = event.theme
            is PlaygroundEvent.ChangeSplit -> split = event.split
                .coerceIn(PlaygroundConfig.SPLIT_MIN, PlaygroundConfig.SPLIT_MAX)

            is PlaygroundEvent.ResetSplit -> split = PlaygroundConfig.DEFAULT_SPLIT
            is PlaygroundEvent.ToggleLogs -> showLogs = showLogs.not()
            is PlaygroundEvent.Restart -> runs++
            is PlaygroundEvent.RestoreSample -> replaceCode(SAMPLE_CODE)
            is PlaygroundEvent.OpenExample -> openExample(event.example)
            is PlaygroundEvent.GoToLine -> {
                if (mode == ViewMode.PREVIEW) mode = ViewMode.SPLIT
                val offset = lineStart(codeState.code, event.line)
                codeState.setSelection(offset, offset)
            }

            // carried out by the screen
            is PlaygroundEvent.Share, is PlaygroundEvent.Search -> Unit
        }
    }

    /** Opens the logs pane when there are problems and closes it when they are fixed. */
    fun onProblemsChange(hasProblems: Boolean) {
        showLogs = hasProblems
    }

    /** Sets the font the editor draws the code with. */
    fun setCodeFont(font: FontFamily) {
        editorSettings.fontFamilyState.value = font
    }

    /** The playground as it is now. */
    fun snapshot(): PlaygroundConfig = PlaygroundConfig(
        code = codeState.code,
        theme = theme,
        fontSize = fontSize.coerceIn(PlaygroundConfig.FONT_SIZE_MIN, PlaygroundConfig.FONT_SIZE_MAX),
        mode = mode,
        split = split
    )

    /** The address that opens the playground as it is now, showing both panes. */
    fun link(): String = shareBaseUrl() + "#" + UrlCodec.encode(snapshot().copy(mode = ViewMode.SPLIT))

    private fun setFontSize(size: Int) {
        editorSettings.setFontSize(size.coerceIn(PlaygroundConfig.FONT_SIZE_MIN, PlaygroundConfig.FONT_SIZE_MAX))
    }

    /** Replaces the whole code with [code], as one edit that can be undone. */
    private fun replaceCode(code: String) {
        val oldCode = codeState.code
        if (oldCode == code) return
        codeState.recordAction(
            FindReplaceAction.Replace(start = 0, end = oldCode.length, oldText = oldCode, newText = code)
        )
        codeState.updateText(code, 0)
    }

    /** Reads the code of [example] from the resources and puts it in the editor, showing its preview. */
    @OptIn(ExperimentalResourceApi::class)
    private fun openExample(example: Example) {
        viewModelScope.launch {
            val code = try {
                Res.readBytes(example.path).decodeToString()
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            replaceCode(code)
            if (mode == ViewMode.EDIT) mode = ViewMode.SPLIT
        }
    }

    /** Shows the playground of [config], as when it is opened from a link. */
    private fun load(config: PlaygroundConfig) {
        replaceCode(config.code)
        editorSettings.themeState.value = config.theme
        setFontSize(config.fontSize)
        mode = config.mode
        split = config.split
    }

    /** Writes the playground to the page address and to storage a moment after each change. */
    @OptIn(FlowPreview::class)
    private fun save() {
        viewModelScope.launch {
            snapshotFlow { UrlCodec.encode(snapshot()) }
                .distinctUntilChanged()
                .debounce(SAVE_DELAY_MS.milliseconds)
                .collect { playground ->
                    savedHash = playground
                    writeUrlHash(playground)
                    storageManager.playground = playground
                }
        }
    }

    /** The playground in the page address, else the one in storage, else the default one. */
    private fun restore(): PlaygroundConfig =
        UrlCodec.decode(readUrlHash())
            ?: UrlCodec.decode(storageManager.playground)
            ?: PlaygroundConfig()
}

/** Offset of the first character of 1-based [line] in [text]. */
private fun lineStart(text: String, line: Int): Int =
    text.lineSequence().take(line - 1).sumOf { it.length + 1 }.coerceAtMost(text.length)
