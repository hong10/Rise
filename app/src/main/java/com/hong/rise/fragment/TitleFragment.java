package com.hong.rise.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hong.rise.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TitleFragment extends Fragment {

    private ImageView ivMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_title, container, false);

        ivMenu = (ImageView) view.findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "I am ImageButton in TitleFragment", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
