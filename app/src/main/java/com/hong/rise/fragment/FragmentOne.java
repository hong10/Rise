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
public class FragmentOne extends Fragment {


    private Button btOpenTwo;
    private FrOneBtListener frOneBtListener;

    public void setFrOneBtListener(FrOneBtListener frOneBtListener) {
        this.frOneBtListener = frOneBtListener;
    }

    public interface FrOneBtListener {
        void onFrOneBtClick();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_one, container, false);

        btOpenTwo = (Button) view.findViewById(R.id.bt_open_fragment_two);
        btOpenTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragmentTwo = new FragmentTwo();
                transaction.replace(R.id.fl_content, fragmentTwo, "two");
                transaction.addToBackStack(null);
                transaction.commit();*/

                //为了让FragmentOne和activity解耦，所以在FragmemtOne暴露一个接口，
                //这接口中暴露一个button点击的方法，供实现接口的activity实现；
                /*if (getActivity() instanceof FrOneBtListener) {
                    ((FrOneBtListener) getActivity()).onFrOneBtClick();
                }*/

                if (frOneBtListener != null) {
                    frOneBtListener.onFrOneBtClick();
                }

            }
        });

        return view;
    }


}
