// Animated Canvas: planets orbit a glowing sun, each at its own speed, driven by one infinite transition.

data class Planet(val distance: Float, val radius: Float, val speed: Float, val color: Color)

val planets = listOf(
    Planet(0.22f, 6f, 4f, Color(0xFFB0BEC5)),
    Planet(0.36f, 10f, 2.5f, Color(0xFFFFB74D)),
    Planet(0.52f, 11f, 1.6f, Color(0xFF42A5F5)),
    Planet(0.68f, 8f, 1f, Color(0xFFEF5350)),
    Planet(0.88f, 16f, 0.5f, Color(0xFFD7CCC8)),
)

@Preview(showBackground = true, heightDp = 400)
@Composable
fun CanvasOrbitsExample() {
    val transition = rememberInfiniteTransition(label = "orbits")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing))
    )
    val glow by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse)
    )

    Canvas(
        Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(listOf(Color(0xFF1A237E), Color(0xFF050814))))
    ) {
        val maxOrbit = size.minDimension / 2 * 0.95f

        // the orbits, as faint discs that get lighter toward the sun
        planets.forEach { planet ->
            drawCircle(
                color = Color.White.copy(alpha = 0.04f),
                radius = maxOrbit * planet.distance,
                center = center
            )
        }

        // the sun and its glow
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFFFF59D), Color(0x00FFC107)),
                center = center,
                radius = maxOrbit * 0.22f * glow
            ),
            radius = maxOrbit * 0.22f * glow,
            center = center
        )
        drawCircle(color = Color(0xFFFFD54F), radius = maxOrbit * 0.09f, center = center)

        // each planet turns around the center at its own speed
        planets.forEach { planet ->
            rotate(angle * planet.speed, pivot = center) {
                drawCircle(
                    color = planet.color,
                    radius = planet.radius.dp.toPx() / 2,
                    center = Offset(center.x + maxOrbit * planet.distance, center.y)
                )
            }
        }
    }
}
