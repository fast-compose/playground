package ma7moud3ly.fast.compose.playground.feature.playground.dialog

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import kotlinx.coroutines.delay
import ma7moud3ly.fast.compose.playground.resources.Res
import ma7moud3ly.fast.compose.playground.resources.share_copied
import ma7moud3ly.fast.compose.playground.resources.share_copy
import ma7moud3ly.fast.compose.playground.resources.share_link_hint
import ma7moud3ly.fast.compose.playground.resources.share_qr_hint
import ma7moud3ly.fast.compose.playground.resources.share_qr_too_long
import ma7moud3ly.fast.compose.playground.resources.share_tab_link
import ma7moud3ly.fast.compose.playground.resources.share_tab_qr
import ma7moud3ly.fast.compose.playground.resources.share_title
import ma7moud3ly.fast.compose.playground.ui.components.MyDialog
import ma7moud3ly.fast.compose.playground.ui.theme.AppTheme
import ma7moud3ly.fast.compose.playground.ui.theme.codeFontFamily
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The longest link drawn as a QR code: about version 30 with low error correction (1,732 bytes), 137 modules wide.
 * Longer links make QR codes too dense for a phone camera to read from a screen.
 */
private const val QR_MAX_LENGTH = 1700

/** How long the copy button says "Copied", in milliseconds. */
private const val COPIED_MS = 2000L

/** The ways the dialog shares the playground. */
private enum class ShareTab(val icon: ImageVector, val label: StringResource) {
    LINK(Icons.Outlined.Link, Res.string.share_tab_link),
    QR(Icons.Outlined.QrCode2, Res.string.share_tab_qr)
}

@Preview
@Composable
private fun ShareDialogPreview() {
    AppTheme {
        ShareDialog(
            link = "https://fast-compose.github.io/playground/#theme=vs-code-light&font=14&mode=split&zcode=K0vNSy4u",
            onCopy = {},
            onDismiss = {}
        )
    }
}

/**
 * Shares the playground as a [link]: the **Link** tab shows it with a button that copies it, and the **QR code** tab
 * draws it as a QR code to scan with another device. A link too long for a QR code gets a note instead.
 *
 * @param onCopy puts the link on the clipboard
 */
@Composable
internal fun ShareDialog(
    link: String,
    onCopy: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var tab by remember { mutableStateOf(ShareTab.LINK) }
    MyDialog(
        title = stringResource(Res.string.share_title),
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ShareTabs(selected = tab, onSelect = { tab = it })
            when (tab) {
                ShareTab.LINK -> LinkTab(link = link, onCopy = onCopy)
                ShareTab.QR -> QrTab(link = link)
            }
        }
    }
}

/** The link in a box, cut short in the middle of a long line, then a button that copies it and says so. */
@Composable
private fun LinkTab(link: String, onCopy: (String) -> Unit) {
    var copied by remember { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(COPIED_MS)
            copied = false
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = link,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = codeFontFamily(),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        )
        Text(
            text = stringResource(Res.string.share_link_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Button(
            onClick = {
                onCopy(link)
                copied = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = if (copied) Icons.Outlined.Check else Icons.Outlined.ContentCopy,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(stringResource(if (copied) Res.string.share_copied else Res.string.share_copy))
        }
    }
}

/**
 * The link as a QR code as wide as the dialog, black on a white card with a quiet border so any camera reads it on any
 * theme. A link longer than [QR_MAX_LENGTH] gets a note instead.
 */
@Composable
private fun QrTab(link: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (link.length > QR_MAX_LENGTH) {
            Text(
                text = stringResource(Res.string.share_qr_too_long),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Image(
                    painter = rememberQrCodePainter(link),
                    contentDescription = stringResource(Res.string.share_tab_qr),
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = stringResource(Res.string.share_qr_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/** Two tabs, Link and QR code, as a segmented control like the editor bar's mode switch. */
@Composable
private fun ShareTabs(selected: ShareTab, onSelect: (ShareTab) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surfaceVariant)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        ShareTab.entries.forEach { item ->
            val isSelected = selected == item
            val chip by animateColorAsState(if (isSelected) colors.background else Color.Transparent)
            val border by animateColorAsState(if (isSelected) colors.outlineVariant else Color.Transparent)
            val content = if (isSelected) colors.onSurface else colors.onSurface.copy(alpha = 0.6f)
            val shape = RoundedCornerShape(7.dp)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .clip(shape)
                    .background(chip)
                    .border(1.dp, border, shape)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .selectable(selected = isSelected, role = Role.Tab, onClick = { onSelect(item) }),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (isSelected) colors.primary else content,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    text = stringResource(item.label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = content,
                    maxLines = 1
                )
            }
        }
    }
}
