package ma7moud3ly.fast.compose.playground.ui.theme

import io.ma7moud3ly.nemo.model.EditorTheme
import io.ma7moud3ly.nemo.model.EditorThemes

/** The themes of the app. Each one paints the whole app, the editor included. */
object AppThemes {

    /** Every theme shipped by nemo-editor, the light ones first. */
    val ALL: List<EditorTheme> = EditorThemes.ALL_THEMES.sortedBy { it.dark }

    /** The theme before the user picks one. */
    val DEFAULT: EditorTheme = EditorThemes.VS_CODE_LIGHT

    /** The theme with this [key], or null when there is none. */
    fun fromKey(key: String?): EditorTheme? = ALL.firstOrNull { it.key == key }
}

/** The theme's name in the page address: "VS Code Light" is "vs-code-light". */
val EditorTheme.key: String get() = name.lowercase().replace(' ', '-')
