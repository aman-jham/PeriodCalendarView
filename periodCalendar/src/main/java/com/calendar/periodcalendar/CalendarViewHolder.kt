package com.calendar.periodcalendar

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.calendar_day_view.view.*
import kotlinx.android.synthetic.main.calendar_month_view.view.*
import kotlinx.android.synthetic.main.calendar_week_view.view.*
import java.text.DateFormatSymbols

internal abstract class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit)
}

internal class MonthViewHolder(private val view: View) : CalendarViewHolder(view) {
    private val name by lazy { view.vMonthName }

    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
        if (item is CalendarEntity.Month) {
            name.text = item.label
        }
    }
}

internal class WeekViewHolder(private val view: View) : CalendarViewHolder(view) {

    private val parentContainer by lazy { view.parent_container }

    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
        val dateFormat = DateFormatSymbols().shortWeekdays
        (1 until dateFormat.size).forEach {
            (parentContainer.getChildAt(it - 1) as TextView).text = dateFormat[it]
        }
    }
}

internal class DayViewHolder(view: View) : CalendarViewHolder(view) {
    private val name by lazy { view.vDayName }
    private var outerCircle = view.outerCircle
    private var innerCircle = view.innerCircle
    private var rvCycle = view.rvCycle
    private var mView = view.mView
    private var mViewDot = view.mViewDot

    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {

        if (item is CalendarEntity.Day) {
            name.text = item.label
            name.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.inside_text
                )
            )
            when (item.selection) {
                SelectionType.START -> {
                    name.select()
                }

                SelectionType.NONE -> {
                    name.deselect()
                }
            }

            if (item.state != DateState.DISABLED) {
                itemView.setOnClickListener {
                    actionListener.invoke(
                        item,
                        adapterPosition
                    )
                }
            } else {
                itemView.setOnClickListener(null)
            }


            val calendarDay = item.calendarData
            if (calendarDay != null) {
                if (calendarDay.isApplyPeriodCalendar) {


                    rvCycle.text = calendarDay.periodDaysCountText
                    rvCycle.visibility = View.VISIBLE
                    innerCircle.visibility = View.VISIBLE

                    if (calendarDay.isLate) {

                        innerCircle.background =
                            ContextCompat.getDrawable(itemView.context, R.drawable.full_circle_late)

                        rvCycle.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.white
                            )
                        )

                        outerCircle.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.transparent_circle
                        )

                        mView.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.late_color
                            )
                        )
                        mView.visibility = View.VISIBLE

                    } else {
                        if (calendarDay.isPeriodDays) {

                            innerCircle.background =
                                ContextCompat.getDrawable(itemView.context, R.drawable.full_circle)
                            rvCycle.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.white
                                )
                            )


                            name.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.calendar_day_selected_bg
                                )
                            )


                            when {
                                calendarDay.isCurrentCycle -> {
                                    mView.setBackgroundColor(
                                        ContextCompat.getColor(
                                            itemView.context,
                                            R.color.calendar_day_selected_bg
                                        )
                                    )
                                    mView.visibility = View.VISIBLE


                                }
                                calendarDay.isFutureCycle -> {
                                    mView.background = ContextCompat.getDrawable(
                                        itemView.context,
                                        R.drawable.dotted_line
                                    )
                                    mView.visibility = View.VISIBLE

                                }
                                calendarDay.isLate -> {
                                    mView.setBackgroundColor(
                                        ContextCompat.getColor(
                                            itemView.context,
                                            android.R.color.transparent
                                        )
                                    )
                                    mView.visibility = View.VISIBLE


                                }

                                else -> {
                                    mView.setBackgroundColor(
                                        ContextCompat.getColor(
                                            itemView.context,
                                            android.R.color.transparent
                                        )
                                    )
                                    mView.visibility = View.VISIBLE
                                }
                            }


                            outerCircle.background = ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.transparent_circle
                            )

                        } else {

                            if (calendarDay.isOvulationRange) {
                                when {
                                    calendarDay.isCurrentCycle && calendarDay.isOvulation -> {

                                        innerCircle.background = ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.full_circle_ovulation
                                        )

                                        outerCircle.background = ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.border_circle
                                        )

                                        rvCycle.setTextColor(
                                            ContextCompat.getColor(
                                                itemView.context,
                                                R.color.white
                                            )
                                        )

                                        mView.setBackgroundColor(
                                            ContextCompat.getColor(
                                                itemView.context,
                                                android.R.color.transparent
                                            )
                                        )
                                        mView.visibility = View.GONE
                                    }
                                    calendarDay.isFutureCycle && calendarDay.isOvulation -> {
                                        innerCircle.background = ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.full_circle_ovulation
                                        )

                                        outerCircle.background = ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.dotted_circle
                                        )

                                        rvCycle.setTextColor(
                                            ContextCompat.getColor(
                                                itemView.context,
                                                R.color.white
                                            )
                                        )

                                        mView.setBackgroundColor(
                                            ContextCompat.getColor(
                                                itemView.context,
                                                android.R.color.transparent
                                            )
                                        )
                                        mView.visibility = View.GONE
                                    }
                                    calendarDay.isLate -> {
                                        mView.background = ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.dotted_line
                                        )
                                        mView.visibility = View.VISIBLE

                                    }
                                    else -> {
                                        innerCircle.background = ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.transparent_circle
                                        )

                                        outerCircle.background = ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.transparent_circle
                                        )

                                        rvCycle.setTextColor(
                                            ContextCompat.getColor(
                                                itemView.context,
                                                R.color.inside_text
                                            )
                                        )

                                        mView.setBackgroundColor(
                                            ContextCompat.getColor(
                                                itemView.context,
                                                android.R.color.transparent
                                            )
                                        )
                                        mView.visibility = View.GONE

                                        name.setTextColor(
                                            ContextCompat.getColor(
                                                itemView.context,
                                                R.color.ovulation_color
                                            )
                                        )
                                    }
                                }
                            } else {

                                innerCircle.background = ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.transparent_circle
                                )

                                innerCircle.background = ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.transparent_circle
                                )

                                outerCircle.background = ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.transparent_circle
                                )

                                rvCycle.setTextColor(
                                    ContextCompat.getColor(
                                        itemView.context,
                                        R.color.inside_text
                                    )
                                )

                                mView.setBackgroundColor(
                                    ContextCompat.getColor(
                                        itemView.context,
                                        android.R.color.transparent
                                    )
                                )
                                mView.visibility = View.GONE
                                name.setTextColor(
                                    ContextCompat.getColor(
                                        itemView.context,
                                        R.color.inside_text
                                    )
                                )
                            }
                        }
                    }
                } else {
                    rvCycle.visibility = View.GONE
                    innerCircle.visibility = View.GONE
                    mView.visibility = View.GONE
                    outerCircle.background = ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.transparent_circle
                    )
                    mView.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            android.R.color.transparent
                        )
                    )
                }
            } else {
                rvCycle.visibility = View.GONE
                innerCircle.visibility = View.GONE
                mView.visibility = View.GONE
                outerCircle.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.transparent_circle
                )
                mView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        android.R.color.transparent
                    )
                )
            }
        }
    }
}


private fun View.select() {
    val drawable = ContextCompat.getDrawable(context, R.drawable.selected_day_bg)
    background = drawable
}

private fun View.deselect() {
    background = null
}

private fun View.dehighlight() {
    val color = ContextCompat.getColor(context, R.color.calendar_day_unselected_bg)
    setBackgroundColor(color)
}

private fun View.highlight() {
    val color = ContextCompat.getColor(context, R.color.white)
    setBackgroundColor(color)
}


internal class EmptyViewHolder(view: View) : CalendarViewHolder(view) {
    override fun onBind(item: CalendarEntity, actionListener: (CalendarEntity, Int) -> Unit) {
    }
}