package com.trivtech.insight.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.trivtech.insight.R;
import com.trivtech.insight.util.Utils;

import java.util.ArrayList;

public class DictionaryAdapter extends BaseAdapter {
    Context context;
    ArrayList<Integer> list;

    public DictionaryAdapter(Context context, ArrayList<Integer> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.dictionary_row, null);
        ImageView image = row.findViewById(R.id.dictionary_image);

        //Style
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) image.getLayoutParams();
        params.setMargins(0, Utils.getScreenHeightPercent(.02), 0, Utils.getScreenHeightPercent(.02));
        params.height = Utils.getScreenHeightPercent(.75);
        params.width = Utils.getScreenWidthPercent(.85);
        image.setLayoutParams(params);

        Glide.with(context)
                .asDrawable()
                .load(list.get(position))
                .into(image);

        return row;
    }
}
