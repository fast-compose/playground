// Lists: a LazyColumn of cards and list items, grouped under sticky headers.

data class Contact(val name: String, val role: String, val online: Boolean)

val contacts = listOf(
    Contact("Ada", "Engineer", true),
    Contact("Alan", "Researcher", false),
    Contact("Grace", "Admiral", true),
    Contact("Linus", "Maintainer", false),
    Contact("Margaret", "Director", true),
    Contact("Tim", "Inventor", false),
)

@Preview(showBackground = true, heightDp = 480)
@Composable
fun ListsExample() {
    val starred = remember { mutableStateListOf("Grace") }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        contacts.groupBy { it.name.first() }.forEach { (letter, group) ->
            stickyHeader {
                Text(
                    letter.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
            items(group) { contact ->
                ListItem(
                    headlineContent = { Text(contact.name) },
                    supportingContent = { Text(contact.role) },
                    leadingContent = { Avatar(contact) },
                    trailingContent = {
                        val isStarred = contact.name in starred
                        IconButton(onClick = {
                            if (isStarred) starred.remove(contact.name) else starred.add(contact.name)
                        }) {
                            Icon(
                                if (isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Star",
                                tint = if (isStarred) Color(0xFFFFB300) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Avatar(contact: Contact) {
    Box {
        Box(
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(contact.name.take(1), fontWeight = FontWeight.Bold)
        }
        if (contact.online) {
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
            )
        }
    }
}
