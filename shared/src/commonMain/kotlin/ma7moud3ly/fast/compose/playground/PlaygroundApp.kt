package ma7moud3ly.fast.compose.playground

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import ma7moud3ly.fast.compose.playground.feature.playground.PlaygroundScreen
import ma7moud3ly.fast.compose.playground.feature.playground.PlaygroundViewModel
import ma7moud3ly.fast.compose.playground.ui.theme.AppTheme
import ma7moud3ly.fast.compose.ui.ComposeCode
import ma7moud3ly.fast.compose.ui.rememberComposeCode

/**
 * The app, painted with the theme the user picked.
 *
 * @param code the code the previews run; pass it to report drawing crashes to it, as the desktop app does
 */
@Composable
fun PlaygroundApp(
    viewModel: PlaygroundViewModel = viewModel { PlaygroundViewModel() },
    code: ComposeCode = rememberComposeCode(viewModel.codeState.code)
) {
    AppTheme(theme = viewModel.theme) {
        PlaygroundScreen(viewModel = viewModel, code = code)
    }
}
