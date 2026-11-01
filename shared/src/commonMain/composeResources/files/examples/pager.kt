// Pager: swipeable onboarding pages with animated page dots and buttons that scroll the pager.

data class Page(val title: String, val text: String, val color: Color)

val pages = listOf(
    Page("Write", "Type Jetpack Compose code in the editor.", Color(0xFF6750A4)),
    Page("Preview", "Every @Preview updates as you type.", Color(0xFF00897B)),
    Page("Share", "Send a link to your playground.", Color(0xFFEF6C00)),
)

@Preview(showBackground = true, heightDp = 420)
@Composable
fun PagerExample() {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            val page = pages[index]
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.verticalGradient(listOf(page.color, page.color.copy(alpha = 0.6f))))
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("${index + 1}", color = Color.White.copy(alpha = 0.5f), fontSize = 64.sp, fontWeight = FontWeight.Bold)
                Text(page.title, color = Color.White, style = MaterialTheme.typography.headlineMedium)
                Text(page.text, color = Color.White)
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(pages.size) { index ->
                    val selected = pagerState.currentPage == index
                    val width by animateDpAsState(if (selected) 24.dp else 8.dp)
                    Box(
                        Modifier
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(if (selected) pages[index].color else Color.LightGray)
                    )
                }
            }
            val last = pagerState.currentPage == pages.size - 1
            Button(onClick = {
                scope.launch { pagerState.animateScrollToPage(if (last) 0 else pagerState.currentPage + 1) }
            }) {
                Text(if (last) "Start over" else "Next")
            }
        }
    }
}
