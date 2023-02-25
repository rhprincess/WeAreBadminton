package io.twinkle.wearebadminton.ui.widget

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import io.twinkle.wearebadminton.R
import io.twinkle.wearebadminton.data.bean.PlayerResult
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.PlayersViewModel

@Composable
fun PlayerAvatarCard(
    playersViewModel: PlayersViewModel = viewModel(),
    result: PlayerResult? = null
) {
    val uiState by playersViewModel.uiState.collectAsState()
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

    Column(
        modifier = Modifier.width(65.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(65.dp)) {
            AsyncImage(
                model = result?.avatar?.url_original,
                contentDescription = "Avatar",
                placeholder = painterResource(id = R.drawable.cn),
                modifier = Modifier
                    .size(55.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape),
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomStart)
            ) {
                AsyncImage(
                    model = "https://extranet.bwfbadminton.com/docs/flags-svg/" + result?.country_model?.flag_name_svg,
                    placeholder = painterResource(id = R.drawable.chinesetaipei),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    imageLoader = imageLoader,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        val name = result?.name_display
        Text(
            text = buildAnnotatedString {
                name?.split(" ")?.forEach {
                    if (it == result.last_name) {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("$it ")
                        }
                    } else {
                        append("$it ")
                    }
                }
            },
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 13.sp
        )
    }
}

@Preview
@Composable
fun PlayerAvatarCardPreview() {
    BwfBadmintonTheme {
        PlayerAvatarCard()
    }
}