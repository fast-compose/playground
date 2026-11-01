// Dialogs: a Material AlertDialog that confirms an action, and a custom Dialog with its own layout.

@Preview(showBackground = true, heightDp = 480)
@Composable
fun DialogsExample() {
    var showConfirm by remember { mutableStateOf(false) }
    var showRating by remember { mutableStateOf(false) }
    var deleted by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(0) }

    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = { showConfirm = true }, enabled = !deleted) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Delete the file")
        }
        OutlinedButton(onClick = { showRating = true }) {
            Text("Rate the playground")
        }
        Text(if (deleted) "The file was deleted." else "The file is safe.")
        Text("Your rating: " + if (rating == 0) "none yet" else "★".repeat(rating))
        if (deleted) TextButton(onClick = { deleted = false }) { Text("Undo") }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text("Delete the file?") },
            text = { Text("This can't be undone. Well, here it can.") },
            confirmButton = {
                TextButton(onClick = {
                    deleted = true
                    showConfirm = false
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Cancel") }
            }
        )
    }

    if (showRating) {
        Dialog(onDismissRequest = { showRating = false }) {
            Surface(shape = RoundedCornerShape(28.dp), tonalElevation = 6.dp) {
                var stars by remember { mutableStateOf(rating) }
                Column(
                    Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("How do you like it?", style = MaterialTheme.typography.titleLarge)
                    Row {
                        (1..5).forEach { star ->
                            IconButton(onClick = { stars = star }) {
                                Icon(
                                    if (star <= stars) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = "$star stars",
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    Button(onClick = {
                        rating = stars
                        showRating = false
                    }, enabled = stars > 0) { Text("Submit") }
                }
            }
        }
    }
}
