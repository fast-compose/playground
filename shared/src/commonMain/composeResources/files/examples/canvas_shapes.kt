// Canvas basics: rectangles, circles, arcs, lines, a path and gradient brushes, all drawn in a DrawScope.

@Preview(showBackground = true, heightDp = 360)
@Composable
fun CanvasShapesExample() {
    Canvas(Modifier.fillMaxSize().padding(16.dp)) {
        val cell = size.width / 3

        // a rounded rectangle with a linear gradient
        drawRoundRect(
            brush = Brush.linearGradient(listOf(Color(0xFF7F52FF), Color(0xFFE91E63))),
            topLeft = Offset(8f, 8f),
            size = Size(cell - 16f, cell - 16f),
            cornerRadius = CornerRadius(32f, 32f)
        )

        // a circle with a radial gradient, on a see-through halo
        val circleCenter = Offset(cell * 1.5f, cell / 2)
        drawCircle(
            color = Color(0xFFFF6D00),
            radius = cell / 2.4f,
            center = circleCenter,
            alpha = 0.25f
        )
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0xFFFFEB3B), Color(0xFFFF6D00)), center = circleCenter),
            radius = cell / 3,
            center = circleCenter
        )

        // a pie slice
        drawArc(
            color = Color(0xFF00BFA5),
            startAngle = -90f,
            sweepAngle = 270f,
            useCenter = true,
            topLeft = Offset(cell * 2 + 8f, 8f),
            size = Size(cell - 16f, cell - 16f)
        )

        // a five-pointed star built from a Path
        val star = Path()
        val starCenter = Offset(cell / 2, cell * 1.6f)
        for (i in 0 until 10) {
            val radius = if (i % 2 == 0) cell / 2.5f else cell / 5.5f
            val angle = -PI / 2 + i * PI / 5
            val x = starCenter.x + radius * cos(angle).toFloat()
            val y = starCenter.y + radius * sin(angle).toFloat()
            if (i == 0) star.moveTo(x, y) else star.lineTo(x, y)
        }
        star.close()
        drawPath(star, color = Color(0xFFFFC107))

        // dashed and rounded lines
        for (i in 0 until 4) {
            val y = cell * 1.2f + i * 24.dp.toPx()
            drawLine(
                color = Color(0xFF4285F4),
                start = Offset(cell + 16f, y),
                end = Offset(size.width - 16f, y),
                strokeWidth = (2 + i * 2).dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = if (i % 2 == 0) PathEffect.dashPathEffect(floatArrayOf(20f, 16f), 0f) else null
            )
        }

        // a sweep gradient band along the bottom
        drawRect(
            brush = Brush.sweepGradient(listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red)),
            topLeft = Offset(0f, cell * 2.3f),
            size = Size(size.width, 24.dp.toPx())
        )
    }
}
