package io.twinkle.wearebadminton.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

@Composable
fun MatchDrawActivityUI() {
    val drawArray = arrayListOf(
        arrayListOf(
            "Viktor Axelsen",
            "Angus Ng Ka Long",
            "Lee Cheuk Yiu",
            "Rasmus Gemke",
            "Kanta Tsuneeyama",
            "Lu Guang Zu",
            "Kento Momota",
            "Shi Yu Qi",
            "Anders Antonsen",
            "Antony Ginting",
            "Lee Choong Wei",
            "Li Shi Feng",
            "Kenta Nishimoto",
            "Zhao Jun Peng",
            "Loh Kean Yew",
            "Chou Tien Chen"
        ),
        arrayListOf(
            "Viktor Axelsen",
            "Rasmus Gemke",
            "Lu Guang Zu",
            "Shi Yu Qi",
            "Antony Ginting",
            "Lee Choong Wei",
            "Zhao Jun Peng",
            "Loh Kean Yew"
        )
    )
    Box(
        Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyRow(Modifier.fillMaxSize()) {
            itemsIndexed(drawArray) { pos, item ->

                LazyColumn(Modifier.fillMaxSize()) {
                    itemsIndexed(item) { index, name ->
                        Row(
                            Modifier
                                .width(165.dp)
                                .height(76.dp)
                                .padding(top = (pos * 76).dp)
                                .clickable { }
                        ) {
                            Box(
                                Modifier
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .defaultMinSize(minHeight = 45.dp)
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = name)
                            }
                            Spacer(
                                Modifier
                                    .width(35.dp)
                                    .height(2.dp)
                                    .align(Alignment.CenterVertically)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            )
                            if (index % 2 == 0) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 37.dp)
                                        .fillMaxHeight()
                                ) {
                                    Spacer(
                                        Modifier
                                            .width(2.dp)
                                            .height(40.dp)
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                    )
                                }
                                Spacer(
                                    Modifier
                                        .width(35.dp)
                                        .height(1.dp)
                                        .align(Alignment.Bottom)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                )
                            } else {
                                Spacer(
                                    Modifier
                                        .width(2.dp)
                                        .height(39.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                )
                                Spacer(
                                    Modifier
                                        .width(35.dp)
                                        .height(1.dp)
                                        .align(Alignment.Top)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchDrawActivityUIPreview() {
    BwfBadmintonTheme {
        MatchDrawActivityUI()
    }
}