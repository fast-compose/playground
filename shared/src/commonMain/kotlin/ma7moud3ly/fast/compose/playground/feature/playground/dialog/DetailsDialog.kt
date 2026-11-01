package ma7moud3ly.fast.compose.playground.feature.playground.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ma7moud3ly.fast.compose.playground.BuildInfo
import ma7moud3ly.fast.compose.playground.feature.playground.components.FAST_COMPOSE_URL
import ma7moud3ly.fast.compose.playground.feature.playground.components.NEMO_EDITOR_URL
import ma7moud3ly.fast.compose.playground.resources.Res
import ma7moud3ly.fast.compose.playground.resources.app_name
import ma7moud3ly.fast.compose.playground.resources.details_description
import ma7moud3ly.fast.compose.playground.resources.details_fast_compose
import ma7moud3ly.fast.compose.playground.resources.details_link_fast_compose
import ma7moud3ly.fast.compose.playground.resources.details_links
import ma7moud3ly.fast.compose.playground.resources.details_nemo
import ma7moud3ly.fast.compose.playground.resources.details_report_binding
import ma7moud3ly.fast.compose.playground.resources.details_report_editor
import ma7moud3ly.fast.compose.playground.resources.details_report_rendering
import ma7moud3ly.fast.compose.playground.resources.details_title
import ma7moud3ly.fast.compose.playground.resources.details_versions
import ma7moud3ly.fast.compose.playground.resources.logo
import ma7moud3ly.fast.compose.playground.ui.components.MyDialog
import ma7moud3ly.fast.compose.playground.ui.theme.AppTheme
import ma7moud3ly.fast.compose.playground.ui.theme.codeFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** New issues on the engine's and the editor's repositories, with a title to complete. */
private const val FAST_COMPOSE_RENDERING_ISSUE_URL = "$FAST_COMPOSE_URL/issues/new?title=Rendering%20bug%3A%20"
private const val FAST_COMPOSE_BINDING_ISSUE_URL = "$FAST_COMPOSE_URL/issues/new?title=Unsupported%20API%3A%20"
private const val NEMO_EDITOR_ISSUE_URL = "$NEMO_EDITOR_URL/issues/new"

@Preview
@Composable
private fun DetailsDialogPreview() {
    AppTheme {
        DetailsDialog(onDismiss = {})
    }
}

/**
 * The details of the app: its logo, name, version and description, the versions of the libraries it is built on with
 * links to their repositories, then links to Fast Compose and to report a rendering bug or an unsupported API to it,
 * or an editor issue to nemo-editor.
 */
@Composable
internal fun DetailsDialog(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    MyDialog(
        title = stringResource(Res.string.details_title),
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "v${BuildInfo.VERSION_NAME}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = stringResource(Res.string.details_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Section(stringResource(Res.string.details_versions)) {
                VersionRow(stringResource(Res.string.details_fast_compose), BuildInfo.FAST_COMPOSE_VERSION) {
                    uriHandler.openUri(FAST_COMPOSE_URL)
                }
                VersionRow(stringResource(Res.string.details_nemo), BuildInfo.NEMO_EDITOR_VERSION) {
                    uriHandler.openUri(NEMO_EDITOR_URL)
                }
            }
            Section(stringResource(Res.string.details_links)) {
                LinkRow(Icons.Outlined.Bolt, stringResource(Res.string.details_link_fast_compose)) {
                    uriHandler.openUri(FAST_COMPOSE_URL)
                }
                LinkRow(Icons.Outlined.BugReport, stringResource(Res.string.details_report_rendering)) {
                    uriHandler.openUri(FAST_COMPOSE_RENDERING_ISSUE_URL)
                }
                LinkRow(Icons.Outlined.Extension, stringResource(Res.string.details_report_binding)) {
                    uriHandler.openUri(FAST_COMPOSE_BINDING_ISSUE_URL)
                }
                LinkRow(Icons.Outlined.Edit, stringResource(Res.string.details_report_editor)) {
                    uriHandler.openUri(NEMO_EDITOR_ISSUE_URL)
                }
            }
        }
    }
}

/** A rounded block with a small [title] above its rows. */
@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(bottom = 6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 14.dp, top = 10.dp, bottom = 4.dp)
        )
        content()
    }
}

/** An [icon] and a [label], then a link icon; a click opens the link. */
@Composable
private fun LinkRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
    }
}

/** A [name] and its [version], then a link icon; a click opens its repository. */
@Composable
private fun VersionRow(name: String, version: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = version,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = codeFontFamily(),
            color = MaterialTheme.colorScheme.primary
        )
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
    }
}
