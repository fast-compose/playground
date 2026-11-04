package ma7moud3ly.fast.compose.playground.feature.playground.model

import io.ma7moud3ly.nemo.model.EditorTheme
import ma7moud3ly.fast.compose.playground.model.Example
import ma7moud3ly.fast.compose.playground.model.ViewMode

/** What the user does on the playground screen. */
sealed interface PlaygroundEvent {
    data object Undo : PlaygroundEvent
    data object Redo : PlaygroundEvent
    data object Format : PlaygroundEvent

    /** Opens the search dialog. */
    data object Search : PlaygroundEvent
    data object ZoomIn : PlaygroundEvent
    data object ZoomOut : PlaygroundEvent

    data class SelectMode(val mode: ViewMode) : PlaygroundEvent
    data class SelectTheme(val theme: EditorTheme) : PlaygroundEvent
    data class ChangeSplit(val split: Float) : PlaygroundEvent
    data object ResetSplit : PlaygroundEvent

    /** Moves the caret to the start of a 1-based [line], showing the code when it is hidden. */
    data class GoToLine(val line: Int) : PlaygroundEvent

    data object ToggleLogs : PlaygroundEvent

    /** Runs the previews again from a fresh state. */
    data object Restart : PlaygroundEvent

    /** Puts back the sample code; it can be undone. */
    data object RestoreSample : PlaygroundEvent

    /** Replaces the code with the code of [example]; it can be undone. */
    data class OpenExample(val example: Example) : PlaygroundEvent

    /** Copies a link that opens the playground as it is now, showing both panes. */
    data object Share : PlaygroundEvent
}
