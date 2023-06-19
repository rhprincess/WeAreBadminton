package utilities

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.SerializableMatchData
import utilities.SvgIcon.*

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type!!

fun getMatchesHistory(json: String): ArrayList<SerializableMatchData> {
    return if (json.isEmpty()) {
        arrayListOf()
    } else {
        Gson().fromJson(json, genericType<ArrayList<SerializableMatchData>>())
    }
}

@Composable
fun useIcon(name: String,type: SvgIcon = BASELINE): Painter {
    return when(type) {
        BASELINE -> painterResource("icons/$name/baseline.svg")
        TWOTONE -> painterResource("icons/$name/twotone.svg")
        ROUND -> painterResource("icons/$name/round.svg")
        SHARP -> painterResource("icons/$name/sharp.svg")
        OUTLINE -> painterResource("icons/$name/outline.svg")
    }
}