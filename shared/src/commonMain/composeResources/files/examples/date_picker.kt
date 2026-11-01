// Date picker: a DatePickerDialog, with the picked date turned into year, month and day by plain Kotlin.

val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

/** The date of [millis] since 1970 (UTC), as "Mon d, yyyy". */
fun formatDate(millis: Long): String {
    // days to a civil date, from Howard Hinnant's algorithm
    val z = (millis / 86400000).toInt() + 719468
    val era = (if (z >= 0) z else z - 146096) / 146097
    val doe = z - era * 146097
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val day = doy - (153 * mp + 2) / 5 + 1
    val month = if (mp < 10) mp + 3 else mp - 9
    val year = yoe + era * 400 + if (month <= 2) 1 else 0
    return "${months[month - 1]} $day, $year"
}

@Preview(showBackground = true, heightDp = 560)
@Composable
fun DatePickerExample() {
    var showPicker by remember { mutableStateOf(false) }
    var picked by remember { mutableStateOf<Long?>(null) }
    val state = rememberDatePickerState()

    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedCard(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Trip date", style = MaterialTheme.typography.labelMedium)
                    Text(
                        picked?.let { formatDate(it) } ?: "Not picked",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                TextButton(onClick = { showPicker = true }) { Text("Pick") }
            }
        }
    }

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    picked = state.selectedDateMillis
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
