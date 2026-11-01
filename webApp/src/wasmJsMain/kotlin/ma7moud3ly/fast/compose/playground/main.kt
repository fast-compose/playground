@file:OptIn(ExperimentalWasmJsInterop::class)

package ma7moud3ly.fast.compose.playground

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    removeLoader()
    ComposeViewport {
        PlaygroundApp()
    }
}

/** Removes the spinner index.html shows while the app loads. */
private fun removeLoader(): Unit = js("document.getElementById('loader')?.remove()")
