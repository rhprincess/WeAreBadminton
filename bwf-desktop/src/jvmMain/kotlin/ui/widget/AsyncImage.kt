package ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.requests.DownloadRequest
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utilities.transferSvg2Png
import java.io.ByteArrayInputStream
import java.io.File


enum class ImageTransformation(var radius: Int = 8) {
    Circle,
    Rectangle,
    RoundedCorner(radius = 8)
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
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
    val recompose = currentRecomposeScope
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
            enter = fadeIn(animationSpec = tween(100)),
            exit = fadeOut(animationSpec = tween(150))
        ) {
            when (val img = imageFor(image!!)) {
                is ImageBitmapWrapper -> {
                    if (img.info == "placeholder") recompose.invalidate()
                    Image(
                        bitmap = img.value,
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
}

fun loadImageBitmap(file: File): ImageBitmap =
    file.inputStream().buffered().use(::loadImageBitmap)

@OptIn(ExperimentalComposeUiApi::class)
suspend fun loadImageBitmap(url: String): ImageBitmapWrapper {
    val file = when {
        url.contains("assets/players/hero") -> File("caches/hero_" + url.substringAfterLast("/"))
        url.contains("assets/players/thumbnail") -> File("caches/thumbnail_" + url.substringAfterLast("/"))
        url.endsWith(".svg") -> File("caches/" + url.substringAfterLast("/").replace(".svg", ".png"))
        else -> File("caches/" + url.substringAfterLast("/"))
    }

    if (file.parentFile != null && !file.parentFile.exists()) { // 创建父级目录
        file.parentFile.mkdirs()
    }

    return if (file.exists()) {
        try {
            return ImageBitmapWrapper(loadImageBitmap(file), info = "local file")
        } catch (e: Exception) {
            return if (url.endsWith(".svg")) {
                parseSvg(url)
                ImageBitmapWrapper(
                    loadImageBitmap(ResourceLoader.Default.load("placeholder.jpg")),
                    info = "placeholder"
                )
            } else {
                ImageBitmapWrapper(urlStream(url).use(::loadImageBitmap), info = "network")
            }
        }
    } else {
        withContext(Dispatchers.IO) {
            val svgFile = File("caches/" + url.substringAfterLast("/"))
            if (!svgFile.exists()) svgFile.createNewFile()
            file.createNewFile()
        }
        if (url.endsWith(".svg")) {
            parseSvg(url)
            ImageBitmapWrapper(loadImageBitmap(ResourceLoader.Default.load("placeholder.jpg")), info = "placeholder")
        } else {
            download(url, file) // 将文件下载至缓存区
            ImageBitmapWrapper(urlStream(url).use(::loadImageBitmap), info = "network")
        }
    }
}

private suspend fun urlStream(url: String) = HttpClient(CIO).use {
    ByteArrayInputStream(it.get(url))
}

//下载文件
private fun download(url: String, file: File): DownloadRequest = Fuel.download(url).fileDestination { _, _ -> file }
private fun parseSvg(url: String) {
    val svgPath = "caches/" + url.substringAfterLast("/")
    val pngPath = svgPath.replace(".svg", ".png")
    val svgFile = File(svgPath)
    download(url, svgFile).response { _, _, result ->
        result.fold(
            success = {
                transferSvg2Png(svgPath, pngPath)
                svgFile.delete()
            },
            failure = { it.printStackTrace() }
        )
    }
}

class ImageBitmapWrapper(val value: ImageBitmap, val info: String)