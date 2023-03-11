package io.twinkle.wearebadminton.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.LiveMatchViewModel
import io.twinkle.wearebadminton.ui.widget.LiveCourtCard

@OptIn(ExperimentalMaterial3Api::class)
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
                        var hasUpdate = false
                        if (uiState.lastMatches.size == uiState.matches.size) {
                            hasUpdate = uiState.matches[it] != uiState.lastMatches[it]
                        }
                        Box(modifier = Modifier.padding(16.dp)) {
                            LiveCourtCard(
                                uiState.matches[it].match_detail,
                                uiState.matches[it].live_detail,
                                hasUpdate = hasUpdate
                            )
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