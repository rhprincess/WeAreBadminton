package ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File

enum class ImageTransformation(var radius: Int = 8) {
    Circle,
    Rectangle,
    RoundedCorner(radius = 8)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> AsyncImage(
    modifier: Modifier,
    load: suspend () -> T,
    imageFor: @Composable (T) -> T,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    imageTransformation: ImageTransformation = ImageTransformation.Rectangle
) {
    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                // instead of printing to console, you can also write this to log,
                // or show some error placeholder
                e.printStackTrace()
                null
            }
        }
    }

    Box(
        modifier = modifier.clip(
            shape = when (imageTransformation) {
                ImageTransformation.Rectangle -> RectangleShape
                ImageTransformation.Circle -> CircleShape
                else -> RoundedCornerShape(imageTransformation.radius.dp)
            }
        )
    ) {
        AnimatedVisibility(
            visible = image != null,
            enter = scaleIn(animationSpec = tween(150)),
            exit = fadeOut(animationSpec = tween(350))
        ) {
            when (val img = imageFor(image!!)) {
                is Painter -> Image(
                    painter = img,
                    contentDescription = contentDescription,
                    alignment = alignment,
                    contentScale = contentScale,
                    colorFilter = colorFilter,
                    alpha = alpha,
                    modifier = Modifier.fillMaxSize()
                )
                is ImageBitmap -> Image(
                    bitmap = img,
                    contentDescription = contentDescription,
                    alignment = alignment,
                    contentScale = contentScale,
                    colorFilter = colorFilter,
                    alpha = alpha,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

fun loadImageBitmap(file: File): ImageBitmap =
    file.inputStream().buffered().use(::loadImageBitmap)

fun loadSvgPainter(file: File, density: Density): Painter =
    file.inputStream().buffered().use { loadSvgPainter(it, density) }

/* Loading from network with Ktor client API (https://ktor.io/docs/client.html). */

suspend fun loadImageBitmap(url: String): ImageBitmap =
    urlStream(url).use(::loadImageBitmap)

suspend fun loadSvgPainter(url: String, density: Density): Painter =
    urlStream(url).use { loadSvgPainter(it, density) }

private suspend fun urlStream(url: String) = HttpClient(CIO).use {
    ByteArrayInputStream(it.get(url))
}

@Composable
fun handleNationIcon(url: String): Painter? {
    var path = ""
    when {
        url.contains("malaysia.svg") -> path = "flags/my.svg"
        url.contains("china.svg") -> path = "flags/cn.svg"
        url.contains("chinese-taipei.svg") -> path = "flags/cn_tpe.png"
        url.contains("india") -> path = "flags/in.svg"
    }
    return if (path.isNotEmpty()) painterResource(path) else null
}