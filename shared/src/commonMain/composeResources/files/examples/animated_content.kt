// AnimatedContent and Crossfade: a counter whose digits slide, and a weather card that fades between states.

@Preview(showBackground = true)
@Composable
fun AnimatedContentExample() {
    var count by remember { mutableStateOf(0) }
    var weather by remember { mutableStateOf("Sunny") }

    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilledTonalIconButton(onClick = { count-- }) { Icon(Icons.Default.Remove, contentDescription = "Less") }
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                },
                modifier = Modifier.width(120.dp),
                contentAlignment = Alignment.Center
            ) { value ->
                Text("$value", fontSize = 56.sp, fontWeight = FontWeight.Bold)
            }
            FilledTonalIconButton(onClick = { count++ }) { Icon(Icons.Default.Add, contentDescription = "More") }
        }

        SingleChoiceSegmentedButtonRow {
            val options = listOf("Sunny", "Cloudy", "Rainy")
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = weather == option,
                    onClick = { weather = option },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) { Text(option) }
            }
        }

        Crossfade(targetState = weather, animationSpec = tween(600)) { state ->
            val color = when (state) {
                "Sunny" -> Color(0xFFFFB300)
                "Cloudy" -> Color(0xFF90A4AE)
                else -> Color(0xFF42A5F5)
            }
            val icon = when (state) {
                "Sunny" -> Icons.Default.WbSunny
                "Cloudy" -> Icons.Default.Cloud
                else -> Icons.Default.WaterDrop
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color.copy(alpha = 0.2f))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = color)
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(state, style = MaterialTheme.typography.headlineSmall)
                    Text(if (state == "Sunny") "28°" else if (state == "Cloudy") "21°" else "16°")
                }
            }
        }
    }
}
