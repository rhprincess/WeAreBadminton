package ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import utilities.SvgIcon
import utilities.useIcon

@Composable
fun WindowScope.WindowTitleBar(windowState: WindowState, onCloseEvent: () -> Unit) = WindowDraggableArea {
    Box(
        Modifier.height(56.dp).fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
    ) {
        if (windowState.placement == WindowPlacement.Maximized) {
            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxSize(),
                enabled = false
            ) {}
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { window.isVisible = false }) {
                Icon(
                    painter = useIcon("minus", SvgIcon.ROUND),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(24.dp).padding(5.dp)
                )
            }
            Spacer(Modifier.width(5.dp))
            IconButton(onClick = {
                if (windowState.placement == WindowPlacement.Floating) {
                    windowState.placement = WindowPlacement.Maximized
                } else {
                    windowState.placement = WindowPlacement.Floating
                }
            }) {
                Icon(
                    painter = useIcon("check_box_outline_blank", SvgIcon.ROUND),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(24.dp).padding(5.dp)
                )
            }
            Spacer(Modifier.width(5.dp))
            IconButton(onClick = { onCloseEvent() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(24.dp).padding(5.dp)
                )
            }
        }
    }
}

@Composable
fun DialogWindowTitleBar(title: String = "", dismiss: () -> Unit) {
    Box(
        Modifier.height(56.dp).fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .padding(start = 16.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.align(Alignment.CenterStart),
            color = MaterialTheme.colors.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = dismiss) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.size(24.dp).padding(5.dp)
            )
        }
    }
}