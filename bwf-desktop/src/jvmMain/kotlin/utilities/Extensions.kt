package utilities

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.google.gson.reflect.TypeToken
import utilities.SvgIcon.*

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type!!


@Composable
fun useIcon(name: String, type: SvgIcon = BASELINE): Painter {
    return when (type) {
        BASELINE -> painterResource("icons/$name/baseline.svg")
        TWOTONE -> painterResource("icons/$name/twotone.svg")
        ROUND -> painterResource("icons/$name/round.svg")
        SHARP -> painterResource("icons/$name/sharp.svg")
        OUTLINE -> painterResource("icons/$name/outline.svg")
    }
}

@Composable
fun AutoThemedIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colors.onSurface
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun AutoThemedIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colors.onSurface
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}