package io.twinkle.wearebadminton.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import io.twinkle.wearebadminton.R
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.theme.Theme
import io.twinkle.wearebadminton.utilities.Constants
import io.twinkle.wearebadminton.utilities.settings
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun SettingsUI() {
    val dark = isSystemInDarkTheme()
    val context = LocalContext.current
    val theme = remember { mutableStateOf(0) }
    LaunchedEffect(key1 = Unit) {
        context.settings.data.collect {
            theme.value = it[Constants.KEY_THEME] ?: 0
        }
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.theme),
                            contentDescription = "Theme",
                            modifier = Modifier
                                .size(45.dp)
                                .padding(8.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "主题颜色",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "当前主题: ${Theme.get(theme.value).name}",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.75f),
                        )
                    }
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(Theme.THEME_SIZE) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = if (dark) {
                                            Theme.get(it).DarkColors.primary
                                        } else {
                                            Theme.get(it).LightColors.primary
                                        },
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        theme.value = it
                                        GlobalScope.launch {
                                            context.settings.edit { pref ->
                                                pref[Constants.KEY_THEME] = it
                                            }
                                        }
                                    }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .alpha(if (theme.value == it) 1f else 0f)
                                        .padding(8.dp)
                                        .fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsUIPreview() {
    BwfBadmintonTheme {
        SettingsUI()
    }
}