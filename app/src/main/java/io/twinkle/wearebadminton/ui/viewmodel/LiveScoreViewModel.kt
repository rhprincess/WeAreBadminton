package io.twinkle.wearebadminton.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.twinkle.wearebadminton.data.Game
import io.twinkle.wearebadminton.data.MatchData
import io.twinkle.wearebadminton.data.SerializableMatchData
import io.twinkle.wearebadminton.data.ServiceDirection
import io.twinkle.wearebadminton.ui.uistate.LiveScoreUiState
import io.twinkle.wearebadminton.utilities.getMatchesHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class LiveScoreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LiveScoreUiState())
    val uiState: StateFlow<LiveScoreUiState> = _uiState.asStateFlow()

    fun isDoubles(isDoubles: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isDoubles = isDoubles)
        }
    }

    fun startGame(start: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isGamePlaying = start)
        }
    }

    fun updateDuration(duration: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                duration = duration,
                matchData = currentState.matchData.copy(duration = "${duration}m")
            )
        }
    }

    fun updateScore(score1: Int, score2: Int) {
        var serviceDirection = ServiceDirection.NONE
        when {
            score1 != _uiState.value.score1 && score2 == _uiState.value.score2 -> {
                // 偶数
                serviceDirection = if (score1 % 2 == 0) {
                    ServiceDirection.BOTTOM_LEFT_TO_TOP_RIGHT
                } else {
                    ServiceDirection.TOP_LEFT_TO_BOTTOM_RIGHT
                }
            }
            score1 == _uiState.value.score1 && score2 != _uiState.value.score2 -> {
                serviceDirection = if (score2 % 2 == 0) {
                    ServiceDirection.TOP_RIGHT_TO_BOTTOM_LEFT
                } else {
                    ServiceDirection.BOTTOM_RIGHT_TO_TOP_LEFT
                }
            }
        }
        judgeGameIsOver(score1, score2, serviceDirection)
    }

    fun applyScoreToCourt(score: Int) {
        val singleOddScore: Boolean
        val singleEvenScore: Boolean
        val doublesOddScore: Boolean
        val doublesEvenScore: Boolean
        if (score % 2 == 0) { // 偶数
            if (_uiState.value.isDoubles) { // 双打
                doublesOddScore = true
                singleOddScore = false
                singleEvenScore = false
                doublesEvenScore = false
            } else { // 单打
                singleOddScore = true
                doublesOddScore = false
                singleEvenScore = false
                doublesEvenScore = false
            }
        } else { // 奇数
            if (_uiState.value.isDoubles) { // 双打
                doublesEvenScore = true
                singleEvenScore = false
                singleOddScore = false
                doublesOddScore = false
            } else { // 单打
                singleEvenScore = true
                doublesEvenScore = false
                singleOddScore = false
                doublesOddScore = false
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                singleOddScore = singleOddScore,
                singleEvenScore = singleEvenScore,
                doublesOddScore = doublesOddScore,
                doublesEvenScore = doublesEvenScore
            )
        }
    }

    fun updateMatchData(data: MatchData) {
        _uiState.update { currentState ->
            currentState.copy(
                matchData = MatchData(
                    player1Scores = data.player1Scores,
                    player2Scores = data.player2Scores,
                    player1Name = data.player1Name,
                    player2Name = data.player2Name,
                    round = data.round,
                    duration = data.duration,
                    player1Country = data.player1Country,
                    player2Country = data.player2Country
                )
            )
        }
    }

    fun setTitle(title: String) {
        _uiState.update { currentState ->
            currentState.copy(
                gameTitle = title
            )
        }
    }

    fun finishCreating() {
        _uiState.update { currentState ->
            currentState.copy(isGameCreated = true)
        }
    }

    fun finishGame() {
        _uiState.update { currentState ->
            currentState.copy(isGamePlaying = false, gameFinished = true)
        }
    }

    // 判断一局或比赛是否结束
    private fun judgeGameIsOver(score1: Int, score2: Int, serviceDirection: ServiceDirection) {
        var player1Won = _uiState.value.player1Won
        var player2Won = _uiState.value.player2Won
        var p1Score = score1
        var p2Score = score2
        var game = _uiState.value.currentGame
        var gameFinished = false
        var isGamePlaying = _uiState.value.isGamePlaying
        when {
            score1 == 21 && score2 < score1 && score1 - score2 > 1 -> {
                player1Won += 1
                p1Score = 0
                p2Score = 0
                saveScores(score1, score2, game)
            }
            score2 == 21 && score1 < score2 && score2 - score1 > 1 -> {
                player2Won += 1
                p1Score = 0
                p2Score = 0
                saveScores(score1, score2, game)
            }
            score1 in 21..30 && score2 in 20..28 && score1 - score2 == 2 -> {
                player1Won += 1
                p1Score = 0
                p2Score = 0
                saveScores(score1, score2, game)
            }
            score2 in 21..30 && score1 in 20..28 && score2 - score1 == 2 -> {
                player2Won += 1
                p1Score = 0
                p2Score = 0
                saveScores(score1, score2, game)
            }
            score1 == 30 && score2 == 29 -> {
                player1Won += 1
                p1Score = 0
                p2Score = 0
                saveScores(score1, score2, game)
            }
            score2 == 30 && score1 == 29 -> {
                player2Won += 1
                p1Score = 0
                p2Score = 0
                saveScores(score1, score2, game)
            }
        }

        when (player1Won + player2Won) {
            0 -> game = Game.ONE
            1 -> game = Game.TWO
            2 -> {
                if (player1Won == 2 || player2Won == 2) {
                    gameFinished = true
                    isGamePlaying = false
                } else {
                    game = Game.THREE
                }
            }
            3 -> {
                gameFinished = true
                isGamePlaying = false
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                score1 = p1Score,
                score2 = p2Score,
                player1Won = player1Won,
                player2Won = player2Won,
                isGamePlaying = isGamePlaying,
                serviceDirection = serviceDirection,
                currentGame = game,
                gameFinished = gameFinished
            )
        }
    }

    private fun saveScores(score1: Int, score2: Int, game: Game) {
        val p1Scores = _uiState.value.matchData.player1Scores
        val p2Scores = _uiState.value.matchData.player2Scores
        when (game) {
            Game.ONE -> {
                p1Scores[0] = score1
                p2Scores[0] = score2
            }
            Game.TWO -> {
                p1Scores[1] = score1
                p2Scores[1] = score2
            }
            Game.THREE -> {
                p1Scores[2] = score1
                p2Scores[2] = score2
            }
        }
        _uiState.update { current ->
            current.copy(
                matchData = current.matchData.copy(
                    player1Scores = p1Scores,
                    player2Scores = p2Scores
                )
            )
        }
    }

    fun saveGame(context: Context) {
        val historyMatchesFile = File(context.filesDir, "matches_history.json")
        val json = historyMatchesFile.readText()
        val dataList = getMatchesHistory(json)
        val data = _uiState.value.matchData
        val serializableData = SerializableMatchData(
            player1Scores = data.player1Scores,
            player2Scores = data.player2Scores,
            player1Name = data.player1Name,
            player2Name = data.player2Name,
            player1Country = data.player1Country.name,
            player2Country = data.player2Country.name,
            round = data.round.name,
            duration = data.duration
        )
        dataList.add(serializableData)
        historyMatchesFile.writeText(Gson().toJson(dataList))
    }

}