package io.twinkle.wearebadminton.ui.widget

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import io.twinkle.wearebadminton.activity.LiveMatchActivity
import io.twinkle.wearebadminton.data.bean.CurrentLiveResult
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

@Composable
fun CurrentLiveMatchCard(result: CurrentLiveResult? = null) {
    val context = LocalContext.current
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
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier.clickable {
                val intent = Intent()
                intent.putExtra("tmtId", result?.id.toString())
                intent.putExtra("tmtType", result?.type_id)
                intent.setClass(context, LiveMatchActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = result?.tmtLogo ?: "",
                    contentDescription = "nation",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp),
                    imageLoader = imageLoader,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(5.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = result?.name ?: "赛事名称",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "地点: ${result?.venue_city}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "时间: ${result?.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                AsyncImage(
                    model = result?.catLogo ?: "",
                    contentDescription = "cat logo",
                    modifier = Modifier
                        .width(100.dp)
                        .height(85.dp)
                        .padding(8.dp),
                    imageLoader = imageLoader
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CurrentLiveMatchCardPreview() {
    BwfBadmintonTheme {
        CurrentLiveMatchCard()
    }
}