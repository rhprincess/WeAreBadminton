package ui.uistate

import data.*

data class LocalMatchUiState(
    val isGamePlaying: Boolean = false,
    val isGameCreated: Boolean = false,
    val gameFinished: Boolean = false,
    val currentGame: Game = Game.ONE,
    val player1Won: Int = 0,
    val player2Won: Int = 0,
    val gameTitle: String = "新建本地比赛",
    val score1: Int = 0,
    val score2: Int = 0,
    val duration: Int = 0,
    val serviceDirection: ServiceDirection = ServiceDirection.NONE,
    val singleOddScore: Boolean = false,
    val singleEvenScore: Boolean = false,
    val doublesOddScore: Boolean = false,
    val doublesEvenScore: Boolean = false,
    val isDoubles: Boolean = false,
    val matchData: MatchData = MatchData(
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
