package com.calendar.periodcalendar

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.RoundingMode
import java.util.*
import java.util.Calendar.*
import kotlin.collections.ArrayList


class CalendarPicker : RecyclerView {

    private val timeZone = TimeZone.getDefault()
    private val locale = Locale.getDefault()

    private val calendarAdapter = CalendarAdapter()
    private val startCalendar = getInstance(timeZone, locale)
    private val endCalendar = getInstance(timeZone, locale)

    private var mCalendarData: MutableList<CalendarEntity> = mutableListOf()
    private var mStartDateSelection: SelectedDate? = null
    private var mEndDateSelection: SelectedDate? = null
    private var mPickerSelectionType = SelectionMode.SINGLE
    private var mShowDayOfWeekTitle = true

    private var mSelectedCalendarArray: MutableList<CalendarDay> = mutableListOf()

    private var mOnStartSelectedListener: (startDate: Date, label: String) -> Unit = { _, _ -> }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        extractAttributes(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        extractAttributes(attributeSet)
    }

    init {
        startCalendar.set(HOUR_OF_DAY, 0)
        startCalendar.set(MINUTE, 0)
        startCalendar.set(SECOND, 0)
        startCalendar.set(MILLISECOND, 0)

        endCalendar.time = startCalendar.time
        endCalendar.add(YEAR, 1)

        setBackgroundColor(ContextCompat.getColor(context, R.color.calendar_picker_bg))
        initAdapter()
        initListener()
    }

    // region setter

    fun setRangeDate(startDate: Date, endDate: Date, arrayList: ArrayList<CalendarDay>) {
        require(startDate.time <= endDate.time) { "startDate can't be higher than endDate" }

        startCalendar.withTime(startDate)
        endCalendar.withTime(endDate)

        mSelectedCalendarArray.clear()
        mSelectedCalendarArray.addAll(arrayList)

        refreshData()
    }


    fun setMultiSelectionDate(arrayList: ArrayList<Range>) {
        for (range in arrayList) {
            itemAnimator = null
            selectDate(range.startDate)
            selectDate(range.endDate)
        }
    }


    fun scrollToDate(date: Date) {
        val index =
            mCalendarData.indexOfFirst { it is CalendarEntity.Day && it.date.isTheSameDay(date) }
        require(index > -1) { "Date to scroll must be included in your Calendar Range Date" }
        //  smoothScrollToPosition(index)
        scrollToPosition(index)
    }

    fun showDayOfWeekTitle(show: Boolean) {
        mShowDayOfWeekTitle = show
    }

    fun setSelectionDate(startDate: Date, endDate: Date? = null) {
        itemAnimator = null
        selectDate(startDate)
        if (endDate != null) selectDate(endDate)
    }

    fun setMode(mode: SelectionMode) {
        mPickerSelectionType = mode
    }

    fun setOnStartSelectedListener(callback: (startDate: Date, label: String) -> Unit) {
        mOnStartSelectedListener = callback
    }


    fun getSelectedDate(): Pair<Date?, Date?> {
        return Pair(mStartDateSelection?.day?.date, mEndDateSelection?.day?.date)
    }

    private fun initListener() {
        calendarAdapter.onActionListener = { item, position ->
            if (itemAnimator == null) itemAnimator = DefaultItemAnimator()
            if (item is CalendarEntity.Day) onDaySelected(item, position)
        }
    }

    private fun initAdapter() {
        layoutManager = GridLayoutManager(context, TOTAL_COLUMN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return mCalendarData[position].columnCount
                }
            }
        }
        adapter = calendarAdapter
        refreshData()
    }

    private fun selectDate(date: Date) {
        val index =
            mCalendarData.indexOfFirst { it is CalendarEntity.Day && it.date.isTheSameDay(date) }
        require(index > -1) {
            "Selection date must be included in your Calendar Range Date"
        }

        onDaySelected(mCalendarData[index] as CalendarEntity.Day, index)
    }

    private fun refreshData() {
        mCalendarData = buildCalendarData()
        calendarAdapter.submitList(mCalendarData)
    }

    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarPicker)
        mPickerSelectionType =
            SelectionMode.values()[typedArray.getInt(R.styleable.CalendarPicker_pickerType, 0)]
        mShowDayOfWeekTitle =
            typedArray.getBoolean(R.styleable.CalendarPicker_showDayOfWeekTitle, true)
        typedArray.recycle()
    }

    private fun buildCalendarData(): MutableList<CalendarEntity> {
        val calendarData = mutableListOf<CalendarEntity>()

        val size: Int = mSelectedCalendarArray.size

        var days = 0
        var daysLate = 0
        var dateMatched = false
        var currentCycle = false
        var isLate = false
        var isFuture = false
        var arrayCount = 0
        var countPeriod = 0

        var isCountPeriod = false
        var avgCountPeriod = 0

        var periodCalendar: CalendarDay? = null
        if (size > 0) {
            periodCalendar = mSelectedCalendarArray[arrayCount]
        }

        val cal = getInstance(timeZone, locale)
        cal.withTime(startCalendar.time)

        val monthDifference = endCalendar.totalMonthDifference(startCalendar)

        cal.set(DAY_OF_MONTH, 1)


        (0..monthDifference).forEach { _ ->
            val totalDayInAMonth = cal.getActualMaximum(DAY_OF_MONTH)
            (1..totalDayInAMonth).forEach { _ ->

                val day = cal.get(DAY_OF_MONTH)
                var daysPretty = day.toString()
//                if (day < 10) {
//                    daysPretty = "0$daysPretty"
//                }


                val selectCalendar = CalendarDay()
                if (size > 0 && periodCalendar != null) {
                    selectCalendar.isApplyPeriodCalendar = false
                    selectCalendar.descriptionText =
                        "Predictions will be more accurate if you log your past periods."
                    val compare = simpleDateFormat.format(cal.time)

                    if (compare.equals(periodCalendar!!.periodStartDate, ignoreCase = true)) {
                        dateMatched = true
                    }
                    if (dateMatched) {
                        days++
                        selectCalendar.isApplyPeriodCalendar = true
                    }

                    selectCalendar.periodDaysCountText = "" + days
                    selectCalendar.periodDaysText = "Day $days"
                    selectCalendar.isPeriodDays = false

                    countPeriod = periodCalendar!!.periodDays

                    val lateral = periodCalendar!!.lutealPhase

                    if (isFuture) {
                        //lateral -= 1
                        countPeriod = 0
                        var periodDay = 0f
                        for (i in 0 until size) {
                            periodDay += mSelectedCalendarArray[i].periodDays
                        }

                         val period = (periodDay / size).toBigDecimal().setScale(0, RoundingMode.UP)
                         countPeriod = period.toInt()
                    }



                    if (days <= countPeriod) {
                        selectCalendar.isPeriodDays = true
                    }

                    selectCalendar.setOvulation(false)
                    selectCalendar.setOvulationRange(false)



                    if (periodCalendar!!.periodCycle - lateral - 7 <= days &&
                        periodCalendar!!.periodCycle - lateral + 2 >= days
                    ) {
                        if (periodCalendar!!.periodCycle - lateral == days) {
                            if (!selectCalendar.isPeriodDays && !selectCalendar.isLate()) {
                                selectCalendar.setOvulation(true)
                            }
                        }
                        if (!selectCalendar.isPeriodDays && !selectCalendar.isLate()) {
                            selectCalendar.setOvulationRange(true)
                        }
                    }

                    if (selectCalendar.isOvulationRange()) {
                        selectCalendar.descriptionText = "Medium chance of getting pregnant in past"
                    } else {
                        if (selectCalendar.isApplyPeriodCalendar) {
                            selectCalendar.descriptionText = "Low chance of getting pregnant"
                        }
                    }

                    if (selectCalendar.isOvulation()) {
                        selectCalendar.periodDaysText = "Ovulation"
                        selectCalendar.descriptionText = "Very high chance of getting pregnant"
                    }

                    selectCalendar.cycleText = "Current Cycle"
                    selectCalendar.isCurrentCycle = true
                    selectCalendar.isFutureCycle = false

                    if (isLate) {
                        daysLate++
                        selectCalendar.periodDaysCountText = "" + daysLate
                        var text = ""
                        text = if (daysLate == 1) "$daysLate day" else "$daysLate days"
                        selectCalendar.periodDaysText = text
                        selectCalendar.setLate(true)
                        selectCalendar.cycleText = "Late for"
                    }


                    if (periodCalendar!!.periodCycle == days) {
                        days = 0
                        if (periodCalendar!!.isLate()) {
                            isLate = true
                        } else {
                            arrayCount++
                            if (size - 1 >= arrayCount) {
                                selectCalendar.cycleText = "Current Cycle"
                                selectCalendar.isCurrentCycle = true
                                selectCalendar.isFutureCycle = false
                                periodCalendar = mSelectedCalendarArray[arrayCount]
                            } else {
                                selectCalendar.isFutureCycle = true
                                selectCalendar.isCurrentCycle = false
                                selectCalendar.cycleText = "Future Cycle"
                                isFuture = true
                            }
                        }
                    }

                    val currentDate = getInstance()
                    if (cal.time > currentDate.time) {
                        if (isLate) {
                            selectCalendar.setLate(false)
                            selectCalendar.isApplyPeriodCalendar = false
                        }
                        if (isFuture) {
                            selectCalendar.isFutureCycle = true
                            selectCalendar.isCurrentCycle = false
                            selectCalendar.cycleText = "Future Cycle"
                        }
                    }

                }

                val dayOfWeek = cal.get(DAY_OF_WEEK)

                val dateState = if (cal.isBefore(startCalendar) || cal.isAfter(endCalendar)) {
                    DateState.DISABLED
                } else {
                    DateState.WEEKDAY
                }

                when (day) {
                    1 -> {
                        calendarData.add(CalendarEntity.Month(cal.toPrettyMonthString()))
                        if (mShowDayOfWeekTitle)
                            calendarData.add(CalendarEntity.Week)

                        calendarData.addAll(createStartEmptyView(dayOfWeek))

                        calendarData.add(
                            CalendarEntity.Day(
                                daysPretty,
                                cal.toPrettyDateString(),
                                cal.time,
                                state = dateState,
                                calendarData = selectCalendar
                            )
                        )

                    }
                    totalDayInAMonth -> {

                        calendarData.add(
                            CalendarEntity.Day(
                                daysPretty,
                                cal.toPrettyDateString(),
                                cal.time,
                                state = dateState,
                                calendarData = selectCalendar
                            )

                        )

                        calendarData.addAll(createEndEmptyView(dayOfWeek))


                    }
                    else -> {

                        calendarData.add(
                            CalendarEntity.Day(
                                daysPretty,
                                cal.toPrettyDateString(),
                                cal.time,
                                state = dateState,
                                calendarData = selectCalendar
                            )
                        )
                    }


                }




                cal.add(DATE, 1)
            }
        }

        return calendarData
    }

    private fun createStartEmptyView(dayOfWeek: Int): List<CalendarEntity.Empty> {
        val numberOfEmptyView = when (dayOfWeek) {
            MONDAY -> 1
            TUESDAY -> 2
            WEDNESDAY -> 3
            THURSDAY -> 4
            FRIDAY -> 5
            SATURDAY -> 6
            else -> 0
        }

        val listEmpty = mutableListOf<CalendarEntity.Empty>()
        repeat((0 until numberOfEmptyView).count()) { listEmpty.add(CalendarEntity.Empty) }
        return listEmpty
    }

    private fun createEndEmptyView(dayOfWeek: Int): List<CalendarEntity.Empty> {
        val numberOfEmptyView = when (dayOfWeek) {
            SUNDAY -> 6
            MONDAY -> 5
            TUESDAY -> 4
            WEDNESDAY -> 3
            THURSDAY -> 2
            FRIDAY -> 1
            else -> 6
        }

        val listEmpty = mutableListOf<CalendarEntity.Empty>()
        repeat((0 until numberOfEmptyView).count()) { listEmpty.add(CalendarEntity.Empty) }
        return listEmpty
    }

    private fun onDaySelected(item: CalendarEntity.Day, position: Int) {

        when (mPickerSelectionType) {

            SelectionMode.SINGLE -> {

                if (item == mStartDateSelection?.day) return

                if (mStartDateSelection != null) {
                    mCalendarData[mStartDateSelection!!.position] =
                        mStartDateSelection!!.day.copy(selection = SelectionType.NONE)
                }
                assignAsStartDate(item, position)
            }
            else -> {

            }
        }

        calendarAdapter.submitList(mCalendarData)
    }


    private fun resetSelection() {
        val startDatePosition = mStartDateSelection?.position
        val endDatePosition = mEndDateSelection?.position

        if (startDatePosition != null && endDatePosition != null) {
            (startDatePosition..endDatePosition).forEach {
                val entity = mCalendarData[it]
                if (entity is CalendarEntity.Day)
                    mCalendarData[it] = entity.copy(selection = SelectionType.NONE)
            }
        }
        mEndDateSelection = null
    }


    private fun highlightDateBetween(
        startIndex: Int,
        endIndex: Int
    ) {
        ((startIndex + 1) until endIndex).forEach {
            val entity = mCalendarData[it]
            if (entity is CalendarEntity.Day) {
                mCalendarData[it] = entity.copy(selection = SelectionType.BETWEEN)
            }
        }
    }

    private fun assignAsStartDate(
        item: CalendarEntity.Day,
        position: Int,
        isRange: Boolean = false
    ) {
        val newItem = item.copy(selection = SelectionType.START, isRange = isRange)
        mCalendarData[position] = newItem
        mStartDateSelection = SelectedDate(newItem, position)
        if (!isRange) mOnStartSelectedListener.invoke(item.date, item.prettyLabel)
    }


    private fun getDatesBetween(
        startDate: Date, endDate: Date
    ): ArrayList<Date> {
        val datesInRange = ArrayList<Date>()
        val calendar: Calendar = GregorianCalendar()
        calendar.time = startDate
        val endCalendar: Calendar = GregorianCalendar()
        endCalendar.time = endDate

        while (calendar.before(endCalendar)) {
            val result = calendar.time
            datesInRange.add(result)
            calendar.add(DATE, 1)
        }
        return datesInRange
    }

    internal data class SelectedDate(val day: CalendarEntity.Day, val position: Int)

    enum class SelectionMode { SINGLE, RANGE, MULTI_RANGE }
}
