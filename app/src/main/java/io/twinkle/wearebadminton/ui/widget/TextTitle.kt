package io.twinkle.wearebadminton.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

@Composable
fun TextTitle(
    modifier: Modifier = Modifier,
    title: String,
    style: TextStyle = MaterialTheme.typography.headlineSmall,
    color: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontFamily: FontFamily? = null,
    fontWeight: FontWeight? = FontWeight.Bold
) {
    Box(modifier = modifier.padding(16.dp)) {
        Text(
            text = title,
            modifier.align(Alignment.CenterStart),
            style = style,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontFamily = fontFamily,
            fontWeight = fontWeight
        )
    }
}

@Preview
@Composable
fun TextTitlePreview() {
    BwfBadmintonTheme {
        TextTitle(title = "Title")
    }
}