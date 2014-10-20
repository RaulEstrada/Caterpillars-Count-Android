package com.jforce.caterpillarscount;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jforce.caterpillarscount.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class OrderCardFragment extends Fragment {


    public OrderCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ordercards, container, false);
    }


}
