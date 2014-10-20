package com.jforce.caterpillarscount;

import android.widget.EditText;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import com.jforce.caterpillarscount.R;

/**
 * Created by justinforsyth on 9/14/14.
 */
public class DatePickerController implements CalendarDatePickerDialog.OnDateSetListener{

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        EditText dateET = (EditText) dialog.getActivity().findViewById(R.id.date_picker_edittext);
        dateET.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + Integer.toString(year).substring(2));
    }
}
