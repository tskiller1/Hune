//package com.hunegroup.hune.lib;
//
//import android.content.Context;
//
//import com.hunegroup.hune.R;
//
//import java.util.ArrayList;
//
///**
// * Created by apple on 12/12/17.
// */
//
//public class MyCalendarAdapter {
//    public MyCalendarAdapter(Context context, ArrayList<EventDateSelectionBean> listEvents, int month, int year) {
//        super();
//        this._context = context;
//        dateDefaultTextColor = context.getResources().getColor(R.color.calendar_date_default_text_color);
//        dateInEventTextColor = context.getResources().getColor(R.color.calendar_date_in_event_text_color);
//        dateInEventSelectedTextColor = context.getResources().getColor(R.color.calendar_date_in_event_selected_text_color);
//
//        this.list = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
//        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
//        this.listEvents = listEvents;
//
//        currentMonth = month;
//        currentYear = year;
//
//        // Print Month
//        printMonth(month, year);
//
//        eventCalendar = Calendar.getInstance();
//        eventCalendar.setTimeInMillis(System.currentTimeMillis());
//        currentDayCalendar = Calendar.getInstance();
//        currentDayCalendar.setTimeInMillis(System.currentTimeMillis());
//
//        currentDayCalendar.set(Calendar.MONTH, currentMonth);
//        currentDayCalendar.set(Calendar.YEAR, currentYear);
//    }
//
//}
