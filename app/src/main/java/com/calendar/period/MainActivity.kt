package com.calendar.period

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.calendar.periodcalendar.CalendarDay
import com.calendar.periodcalendar.CalendarPicker
import com.calendar.periodcalendar.correctFormatData
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val firstCalendarDate = Calendar.getInstance()
        firstCalendarDate.set(Calendar.DAY_OF_MONTH, 1)
        firstCalendarDate.set(Calendar.MONTH, 0)
        firstCalendarDate.add(Calendar.YEAR, -3)

//        firstCalendarDate.set(Calendar.DAY_OF_MONTH, 0)
//        firstCalendarDate.add(Calendar.MONTH, -3)

        val secondCalendarDate = Calendar.getInstance()
        secondCalendarDate.set(Calendar.DAY_OF_MONTH, 0)
        secondCalendarDate.add(Calendar.MONTH, 1)
        secondCalendarDate.add(Calendar.YEAR, 1)

//        secondCalendarDate.set(Calendar.DAY_OF_MONTH, 0)
//        secondCalendarDate.add(Calendar.MONTH, 1)


        calendar_view.setOnStartSelectedListener { _, _ ->
            // departure_date.text = label
            // return_date.text = "-"
        }



        calendar_view.apply {
            val calendars = ArrayList<CalendarDay>()
            val calendar = CalendarDay()
            calendar.periodDays = 5
            calendar.periodStartDate = "2020-08-01"
            calendar.periodEndDate = "2020-08-04"
            calendar.isManualInputFromCalendar = true
            calendar.periodCycle = 30
            calendar.lutealPhase = 14
            calendars.add(calendar)

//            calendar = CalendarDay()
//            calendar.periodDays = 5
//            calendar.periodStartDate = "2020-09-01"
//            calendar.periodEndDate = "2020-09-12"
//            calendar.isManualInputFromCalendar = true
//            calendar.periodCycle = 30
//            calendar.lutealPhase = 14
//            calendars.add(calendar)

//            calendar = CalendarDay()
//            calendar.periodDays = 5
//            calendar.periodStartDate = "2020-08-26"
//            calendar.periodEndDate = "2020-08-31"
//            calendar.isManualInputFromCalendar = true
//            calendar.periodCycle = 30
//            calendar.lutealPhase = 14
//            calendars.add(calendar)

            setMode(CalendarPicker.SelectionMode.SINGLE)

            setRangeDate(firstCalendarDate.time, secondCalendarDate.time,
                correctFormatData(calendars)
            )

//            val firstCalendarDate = Calendar.getInstance()
//            firstCalendarDate.set(2019, 9, 1)
//            val secondDate = Calendar.getInstance()
//            secondDate.set(2019, 9, 12)
//            val arrayRange = ArrayList<Range>()
//            var range = Range(firstCalendarDate.time, secondDate.time)
//            arrayRange.add(range)
//            firstCalendarDate.set(2020, 3, 1)
//            secondDate.set(2020, 3, 9)
//            range = Range(firstCalendarDate.time, secondDate.time)
//            arrayRange.add(range)
//            firstCalendarDate.set(2020, 6, 1)
//            secondDate.set(2020, 6, 10)
//            range = Range(firstCalendarDate.time, secondDate.time)
//            arrayRange.add(range)

        }


        calendar_view.postDelayed({
            calendar_view.apply {
                scrollToDate(Calendar.getInstance().time)
            }
        }, 500)





    }


}