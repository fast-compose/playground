// Gestures with Animatable: drag the ball anywhere; let go and a spring pulls it back. Double tap to change color.

val ballColors = listOf(Color(0xFF7F52FF), Color(0xFFE91E63), Color(0xFF00BFA5), Color(0xFFFF6D00))

@Preview(showBackground = true, heightDp = 400)
@Composable
fun GesturesExample() {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var colorIndex by remember { mutableStateOf(0) }
    var dragging by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val ballScale by animateFloatAsState(if (dragging) 1.2f else 1f)

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            if (dragging) "Let go!" else "Drag the ball, double tap to recolor",
            modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
            Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .scale(ballScale)
                .size(72.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(ballColors[colorIndex])
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { colorIndex = (colorIndex + 1) % ballColors.size })
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { dragging = true },
                        onDragEnd = {
                            dragging = false
                            scope.launch { offsetX.animateTo(0f, spring(dampingRatio = Spring.DampingRatioHighBouncy)) }
                            scope.launch { offsetY.animateTo(0f, spring(dampingRatio = Spring.DampingRatioHighBouncy)) }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    }
                }
        )
    }
}
