// Bottom sheet and menus: a ModalBottomSheet with share options and a DropdownMenu of actions.

@Preview(showBackground = true, heightDp = 480)
@Composable
fun BottomSheetMenuExample() {
    var showSheet by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var lastAction by remember { mutableStateOf("Nothing yet") }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("My document", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        onClick = {
                            lastAction = "Edit"
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Share") },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            showSheet = true
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) },
                        onClick = {
                            lastAction = "Delete"
                            showMenu = false
                        }
                    )
                }
            }
        }
        Text("Last action: $lastAction")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { showSheet = true }) { Text("Open the bottom sheet") }
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Text(
                "Share with",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            listOf(
                Icons.Default.Email to "Email",
                Icons.Default.Sms to "Message",
                Icons.Default.Link to "Copy link",
                Icons.Default.Print to "Print"
            ).forEach { (icon, label) ->
                ListItem(
                    headlineContent = { Text(label) },
                    leadingContent = { Icon(icon, contentDescription = null) },
                    modifier = Modifier.clickable {
                        lastAction = "Share by ${label.lowercase()}"
                        showSheet = false
                    }
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
