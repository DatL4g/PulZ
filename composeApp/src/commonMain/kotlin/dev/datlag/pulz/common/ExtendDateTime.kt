package dev.datlag.pulz.common

import androidx.compose.runtime.Composable
import dev.datlag.pulz.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.*

fun LocalDate.nextDateWithWeekDay(newDayOfWeek: DayOfWeek): LocalDate {
    val daysDiff = this.dayOfWeek.isoDayNumber - newDayOfWeek.isoDayNumber
    val daysToAdd = if (daysDiff >= 0) 7 - daysDiff else -daysDiff
    return plus(daysToAdd, DateTimeUnit.DAY)
}

@Composable
fun LocalDateTime.formatDayMon(addYear: Boolean): String? {
    var dayString = this.dayOfMonth.toString()
    if (dayString.length == 1) {
        dayString = "0$dayString"
    }

    val monthString = when (this.month) {
        Month.JANUARY -> stringResource(SharedRes.strings.jan)
        Month.FEBRUARY -> stringResource(SharedRes.strings.feb)
        Month.MARCH -> stringResource(SharedRes.strings.mar)
        Month.APRIL -> stringResource(SharedRes.strings.apr)
        Month.MAY -> stringResource(SharedRes.strings.may)
        Month.JUNE -> stringResource(SharedRes.strings.jun)
        Month.JULY -> stringResource(SharedRes.strings.jul)
        Month.AUGUST -> stringResource(SharedRes.strings.aug)
        Month.SEPTEMBER -> stringResource(SharedRes.strings.sep)
        Month.OCTOBER -> stringResource(SharedRes.strings.oct)
        Month.NOVEMBER -> stringResource(SharedRes.strings.nov)
        Month.DECEMBER -> stringResource(SharedRes.strings.dec)
        else -> when (this.monthNumber) {
            1 -> stringResource(SharedRes.strings.jan)
            2 -> stringResource(SharedRes.strings.feb)
            3 -> stringResource(SharedRes.strings.mar)
            4 -> stringResource(SharedRes.strings.apr)
            5 -> stringResource(SharedRes.strings.may)
            6 -> stringResource(SharedRes.strings.jun)
            7 -> stringResource(SharedRes.strings.jul)
            8 -> stringResource(SharedRes.strings.aug)
            9 -> stringResource(SharedRes.strings.sep)
            10 -> stringResource(SharedRes.strings.oct)
            11 -> stringResource(SharedRes.strings.nov)
            12 -> stringResource(SharedRes.strings.dec)
            else -> ""
        }
    }

    val yearSuffix = if (addYear) {
        val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
        if (this.year != currentYear) {
            this.year.toString()
        } else {
            ""
        }
    } else {
        ""
    }

    return "$dayString. $monthString $yearSuffix"
}

@Composable
fun Pair<LocalDateTime?, LocalDateTime?>.formatDayMon(): String? {
    val start = this.first
    val end = this.second

    return if (start != null && end != null) {
        "${start.formatDayMon(start.year != end.year)} - ${end.formatDayMon(true)}"
    } else {
        start?.formatDayMon(true) ?: end?.formatDayMon(true)
    }
}