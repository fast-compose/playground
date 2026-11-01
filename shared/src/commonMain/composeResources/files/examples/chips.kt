// Chips and segmented buttons: filter a list of tags and pick a view.

@Preview(showBackground = true)
@Composable
fun ChipsExample() {
    val tags = listOf("Kotlin", "Compose", "Android", "Desktop", "Web", "iOS")
    val selected = remember { mutableStateListOf("Kotlin", "Compose") }
    val views = listOf("Day", "Week", "Month")
    var view by remember { mutableStateOf(0) }

    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Filter chips", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            tags.forEach { tag ->
                val isSelected = tag in selected
                FilterChip(
                    selected = isSelected,
                    onClick = { if (isSelected) selected.remove(tag) else selected.add(tag) },
                    label = { Text(tag) },
                    leadingIcon = {
                        if (isSelected) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                )
            }
        }

        Text("Assist and input chips", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = {},
                label = { Text("Add event") },
                leadingIcon = { Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            InputChip(
                selected = true,
                onClick = {},
                label = { Text("Lexi") },
                trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        Text("Segmented buttons", style = MaterialTheme.typography.titleMedium)
        SingleChoiceSegmentedButtonRow {
            views.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = view == index,
                    onClick = { view = index },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = views.size)
                ) {
                    Text(label)
                }
            }
        }

        Text("Showing ${views[view].lowercase()} view of ${selected.size} tags")
    }
}
