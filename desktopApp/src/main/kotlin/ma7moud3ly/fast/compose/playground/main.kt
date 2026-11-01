package ma7moud3ly.fast.compose.playground

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.LocalWindowExceptionHandlerFactory
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowExceptionHandler
import androidx.compose.ui.window.WindowExceptionHandlerFactory
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import ma7moud3ly.fast.compose.playground.feature.playground.PlaygroundViewModel
import ma7moud3ly.fast.compose.playground.resources.Res
import ma7moud3ly.fast.compose.playground.resources.logo
import ma7moud3ly.fast.compose.ui.ComposeCode
import ma7moud3ly.fast.compose.ui.rememberComposeCode
import org.jetbrains.compose.resources.painterResource

/** The code the window runs; drawing crashes are reported to it. */
private class CrashTarget {
    var code: ComposeCode? = null
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val target = remember { CrashTarget() }
    // Exceptions thrown while Compose measures or draws a preview are logged instead of closing the window.
    val crashHandler = remember {
        WindowExceptionHandlerFactory {
            WindowExceptionHandler { error -> target.code?.reportCrash(error) ?: throw error }
        }
    }
    CompositionLocalProvider(LocalWindowExceptionHandlerFactory provides crashHandler) {
        Window(
            onCloseRequest = ::exitApplication,
            title = BuildInfo.NAME,
            icon = painterResource(Res.drawable.logo),
            state = rememberWindowState(placement = WindowPlacement.Maximized)
        ) {
            val viewModel = viewModel { PlaygroundViewModel() }
            val code = rememberComposeCode(viewModel.codeState.code)
            DisposableEffect(code) {
                target.code = code
                onDispose { target.code = null }
            }
            PlaygroundApp(viewModel = viewModel, code = code)
        }
    }
}
