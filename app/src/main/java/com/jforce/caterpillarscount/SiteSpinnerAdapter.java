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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justinforsyth on 12/5/14.
 */
public class SiteSpinnerAdapter  extends ArrayAdapter<Site> {


    private Context context;
    private ArrayList<Site> sites;


    public SiteSpinnerAdapter(Context context, ArrayList<Site> sites) {
        super(context, android.R.layout.simple_spinner_item, sites);

        this.sites = sites;
        this.context = context;


    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        TextView tv = (TextView) rowView.findViewById(android.R.id.text1);
        tv.setText(sites.get(position).getName());

        return rowView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);

        TextView tv = (TextView) rowView.findViewById(android.R.id.text1);
        tv.setText(sites.get(position).getName());

        return rowView;
    }






}
