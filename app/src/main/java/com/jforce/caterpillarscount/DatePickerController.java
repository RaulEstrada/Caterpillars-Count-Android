package com.jforce.caterpillarscount;

import android.widget.EditText;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import com.jforce.caterpillarscount.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by justinforsyth on 9/14/14.
 */
public class DatePickerController implements CalendarDatePickerDialog.OnDateSetListener{

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        EditText dateET = (EditText) dialog.getActivity().findViewById(R.id.date_picker_edittext);

        HomeActivity homeActivity = (HomeActivity) dialog.getActivity();

        GregorianCalendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat dateFormatLong = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(cal.getTime());
        homeActivity.setDateLongFormat(dateFormatLong.format(cal.getTime()));

        dateET.setText(date);
    }
}
