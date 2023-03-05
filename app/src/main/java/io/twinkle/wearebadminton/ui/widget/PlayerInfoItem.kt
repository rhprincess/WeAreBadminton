package io.twinkle.wearebadminton.ui.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

@Composable
fun PlayerInfoItem(key: String, value: String) {
    Text(
        text = "$key:   $value",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(5.dp))
}

@Preview(showBackground = true)
@Composable
fun PlayerInfoItemPreview() {
    BwfBadmintonTheme {
        PlayerInfoItem("Name", "Name")
    }
}