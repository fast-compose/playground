// Value animations: animate*AsState for one value, and updateTransition to move several values together.

enum class BoxState { Collapsed, Expanded }

@Preview(showBackground = true, heightDp = 460)
@Composable
fun StateAnimationsExample() {
    var on by remember { mutableStateOf(false) }
    val color by animateColorAsState(if (on) Color(0xFF4CAF50) else Color(0xFFBDBDBD))
    val knob by animateDpAsState(if (on) 28.dp else 0.dp, spring(dampingRatio = Spring.DampingRatioMediumBouncy))

    var state by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(state, label = "box")
    val size by transition.animateDp(label = "size") { if (it == BoxState.Expanded) 160.dp else 80.dp }
    val corner by transition.animateDp(label = "corner") { if (it == BoxState.Expanded) 48.dp else 12.dp }
    val angle by transition.animateFloat(label = "angle") { if (it == BoxState.Expanded) 45f else 0f }
    val boxColor by transition.animateColor(label = "color") {
        if (it == BoxState.Expanded) Color(0xFF7F52FF) else Color(0xFF4285F4)
    }

    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("animate*AsState", style = MaterialTheme.typography.titleMedium)
        Box(
            Modifier
                .size(width = 64.dp, height = 36.dp)
                .clip(CircleShape)
                .background(color)
                .clickable { on = !on }
                .padding(4.dp)
        ) {
            Box(
                Modifier
                    .offset(x = knob)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }

        Text("updateTransition", style = MaterialTheme.typography.titleMedium)
        Box(Modifier.size(180.dp), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(size)
                    .rotate(angle)
                    .clip(RoundedCornerShape(corner))
                    .background(boxColor)
                    .clickable {
                        state = if (state == BoxState.Collapsed) BoxState.Expanded else BoxState.Collapsed
                    }
            )
        }
        Text("Tap the switch and the square")
    }
}
