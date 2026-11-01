package ma7moud3ly.fast.compose.playground.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ma7moud3ly.fast.compose.playground.BuildInfo

// The Android app has no page address: the playground is kept in storage only.

actual val currentPlatform: Platform = Platform.Android

actual fun readUrlHash(): String = ""

actual fun writeUrlHash(hash: String) = Unit

actual fun onUrlHashChange(onChange: (String) -> Unit) = Unit

actual fun shareBaseUrl(): String = BuildInfo.WEB_URL

@Composable
actual fun rememberCopyText(): (String) -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { text: String ->
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(BuildInfo.NAME, text))
        }
    }
}
