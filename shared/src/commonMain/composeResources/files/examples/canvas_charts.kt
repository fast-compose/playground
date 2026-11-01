// Charts on a Canvas: animated bars and a donut chart with a legend. Tap a bar or a legend item to highlight it.

data class Slice(val label: String, val value: Float, val color: Color)

val sales = listOf(
    Slice("Mon", 42f, Color(0xFF7F52FF)),
    Slice("Tue", 68f, Color(0xFF4285F4)),
    Slice("Wed", 35f, Color(0xFF00BFA5)),
    Slice("Thu", 90f, Color(0xFFFFB300)),
    Slice("Fri", 57f, Color(0xFFE91E63)),
)

@Preview(showBackground = true, heightDp = 520)
@Composable
fun CanvasChartsExample() {
    var selected by remember { mutableStateOf(3) }
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(1200, easing = FastOutSlowInEasing))
    }
    val max = sales.maxOf { it.value }
    val total = sales.sumOf { it.value.toDouble() }.toFloat()

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Sales this week", style = MaterialTheme.typography.titleMedium)

        Row(Modifier.fillMaxWidth().height(160.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            sales.forEachIndexed { index, slice ->
                Column(
                    Modifier.weight(1f).fillMaxHeight().clickable { selected = index },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Canvas(Modifier.weight(1f).fillMaxWidth()) {
                        val barHeight = size.height * slice.value / max * progress.value
                        drawRoundRect(
                            color = if (index == selected) slice.color else slice.color.copy(alpha = 0.35f),
                            topLeft = Offset(0f, size.height - barHeight),
                            size = Size(size.width, barHeight),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                    }
                    Text(slice.label, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                // pie slices with a hole cut in the middle; the selected slice sticks out
                Canvas(Modifier.fillMaxSize()) {
                    var start = -90f
                    sales.forEachIndexed { index, slice ->
                        val sweep = 360f * slice.value / total * progress.value
                        val margin = if (index == selected) 0f else 8.dp.toPx()
                        inset(margin, margin) {
                            drawArc(
                                color = slice.color,
                                startAngle = start,
                                sweepAngle = sweep - 2f,
                                useCenter = true
                            )
                        }
                        start += sweep
                    }
                    drawCircle(color = Color.White, radius = size.minDimension / 2 - 30.dp.toPx())
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${sales[selected].value.roundToInt()}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(sales[selected].label, style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                sales.forEachIndexed { index, slice ->
                    Row(
                        Modifier.clickable { selected = index },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(12.dp).clip(CircleShape).background(slice.color))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "${slice.label}  ${(slice.value / total * 100).roundToInt()}%",
                            fontWeight = if (index == selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
