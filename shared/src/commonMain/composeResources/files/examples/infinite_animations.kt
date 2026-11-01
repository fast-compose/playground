// Infinite animations: a pulsing heart, a bouncing dots loader and a shimmering placeholder.

@Preview(showBackground = true)
@Composable
fun InfiniteAnimationsExample() {
    val transition = rememberInfiniteTransition(label = "loop")

    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse)
    )
    val shimmer by transition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing))
    )

    Column(
        Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Icon(
            Icons.Default.Favorite,
            contentDescription = null,
            tint = Color(0xFFE91E63),
            modifier = Modifier.size(56.dp).scale(scale)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(3) { index ->
                val offset by transition.animateFloat(
                    initialValue = 0f,
                    targetValue = -16f,
                    animationSpec = infiniteRepeatable(
                        tween(400, delayMillis = index * 120, easing = FastOutSlowInEasing),
                        RepeatMode.Reverse
                    )
                )
                Box(
                    Modifier
                        .offset(y = offset.dp)
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }

        val brush = Brush.linearGradient(
            colors = listOf(Color(0xFFE0E0E0), Color(0xFFF5F5F5), Color(0xFFE0E0E0)),
            start = Offset(shimmer, 0f),
            end = Offset(shimmer + 300f, 300f)
        )
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(56.dp).clip(CircleShape).background(brush))
            Spacer(Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(4.dp)).background(brush))
                Box(Modifier.fillMaxWidth(0.6f).height(14.dp).clip(RoundedCornerShape(4.dp)).background(brush))
            }
        }
    }
}
