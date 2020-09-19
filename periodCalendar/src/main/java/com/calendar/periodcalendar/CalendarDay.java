package com.calendar.periodcalendar;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public final class CalendarDay implements Serializable, Comparable<CalendarDay> {

    private static final long serialVersionUID = 141315161718191143L;

    boolean isLate = false;
    boolean isOvulation = false;
    boolean isOvulationRange = false;
    private String periodDaysCountText = "";
    private String periodDaysText = "";
    private String cycleText = "";
    private String descriptionText = "";
    private int periodDays = 0;
    private String periodStartDate = "";
    private String periodEndDate = "";
    private int periodCycle = 0;
    private int lutealPhase = 0;
    private boolean isCurrentCycle = false;
    private boolean isFutureCycle = false;
    private boolean isManualInputFromCalendar = false;
    private boolean isPeriodDays = false;
    private boolean isApplyPeriodCalendar = false;


    public boolean isManualInputFromCalendar() {
        return isManualInputFromCalendar;
    }

    public void setManualInputFromCalendar(boolean manualInputFromCalendar) {
        isManualInputFromCalendar = manualInputFromCalendar;
    }


    public boolean isCurrentCycle() {
        return isCurrentCycle;
    }

    public void setCurrentCycle(boolean currentCycle) {
        isCurrentCycle = currentCycle;
    }

    public boolean isFutureCycle() {
        return isFutureCycle;
    }

    public void setFutureCycle(boolean futureCycle) {
        isFutureCycle = futureCycle;
    }


    private int year = 0;
    private int month = 0;
    private int leapMonth = 0;
    private int day = 0;
    private boolean isLeapYear = false;
    private boolean isCurrentMonth = false;
    private boolean isCurrentDay = false;
    private String lunar = "";
    private String solarTerm = "";
    private String gregorianFestival = "";
    private String traditionFestival = "";
    private String scheme = "";
    private int schemeColor = 0;
    private List<Scheme> schemes;
    private boolean isWeekend = false;
    private int week = 0;
    private CalendarDay lunarCalendar = null;

    public String getPeriodDaysCountText() {
        return periodDaysCountText;
    }

    public void setPeriodDaysCountText(String periodDaysCountText) {
        this.periodDaysCountText = periodDaysCountText;
    }

    public String getPeriodDaysText() {
        return periodDaysText;
    }

    public void setPeriodDaysText(String periodDaysText) {
        this.periodDaysText = periodDaysText;
    }

    public boolean isOvulation() {
        return isOvulation;
    }

    public void setOvulation(boolean ovulation) {
        isOvulation = ovulation;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    public boolean isOvulationRange() {
        return isOvulationRange;
    }

    public void setOvulationRange(boolean ovulationRange) {
        isOvulationRange = ovulationRange;
    }

    public String getCycleText() {
        return cycleText;
    }

    public void setCycleText(String cycleText) {
        this.cycleText = cycleText;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public boolean isPeriodDays() {
        return isPeriodDays;
    }

    public boolean isApplyPeriodCalendar() {
        return isApplyPeriodCalendar;
    }

    public void setApplyPeriodCalendar(boolean applyPeriodCalendar) {
        isApplyPeriodCalendar = applyPeriodCalendar;
    }

    public int getPeriodDays() {
        return periodDays;
    }

    public void setPeriodDays(boolean periodDays) {
        isPeriodDays = periodDays;
    }

    public void setPeriodDays(int periodDays) {
        this.periodDays = periodDays;
    }

    public String getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(String periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public int getPeriodCycle() {
        return periodCycle;
    }

    public void setPeriodCycle(int periodCycle) {
        this.periodCycle = periodCycle;
    }

    public String getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(String periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public int getLutealPhase() {
        return lutealPhase;
    }

    public void setLutealPhase(int lutealPhase) {
        this.lutealPhase = lutealPhase;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }


    public void setCurrentMonth(boolean currentMonth) {
        this.isCurrentMonth = currentMonth;
    }

    public boolean isCurrentDay() {
        return isCurrentDay;
    }

    public void setCurrentDay(boolean currentDay) {
        isCurrentDay = currentDay;
    }


    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }


    public String getScheme() {
        return scheme;
    }


    public void setScheme(String scheme) {
        this.scheme = scheme;
    }


    public int getSchemeColor() {
        return schemeColor;
    }

    public void setSchemeColor(int schemeColor) {
        this.schemeColor = schemeColor;
    }


    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }


    public void addScheme(Scheme scheme) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(scheme);
    }

    public void addScheme(int schemeColor, String scheme) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(new Scheme(schemeColor, scheme));
    }

    public void addScheme(int type, int schemeColor, String scheme) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(new Scheme(type, schemeColor, scheme));
    }

    public void addScheme(int type, int schemeColor, String scheme, String other) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(new Scheme(type, schemeColor, scheme, other));
    }

    public void addScheme(int schemeColor, String scheme, String other) {
        if (schemes == null) {
            schemes = new ArrayList<>();
        }
        schemes.add(new Scheme(schemeColor, scheme, other));
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public CalendarDay getLunarCalendar() {
        return lunarCalendar;
    }

    public void setLunarCalendar(CalendarDay lunarCakendar) {
        this.lunarCalendar = lunarCakendar;
    }

    public String getSolarTerm() {
        return solarTerm;
    }

    public void setSolarTerm(String solarTerm) {
        this.solarTerm = solarTerm;
    }

    public String getGregorianFestival() {
        return gregorianFestival;
    }

    public void setGregorianFestival(String gregorianFestival) {
        this.gregorianFestival = gregorianFestival;
    }


    public int getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(int leapMonth) {
        this.leapMonth = leapMonth;
    }

    public boolean isLeapYear() {
        return isLeapYear;
    }

    public void setLeapYear(boolean leapYear) {
        isLeapYear = leapYear;
    }

    public String getTraditionFestival() {
        return traditionFestival;
    }

    public void setTraditionFestival(String traditionFestival) {
        this.traditionFestival = traditionFestival;
    }

    public boolean hasScheme() {
        if (schemes != null && schemes.size() != 0) {
            return true;
        }
        if (!TextUtils.isEmpty(scheme)) {
            return true;
        }
        return false;
    }

    public boolean isSameMonth(CalendarDay calendar) {
        return year == calendar.getYear() && month == calendar.getMonth();
    }

    public int compareTo(CalendarDay calendar) {
        if (calendar == null) {
            return 1;
        }
        return toString().compareTo(calendar.toString());
    }

    public final int differ(CalendarDay calendar) {
        return CalendarUtilsKt.differ(this, calendar);
    }

    public boolean isAvailable() {
        return year > 0 & month > 0 & day > 0 & day <= 31 & month <= 12 & year >= 1900 & year <= 2099;
    }

    public long getTimeInMillis() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof CalendarDay) {
            if (((CalendarDay) o).getYear() == year && ((CalendarDay) o).getMonth() == month && ((CalendarDay) o).getDay() == day)
                return true;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + " cycle " + cycleText + " " + periodDaysText + " periodCycle " + periodCycle;
    }

//    @Override
//    public int compare(Calendar lhs, Calendar rhs) {
//        if (lhs == null || rhs == null) {
//            return 0;
//        }
//        int result = lhs.compareTo(rhs);
//        return result;
//    }

    final void mergeScheme(CalendarDay calendar, String defaultScheme) {
        if (calendar == null)
            return;
        setScheme(TextUtils.isEmpty(calendar.getScheme()) ?
                defaultScheme : calendar.getScheme());
        setSchemeColor(calendar.getSchemeColor());
        setSchemes(calendar.getSchemes());
    }

    final void clearScheme() {
        setScheme("");
        setSchemeColor(0);
        setSchemes(null);
    }

    public final static class Scheme implements Serializable {
        private int type;
        private int shcemeColor;
        private String scheme;
        private String other;
        private Object obj;

        public Scheme() {
        }

        public Scheme(int type, int shcemeColor, String scheme, String other) {
            this.type = type;
            this.shcemeColor = shcemeColor;
            this.scheme = scheme;
            this.other = other;
        }

        public Scheme(int type, int shcemeColor, String scheme) {
            this.type = type;
            this.shcemeColor = shcemeColor;
            this.scheme = scheme;
        }

        public Scheme(int shcemeColor, String scheme) {
            this.shcemeColor = shcemeColor;
            this.scheme = scheme;
        }

        public Scheme(int shcemeColor, String scheme, String other) {
            this.shcemeColor = shcemeColor;
            this.scheme = scheme;
            this.other = other;
        }

        public int getShcemeColor() {
            return shcemeColor;
        }

        public void setShcemeColor(int shcemeColor) {
            this.shcemeColor = shcemeColor;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }
    }
}
