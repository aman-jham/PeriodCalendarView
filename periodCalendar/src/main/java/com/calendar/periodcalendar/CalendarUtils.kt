@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.calendar.periodcalendar

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import kotlin.collections.ArrayList


val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

internal fun Calendar.toPrettyMonthString(
    style: Int = LONG,
    locale: Locale = Locale.getDefault()
): String {
    val month = getDisplayName(MONTH, style, locale)
    val year = get(YEAR).toString()
    return if (month == null) throw IllegalStateException("Cannot get pretty name")
    else "$month, $year"
}

internal fun Calendar.toPrettyDateString(locale: Locale = Locale.getDefault()): String {
    val day = get(DAY_OF_MONTH)
    var days = day.toString()
    if (day < 10) {
        days = "0$days"
    }

    return "$days ${this.toPrettyMonthString(Calendar.SHORT, locale)}"
}

internal fun Calendar.isBefore(otherCalendar: Calendar): Boolean {
    return get(YEAR) == otherCalendar.get(YEAR)
            && get(MONTH) == otherCalendar.get(MONTH)
            && get(DAY_OF_MONTH) < otherCalendar.get(DAY_OF_MONTH)
}

internal fun Calendar.isAfter(otherCalendar: Calendar): Boolean {
    return get(YEAR) == otherCalendar.get(YEAR)
            && get(MONTH) == otherCalendar.get(MONTH)
            && get(DAY_OF_MONTH) > otherCalendar.get(DAY_OF_MONTH)
}

internal fun Calendar.totalMonthDifference(startCalendar: Calendar): Int {
    val yearDiff = get(YEAR) - startCalendar.get(YEAR)
    val monthDiff = get(MONTH) - startCalendar.get(MONTH)

    return monthDiff + (yearDiff * 12)
}

internal fun differ(calendar1: CalendarDay?, calendar2: CalendarDay?): Int {

    val ONE_DAY = 1000 * 3600 * 24.toLong()

    if (calendar1 == null) {
        return Int.MIN_VALUE
    }
    if (calendar2 == null) {
        return Int.MAX_VALUE
    }
    val date = getInstance()
    date[calendar1.year, calendar1.month - 1, calendar1.day, 12, 0] = 0 //
    val startTimeMills = date.timeInMillis
    date[calendar2.year, calendar2.month - 1, calendar2.day, 12, 0] = 0 //
    val endTimeMills = date.timeInMillis
    return ((startTimeMills - endTimeMills) / ONE_DAY).toInt()

}

//    public static int getCalendarData(Calendar firstDate, Calendar secondDate) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            java.util.Calendar calendar1 = java.util.Calendar.getInstance();
//
//            java.util.Calendar calendar2 = java.util.Calendar.getInstance();
//
//            Date date1 = simpleDateFormat.parse(firstDate.getPeriodStartDate());
//
//            Date date2 = null;
//
//            date2 = simpleDateFormat.parse(secondDate.getPeriodStartDate());
//
//            calendar1.setTime(date1);
//
//            calendar2.setTime(date2);
//
//            long milliseconds1 = calendar1.getTimeInMillis();
//            long milliseconds2 = calendar2.getTimeInMillis();
//            int diff = (int) (milliseconds2 - milliseconds1);
//
//            Date currentDate = java.util.Calendar.getInstance().getTime();
//            int difference = CalendarUtil.getDaysFromDates(date1, currentDate);
//
//            if(difference > diff){
//                calendar.setLate(true);
//            }
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return (int) diffDays;
//    }

fun correctFormatData(arrayList: ArrayList<CalendarDay>): ArrayList<CalendarDay> {

    val arrayList1 = ArrayList<CalendarDay>()
    try {
        if (arrayList.size > 1) {
            Collections.sort(arrayList,
                Comparator { o1, o2 ->
                    try {
                        val d1: Date = simpleDateFormat.parse(o1.periodStartDate)
                        val d2: Date =
                            simpleDateFormat.parse(o2.periodStartDate)
                        return@Comparator d1.compareTo(d2) ?: 0
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        return@Comparator 0
                    }
                })
        }

        arrayList1.clear()
        arrayList1.addAll(getArrayData(arrayList))

        if (arrayList1.size == 0) {
            val calendarDay = arrayList[0]
            val date1: Date = simpleDateFormat.parse(calendarDay.periodStartDate)
            var date2: Date? = simpleDateFormat.parse(calendarDay.periodEndDate)
            if (calendarDay.isManualInputFromCalendar) {
                val diffDays: Int = getDaysFromDates(date1, date2!!)
                calendarDay.periodDays = diffDays
            }

            date2 = getInstance().time
            val difference: Int =
                getDaysFromDates(date1, date2)
            if (difference > calendarDay.periodCycle) {
                calendarDay.setLate(true)
            }
            arrayList1.add(calendarDay)

        } else {

            val calendarDay = arrayList[(arrayList.size - 1)]
            val date1: Date =
                simpleDateFormat.parse(calendarDay.periodStartDate)
            var date2: Date? =
                simpleDateFormat.parse(calendarDay.periodEndDate)

            val diffDays: Int = getDaysFromDates(date1, date2!!)
            calendarDay.periodDays = diffDays

            var sumOfAllPeriodCycle = 0f
            for (calendar3 in arrayList1) {
                sumOfAllPeriodCycle += calendar3.periodCycle
            }

            val sumPeriodCycle = (sumOfAllPeriodCycle / arrayList1.size).toBigDecimal() //.setScale(0, RoundingMode.UP)
            val averagePeriodCycle = sumPeriodCycle.multiply(BigDecimal(0.4)) //.setScale(0, RoundingMode.UP)
            val userGivenCycle = BigDecimal(calendarDay.periodCycle).multiply(BigDecimal(0.6)) //.setScale(0, RoundingMode.UP)


            val periodCycle = averagePeriodCycle.plus(userGivenCycle) //.setScale(0, RoundingMode.UP)

            calendarDay.periodCycle = periodCycle.toInt()

            date2 = getInstance().time

            val difference: Int = getDaysFromDates(date1, date2)
            if (difference > calendarDay.periodCycle) {
                calendarDay.setLate(true)
            }

            arrayList1.add(calendarDay)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return arrayList1
}

internal fun getArrayData(arrayList: ArrayList<CalendarDay>) : ArrayList<CalendarDay>{
    val arrayList1 = ArrayList<CalendarDay>()
    val size = arrayList.size
    for (i in 0 until size) {
        for (j in i + 1 until size) {
            val date1: Date = simpleDateFormat.parse(
                arrayList[i].periodStartDate
            )
            var date2: Date = simpleDateFormat.parse(
                arrayList[j].periodStartDate
            )
            var diffDays: Int =
                getDaysFromCycle(date1, date2)
            val calendar = arrayList[i]
            calendar.periodCycle = diffDays
            date2 =
                simpleDateFormat.parse(arrayList[i].periodEndDate)
            diffDays = getDaysFromDates(date1, date2)
            calendar.periodDays = diffDays
            arrayList1.add(calendar)
            break
        }
    }
    return arrayList1
}

internal fun getDaysFromDates(date1: Date, date2: Date): Int {
    val diff = date2.time - date1.time
    //        long diffSeconds = diff / 1000;
//        long diffMinutes = diff / (60 * 1000);
//        long diffHours = diff / (60 * 60 * 1000);
    val diffDays = diff / (24 * 60 * 60 * 1000)
    return (diffDays.toInt() + 1)
}

internal fun getDaysFromCycle(date1: Date, date2: Date): Int {
    val diff = date2.time - date1.time
    //        long diffSeconds = diff / 1000;
//        long diffMinutes = diff / (60 * 1000);
//        long diffHours = diff / (60 * 60 * 1000);
    val diffDays = diff / (24 * 60 * 60 * 1000)
    return diffDays.toInt()
}