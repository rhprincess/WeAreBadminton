package screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.PlayerOrder
import data.bean.PlayerResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import navcontroller.NavController
import ui.viewmodel.HTHViewModel
import ui.widget.AsyncImage
import ui.widget.ImageTransformation
import ui.widget.PlayerAvatarCardHorizontal
import ui.widget.loadImageBitmap
import utilities.LocalScreenSize
import utilities.PlayerNameUtil

@Composable
fun HTHScreen(navController: NavController, viewModel: HTHViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val size by animateFloatAsState(
        targetValue = if (uiState.isLoading) 0f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    val bundle = navController.screenBundle
    val player11Name: String = bundle.strings["p11n"] ?: ""
    val player12Name: String = bundle.strings["p12n"] ?: ""
    val player21Name: String = bundle.strings["p21n"] ?: ""
    val player22Name: String = bundle.strings["p22n"] ?: ""
    LaunchedEffect(key1 = Unit) {
        if (player11Name.isNotEmpty() && player21Name.isNotEmpty()) {
            coroutineScope {
                viewModel.update { currentState ->
                    currentState.copy(
                        player11Name = player11Name,
                        player12Name = player12Name,
                        player21Name = player21Name,
                        player22Name = player22Name
                    )
                }
                viewModel.updateTransfer()
                viewModel.setLoading(true)
                delay(1000)
                viewModel.fetchHTHResult()
            }
        }
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "头对头",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {},
                backgroundColor = Color.Transparent,
                elevation = Dp.Unspecified,
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.previousScreen.value == Screen.PlayerProfileScreen) {
                            navController.navigateBack(bundle)
                        } else {
                            navController.navigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.surface,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.15f),
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
                                Spacer(modifier = Modifier.height(8.dp))
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
                                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.15f))
                                )
                            }
                            Column {
                                PlayerAvatar(
                                    order = PlayerOrder.TEAM_TWO_FIRST,
                                    playerResult = uiState.player21,
                                    viewModel = viewModel
                                )
                                Spacer(modifier = Modifier.height(8.dp))
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
                                style = MaterialTheme.typography.h3
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    if (uiState.player11 == null || uiState.player21 == null) {
//                                        Toast.makeText(
//                                            context,
//                                            "请至少选择两名作比较的运动员",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
                                        //TODO: toast弹窗
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
                                CircularProgressIndicator(color = MaterialTheme.colors.primary)
                            }
                        }
                    }
                }
            }

            val dialogHeight = LocalScreenSize.current.height * 0.4f
            if (uiState.showPlayersDialog) {
                Dialog(onDismissRequest = {
                    viewModel.showPlayersDialog(
                        uiState.currentOrder,
                        false
                    )
                }) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(
                                color = MaterialTheme.colors.background,
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
                                .height(dialogHeight),
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
    viewModel: HTHViewModel
) {
    val screenWidth = LocalScreenSize.current.width
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(screenWidth * 0.35f)
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primarySurface.copy(alpha = 0.25f),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .padding(5.dp)
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
                text = PlayerNameUtil.getBwfZhName(
                    id = playerResult?.id ?: 0,
                    defaultName = playerResult?.name_display ?: ""
                ),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }

        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colors.primarySurface.copy(0.25f),
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
                    tint = MaterialTheme.colors.onSurface
                )
                if (playerResult?.avatar?.url_cloudinary != null) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        load = {
                            loadImageBitmap(playerResult.avatar.url_cloudinary)
                        },
                        imageFor = { it },
                        contentDescription = "avatar",
                        imageTransformation = ImageTransformation.Circle,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        if (order == PlayerOrder.TEAM_ONE_FIRST || order == PlayerOrder.TEAM_ONE_SECOND) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = PlayerNameUtil.getBwfZhName(
                    id = playerResult?.id ?: 0,
                    defaultName = playerResult?.name_display ?: ""
                ),
                style = MaterialTheme.typography.body2
            )
        }
    }
}