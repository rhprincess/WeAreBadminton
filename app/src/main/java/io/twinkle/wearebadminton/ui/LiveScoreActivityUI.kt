package io.twinkle.wearebadminton.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.twinkle.wearebadminton.R
import io.twinkle.wearebadminton.activity.LiveScoreActivity
import io.twinkle.wearebadminton.data.*
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.LiveScoreViewModel
import io.twinkle.wearebadminton.ui.widget.CourtField
import io.twinkle.wearebadminton.ui.widget.LiveScoreBoard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveScoreActivityUI(
    activity: LiveScoreActivity,
    liveScoreViewModel: LiveScoreViewModel = viewModel()
) {
    val uiState by liveScoreViewModel.uiState.collectAsState()
    val content = LocalContext.current
    val matchData = uiState.matchData
    val gameTitle = remember { mutableStateOf("新建本地比赛") }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (!uiState.isGameCreated) {
                        IconButton(onClick = {
                            activity.finish()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "To Cancel A Game's Creating",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                title = {
                    Text(
                        uiState.gameTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    if (uiState.isGamePlaying && !uiState.gameFinished) {
                        IconButton(onClick = {
                            liveScoreViewModel.finishGame()
                            println(uiState.matchData.player1Scores)
                            println(uiState.matchData.player2Scores)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.stop),
                                contentDescription = "End",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            when {
                uiState.isGameCreated && !uiState.gameFinished -> FloatingActionButton(
                    onClick = {
                        liveScoreViewModel.startGame(!uiState.isGamePlaying)
                    }
                ) {
                    Icon(
                        imageVector = if (!uiState.isGamePlaying) Icons.Filled.PlayArrow else ImageVector.vectorResource(
                            id = R.drawable.pause
                        ),
                        contentDescription = "Play Game"
                    )
                }
                !uiState.isGameCreated -> ExtendedFloatingActionButton(
                    onClick = {
                        liveScoreViewModel.updateMatchData(matchData)
                        liveScoreViewModel.setTitle(title = gameTitle.value)
                        liveScoreViewModel.finishCreating()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done"
                        )
                    },
                    text = { Text("创建") }
                )
            }
            if (uiState.gameFinished && uiState.isGameCreated && !uiState.isGamePlaying) {
                ExtendedFloatingActionButton(
                    onClick = {
                        liveScoreViewModel.saveGame(content)
                        activity.finish()
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = "Save"
                        )
                    },
                    text = { Text("保存") }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { innerPadding ->
            if (uiState.isGameCreated) {
                LiveScoreContent(
                    innerPaddingValues = innerPadding,
                    liveScoreViewModel = liveScoreViewModel
                )
            } else {
                NewLocalMatch(
                    data = matchData,
                    gameTitle = gameTitle,
                    innerPaddingValues = innerPadding,
                    liveScoreViewModel = liveScoreViewModel
                )
            }
            if (uiState.gameFinished && uiState.isGameCreated && !uiState.isGamePlaying) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) {})
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewLocalMatch(
    data: MatchData,
    gameTitle: MutableState<String> = mutableStateOf("新建本地比赛"),
    liveScoreViewModel: LiveScoreViewModel = viewModel(),
    innerPaddingValues: PaddingValues = PaddingValues()
) {
    val uiState by liveScoreViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = gameTitle.value,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "比赛标题") },
                onValueChange = {
                    gameTitle.value = it
                })
        }
        PlayerAndCountryMenu(specific = MatchSpecific.PLAYER1, data = uiState.matchData)
        PlayerAndCountryMenu(specific = MatchSpecific.PLAYER2, data = uiState.matchData)
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "单/双 打",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Switch(checked = uiState.isDoubles, onCheckedChange = {
                liveScoreViewModel.isDoubles(it)
            })
        }
    }
}

@Composable
private fun LiveScoreContent(
    liveScoreViewModel: LiveScoreViewModel = viewModel(),
    innerPaddingValues: PaddingValues = PaddingValues()
) {
    val uiState by liveScoreViewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(innerPaddingValues)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(56.dp)
            ) {
                CourtField(
                    modifier = Modifier.scale(1.54f),
                    liveScoreViewModel = liveScoreViewModel
                )
            }
            GameBoard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                liveScoreViewModel = liveScoreViewModel
            )
            // 手动记分牌
            LiveScoreBoard(liveScoreViewModel)
        }
    }
}

@Composable
fun GameBoard(modifier: Modifier = Modifier, liveScoreViewModel: LiveScoreViewModel) {
    val uiState by liveScoreViewModel.uiState.collectAsState()
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(ButtonDefaults.outlinedButtonBorder, shape = RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(
                        color = when (uiState.currentGame) {
                            Game.ONE, Game.TWO, Game.THREE -> MaterialTheme.colorScheme.primary.copy(
                                0.15f
                            )
                        }
                    ),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    text = "Game1", color = when (uiState.currentGame) {
                        Game.ONE, Game.TWO, Game.THREE -> MaterialTheme.colorScheme.primary
                    }
                )
            }
            Spacer(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onSurface.copy(0.75f))
                    .size(1.dp, 48.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(
                        color = when (uiState.currentGame) {
                            Game.TWO, Game.THREE -> MaterialTheme.colorScheme.primary.copy(0.15f)
                            else -> Color.Transparent
                        }
                    ),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    text = "Game2", color = when (uiState.currentGame) {
                        Game.TWO, Game.THREE -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    }
                )
            }
            Spacer(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onSurface.copy(0.75f))
                    .size(1.dp, 48.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(
                        color = when (uiState.currentGame) {
                            Game.THREE -> MaterialTheme.colorScheme.primary.copy(0.15f)
                            else -> Color.Transparent
                        }
                    ),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    text = "Game3", color = when (uiState.currentGame) {
                        Game.THREE -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerAndCountryMenu(
    specific: MatchSpecific = MatchSpecific.PLAYER1,
    data: MatchData
) {
    val options = arrayListOf<String>()
    val playerName = when (specific) {
        MatchSpecific.PLAYER1 -> data.player1Name
        MatchSpecific.PLAYER2 -> data.player2Name
    }
    val name = remember { mutableStateOf(playerName) }
    CountryIcon.values().forEach {
        options.add(it.name)
    }
    val expanded = remember { mutableStateOf(false) }
    val selectedOptionText = remember {
        mutableStateOf(
            when (specific) {
                MatchSpecific.PLAYER1 -> data.player1Country.name
                MatchSpecific.PLAYER2 -> data.player2Country.name
            }
        )
    }
// We want to react on tap/press on TextField to show menu
    Row(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = name.value,
            onValueChange = {
                name.value = it
                when (specific) {
                    MatchSpecific.PLAYER1 -> data.player1Name = it
                    MatchSpecific.PLAYER2 -> data.player2Name = it
                }
            },
            label = {
                Text(
                    when (specific) {
                        MatchSpecific.PLAYER1 -> "选手一姓名"
                        MatchSpecific.PLAYER2 -> "选手二姓名"
                    }
                )
            },
            modifier = Modifier.weight(1f)
        )
        Box(modifier = Modifier.size(5.dp))
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOptionText.value,
                onValueChange = {},
                label = { Text("国家") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(containerColor = Color.Transparent),
                maxLines = 1
            )
            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                options.withIndex().forEach { (index, selectionOption) ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(15.dp)
                                    .clip(CircleShape),
                                painter = painterResource(id = CountryIcon.values()[index].id),
                                contentDescription = "flags",
                                tint = Color.Unspecified
                            )
                        },
                        onClick = {
                            selectedOptionText.value = selectionOption
                            when (specific) {
                                MatchSpecific.PLAYER1 -> data.player1Country =
                                    CountryIcon.values()[index]
                                MatchSpecific.PLAYER2 -> data.player2Country =
                                    CountryIcon.values()[index]
                            }
                            expanded.value = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LiveScoreContentPreview() {
    BwfBadmintonTheme {
        LiveScoreContent()
    }
}

@Preview(showBackground = true)
@Composable
fun NewLocalMatchPreview() {
    BwfBadmintonTheme {
        NewLocalMatch(
            MatchData(
                player1Scores = arrayListOf(0, 0, 0),
                player2Scores = arrayListOf(0, 0, 0),
                player1Name = "石宇奇",
                player2Name = "桃田贤斗",
                round = Round.MSR32,
                duration = "0m",
                player1Country = CountryIcon.CHINA,
                player2Country = CountryIcon.JAPAN
            )
        )
    }
}
