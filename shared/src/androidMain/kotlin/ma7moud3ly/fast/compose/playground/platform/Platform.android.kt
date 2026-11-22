package ma7moud3ly.fast.compose.playground.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.Inflater
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

actual suspend fun deflateRaw(bytes: ByteArray): ByteArray? {
    val deflater = Deflater(Deflater.BEST_COMPRESSION, true)
    return try {
        deflater.setInput(bytes)
        deflater.finish()
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        while (!deflater.finished()) out.write(buffer, 0, deflater.deflate(buffer))
        out.toByteArray()
    } finally {
        deflater.end()
    }
}

actual suspend fun inflateRaw(deflated: ByteArray): ByteArray? {
    // without a zlib header the inflater needs one extra byte after the data
    val inflater = Inflater(true)
    return try {
        inflater.setInput(deflated + 0.toByte())
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        while (!inflater.finished()) {
            val count = inflater.inflate(buffer)
            if (count == 0 && (inflater.needsInput() || inflater.needsDictionary())) break
            out.write(buffer, 0, count)
        }
        if (inflater.finished()) out.toByteArray() else null
    } catch (e: DataFormatException) {
        e.printStackTrace()
        null
    } finally {
        inflater.end()
    }
}
