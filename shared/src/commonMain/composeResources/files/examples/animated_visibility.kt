// AnimatedVisibility and animateContentSize: an expandable FAQ and a card that grows with its text.

data class Question(val title: String, val answer: String)

val questions = listOf(
    Question("What is Fast Compose?", "A Kotlin interpreter that runs Jetpack Compose code without a compiler."),
    Question("Do I need imports?", "No. Every bound API works without import lines."),
    Question("Where does it run?", "On Android, iOS, desktop and the web."),
)

@Preview(showBackground = true, heightDp = 520)
@Composable
fun AnimatedVisibilityExample() {
    var open by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        questions.forEachIndexed { index, question ->
            val isOpen = open == index
            val rotation by animateFloatAsState(if (isOpen) 180f else 0f)
            Card(Modifier.fillMaxWidth().clickable { open = if (isOpen) -1 else index }) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(question.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.rotate(rotation))
                    }
                    AnimatedVisibility(
                        visible = isOpen,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Text(question.answer, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }

        OutlinedCard(
            Modifier
                .fillMaxWidth()
                .animateContentSize()
                .clickable { expanded = !expanded }
        ) {
            Text(
                if (expanded) {
                    "animateContentSize animates the size of this card whenever its content changes. " +
                        "Tap again to make it short."
                } else {
                    "Tap to read more…"
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
