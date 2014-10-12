package jforce.com.caterpillarcount;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by justinforsyth on 9/15/14.
 */
public class OrderSpinnerController implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        OrderActivity activity = (OrderActivity) view.getContext();
        String order = (String) parent.getItemAtPosition(pos);

        //do something
        activity.setOrder(order);



    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
