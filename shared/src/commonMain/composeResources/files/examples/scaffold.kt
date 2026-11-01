// Scaffold: a top app bar, a bottom navigation bar, a floating action button and a snackbar.

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ScaffoldExample() {
    val tabs = listOf("Home", "Search", "Profile")
    val icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.Person)
    var tab by remember { mutableStateOf(0) }
    var notes by remember { mutableStateOf(3) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tabs[tab]) },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Menu, contentDescription = "Menu") }
                },
                actions = {
                    BadgedBox(badge = { Badge { Text("$notes") } }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    Spacer(Modifier.width(16.dp))
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = tab == index,
                        onClick = { tab = index },
                        icon = { Icon(icons[index], contentDescription = null) },
                        label = { Text(label) }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                notes++
                scope.launch { snackbarHostState.showSnackbar("Note added") }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(icons[tab], contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                Text("${tabs[tab]} screen", style = MaterialTheme.typography.headlineSmall)
                Text("$notes notes")
            }
        }
    }
}
