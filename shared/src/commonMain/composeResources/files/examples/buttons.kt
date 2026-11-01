// Buttons: every Material 3 button style, icon buttons and floating action buttons.

@Preview(showBackground = true)
@Composable
fun ButtonsExample() {
    var clicks by remember { mutableStateOf(0) }
    var liked by remember { mutableStateOf(false) }

    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Clicked $clicks times", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { clicks++ }) { Text("Filled") }
            FilledTonalButton(onClick = { clicks++ }) { Text("Tonal") }
            OutlinedButton(onClick = { clicks++ }) { Text("Outlined") }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ElevatedButton(onClick = { clicks++ }) { Text("Elevated") }
            TextButton(onClick = { clicks = 0 }) { Text("Reset") }
        }

        Button(onClick = { clicks++ }) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("With an icon")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { liked = !liked }) {
                Icon(
                    if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (liked) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface
                )
            }
            FilledIconButton(onClick = { clicks++ }) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
            OutlinedIconButton(onClick = { clicks++ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
            SmallFloatingActionButton(onClick = { clicks++ }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            FloatingActionButton(onClick = { clicks++ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

        ExtendedFloatingActionButton(
            text = { Text("Compose") },
            icon = { Icon(Icons.Default.Create, contentDescription = null) },
            onClick = { clicks++ }
        )
    }
}
