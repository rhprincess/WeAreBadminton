package ui.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ui.theme.BwfTheme

@Composable
fun GalleryItem(imgUrl: String = "") {
    val screenWidth = 375.dp - 16.dp
    var loading by remember { mutableStateOf(true) }
    val placeholder = painterResource("svg/logo-bwf-rgb.svg")
    Box(
        modifier = Modifier
            .size(screenWidth, screenWidth * 0.65f)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            load = { if (imgUrl.isNotEmpty()) loadImageBitmap(imgUrl) else placeholder },
            imageFor = {
                loading = false
                it
            },
            contentDescription = "player's gallery",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(56.dp))
            }
        }
    }
}

@Preview
@Composable
fun GalleryItemPreview() {
    BwfTheme {
        GalleryItem()
    }
}