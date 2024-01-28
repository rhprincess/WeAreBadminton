package ui.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.BwfTheme

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
                Text(text = title, fontSize = 15.sp, color = MaterialTheme.colors.onSurface)
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f)
                )
            }
            custom()
        }
    }
}