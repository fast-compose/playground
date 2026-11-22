package ma7moud3ly.fast.compose.playground.managers

import kotlin.io.encoding.Base64
import kotlin.math.roundToInt
import ma7moud3ly.fast.compose.playground.model.PlaygroundConfig
import ma7moud3ly.fast.compose.playground.model.ViewMode
import ma7moud3ly.fast.compose.playground.platform.deflateRaw
import ma7moud3ly.fast.compose.playground.platform.inflateRaw
import ma7moud3ly.fast.compose.playground.ui.theme.AppThemes
import ma7moud3ly.fast.compose.playground.ui.theme.key

/**
 * Turns a [PlaygroundConfig] into the text after `#` in the page address, and back.
 *
 * The text is a list of `key=value` pairs joined by `&`, for example
 * `theme=vs-code-light&font=14&mode=split&split=70&zcode=K0vNSy4uyc_PUcjMS8tXqFYoS8wpTVWwUkgvSs0rAQA`.
 * The split is a percentage. The code is kept as `zcode`: its UTF-8 bytes compressed with raw deflate, in URL-safe
 * Base64 without padding, as the website's Try page does. When compression isn't available it is kept as `code`, the
 * same without compression, and links in that older form still open.
 */
object UrlCodec {

    private val base64 = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT_OPTIONAL)

    /**
     * A config read from a link before its code is unpacked.
     *
     * @param config the config; its code is empty when the code is compressed
     * @param packedCode the compressed code, for [unpack], or null when the code was not compressed
     */
    class Decoded(val config: PlaygroundConfig, val packedCode: String?)

    /** Writes [config] as `key=value` pairs, with the code compressed when the platform can. */
    suspend fun encode(config: PlaygroundConfig): String {
        val bytes = config.code.encodeToByteArray()
        val packed = deflateRaw(bytes)
        return listOf(
            KEY_THEME to config.theme.key,
            KEY_FONT to config.fontSize.toString(),
            KEY_MODE to config.mode.key,
            KEY_SPLIT to (config.split * 100).roundToInt().toString(),
            if (packed != null) KEY_PACKED_CODE to base64.encode(packed) else KEY_CODE to base64.encode(bytes)
        ).joinToString("&") { (key, value) -> "$key=$value" }
    }

    /**
     * Reads a config from [text]. A compressed code is left in [Decoded.packedCode], for [unpack].
     *
     * A missing or unreadable value takes its default from [PlaygroundConfig].
     * Returns null when [text] holds no pairs at all.
     */
    fun decode(text: String): Decoded? {
        val values = text.removePrefix("#")
            .split('&')
            .mapNotNull { pair ->
                val index = pair.indexOf('=')
                if (index <= 0) null else pair.take(index) to pair.substring(index + 1)
            }
            .toMap()
        if (values.isEmpty()) return null

        val defaults = PlaygroundConfig()
        val packedCode = values[KEY_PACKED_CODE]
        val config = PlaygroundConfig(
            code = if (packedCode != null) "" else values[KEY_CODE]?.let(::decodeCode) ?: defaults.code,
            theme = AppThemes.fromKey(values[KEY_THEME]) ?: defaults.theme,
            fontSize = values[KEY_FONT]?.toIntOrNull()
                ?.coerceIn(PlaygroundConfig.FONT_SIZE_MIN, PlaygroundConfig.FONT_SIZE_MAX) ?: defaults.fontSize,
            mode = ViewMode.fromKey(values[KEY_MODE]) ?: defaults.mode,
            split = values[KEY_SPLIT]?.toIntOrNull()
                ?.let { it / 100f }
                ?.coerceIn(PlaygroundConfig.SPLIT_MIN, PlaygroundConfig.SPLIT_MAX) ?: defaults.split
        )
        return Decoded(config, packedCode)
    }

    /** The code held by a compressed [value], or null when it can't be read. */
    suspend fun unpack(value: String): String? {
        val bytes = fromBase64(value) ?: return null
        return inflateRaw(bytes)?.decodeToString()
    }

    /** The full config of [text], with its code unpacked; null when [text] holds no pairs or the code can't be read. */
    suspend fun decodeAll(text: String): PlaygroundConfig? {
        val decoded = decode(text) ?: return null
        val packedCode = decoded.packedCode ?: return decoded.config
        return unpack(packedCode)?.let { decoded.config.copy(code = it) }
    }

    /** The code held by a Base64 [value], or null when it can't be read. */
    private fun decodeCode(value: String): String? = fromBase64(value)?.decodeToString()

    private fun fromBase64(value: String): ByteArray? = try {
        base64.decode(value)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }

    private const val KEY_THEME = "theme"
    private const val KEY_FONT = "font"
    private const val KEY_MODE = "mode"
    private const val KEY_SPLIT = "split"
    private const val KEY_CODE = "code"
    private const val KEY_PACKED_CODE = "zcode"
}
