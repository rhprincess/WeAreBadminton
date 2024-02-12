package ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.bean.MonthState
import ui.theme.RankDownColor
import ui.theme.RankUpColor

@Composable
fun MonthEachMatchCard(state: MonthState) {
    Row(
        modifier = Modifier.fillMaxWidth().height(125.dp).background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            load = {
                loadImageBitmap(state.logo.replace("5147", "5130"))
            },
            contentDescription = "logo",
            modifier = Modifier.size(75.dp),
            imageFor = { it },
            contentScale = ContentScale.Crop
        )
        Box(Modifier.fillMaxSize()) {
            if (state.header_url != null) {
                AsyncImage(
                    load = {
                        loadImageBitmap(state.header_url)
                    },
                    contentDescription = "header",
                    modifier = Modifier.fillMaxSize(),
                    imageFor = { it },
                    contentScale = ContentScale.Crop
                )
            }
            Row(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.weight(1f).padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = state.name, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(15.dp))
                    Text(text = state.location, fontSize = 13.sp, color = Color.White)
                }
                Column(
                    Modifier.width(100.dp).fillMaxHeight()
                        .background(color = Color.Black.copy(alpha = 0.25f), shape = RoundedCornerShape(8.dp))
                ) {
                    Box(
                        Modifier.fillMaxWidth().height(35.dp).background(
                            color = when (state.progress) {
                                "finished" -> Color.LightGray.copy(alpha = 0.65f)
                                "current" -> RankUpColor.copy(alpha = 0.65f)
                                "future" -> RankDownColor.copy(alpha = 0.65f)
                                else -> Color.Yellow.copy(alpha = 0.65f)
                            },
                            shape = RoundedCornerShape(8.dp),
                        ).padding(5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.date,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = if (state.progress == "finished") Color.Black else Color.White
                        )
                    }
                    if (!state.cat_logo.isNullOrEmpty()) {
                        AsyncImage(
                            load = {
                                loadImageBitmap(state.cat_logo)
                            },
                            contentDescription = "cat_logo",
                            modifier = Modifier.fillMaxSize(),
                            imageFor = { it },
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}