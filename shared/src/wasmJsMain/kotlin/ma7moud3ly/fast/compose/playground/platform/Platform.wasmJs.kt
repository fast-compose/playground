@file:OptIn(ExperimentalWasmJsInterop::class)

package ma7moud3ly.fast.compose.playground.platform

import androidx.compose.runtime.Composable

actual val currentPlatform: Platform = Platform.Web

actual fun readUrlHash(): String = jsReadHash()

actual fun writeUrlHash(hash: String) = jsWriteHash(hash)

actual fun onUrlHashChange(onChange: (String) -> Unit) = jsOnHashChange { onChange(jsReadHash()) }

actual fun shareBaseUrl(): String = jsPageUrl()

@Composable
actual fun rememberCopyText(): (String) -> Unit = { text -> jsCopyText(text) }

private fun jsReadHash(): String = js("window.location.hash.substring(1)")

private fun jsWriteHash(hash: String): Unit = js("history.replaceState(null, '', '#' + hash)")

private fun jsOnHashChange(callback: () -> Unit): Unit = js("window.addEventListener('hashchange', () => callback())")

/** The page address without the text after `#`. */
private fun jsPageUrl(): String = js("window.location.href.split('#')[0]")

private fun jsCopyText(text: String): Unit = js("navigator.clipboard.writeText(text).catch(() => {})")
