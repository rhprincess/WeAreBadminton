package io.twinkle.wearebadminton.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.LiveMatchViewModel
import io.twinkle.wearebadminton.ui.widget.LiveCourtCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LiveMatchActivityUI(viewModel: LiveMatchViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "场地比分直播",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(verticalArrangement = Arrangement.Center, state = listState) {

                    // 标题头
                    stickyHeader {
                        if (uiState.topIndex != null && uiState.isSafeToStickOnTop) {
                            var hasUpdate = false
                            if (uiState.lastMatches.size == uiState.matches.size) {
                                hasUpdate =
                                    uiState.matches[uiState.topIndex!!] != uiState.lastMatches[uiState.topIndex!!]
                            }
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.background,
                                                MaterialTheme.colorScheme.background.copy(0.75f),
                                                MaterialTheme.colorScheme.background.copy(0.5f),
                                                MaterialTheme.colorScheme.background.copy(0.25f),
                                                Color.Transparent,
                                            )
                                        )
                                    )
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                LiveCourtCard(
                                    index = uiState.topIndex!!,
                                    matchDetail = uiState.matches[uiState.topIndex!!].match_detail,
                                    liveDetail = uiState.matches[uiState.topIndex!!].live_detail,
                                    matchStatResults = uiState.matchStatResults,
                                    hasUpdate = hasUpdate,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }

                    if (uiState.matches.isEmpty() && !uiState.isRefreshing) {
                        item {
                            Text(
                                text = "无正在进行的比赛",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    items(uiState.matches.size) {
                        if (it != uiState.topIndex) {
                            var hasUpdate = false
                            if (uiState.lastMatches.size == uiState.matches.size) {
                                hasUpdate = uiState.matches[it] != uiState.lastMatches[it]
                            }
                            Box(modifier = Modifier.padding(16.dp)) {
                                LiveCourtCard(
                                    index = it,
                                    matchDetail = uiState.matches[it].match_detail,
                                    liveDetail = uiState.matches[it].live_detail,
                                    matchStatResults = uiState.matchStatResults,
                                    hasUpdate = hasUpdate,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }

                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LiveMatchActivityUIPreview() {
    BwfBadmintonTheme {
        LiveMatchActivityUI()
    }
}