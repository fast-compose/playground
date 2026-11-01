// Drawing pad: draw with your finger or mouse. Each stroke is a list of points, joined by rounded lines.

data class Line(val points: List<Offset>, val color: Color)

val penColors = listOf(Color(0xFF212121), Color(0xFFE91E63), Color(0xFF4285F4), Color(0xFF00BFA5), Color(0xFFFFB300))

@Preview(showBackground = true, heightDp = 480)
@Composable
fun DrawingPadExample() {
    val lines = remember { mutableStateListOf<Line>() }
    var current by remember { mutableStateOf(listOf<Offset>()) }
    var color by remember { mutableStateOf(penColors[0]) }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            penColors.forEach { penColor ->
                Box(
                    Modifier
                        .size(if (penColor == color) 32.dp else 26.dp)
                        .clip(CircleShape)
                        .background(penColor)
                        .clickable { color = penColor }
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { if (lines.isNotEmpty()) lines.removeAt(lines.lastIndex) }) {
                Icon(Icons.Default.Replay, contentDescription = "Undo")
            }
            IconButton(onClick = { lines.clear() }) {
                Icon(Icons.Default.Delete, contentDescription = "Clear")
            }
        }

        Canvas(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFAFAFA))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { start -> current = listOf(start) },
                        onDragEnd = {
                            lines.add(Line(current, color))
                            current = listOf()
                        }
                    ) { change, _ ->
                        change.consume()
                        current = current + change.position
                    }
                }
        ) {
            // each stroke is drawn as short rounded lines between its points
            (lines + Line(current, color)).forEach { line ->
                line.points.zip(line.points.drop(1)).forEach { (from, to) ->
                    drawLine(
                        color = line.color,
                        start = from,
                        end = to,
                        strokeWidth = 5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
