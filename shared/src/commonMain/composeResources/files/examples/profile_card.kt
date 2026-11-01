// Profile card: a layout built from Box, Row and Column with a gradient cover, an overlapping avatar and stats.

@Preview(showBackground = true)
@Composable
fun ProfileCardExample() {
    var following by remember { mutableStateOf(false) }
    val followers = if (following) 1281 else 1280

    ElevatedCard(Modifier.padding(16.dp).fillMaxWidth()) {
        Box {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(Brush.horizontalGradient(listOf(Color(0xFF7F52FF), Color(0xFF4285F4))))
            )
            AsyncImage(
                model = "https://picsum.photos/id/64/200",
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 16.dp, top = 56.dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }

        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Lexi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF4285F4))
                    }
                    Text("Compose developer", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (following) {
                    OutlinedButton(onClick = { following = false }) { Text("Following") }
                } else {
                    Button(onClick = { following = true }) { Text("Follow") }
                }
            }

            Text("Building beautiful UIs, one composable at a time.")

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Stat("Posts", "86")
                Stat("Followers", "$followers")
                Stat("Following", "214")
            }
        }
    }
}

@Composable
fun Stat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
