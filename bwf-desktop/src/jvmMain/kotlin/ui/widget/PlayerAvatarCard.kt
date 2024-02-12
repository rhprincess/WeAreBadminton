package ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.bean.PlayerResult
import utilities.BwfApi

@Composable
fun PlayerAvatarCardVertical(result: PlayerResult? = null) {
    val density = LocalDensity.current
    Column(
        modifier = Modifier.width(65.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(65.dp)) {
            if (result?.avatar?.url_original != null) {
                AsyncImage(
                    modifier = Modifier
                        .size(55.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    load = {
                        loadImageBitmap(result.avatar.url_original)
                    },
                    imageFor = { it },
                    contentDescription = "Avatar",
                    imageTransformation = ImageTransformation.Circle,
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomStart)
            ) {
                if (result?.country_model?.flag_name_svg != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape),
                        load = {
                            loadImageBitmap(BwfApi.FLAG_URL + result.country_model.flag_name_svg)
                        },
                        imageFor = { it },
                        contentDescription = "Avatar",
                        imageTransformation = ImageTransformation.Circle,
                        contentScale = ContentScale.Crop
                    )
                }
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
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 13.sp
        )
    }
}

@Composable
fun PlayerAvatarCardHorizontal(result: PlayerResult? = null) {
    val density = LocalDensity.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(48.dp)) {
            if (result?.avatar?.url_original != null) {
                AsyncImage(
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    load = {
                        loadImageBitmap(result.avatar.url_original)
                    },
                    imageFor = { it },
                    contentDescription = "Avatar",
                    imageTransformation = ImageTransformation.Circle,
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                if (result?.country_model?.flag_name_svg != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape),
                        load = {
                            loadImageBitmap(BwfApi.FLAG_URL + result.country_model.flag_name_svg)
                        },
                        imageFor = { it },
                        contentDescription = "Avatar",
                        imageTransformation = ImageTransformation.Circle,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
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
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.weight(1f),
            fontSize = 13.sp
        )
    }
}