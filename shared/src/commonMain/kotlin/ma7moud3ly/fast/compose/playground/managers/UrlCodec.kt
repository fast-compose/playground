package ma7moud3ly.fast.compose.playground.managers

import kotlin.io.encoding.Base64
import kotlin.math.roundToInt
import ma7moud3ly.fast.compose.playground.model.PlaygroundConfig
import ma7moud3ly.fast.compose.playground.model.ViewMode
import ma7moud3ly.fast.compose.playground.ui.theme.AppThemes
import ma7moud3ly.fast.compose.playground.ui.theme.key

/**
 * Turns a [PlaygroundConfig] into the text after `#` in the page address, and back.
 *
 * The text is a list of `key=value` pairs joined by `&`, for example
 * `theme=vs-code-light&font=14&mode=split&split=70&code=ZnVuIG1haW4oKQ`.
 * The code is Base64 (URL-safe, without padding) and the split is a percentage.
 */
object UrlCodec {

    private val base64 = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT_OPTIONAL)

    /** Writes [config] as `key=value` pairs. */
    fun encode(config: PlaygroundConfig): String = listOf(
        KEY_THEME to config.theme.key,
        KEY_FONT to config.fontSize.toString(),
        KEY_MODE to config.mode.key,
        KEY_SPLIT to (config.split * 100).roundToInt().toString(),
        KEY_CODE to base64.encode(config.code.encodeToByteArray())
    ).joinToString("&") { (key, value) -> "$key=$value" }

    /**
     * Reads a config from [text].
     *
     * A missing or unreadable value takes its default from [PlaygroundConfig].
     * Returns null when [text] holds no pairs at all.
     */
    fun decode(text: String): PlaygroundConfig? {
        val values = text.removePrefix("#")
            .split('&')
            .mapNotNull { pair ->
                val index = pair.indexOf('=')
                if (index <= 0) null else pair.take(index) to pair.substring(index + 1)
            }
            .toMap()
        if (values.isEmpty()) return null

        val defaults = PlaygroundConfig()
        return PlaygroundConfig(
            code = values[KEY_CODE]?.let(::decodeCode) ?: defaults.code,
            theme = AppThemes.fromKey(values[KEY_THEME]) ?: defaults.theme,
            fontSize = values[KEY_FONT]?.toIntOrNull()
                ?.coerceIn(PlaygroundConfig.FONT_SIZE_MIN, PlaygroundConfig.FONT_SIZE_MAX) ?: defaults.fontSize,
            mode = ViewMode.fromKey(values[KEY_MODE]) ?: defaults.mode,
            split = values[KEY_SPLIT]?.toIntOrNull()
                ?.let { it / 100f }
                ?.coerceIn(PlaygroundConfig.SPLIT_MIN, PlaygroundConfig.SPLIT_MAX) ?: defaults.split
        )
    }

    /** The code held by a Base64 [value], or null when it can't be read. */
    private fun decodeCode(value: String): String? = try {
        base64.decode(value).decodeToString()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }

    private const val KEY_THEME = "theme"
    private const val KEY_FONT = "font"
    private const val KEY_MODE = "mode"
    private const val KEY_SPLIT = "split"
    private const val KEY_CODE = "code"
}
