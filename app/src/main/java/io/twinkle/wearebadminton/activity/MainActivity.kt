package io.twinkle.wearebadminton.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import io.twinkle.wearebadminton.data.*
import io.twinkle.wearebadminton.ui.MainActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.widget.MatchScoreCard
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BwfBadmintonTheme {
//                MainUI(this)
                val historyMatchesFile: File = File(filesDir, "matches_history.json")
                if (!historyMatchesFile.exists()) historyMatchesFile.createNewFile()
                MainActivityUI(this)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun MainUI(activity: ComponentActivity) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val openDialog = remember { mutableStateOf(false) }
    val editableMode = remember { mutableStateOf(false) }
    val matchSize = remember { mutableStateOf(IndonesiaOpen.size) }
    val selectedMatch = remember { mutableStateOf(0) }
    // 在UI间传递的比赛数据
    val data = remember { mutableStateListOf(newMatchData()) }
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "WeAreBadminton",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        val intent = Intent()
                        intent.setClass(activity, LiveScoreActivity::class.java)
                        activity.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    IconButton(onClick = {
                        val intent = Intent()
                        intent.setClass(activity, AllYearMatchActivity::class.java)
                        activity.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    IconButton(onClick = {
                        editableMode.value = false
                        openDialog.value = !openDialog.value
                        // 重新更新数据
                        data[0] = data[0].copy(
                            player1Scores = arrayListOf(0, 0, 0),
                            player2Scores = arrayListOf(0, 0, 0),
                            player1Name = "选手一",
                            player2Name = "选手二",
                            round = Round.MSR32,
                            duration = "0m",
                            player1Country = CountryIcon.CHINA,
                            player2Country = CountryIcon.CHINA
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
            ) {
                items(count = matchSize.value) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 0.dp, 10.dp, 10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .animateItemPlacement()
                            .clickable {
                                val list = IndonesiaOpen[it]
                                data[0] = data[0].copy(
                                    player1Name = list.player1Name,
                                    player2Name = list.player2Name,
                                    player1Country = list.player1Country,
                                    player2Country = list.player2Country,
                                    player1Scores = list.player1Scores,
                                    player2Scores = list.player2Scores,
                                    round = list.round,
                                    duration = list.duration,
                                )
                                openDialog.value = true
                                editableMode.value = true
                                selectedMatch.value = it
                            }) {
                        MatchScoreCard(IndonesiaOpen[it], it, matchSize)
                    }
                }
            }

            NewScoreBoard(
                openDialog = openDialog,
                data = data,
                matchSize = matchSize,
                editableMode = editableMode,
                selectedMatch = selectedMatch
            )

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun NewScoreBoard(
    openDialog: MutableState<Boolean>,
    editableMode: MutableState<Boolean>,
    data: SnapshotStateList<MatchData>,
    matchSize: MutableState<Int>,
    selectedMatch: MutableState<Int>
) {
    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp)
                    .background(
                        MaterialTheme.colorScheme.background, shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Text(
                    text = "新建比赛记分牌",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(15.dp)
                )

                PlayerAndCountryMenu(MatchSpecific.PLAYER1, data)
                PlayerAndCountryMenu(MatchSpecific.PLAYER2, data)
                MatchType(data)
                GameScoreBoard(data)

                Box(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = data[0].duration,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "比赛时长") },
                        onValueChange = {
                            data[0] = data[0].copy(duration = it)
                        })
                }

                // 底部按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        openDialog.value = false
                        editableMode.value = false
                    }) {
                        Text(text = "取消")
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                    if (editableMode.value) {
                        TextButton(onClick = {
                            openDialog.value = false
                            IndonesiaOpen[selectedMatch.value] = data[0]
                            matchSize.value = IndonesiaOpen.size
                        }) {
                            Text(text = "修改")
                        }
                    } else {
                        TextButton(onClick = {
                            openDialog.value = false
                            IndonesiaOpen.add(data[0])
                            matchSize.value = IndonesiaOpen.size
                        }) {
                            Text(text = "添加")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MatchType(data: SnapshotStateList<MatchData>) {
    val options = arrayListOf<String>()
    Round.values().forEach {
        options.add(it.name)
    }
    val expanded = remember { mutableStateOf(false) }
    val selectedOptionText = remember { mutableStateOf(data[0].round.name) }
    Row(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedOptionText.value,
                onValueChange = {},
                label = { Text("轮次") },
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
                        onClick = {
                            selectedOptionText.value = selectionOption
                            expanded.value = false
                            data[0] = data[0].copy(round = Round.values()[index])
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScoreBoard(data: SnapshotStateList<MatchData>) {
    val state = remember { mutableStateOf(0) }
    val titles = listOf("Game1", "Game2", "Game3")
    val tab1 = remember { mutableStateOf(true) }
    val tab2 = remember { mutableStateOf(false) }
    val tab3 = remember { mutableStateOf(false) }
    val p1g1 = remember { mutableStateOf(data[0].player1Scores[0]) }
    val p1g2 = remember { mutableStateOf(data[0].player1Scores[1]) }
    val p1g3 = remember { mutableStateOf(data[0].player1Scores[2]) }
    val p2g1 = remember { mutableStateOf(data[0].player2Scores[0]) }
    val p2g2 = remember { mutableStateOf(data[0].player2Scores[1]) }
    val p2g3 = remember { mutableStateOf(data[0].player2Scores[2]) }
    Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)) {
        TabRow(selectedTabIndex = state.value) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state.value == index,
                    onClick = {
                        state.value = index
                        when (index) {
                            0 -> {
                                tab1.value = true
                                tab2.value = false
                                tab3.value = false
                            }
                            1 -> {
                                tab1.value = false
                                tab2.value = true
                                tab3.value = false
                            }
                            2 -> {
                                tab1.value = false
                                tab2.value = false
                                tab3.value = true
                            }
                        }
                    },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        // Game 1
        if (tab1.value) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = p1g1.value.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        p1g1.value = if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                        data[0].player1Scores[0] =
                            if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                    })
                Divider(
                    modifier = Modifier
                        .width(35.dp)
                        .padding(horizontal = 5.dp, vertical = 25.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = p2g1.value.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        p2g1.value = if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                        data[0].player2Scores[0] =
                            if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                    })
            }
        }
        // Game 2
        if (tab2.value) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = p1g2.value.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        p1g2.value = if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                        data[0].player1Scores[1] =
                            if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                    })
                Divider(
                    modifier = Modifier
                        .width(35.dp)
                        .padding(horizontal = 5.dp, vertical = 25.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = p2g2.value.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        p2g2.value = if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                        data[0].player2Scores[1] =
                            if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                    })
            }
        }
        // Game 3
        if (tab3.value) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = p1g3.value.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        p1g3.value = if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                        data[0].player1Scores[2] =
                            if (it.isNotEmpty() && it.isDigitsOnly()) it.toInt() else 0
                    })
                Divider(
                    modifier = Modifier
                        .width(35.dp)
                        .padding(horizontal = 5.dp, vertical = 25.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = p2g3.value.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        p2g3.value = if (it != "") it.toInt() else 0
                        data[0].player2Scores[2] = if (it != "") it.toInt() else 0
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerAndCountryMenu(specific: MatchSpecific, data: SnapshotStateList<MatchData>) {
    val options = arrayListOf<String>()
    val playerName = when (specific) {
        MatchSpecific.PLAYER1 -> data[0].player1Name
        MatchSpecific.PLAYER2 -> data[0].player2Name
    }
    val name = remember { mutableStateOf(playerName) }
    CountryIcon.values().forEach {
        options.add(it.name)
    }
    val expanded = remember { mutableStateOf(false) }
    val selectedOptionText = remember {
        mutableStateOf(
            when (specific) {
                MatchSpecific.PLAYER1 -> data[0].player1Country.name
                MatchSpecific.PLAYER2 -> data[0].player2Country.name
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
                    MatchSpecific.PLAYER1 -> data[0] = data[0].copy(player1Name = it)
                    MatchSpecific.PLAYER2 -> data[0] = data[0].copy(player2Name = it)
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
                                MatchSpecific.PLAYER1 -> data[0] = data[0].copy(
                                    player1Country =
                                    CountryIcon.values()[index]
                                )
                                MatchSpecific.PLAYER2 -> data[0] = data[0].copy(
                                    player2Country =
                                    CountryIcon.values()[index]
                                )
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

private fun newMatchData(): MatchData {
    return MatchData(
        player1Scores = arrayListOf(0, 0, 0),
        player2Scores = arrayListOf(0, 0, 0),
        player1Name = "PLAYER1",
        player2Name = "PLAYER2",
        round = Round.MSR32,
        duration = "0m",
        player1Country = CountryIcon.CHINA,
        player2Country = CountryIcon.CHINA
    )
}