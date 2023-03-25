package ui.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.theme.BwfTheme

@Composable
fun PlayerInfoItem(key: String, value: String) {
    Text(
        text = "$key:   $value",
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface
    )
    Spacer(modifier = Modifier.height(5.dp))
}

@Preview
@Composable
fun PlayerInfoItemPreview() {
    BwfTheme {
        PlayerInfoItem("Name", "Name")
    }
}