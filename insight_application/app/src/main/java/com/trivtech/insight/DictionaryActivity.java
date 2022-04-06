package com.trivtech.insight;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class DictionaryActivity extends AppCompatActivity {
    public static String logTag = "DictionaryActivity";

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        Utils.changeStatusBarColor(this, Color.BLACK);

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            Utils.nextActivity(this, MainActivity.class);
        });

        ArrayList<Integer> signs = new ArrayList<>();
        addSigns(signs);

        list = findViewById(R.id.dictionary_list);
        list.setAdapter(new DictionaryAdapter(this, signs));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) list.getLayoutParams();
        //params.setMargins(0, 0, 0, Utils.getScreenHeightPercent(.01));
        params.height = Utils.getScreenHeightPercent(.85);
        list.setLayoutParams(params);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Utils.nextActivity(this, MainActivity.class);
    }


    public void addSigns(ArrayList<Integer> list){

        list.add(R.drawable.dictionary_letter_a);
        list.add(R.drawable.dictionary_letter_b);
        list.add(R.drawable.dictionary_letter_c);
        list.add(R.drawable.dictionary_letter_d);
        list.add(R.drawable.dictionary_letter_e);
        list.add(R.drawable.dictionary_letter_f);
        list.add(R.drawable.dictionary_letter_g);
        list.add(R.drawable.dictionary_letter_h);
        list.add(R.drawable.dictionary_letter_i);
        list.add(R.drawable.dictionary_letter_j);
        list.add(R.drawable.dictionary_letter_k);
        list.add(R.drawable.dictionary_letter_l);
        list.add(R.drawable.dictionary_letter_m);
        list.add(R.drawable.dictionary_letter_n);
        list.add(R.drawable.dictionary_letter_o);
        list.add(R.drawable.dictionary_letter_p);
        list.add(R.drawable.dictionary_letter_q);
        list.add(R.drawable.dictionary_letter_r);
        list.add(R.drawable.dictionary_letter_s);
        list.add(R.drawable.dictionary_letter_t);
        list.add(R.drawable.dictionary_letter_u);
        list.add(R.drawable.dictionary_letter_v);
        list.add(R.drawable.dictionary_letter_w);
        list.add(R.drawable.dictionary_letter_x);
        list.add(R.drawable.dictionary_letter_y);
        list.add(R.drawable.dictionary_letter_z);

        list.add(R.drawable.dictionary_number_1);
        list.add(R.drawable.dictionary_number_2);
        list.add(R.drawable.dictionary_number_3);
        list.add(R.drawable.dictionary_number_4);
        list.add(R.drawable.dictionary_number_5);
        list.add(R.drawable.dictionary_number_6);
        list.add(R.drawable.dictionary_number_7);
        list.add(R.drawable.dictionary_number_8);
        list.add(R.drawable.dictionary_number_9);

    }


}