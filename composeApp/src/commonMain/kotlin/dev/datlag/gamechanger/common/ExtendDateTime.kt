package dev.datlag.gamechanger.common

import kotlinx.datetime.*

fun LocalDate.nextDateWithWeekDay(newDayOfWeek: DayOfWeek): LocalDate {
    val daysDiff = this.dayOfWeek.isoDayNumber - newDayOfWeek.isoDayNumber
    val daysToAdd = if (daysDiff >= 0) 7 - daysDiff else -daysDiff
    return plus(daysToAdd, DateTimeUnit.DAY)
}