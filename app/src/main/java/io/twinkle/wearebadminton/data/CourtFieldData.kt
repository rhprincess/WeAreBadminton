package io.twinkle.wearebadminton.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class CourtFieldData(
    var data: MatchData = MatchData(
        player1Scores = arrayListOf(0, 0, 0),
        player2Scores = arrayListOf(0, 0, 0),
        player1Name = "石宇奇",
        player2Name = "桃田贤斗",
        round = Round.MSR32,
        duration = "0m",
        player1Country = CountryIcon.CHINA,
        player2Country = CountryIcon.JAPAN
    ),
    var isDoubles: MutableState<Boolean> = mutableStateOf(false),
    var singleOddScore: MutableState<Boolean> = mutableStateOf(false),
    var singleEvenScore: MutableState<Boolean> = mutableStateOf(false),
    var doublesOddScore: MutableState<Boolean> = mutableStateOf(false),
    var doublesEvenScore: MutableState<Boolean> = mutableStateOf(false)
)
