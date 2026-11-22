@file:OptIn(ExperimentalWasmJsInterop::class)

package ma7moud3ly.fast.compose.playground.platform

import androidx.compose.runtime.Composable
import kotlin.io.encoding.Base64
import kotlin.js.Promise
import kotlinx.coroutines.await

actual val currentPlatform: Platform = Platform.Web

actual fun readUrlHash(): String = jsReadHash()

actual fun writeUrlHash(hash: String) = jsWriteHash(hash)

actual fun onUrlHashChange(onChange: (String) -> Unit) = jsOnHashChange { onChange(jsReadHash()) }

actual fun shareBaseUrl(): String = jsPageUrl()

@Composable
actual fun rememberCopyText(): (String) -> Unit = { text -> jsCopyText(text) }

actual suspend fun deflateRaw(bytes: ByteArray): ByteArray? = pipe(bytes, compress = true)

actual suspend fun inflateRaw(deflated: ByteArray): ByteArray? = pipe(deflated, compress = false)

/** [bytes] passed through the browser's `CompressionStream` or `DecompressionStream`, or null when that fails. */
private suspend fun pipe(bytes: ByteArray, compress: Boolean): ByteArray? {
    // bytes cross to JavaScript as Base64, which is cheaper than copying them one by one
    val result = jsPipe(Base64.Default.encode(bytes), compress).await() ?: return null
    return Base64.Default.decode(result.toString())
}

private fun jsPipe(base64: String, compress: Boolean): Promise<JsString?> = js(
    """(async () => {
        try {
            const format = 'deflate-raw';
            const stream = compress ? new CompressionStream(format) : new DecompressionStream(format);
            const binary = atob(base64);
            const input = new Uint8Array(binary.length);
            for (let i = 0; i < binary.length; i++) input[i] = binary.charCodeAt(i);
            const piped = new Blob([input]).stream().pipeThrough(stream);
            const output = new Uint8Array(await new Response(piped).arrayBuffer());
            let text = '';
            for (let i = 0; i < output.length; i += 0x8000) {
                text += String.fromCharCode.apply(null, output.subarray(i, i + 0x8000));
            }
            return btoa(text);
        } catch (e) {
            return null;
        }
    })()"""
)

private fun jsReadHash(): String = js("window.location.hash.substring(1)")

private fun jsWriteHash(hash: String): Unit = js("history.replaceState(null, '', '#' + hash)")

private fun jsOnHashChange(callback: () -> Unit): Unit = js("window.addEventListener('hashchange', () => callback())")

/** The page address without the text after `#`. */
private fun jsPageUrl(): String = js("window.location.href.split('#')[0]")

private fun jsCopyText(text: String): Unit = js("navigator.clipboard.writeText(text).catch(() => {})")
