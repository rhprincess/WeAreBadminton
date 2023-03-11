package io.twinkle.wearebadminton.ui

import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import io.twinkle.wearebadminton.data.PlayerOrder
import io.twinkle.wearebadminton.data.bean.PlayerResult
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.HTHViewModel
import io.twinkle.wearebadminton.ui.widget.PlayerAvatarCardHorizontal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HTHActivityUI(viewModel: HTHViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val size by animateFloatAsState(
        targetValue = if (uiState.isLoading) 0f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "头对头",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                PlayerAvatar(
                                    order = PlayerOrder.TEAM_ONE_FIRST,
                                    playerResult = uiState.player11,
                                    viewModel = viewModel
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                PlayerAvatar(
                                    order = PlayerOrder.TEAM_ONE_SECOND,
                                    playerResult = uiState.player12,
                                    viewModel = viewModel
                                )
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .width(42.dp)
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                                )
                            }
                            Column {
                                PlayerAvatar(
                                    order = PlayerOrder.TEAM_TWO_FIRST,
                                    playerResult = uiState.player21,
                                    viewModel = viewModel
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                PlayerAvatar(
                                    order = PlayerOrder.TEAM_TWO_SECOND,
                                    playerResult = uiState.player22,
                                    viewModel = viewModel
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${uiState.team1}   -   ${uiState.team2}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            FilledIconButton(
                                onClick = {
                                    if (uiState.player11 == null || uiState.player21 == null) {
                                        Toast.makeText(
                                            context,
                                            "请至少选择两名作比较的运动员",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        viewModel.setLoading(true)
                                        viewModel.fetchHTHResult()
                                    }
                                },
                                modifier = Modifier.width(40.dp + (35.dp * size))
                            ) {
                                if (!uiState.isLoading) {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "query h2h data"
                                    )
                                }
                            }
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                }
            }

            val dialogHeight = LocalConfiguration.current.screenHeightDp * 0.4
            if (uiState.showPlayersDialog) {
                AlertDialog(onDismissRequest = {
                    viewModel.showPlayersDialog(
                        uiState.currentOrder,
                        false
                    )
                }) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {

                        TextField(
                            value = uiState.searchKey,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search"
                                )
                            },
                            singleLine = true,
                            onValueChange = {
                                viewModel.updateSearchKey(it)
                                viewModel.updatePlayersList(it)
                            }, modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 8.dp)
                                .fillMaxWidth()
                        )

                        LazyColumn(
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dialogHeight.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if (uiState.playersBean != null) {
                                val list = uiState.playersBean!!.results
                                items(list.size) {
                                    Box(modifier = Modifier.clickable {
                                        viewModel.update { currentState ->
                                            when (uiState.currentOrder) {
                                                PlayerOrder.TEAM_ONE_FIRST -> {
                                                    viewModel.showPlayersDialog(
                                                        uiState.currentOrder,
                                                        false
                                                    )
                                                    currentState.copy(player11 = list[it])
                                                }
                                                PlayerOrder.TEAM_ONE_SECOND -> {
                                                    viewModel.showPlayersDialog(
                                                        uiState.currentOrder,
                                                        false
                                                    )
                                                    currentState.copy(player12 = list[it])
                                                }
                                                PlayerOrder.TEAM_TWO_FIRST -> {
                                                    viewModel.showPlayersDialog(
                                                        uiState.currentOrder,
                                                        false
                                                    )
                                                    currentState.copy(player21 = list[it])
                                                }
                                                PlayerOrder.TEAM_TWO_SECOND -> {
                                                    viewModel.showPlayersDialog(
                                                        uiState.currentOrder,
                                                        false
                                                    )
                                                    currentState.copy(player22 = list[it])
                                                }
                                            }
                                        }
                                    }, contentAlignment = Alignment.Center) {
                                        PlayerAvatarCardHorizontal(result = list[it])
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerAvatar(
    order: PlayerOrder,
    playerResult: PlayerResult?,
    viewModel: HTHViewModel = viewModel()
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width((screenWidth * 0.35).dp)
            .combinedClickable(
                onClick = {
                    viewModel.showPlayersDialog(order, true)
                },
                onLongClick = {
                    viewModel.update { currentState ->
                        when (order) {
                            PlayerOrder.TEAM_ONE_FIRST -> {
                                currentState.copy(player11 = null)
                            }
                            PlayerOrder.TEAM_ONE_SECOND -> {
                                currentState.copy(player12 = null)
                            }
                            PlayerOrder.TEAM_TWO_FIRST -> {
                                currentState.copy(player21 = null)
                            }
                            PlayerOrder.TEAM_TWO_SECOND -> {
                                currentState.copy(player22 = null)
                            }
                        }
                    }
                }
            )
    ) {
        if (order == PlayerOrder.TEAM_TWO_FIRST || order == PlayerOrder.TEAM_TWO_SECOND) {
            Text(
                text = playerResult?.name_display ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }

        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(0.25f),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .size(56.dp)
        ) {
            Box {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "add",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                AsyncImage(
                    model = playerResult?.avatar?.url_cloudinary,
                    contentDescription = "avatar",
                    imageLoader = imageLoader,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (order == PlayerOrder.TEAM_ONE_FIRST || order == PlayerOrder.TEAM_ONE_SECOND) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = playerResult?.name_display ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerAvatarPreview() {
    BwfBadmintonTheme {
        PlayerAvatar(order = PlayerOrder.TEAM_ONE_FIRST, playerResult = null)
    }
}

@Preview(showBackground = true)
@Composable
fun HTHActivityUIPreview() {
    BwfBadmintonTheme {
        HTHActivityUI()
    }
}