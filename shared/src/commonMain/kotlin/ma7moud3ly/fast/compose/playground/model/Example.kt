package ma7moud3ly.fast.compose.playground.model

/** The groups the examples menu shows. */
enum class ExampleCategory {
    COMPONENTS,
    DIALOGS,
    ANIMATIONS,
    CANVAS
}

/**
 * A ready-made playground: Compose code kept in `composeResources/files/examples/[file].kt`.
 *
 * @param title the name shown in the examples menu
 */
data class Example(
    val title: String,
    val file: String,
    val category: ExampleCategory
) {
    /** The path of the code among the compose resources, for `Res.readBytes`. */
    val path: String get() = "files/examples/$file.kt"
}

/** Every example, in the order the menu shows them. */
val EXAMPLES: List<Example> = listOf(
    Example("Buttons", "buttons", ExampleCategory.COMPONENTS),
    Example("Text fields", "text_fields", ExampleCategory.COMPONENTS),
    Example("Selection controls", "selection_controls", ExampleCategory.COMPONENTS),
    Example("Chips and segmented buttons", "chips", ExampleCategory.COMPONENTS),
    Example("Lists", "lists", ExampleCategory.COMPONENTS),
    Example("Photo grid", "photo_grid", ExampleCategory.COMPONENTS),
    Example("Pager", "pager", ExampleCategory.COMPONENTS),
    Example("Profile card", "profile_card", ExampleCategory.COMPONENTS),
    Example("Scaffold", "scaffold", ExampleCategory.COMPONENTS),
    Example("Alert and custom dialogs", "dialogs", ExampleCategory.DIALOGS),
    Example("Bottom sheet and menu", "bottom_sheet_menu", ExampleCategory.DIALOGS),
    Example("Date picker", "date_picker", ExampleCategory.DIALOGS),
    Example("Animated visibility", "animated_visibility", ExampleCategory.ANIMATIONS),
    Example("Animated content", "animated_content", ExampleCategory.ANIMATIONS),
    Example("State animations", "state_animations", ExampleCategory.ANIMATIONS),
    Example("Infinite animations", "infinite_animations", ExampleCategory.ANIMATIONS),
    Example("Gestures", "gestures", ExampleCategory.ANIMATIONS),
    Example("Shapes and gradients", "canvas_shapes", ExampleCategory.CANVAS),
    Example("Charts", "canvas_charts", ExampleCategory.CANVAS),
    Example("Orbits", "canvas_orbits", ExampleCategory.CANVAS),
    Example("Drawing pad", "drawing_pad", ExampleCategory.CANVAS),
)
