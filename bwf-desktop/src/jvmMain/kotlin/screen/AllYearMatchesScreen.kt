package screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import data.bean.MonthState
import data.bean.YearMatchCalendarBean
import data.payload.YearMatchCalendarPayload
import navcontroller.NavController
import ui.viewmodel.AllYearMatchesViewModel
import ui.widget.MonthEachMatchCard
import utilities.BwfApi
import utilities.LocalWindowState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllYearMatchesScreen(
    navController: NavController,
    viewModel: AllYearMatchesViewModel = AllYearMatchesViewModel.viewModel()
) {
    val totalMonthState = remember { mutableStateListOf<MonthState>() }
    val windowState = LocalWindowState.current
    SideEffect {
        fetchYearMatches(totalMonthState)
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "今年全年赛事",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = Dp.Unspecified
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val lazyGridState = rememberLazyGridState()
                    val adapter = rememberScrollbarAdapter(lazyGridState)
                    LazyVerticalGrid(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        state = lazyGridState,
                        columns = GridCells.Fixed(if (windowState.placement == WindowPlacement.Maximized) 4 else 1),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (totalMonthState.isEmpty()) {
                            item {
                                Text(
                                    text = "当前没有赛事",
                                    style = MaterialTheme.typography.h5,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        items(totalMonthState.size) {
                            Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
                                Box(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .background(color = MaterialTheme.colors.background)
                                        .shadow(
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    MonthEachMatchCard(totalMonthState[it])
                                }
                            }
                        }
                    }
                    VerticalScrollbar(
                        adapter = adapter,
                        modifier = Modifier.fillMaxHeight(),
                        style = LocalScrollbarStyle.current.copy(
                            hoverColor = MaterialTheme.colors.primary,
                            unhoverColor = MaterialTheme.colors.primary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    )
}

private fun fetchYearMatches(totalMonthState: SnapshotStateList<MonthState>) {
    Fuel.post(BwfApi.ALL_YEAR_MATCHES)
        .body(YearMatchCalendarPayload().toJson())
        .header(Headers.CONTENT_TYPE, "application/json;charset=UTF-8")
        .header(Headers.AUTHORIZATION, BwfApi.BWFAPI_AUTHORIZATION)
        .responseString { result ->
            result.fold({ str ->
                val orderReplace = str.replace(regex = Regex("(\\{\"\\d+\":)"), "[")
                val orderReplace2 = orderReplace.replace(regex = Regex("(\"\\d+\":)"), "")
                val braceReplace = orderReplace2.replace("}},", "}],")
                val finalReplace = braceReplace.replaceRange(
                    startIndex = braceReplace.length - 3,
                    endIndex = braceReplace.length - 2,
                    "]"
                )
                val bean = YearMatchCalendarBean.Deserializer().deserialize(finalReplace)
                if (bean != null) {
                    bean.results.January.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.February.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.March.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.April.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.May.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.June.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.July.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.August.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.September.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.October.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.November.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                    bean.results.December.forEach { state ->
                        if (!totalMonthState.contains(state)) {
                            totalMonthState.add(state)
                        }
                    }
                }
            }, { println(it) })
        }
}