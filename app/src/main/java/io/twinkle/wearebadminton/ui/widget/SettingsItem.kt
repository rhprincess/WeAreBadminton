package io.twinkle.wearebadminton.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = "",
    icon: @Composable () -> Unit = {},
    custom: @Composable () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                icon()
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)) {
                Text(text = title, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }
            custom()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsItemPreview() {
    BwfBadmintonTheme {
        SettingsItem(title = "近期比赛赛事显示数量")
    }
}