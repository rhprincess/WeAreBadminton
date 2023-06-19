package io.twinkle.wearebadminton.data

import androidx.compose.ui.graphics.Color

enum class FunctionListItem(val title: String, val color: Color) {
    NewMatch("新建本地比赛", Color((0..255).random(), (0..255).random(), (0..255).random())),
    AllYearMatches("今年全年赛事", Color((0..255).random(), (0..255).random(), (0..255).random())),
    MatchHistory("本地赛事历史", Color((0..255).random(), (0..255).random(), (0..255).random())),
    WorldRanking("世界羽联排名", Color((0..255).random(), (0..255).random(), (0..255).random())),
    PopularPlayers("热门运动员", Color((0..255).random(), (0..255).random(), (0..255).random())),
    LiveMatch("实时比分", Color((0..255).random(), (0..255).random(), (0..255).random())),
    HeadToHead("头对头", Color((0..255).random(), (0..255).random(), (0..255).random())),
    MatchDraw("本地比赛签表", Color((0..255).random(), (0..255).random(), (0..255).random()))
}