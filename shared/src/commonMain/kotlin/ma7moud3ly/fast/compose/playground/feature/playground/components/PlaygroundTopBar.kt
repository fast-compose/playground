package ma7moud3ly.fast.compose.playground.feature.playground.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.WavingHand
import androidx.compose.material.icons.outlined.WebAsset
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ma7moud3ly.nemo.model.EditorTheme
import io.ma7moud3ly.nemo.model.EditorThemes
import ma7moud3ly.fast.compose.playground.BuildInfo
import ma7moud3ly.fast.compose.playground.feature.playground.model.PlaygroundEvent
import ma7moud3ly.fast.compose.playground.model.EXAMPLES
import ma7moud3ly.fast.compose.playground.model.ExampleCategory
import ma7moud3ly.fast.compose.playground.platform.LocalPlatform
import ma7moud3ly.fast.compose.playground.platform.isWeb
import ma7moud3ly.fast.compose.playground.resources.Res
import ma7moud3ly.fast.compose.playground.resources.app_fast_compose
import ma7moud3ly.fast.compose.playground.resources.app_name
import ma7moud3ly.fast.compose.playground.resources.app_nemo_editor
import ma7moud3ly.fast.compose.playground.resources.app_powered_by
import ma7moud3ly.fast.compose.playground.resources.app_wordmark_compose
import ma7moud3ly.fast.compose.playground.resources.app_wordmark_playground
import ma7moud3ly.fast.compose.playground.resources.examples_animations
import ma7moud3ly.fast.compose.playground.resources.examples_canvas
import ma7moud3ly.fast.compose.playground.resources.examples_components
import ma7moud3ly.fast.compose.playground.resources.examples_dialogs
import ma7moud3ly.fast.compose.playground.resources.examples_welcome
import ma7moud3ly.fast.compose.playground.resources.logo
import ma7moud3ly.fast.compose.playground.resources.theme_dark
import ma7moud3ly.fast.compose.playground.resources.theme_light
import ma7moud3ly.fast.compose.playground.resources.toolbar_details
import ma7moud3ly.fast.compose.playground.resources.toolbar_examples
import ma7moud3ly.fast.compose.playground.resources.toolbar_github
import ma7moud3ly.fast.compose.playground.resources.toolbar_more
import ma7moud3ly.fast.compose.playground.resources.toolbar_share
import ma7moud3ly.fast.compose.playground.resources.toolbar_theme
import ma7moud3ly.fast.compose.playground.ui.components.ToolbarButton
import ma7moud3ly.fast.compose.playground.ui.theme.AppTheme
import ma7moud3ly.fast.compose.playground.ui.theme.AppThemes
import ma7moud3ly.fast.compose.playground.ui.theme.toColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** The engine's repository, linked from the brand. */
internal const val FAST_COMPOSE_URL = "https://github.com/fast-compose/fast-compose"

/** The code editor's repository, linked from the brand. */
internal const val NEMO_EDITOR_URL = "https://github.com/Ma7moud3ly/nemo-editor"

@Preview
@Composable
private fun PlaygroundTopBarPreview() {
    AppTheme {
        PlaygroundTopBar(theme = AppThemes.DEFAULT, compact = false, uiEvents = {}, onShowDetails = {})
    }
}

@Preview(widthDp = 360)
@Composable
private fun PlaygroundTopBarPreviewCompact() {
    AppTheme(theme = EditorThemes.VS_CODE_DARK) {
        PlaygroundTopBar(theme = EditorThemes.VS_CODE_DARK, compact = true, uiEvents = {}, onShowDetails = {})
    }
}

/**
 * The top bar: the brand, then the examples and theme menus, the link to the repository, the share button (on the web
 * only) and the details button.
 *
 * @param compact whether the window is narrow; only the share button stays, as an icon, and everything else moves
 *   into the more menu
 */
@Composable
internal fun PlaygroundTopBar(
    theme: EditorTheme,
    compact: Boolean,
    uiEvents: (PlaygroundEvent) -> Unit,
    onShowDetails: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val isWeb = LocalPlatform.current.isWeb
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            .height(if (compact) 48.dp else 56.dp)
            .padding(start = if (compact) 10.dp else 16.dp, end = if (compact) 4.dp else 12.dp),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 0.dp else 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBrand(compact = compact)
        Spacer(Modifier.weight(1f))
        if (compact.not()) {
            DropdownButton(
                icon = Icons.Outlined.Lightbulb,
                label = stringResource(Res.string.toolbar_examples)
            ) { dismiss ->
                ExampleItems(
                    onSelect = { event ->
                        dismiss()
                        uiEvents(event)
                    }
                )
            }
            DropdownButton(
                icon = Icons.Outlined.Palette,
                label = theme.name
            ) {
                ThemeItems(
                    theme = theme,
                    onSelect = { uiEvents(PlaygroundEvent.SelectTheme(it)) }
                )
            }
            ToolbarButton(
                icon = Icons.Outlined.Code,
                label = stringResource(Res.string.toolbar_github),
                showLabel = true,
                onClick = { uriHandler.openUri(BuildInfo.REPO_URL) }
            )
        }
        // only the web app has an address to share
        if (isWeb) ToolbarButton(
            icon = Icons.Outlined.Share,
            label = stringResource(Res.string.toolbar_share),
            showLabel = compact.not(),
            filled = true,
            compact = compact,
            onClick = { uiEvents(PlaygroundEvent.Share) },
            modifier = Modifier.padding(horizontal = if (compact) 2.dp else 4.dp)
        )
        if (compact) MoreMenu(
            theme = theme,
            uiEvents = uiEvents,
            onShowDetails = onShowDetails
        ) else ToolbarButton(
            icon = Icons.Outlined.Info,
            label = stringResource(Res.string.toolbar_details),
            onClick = onShowDetails
        )
    }
}

/** A bar button with a down arrow; it opens a menu of [content], which can close it with the function it gets. */
@Composable
private fun DropdownButton(
    icon: ImageVector,
    label: String,
    content: @Composable (dismiss: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        ToolbarButton(
            icon = icon,
            label = label,
            showLabel = true,
            onClick = { expanded = true },
            trailing = {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 520.dp)
        ) {
            content { expanded = false }
        }
    }
}

/**
 * The logo, then the app's name with a credit to Fast Compose and nemo-editor under it; only the logo and name on a
 * [compact] bar.
 */
@Composable
private fun AppBrand(compact: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = stringResource(Res.string.app_name),
            modifier = Modifier.size(if (compact) 26.dp else 32.dp)
        )
        Column {
            Wordmark(compact = compact)
            if (compact.not()) PoweredBy()
        }
    }
}

/** The app's name: "Fast Compose" in the regular face and muted, "Playground" in the semi-bold face. */
@Composable
private fun Wordmark(compact: Boolean) {
    val compose = stringResource(Res.string.app_wordmark_compose)
    val playground = stringResource(Res.string.app_wordmark_playground)
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = muted)) { append(compose) }
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(playground) }
        },
        fontSize = if (compact) 15.sp else 18.sp,
        lineHeight = 19.sp,
        letterSpacing = (-0.2).sp,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1
    )
}

/**
 * Credit to the engine that runs the previews and to the code editor, in small print under the wordmark: "powered by
 * Fast Compose & Nemo Editor", each name a link to its repository.
 */
@Composable
private fun PoweredBy() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmallPrint(stringResource(Res.string.app_powered_by))
        CreditLink(stringResource(Res.string.app_fast_compose), FAST_COMPOSE_URL)
        SmallPrint("&")
        CreditLink(stringResource(Res.string.app_nemo_editor), NEMO_EDITOR_URL)
    }
}

@Composable
private fun SmallPrint(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1
    )
}

/** A name in the accent color that opens [url]; it is underlined while the pointer is over it. */
@Composable
private fun CreditLink(text: String, url: String) {
    val uriHandler = LocalUriHandler.current
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    Text(
        text = text,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        textDecoration = if (hovered) TextDecoration.Underline else null,
        maxLines = 1,
        modifier = Modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { uriHandler.openUri(url) }
            )
    )
}

/** The pages of the more menu: its main list, and the lists its items open. */
private enum class MenuPage { MAIN, EXAMPLES, THEMES }

/**
 * The more button of a compact bar; it opens a menu of examples, themes, the details of the app and the link to the
 * repository. Examples and themes each open their own list in the same menu, with a back item on top.
 */
@Composable
private fun MoreMenu(
    theme: EditorTheme,
    uiEvents: (PlaygroundEvent) -> Unit,
    onShowDetails: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    var expanded by remember { mutableStateOf(false) }
    var page by remember { mutableStateOf(MenuPage.MAIN) }

    fun dismiss() {
        expanded = false
        page = MenuPage.MAIN
    }

    Box {
        ToolbarButton(
            icon = Icons.Outlined.MoreVert,
            label = stringResource(Res.string.toolbar_more),
            compact = true,
            onClick = { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = ::dismiss,
            modifier = Modifier.heightIn(max = 520.dp)
        ) {
            when (page) {
                MenuPage.MAIN -> {
                    MenuItem(
                        icon = Icons.Outlined.Lightbulb,
                        text = stringResource(Res.string.toolbar_examples),
                        trailing = { MenuArrow() },
                        onClick = { page = MenuPage.EXAMPLES }
                    )
                    MenuItem(
                        icon = Icons.Outlined.Palette,
                        text = stringResource(Res.string.toolbar_theme),
                        supporting = theme.name,
                        trailing = { MenuArrow() },
                        onClick = { page = MenuPage.THEMES }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    MenuItem(
                        icon = Icons.Outlined.Info,
                        text = stringResource(Res.string.toolbar_details),
                        onClick = {
                            dismiss()
                            onShowDetails()
                        }
                    )
                    MenuItem(
                        icon = Icons.Outlined.Code,
                        text = stringResource(Res.string.toolbar_github),
                        onClick = {
                            dismiss()
                            uriHandler.openUri(BuildInfo.REPO_URL)
                        }
                    )
                }

                MenuPage.EXAMPLES -> {
                    BackItem(stringResource(Res.string.toolbar_examples)) { page = MenuPage.MAIN }
                    ExampleItems(
                        onSelect = { event ->
                            dismiss()
                            uiEvents(event)
                        }
                    )
                }

                MenuPage.THEMES -> {
                    BackItem(stringResource(Res.string.toolbar_theme)) { page = MenuPage.MAIN }
                    ThemeItems(
                        theme = theme,
                        onSelect = { uiEvents(PlaygroundEvent.SelectTheme(it)) }
                    )
                }
            }
        }
    }
}

/** The welcome sample, then every example grouped by category. [onSelect] gets the event that opens it. */
@Composable
private fun ExampleItems(onSelect: (PlaygroundEvent) -> Unit) {
    MenuItem(
        icon = Icons.Outlined.WavingHand,
        text = stringResource(Res.string.examples_welcome),
        onClick = { onSelect(PlaygroundEvent.RestoreSample) }
    )
    EXAMPLES.groupBy { it.category }.forEach { (category, examples) ->
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        MenuHeader(stringResource(category.label))
        examples.forEach { example ->
            MenuItem(
                icon = category.icon,
                text = example.title,
                onClick = { onSelect(PlaygroundEvent.OpenExample(example)) }
            )
        }
    }
}

private val ExampleCategory.label: StringResource
    get() = when (this) {
        ExampleCategory.COMPONENTS -> Res.string.examples_components
        ExampleCategory.DIALOGS -> Res.string.examples_dialogs
        ExampleCategory.ANIMATIONS -> Res.string.examples_animations
        ExampleCategory.CANVAS -> Res.string.examples_canvas
    }

private val ExampleCategory.icon: ImageVector
    get() = when (this) {
        ExampleCategory.COMPONENTS -> Icons.Outlined.Widgets
        ExampleCategory.DIALOGS -> Icons.Outlined.WebAsset
        ExampleCategory.ANIMATIONS -> Icons.Outlined.Animation
        ExampleCategory.CANVAS -> Icons.Outlined.Brush
    }

/**
 * Every nemo-editor theme, light themes first, with a check on the app's [theme]. The menu stays open, so themes can
 * be tried one after another.
 */
@Composable
private fun ThemeItems(
    theme: EditorTheme,
    onSelect: (EditorTheme) -> Unit
) {
    AppThemes.ALL.groupBy { it.dark }.forEach { (dark, themes) ->
        MenuHeader(stringResource(if (dark) Res.string.theme_dark else Res.string.theme_light))
        themes.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.name, style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { ThemeSwatch(item) },
                trailingIcon = {
                    if (item == theme) Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = { onSelect(item) }
            )
        }
    }
}

/** A menu item with an [icon], a [text] and, when given, a [supporting] line under it. */
@Composable
private fun MenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    supporting: String? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    DropdownMenuItem(
        text = {
            Column {
                Text(text, style = MaterialTheme.typography.bodyMedium)
                if (supporting != null) Text(
                    text = supporting,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        leadingIcon = { Icon(icon, contentDescription = null, Modifier.size(20.dp)) },
        trailingIcon = trailing,
        onClick = onClick
    )
}

/** The first item of a sub-list: a back arrow and the list's [title]; it returns to the main list. */
@Composable
private fun BackItem(title: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold) },
        leadingIcon = {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, Modifier.size(20.dp))
        },
        onClick = onClick
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}

/** The arrow of an item that opens a sub-list. */
@Composable
private fun MenuArrow() {
    Icon(
        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(18.dp)
    )
}

/** The background of [theme] with three dots in its keyword, string and function colors. */
@Composable
private fun ThemeSwatch(theme: EditorTheme) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(theme.background.toColor())
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
            .padding(horizontal = 5.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Dot(theme.syntax.keyword.toColor())
        Dot(theme.syntax.string.toColor())
        Dot(theme.syntax.function.toColor())
    }
}

/** A small caption above a group of menu items. */
@Composable
private fun MenuHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun Dot(color: Color) {
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(color)
    )
}
