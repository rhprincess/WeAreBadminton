package data

data class MatchData(
    var player1Scores: ArrayList<Int>,
    var player2Scores: ArrayList<Int>,
    var player1Name: String,
    var player2Name: String,
    var round: Round,
    var duration: String = "0m",
    var player1Country: CountryIcon = CountryIcon.CHINA,
    var player2Country: CountryIcon = CountryIcon.CHINA
)

data class SerializableMatchData(
    var player1Scores: ArrayList<Int>,
    var player2Scores: ArrayList<Int>,
    var player1Name: String,
    var player2Name: String,
    var round: String,
    var duration: String = "0m",
    var player1Country: String = "",
    var player2Country: String = ""
)

enum class MatchSpecific {
    PLAYER1,
    PLAYER2
}

val IndonesiaOpen = arrayListOf(
    MatchData(
        player1Scores = arrayListOf(21, 21, 0),
        player2Scores = arrayListOf(18, 7, 0),
        player1Name = "石宇奇",
        player2Name = "桃田贤斗",
        round = Round.MSR32,
        duration = "40m",
        player1Country = CountryIcon.CHINA,
        player2Country = CountryIcon.JAPAN
    ),
    MatchData(
        player1Scores = arrayListOf(17, 21, 9),
        player2Scores = arrayListOf(21, 13, 21),
        player1Name = "骆健佑",
        player2Name = "李梓嘉",
        round = Round.MSR16,
        duration = "67m",
        player1Country = CountryIcon.SINGAPORE,
        player2Country = CountryIcon.MALAYSIA
    ),
    MatchData(
        player1Scores = arrayListOf(30, 17, 21),
        player2Scores = arrayListOf(29, 21, 9),
        player1Name = "李梓嘉",
        player2Name = "安赛龙",
        round = Round.MSF,
        duration = "87m",
        player1Country = CountryIcon.MALAYSIA,
        player2Country = CountryIcon.DENMARK
    ),
    MatchData(
        player1Scores = arrayListOf(21, 21, 0),
        player2Scores = arrayListOf(17, 13, 0),
        player1Name = "冯彦哲&黄东萍",
        player2Name = "乔丹&梅拉蒂",
        round = Round.XDR32,
        duration = "37m",
        player1Country = CountryIcon.CHINA,
        player2Country = CountryIcon.INDONESIA
    ),
    MatchData(
        player1Scores = arrayListOf(9, 21, 21),
        player2Scores = arrayListOf(21, 17, 11),
        player1Name = "陆光祖",
        player2Name = "达玛新",
        round = Round.MSR32,
        duration = "1h09m",
        player1Country = CountryIcon.CHINA,
        player2Country = CountryIcon.THAILAND
    ),
    MatchData(
        player1Scores = arrayListOf(7, 14, 0),
        player2Scores = arrayListOf(21, 21, 0),
        player1Name = "维廷哈斯",
        player2Name = "黄智勇",
        round = Round.MSR32,
        duration = "36m",
        player1Country = CountryIcon.DENMARK,
        player2Country = CountryIcon.MALAYSIA
    ),
    MatchData(
        player1Scores = arrayListOf(21, 21, 0),
        player2Scores = arrayListOf(12, 11, 0),
        player1Name = "拉克什亚",
        player2Name = "奈良冈功大",
        round = Round.MSR32,
        duration = "42m",
        player1Country = CountryIcon.INDIA,
        player2Country = CountryIcon.JAPAN
    ),
    MatchData(
        player1Scores = arrayListOf(21, 22, 0),
        player2Scores = arrayListOf(18, 20, 0),
        player1Name = "梁伟铿&王昶",
        player2Name = "拉姆斯富斯&塞德尔",
        round = Round.MDR32,
        duration = "37m",
        player1Country = CountryIcon.CHINA,
        player2Country = CountryIcon.GERMANY
    )
)