// Photo grid: a LazyVerticalGrid of network images with Coil's AsyncImage, and a full-width header.

@Preview(showBackground = true, heightDp = 560)
@Composable
fun PhotoGridExample() {
    val photos = (10..30).toList()
    var selected by remember { mutableStateOf(-1) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(110.dp),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                if (selected < 0) "Tap a photo" else "Photo #$selected",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        items(photos) { id ->
            AsyncImage(
                model = "https://picsum.photos/id/$id/300",
                contentDescription = "Photo $id",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = if (selected == id) 3.dp else 0.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { selected = id }
            )
        }
    }
}
