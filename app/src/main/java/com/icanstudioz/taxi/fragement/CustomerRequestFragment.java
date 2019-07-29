package com.icanstudioz.taxi.fragement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icanstudioz.taxi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerRequestFragment extends Fragment {


    public CustomerRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_request, container, false);
    }

}
