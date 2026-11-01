package ma7moud3ly.fast.compose.playground.platform

import androidx.compose.runtime.Composable
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import ma7moud3ly.fast.compose.playground.BuildInfo

// The desktop app has no page address: the playground is kept in storage only.

actual val currentPlatform: Platform = Platform.Desktop

actual fun readUrlHash(): String = ""

actual fun writeUrlHash(hash: String) = Unit

actual fun onUrlHashChange(onChange: (String) -> Unit) = Unit

actual fun shareBaseUrl(): String = BuildInfo.WEB_URL

@Composable
actual fun rememberCopyText(): (String) -> Unit = { text ->
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
}
