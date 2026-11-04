package ma7moud3ly.fast.compose.playground.model

/** The code a new visitor starts with: a small preview that shows what the playground can run. */
internal val SAMPLE_CODE = """
// Welcome to Fast Compose Playground!
// Edit the code: the preview updates as you type.
// More in the examples menu.

@Preview(showBackground = true)
@Composable
fun Welcome() {
    var likes by remember { mutableStateOf(0) }
    val scale by animateFloatAsState(
        targetValue = if (likes % 2 == 0) 1f else 1.3f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)
    )

    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Hello, Compose!", style = MaterialTheme.typography.headlineSmall)
        Text("Change this text and watch it update", color = Color.Gray)

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { likes++ }) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.scale(scale)
                )
            }
            Text("${'$'}likes likes")
        }

        Button(onClick = { likes = 0 }) {
            Text("Reset")
        }
    }
}
""".trimIndent()
