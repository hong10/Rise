package com.hong.rise.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hong.rise.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {
    private Button btOpenThree;
    private FrTwoBtListener frTwoBtListener;


    public FragmentTwo() {
        // Required empty public constructor
    }

    public interface FrTwoBtListener {
        void onFrTwoBtClick();
    }

    public void setFrTwoBtListener(FrTwoBtListener frTwoBtListener) {
        this.frTwoBtListener = frTwoBtListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_two, container, false);
        btOpenThree = (Button) view.findViewById(R.id.bt_open_fragment_three);

        btOpenThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragmentThree = new FragmentThree();
                transaction.replace(R.id.fl_content, fragmentThree, "three");
                transaction.addToBackStack(null);
                transaction.commit();
                */

                //与activity解耦
                if (frTwoBtListener != null) {
                    frTwoBtListener.onFrTwoBtClick();
                }
            }
        });

        return view;
    }


}
