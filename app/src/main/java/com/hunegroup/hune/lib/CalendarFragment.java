//package com.hunegroup.hune.lib;
//
//import android.app.Dialog;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.hunegroup.hune.R;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created by apple on 12/12/17.
// */
//
//public class CalendarFragment extends DialogFragment {
//
//    private final String TAG = CalendarFragment.class.getSimpleName();
//
//    private static CalendarFragment instance;
//    private View view;
//    private ImageView ivDecrementMonth, ivIncrementMonth;
//    private TextView btnOk, btnCancel;
//    public TextView tvMonthYear;
//    private int counter = 0;
//    private int year = 2017;
//    private static CalendarActivity INSTANCE;
//
//    private Calendar calendarMinimumDate, calendarMaximumDate;
//
//    private GridView gridCalendarView;
//    private ArrayList<EventDateSelectionBean> listEventDates;
//    private String from;
//
//    public static final String KEY_EVENT_DATES = "key event dates";
//    public static final String KEY_FROM = "key from";
//
//    public static final String FROM_ANNOUNCEMENT_ACTIVITY = "from announcement activity";
//    public static final String FROM_BOOKING_DIALOG = "from booking dialog";
//    private List<String> listMonths;
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
//        wlmp.gravity = Gravity.CENTER | Gravity.FILL_HORIZONTAL;
//        wlmp.dimAmount = 0.0F;
//        dialog.getWindow().setAttributes(wlmp);
//        dialog.getWindow().setWindowAnimations(R.style.DialogTheme);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        return dialog;
//    }
////End of onCreateDialog()
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (view == null) {
//            instance = this;
//            view = inflater.inflate(R.layout.activity_calendar, container);
//            findViewsId();
//            setData();
//            setClickListeners();
//        }
//
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//
//
//        return view;
//    }
////End of onCreateView()
//
//    private void findViewsId() {
//
//        ivDecrementMonth = (ImageView) view.findViewById(R.id.ivDecrementMonth);
//        ivIncrementMonth = (ImageView) view.findViewById(R.id.ivIncrementMonth);
//        gridCalendarView = (GridView) view.findViewById(R.id.calendar);
//        tvMonthYear = (TextView) view.findViewById(R.id.tvMonthYear);
//        btnOk = (TextView) view.findViewById(R.id.btnOk);
//        btnCancel = (TextView) view.findViewById(R.id.btnCancel);
//
//    }
////End of findViewsId()
//
//    private void setData() {
//        listMonths = Arrays.asList(getResources().getStringArray(R.array.months));
//
//        Bundle bundle = getArguments();
//
//        calendarMinimumDate = Calendar.getInstance();
//        calendarMinimumDate.setTimeInMillis(System.currentTimeMillis());
//        calendarMaximumDate = Calendar.getInstance();
//        calendarMaximumDate.setTimeInMillis(System.currentTimeMillis());
//
//        btnOk.setText(getString(R.string.ok));
//        btnCancel.setText(getString(R.string.cancel));
//
//        listEventDates = bundle.getParcelableArrayList(KEY_EVENT_DATES);
//        from = bundle.getString(KEY_FROM);
//
//        //setMinMaxDateCalendar();
//
//        counter = calendarMinimumDate.get(Calendar.MONTH);
//        year = calendarMinimumDate.get(Calendar.YEAR);
//        tvMonthYear.setText(listMonths.get(counter) + " " + year);
//
//        MyCalendarAdapter adapter = new MyCalendarAdapter(GoToTourActivity.getInstance(),
//                listEventDates, counter, year);
//        gridCalendarView.setAdapter(adapter);
//    }
////End of setData()
//
//    private void setClickListeners() {
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (from.equals(FROM_ANNOUNCEMENT_ACTIVITY)) {
//                    GoToTourActivity.getInstance().setDate(listEventDates);
//                } else if (from.equals(FROM_BOOKING_DIALOG)) {
//                    BookDialogs.getInstance().setDate(listEventDates);
//                }
//                dismiss();
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
//
//        ivDecrementMonth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (counter == 0) {
//                    counter = listMonths.size() - 1;
//                    year = year - 1;
//                } else {
//                    counter--;
//                }
//                tvMonthYear.setText(listMonths.get(counter) + " " + year);
//                updateCalendar();
//            }
//        });
//
//        ivIncrementMonth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (counter == (listMonths.size() - 1)) {
//                    counter = 0;
//                    year = year + 1;
//                } else {
//                    counter++;
//                }
//                tvMonthYear.setText(listMonths.get(counter) + " " + year);
//                updateCalendar();
//            }
//        });
//    }
////End of setClickListeners()
//
//    private void updateCalendar() {
//        MyCalendarAdapter adapter = new MyCalendarAdapter(GoToTourActivity.getInstance(),
//                listEventDates, counter, year);
//        gridCalendarView.setAdapter(adapter);
//    }
//}