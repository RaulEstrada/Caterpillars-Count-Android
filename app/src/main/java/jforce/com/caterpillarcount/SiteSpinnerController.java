package jforce.com.caterpillarcount;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by justinforsyth on 9/3/14.
 */
public class SiteSpinnerController implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
//        Toast toast = Toast.makeText(view.getContext(), (String) parent.getItemAtPosition(pos), Toast.LENGTH_SHORT);
//        toast.show();

        HomeActivity activity = (HomeActivity) view.getContext();
        activity.setSite((String) parent.getItemAtPosition(pos));




    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
