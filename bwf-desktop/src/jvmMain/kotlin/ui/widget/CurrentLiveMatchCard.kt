package ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.bean.CurrentLiveResult
import navcontroller.NavController
import screen.Screen

@Composable
fun CurrentLiveMatchCard(navController: NavController, result: CurrentLiveResult? = null) {
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colors.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                MaterialTheme.colors.onSurface.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier.clickable {
                val screenBundle = NavController.ScreenBundle()
                screenBundle.strings["tmtId"] = result?.id.toString()
                screenBundle.ints["tmtType"] = result?.type_id ?: 0
                navController.navigate(Screen.LiveMatchScreen.name, screenBundle)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                if (result?.tmtLogo != null) {
                    AsyncImage(
                        load = {
//                            loadSvgPainter(result.tmtLogo, density)
                        },
                        contentDescription = "nation",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp),
                        imageFor = { it },
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = result?.name ?: "赛事名称",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "地点: ${result?.venue_city}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "时间: ${result?.date}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                if (result?.catLogo != null) {
                    AsyncImage(
                        load = {
//                            loadSvgPainter(result.catLogo, density)
                        },
                        contentDescription = "nation",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp),
                        imageFor = { it },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

}