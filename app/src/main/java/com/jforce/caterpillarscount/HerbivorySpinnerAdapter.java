package com.jforce.caterpillarscount;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by justinforsyth on 11/11/14.
 */
public class HerbivorySpinnerAdapter extends ArrayAdapter<String>{

    private Context context;
    private String[] strings;


    public HerbivorySpinnerAdapter(Context context) {
        super(context, R.layout.spinner_item_herbivory_none, context.getResources().getStringArray(R.array.herbivory_array));
        this.strings = context.getResources().getStringArray(R.array.herbivory_array);
        this.context = context;


    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);

    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        return getCustomView(pos, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView;


        if(position == 0){
            rowView = inflater.inflate(R.layout.spinner_item_herbivory_none, parent, false);

        }
        else{
            rowView = inflater.inflate(R.layout.spinner_item_herbivory, parent, false);

            TypedArray leftImageResources = context.getResources().obtainTypedArray(R.array.herbivory_array_images_left);
            TypedArray rightImageResources = context.getResources().obtainTypedArray(R.array.herbivory_array_images_right);

            TextView tv = (TextView) rowView.findViewById(R.id.herbivory_text1);
            tv.setText(strings[position]);

            ImageView left = (ImageView) rowView.findViewById(R.id.herbivory_spinner_left);
            ImageView right = (ImageView) rowView.findViewById(R.id.herbivory_spinner_right);

//            left.setImageResource(leftImageResources[position - 1]);
//            right.setImageResource(rightImageResources[position - 1]);

            Drawable dLeft = context.getResources().getDrawable(leftImageResources.getResourceId(position - 1, -1));
            Drawable dRight = context.getResources().getDrawable(rightImageResources.getResourceId(position - 1, -1));

            left.setBackgroundDrawable(dLeft);
            right.setBackgroundDrawable(dRight);

        }





        return rowView;




    }







}
