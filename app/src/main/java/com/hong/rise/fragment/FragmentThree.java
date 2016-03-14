package com.hong.rise.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hong.rise.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThree extends Fragment {

    private Button btFragmentThree;

    public FragmentThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_three, container, false);
        btFragmentThree = (Button) view.findViewById(R.id.bt_fragment_three);
        btFragmentThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "fragment three", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


}
