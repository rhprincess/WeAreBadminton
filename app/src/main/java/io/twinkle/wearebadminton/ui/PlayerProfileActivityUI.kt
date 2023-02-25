package io.twinkle.wearebadminton.ui

import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import io.twinkle.wearebadminton.data.bean.MatchPreviousBean
import io.twinkle.wearebadminton.data.bean.MatchPreviousResults
import io.twinkle.wearebadminton.data.payload.MatchPreviousPayload
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.PlayerProfileViewModel
import io.twinkle.wearebadminton.ui.widget.MatchPanel
import io.twinkle.wearebadminton.utilities.BwfApi
import io.twinkle.wearebadminton.utilities.genericType

@Composable
fun PlayerProfileActivityUI(playerProfileViewModel: PlayerProfileViewModel = viewModel()) {
    val uiState by playerProfileViewModel.uiState.collectAsState()
    val previousResults = remember {
        mutableStateListOf<MatchPreviousResults>()
    }
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

    val view = LocalView.current
    SideEffect {
        ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = false

        for (offset in 0..2) {
            Fuel.post(BwfApi.MATCH_PREVIOUS).body(
                Gson().toJson(
                    MatchPreviousPayload(
                        playerId = uiState.id,
                        previousOffset = offset,
                        isPara = false,
                        drawCount = 1 + offset
                    )
                )
            )
                .header(Headers.CONTENT_TYPE, "application/json")
                .header(
                    Headers.AUTHORIZATION,
                    BwfApi.WORLD_RANKING_AUTHORIZATION
                ).responseString { _, _, result ->
                    result.fold({
                        if (!it.contains("spanish-para-badminton-international-2023")) {
                            val bean = Gson().fromJson<MatchPreviousBean>(
                                it,
                                genericType<MatchPreviousBean>()
                            )
                            if (!previousResults.contains(bean.results)) {
                                previousResults.add(bean.results)
                            }
                        }
                    }, {
                        Log.e("WorldRanking", "failed to fetch the ranking table")
                    })
                }
        }
    }

// Player Banner
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 225.dp)
        ) {
            // Player Image
            AsyncImage(
                model = uiState.bannerImgUrl,
                contentDescription = "background image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp),
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterEnd),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(75.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.worldRank,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 48.sp,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp, top = 42.dp)
            ) {
                // Player Information
                Row {
                    AsyncImage(
                        model = uiState.avatarUrl,
                        contentDescription = "Player Avatar",
                        modifier = Modifier
                            .size(85.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, shape = CircleShape),
                        imageLoader = imageLoader,
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        // Name
                        Text(
                            text = buildAnnotatedString {
                                uiState.name.split(" ").forEach {
                                    if (it == uiState.lastName) {
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
                            color = Color.White,
                            modifier = Modifier.width(132.dp),
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nation Icon
                            AsyncImage(
                                model = uiState.flagUrl,
                                contentDescription = "nation",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.White, shape = CircleShape),
                                imageLoader = imageLoader,
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = uiState.country,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = "WORLD TOUR RANK: ${uiState.worldTourRank}",
//                    fontSize = 13.sp,
//                    color = Color.White
//                )
            }
        }

        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(previousResults.size) {
                Box(
                    modifier = Modifier
                        .width(screenWidth)
                        .padding(16.dp)
                        .shadow(3.dp, shape = RoundedCornerShape(8.dp))
                ) {
                    MatchPanel(results = previousResults[it])
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PlayerProfileActivityUIPreview() {
    BwfBadmintonTheme {
        PlayerProfileActivityUI()
    }
}