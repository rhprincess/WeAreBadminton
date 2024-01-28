package data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class YearMatchCalendarBean(
    val results: YearMatchResults
) {
    class Deserializer : ResponseDeserializable<YearMatchCalendarBean> {
        override fun deserialize(content: String): YearMatchCalendarBean? =
            Gson().fromJson(content, YearMatchCalendarBean::class.java)
    }
}

data class YearMatchResults(
    val January: List<MonthState>,
    val February: List<MonthState>,
    val March: List<MonthState>,
    val April: List<MonthState>,
    val May: List<MonthState>,
    val June: List<MonthState>,
    val July: List<MonthState>,
    val August: List<MonthState>,
    val September: List<MonthState>,
    val October: List<MonthState>,
    val November: List<MonthState>,
    val December: List<MonthState>
)

data class MonthState(
    val cat_logo: String?,
    val date: String,
    val end_date: String,
    val header_url: String?,
    val location: String,
    val logo: String,
    val name: String,
    val progress: String,
    val start_date: String,
    val url: String
)