// Selection controls: checkboxes, radio buttons, switches and sliders.

@Preview(showBackground = true)
@Composable
fun SelectionControlsExample() {
    val toppings = listOf("Cheese", "Olives", "Mushrooms")
    val checked = remember { mutableStateListOf("Cheese") }
    var size by remember { mutableStateOf("Medium") }
    var delivery by remember { mutableStateOf(true) }
    var spicy by remember { mutableStateOf(0.3f) }

    Column(Modifier.padding(16.dp)) {
        Text("Toppings", style = MaterialTheme.typography.titleMedium)
        toppings.forEach { topping ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = topping in checked,
                    onCheckedChange = { if (it) checked.add(topping) else checked.remove(topping) }
                )
                Text(topping)
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Size", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.selectableGroup()) {
            listOf("Small", "Medium", "Large").forEach { option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = size == option, onClick = { size = option })
                    Text(option)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Delivery", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Switch(checked = delivery, onCheckedChange = { delivery = it })
        }

        Spacer(Modifier.height(8.dp))
        Text("Spicy: ${(spicy * 100).roundToInt()}%", style = MaterialTheme.typography.titleMedium)
        Slider(value = spicy, onValueChange = { spicy = it })

        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Text(
            "$size pizza with ${checked.joinToString().ifEmpty { "nothing" }}" +
                if (delivery) ", delivered" else ", picked up"
        )
    }
}
