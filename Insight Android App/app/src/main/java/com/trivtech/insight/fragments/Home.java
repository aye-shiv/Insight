package com.trivtech.insight.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trivtech.insight.R;
import com.trivtech.insight.activities.DictionaryActivity;
import com.trivtech.insight.util.Utils;

public class Home extends Fragment {

    public Home() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view.findViewById(R.id.dictionaryButton).setOnClickListener(v -> {
            Utils.nextActivity((AppCompatActivity) requireActivity(), DictionaryActivity.class);
        });

        ImageView logo = view.findViewById(R.id.logo);
        TextView s = view.findViewById(R.id.title_S);

        ConstraintLayout.LayoutParams params;
        params = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
        params.height = Utils.getScreenHeightPercent(0.45);
        params.width = Utils.getScreenWidthPercent(0.75);
        logo.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) s.getLayoutParams();
        params.setMarginEnd(Utils.getScreenWidthPercent(0.1));
        s.setLayoutParams(params);

        return view;
    }

}