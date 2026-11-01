package ma7moud3ly.fast.compose.playground.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

/** The platforms the app is built for. [label] is the name to show a user. */
enum class Platform(val label: String) {
    Web("Web"),
    Desktop("Desktop"),
    Android("Android")
}

/** The platform this code is running on. */
expect val currentPlatform: Platform

val Platform.isWeb: Boolean get() = this == Platform.Web

val Platform.isAndroid: Boolean get() = this == Platform.Android

/**
 * The platform the surrounding composition renders for.
 *
 * Defaults to [currentPlatform]; provide it to render another platform's layout, as in a `@Preview`.
 */
val LocalPlatform = staticCompositionLocalOf { currentPlatform }

/** The text after `#` in the page address, without the `#`. Empty outside the web. */
expect fun readUrlHash(): String

/** Replaces the text after `#` in the page address, without adding a history entry. Does nothing outside the web. */
expect fun writeUrlHash(hash: String)

/** Calls [onChange] with the new text after `#` whenever the user changes it, as by pasting a link. */
expect fun onUrlHashChange(onChange: (String) -> Unit)

/** The address shared links start with: this page on the web, the public playground elsewhere. */
expect fun shareBaseUrl(): String

/** A function that puts a text on the clipboard. */
@Composable
expect fun rememberCopyText(): (String) -> Unit
