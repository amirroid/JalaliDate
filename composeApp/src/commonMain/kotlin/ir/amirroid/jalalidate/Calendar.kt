package ir.amirroid.jalalidate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import ir.amirroid.jalalidate.date.JalaliDateTime
import ir.amirroid.jalalidate.formatter.Locale
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar() {
    val initialPage = remember { Int.MAX_VALUE / 2 }
    val pagerState = rememberPagerState(initialPage) { Int.MAX_VALUE }
    var currentFormattedDate by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collectLatest {
            val date = JalaliDateTime.now().plusMonths(it - initialPage)
            currentFormattedDate = date.format {
                applyLocale(Locale.ENGLISH)
                byUnicodePattern("MMMM yyyy")
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text("JalaliDate Sample (Current:${currentFormattedDate})")
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Transparent),
            actions = {
                AnimatedVisibility(initialPage != pagerState.currentPage) {
                    TextButton(onClick = {
                        scope.launch { pagerState.animateScrollToPage(initialPage) }
                    }) {
                        Text("Reset")
                    }
                }
            }
        )
        HorizontalPager(pagerState, modifier = Modifier.fillMaxSize()) {
            val deltaMonth = it - initialPage
            CalendarDaysContent(deltaMonth)
        }
    }
}

@Composable
fun CalendarDaysContent(deltaMonth: Int) {
    var days by remember { mutableStateOf<List<Day>>(emptyList()) }
    var columns by remember { mutableIntStateOf(5) }

    val rows = 7
    val space = 4.dp
    val nameOfDayHeight = 36.dp
    val namesOfDays = remember { JalaliDateTime.jalaliDayNamesEnglish }
    val count = remember(columns) { columns * rows }

    LaunchedEffect(deltaMonth) {
        val today = JalaliDateTime.now()
        val targetMonth = today.plusMonths(deltaMonth).copyJalali(day = 1)
        val previousMonth = targetMonth.minusMonths(1)
        val nextMonth = targetMonth.plusMonths(1)

        val emptyDays = targetMonth.dayOfWeekNumber() - 1
        val newList = mutableListOf<Day>()

        // Add tail of previous month
        if (emptyDays > 0) {
            val previewDaysRange =
                (previousMonth.monthLength - emptyDays + 1)..previousMonth.monthLength
            newList += previewDaysRange.map {
                Day(
                    day = it,
                    month = previousMonth.jalaliMonth,
                    monthName = previousMonth.monthName.english,
                    year = previousMonth.jalaliYear
                )
            }
        }

        // Add current month
        newList += (1..targetMonth.monthLength).map { day ->
            Day(
                day = day,
                isToday = day == today.jalaliDay && deltaMonth == 0,
                month = targetMonth.jalaliMonth,
                monthName = targetMonth.monthName.english,
                year = targetMonth.jalaliYear
            )
        }

        // Add head of next month
        if (newList.size < count) {
            val needed = count - newList.size
            newList += (1..needed).map {
                Day(
                    day = it,
                    month = nextMonth.jalaliMonth,
                    monthName = nextMonth.monthName.english,
                    year = nextMonth.jalaliYear
                )
            }
        } else {
            columns++
            val newCount = columns * rows
            val needed = newCount - newList.size
            newList += (1..needed).map {
                Day(
                    day = it,
                    month = nextMonth.jalaliMonth,
                    monthName = nextMonth.monthName.english,
                    year = nextMonth.jalaliYear
                )
            }
        }

        days = newList.distinctBy { "${it.monthName}${it.day}" }
    }

    BoxWithConstraints(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        val eachHeight =
            (maxHeight - (space * (columns + 1)) - nameOfDayHeight) / columns

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            columns = GridCells.Fixed(rows),
            verticalArrangement = Arrangement.spacedBy(space),
            horizontalArrangement = Arrangement.spacedBy(space),
            userScrollEnabled = false
        ) {
            items(namesOfDays, key = { it }) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(nameOfDayHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        it,
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            items(days, key = { it.monthName + it.day }) { date ->
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth()
                        .height(eachHeight)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = date.day.toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.drawBehind {
                                if (date.isToday) {
                                    drawCircle(
                                        color = Color.Black.copy(alpha = 0.2f),
                                        radius = size.maxDimension / 2
                                    )
                                }
                            }
                        )
                        if (date.day == 1) {
                            Text(
                                text = date.monthName,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                                lineHeight = 1.em,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}


@Immutable
data class Day(
    val day: Int,
    val monthName: String,
    val month: Int,
    val year: Int,
    val isToday: Boolean = false,
)