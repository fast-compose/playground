package ma7moud3ly.fast.compose.playground.model

import io.ma7moud3ly.nemo.model.EditorTheme
import ma7moud3ly.fast.compose.playground.platform.Platform
import ma7moud3ly.fast.compose.playground.platform.currentPlatform
import ma7moud3ly.fast.compose.playground.ui.theme.AppThemes

/** What the workspace shows: the code, the preview, or both side by side. [key] is its name in a link. */
enum class ViewMode(val key: String) {
    EDIT("edit"),
    SPLIT("split"),
    PREVIEW("preview");

    companion object {
        /** The mode with this [key], or null when there is none. */
        fun fromKey(key: String?): ViewMode? = entries.firstOrNull { it.key == key }
    }
}

/**
 * Everything that makes up one playground: the code and how it is shown.
 *
 * It is what the page address and the storage hold, so a playground can be reopened or shared as a link.
 *
 * @param code the code in the editor
 * @param theme colors of the whole app, editor included
 * @param fontSize font size of the code
 * @param mode whether the code, the preview or both are shown
 * @param split the code pane's share of the workspace, between [SPLIT_MIN] and [SPLIT_MAX]
 */
data class PlaygroundConfig(
    val code: String = SAMPLE_CODE,
    val theme: EditorTheme = AppThemes.DEFAULT,
    val fontSize: Int = DEFAULT_FONT_SIZE,
    val mode: ViewMode = ViewMode.SPLIT,
    val split: Float = DEFAULT_SPLIT
) {
    companion object {
        const val FONT_SIZE_MIN = 8
        const val FONT_SIZE_MAX = 32

        const val SPLIT_MIN = 0.15f
        const val SPLIT_MAX = 0.85f
        const val DEFAULT_SPLIT = 0.7f

        /** Phones get a smaller font, so more of each line fits. */
        val DEFAULT_FONT_SIZE = if (currentPlatform == Platform.Android) 12 else 14
    }
}
