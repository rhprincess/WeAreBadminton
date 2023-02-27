package io.twinkle.wearebadminton.ui.widget

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

@Composable
fun GalleryItem(imgUrl: String = "") {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageLoader = ImageLoader.Builder(
        LocalContext.current
    ).components {
        add(SvgDecoder.Factory())
        if (Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()
    Box(
        modifier = Modifier
            .size(screenWidth)
            .padding(16.dp)
    ) {
        SubcomposeAsyncImage(
            model = imgUrl,
            contentDescription = "player's gallery",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            imageLoader = imageLoader,
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(56.dp))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryItemPreview() {
    BwfBadmintonTheme {
        GalleryItem()
    }
}