package com.trivtech.insight.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.trivtech.insight.R;

import java.util.List;

public class FeatureListAdapter extends RecyclerView.Adapter<FeatureListAdapter.ViewHolder> {

    List<Feature> list;

    public FeatureListAdapter(List<Feature> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_featurelist_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Feature developer = list.get(position);

        holder.name.setText(developer.name);
        holder.description.setText(developer.description);

        holder.containerExpanded.setVisibility(developer.expanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout container, containerExpanded;

        public TextView name;
        public TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.about_featurelist_container);
            containerExpanded = itemView.findViewById(R.id.about_featurelist_containerExpanded);

            name = itemView.findViewById(R.id.about_featurelist_name);
            description = itemView.findViewById(R.id.about_featurelist_description);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Feature developer = list.get(getAbsoluteAdapterPosition());
                    developer.expanded = !developer.expanded;
                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            });
        }
    }

    //======================================

    public static class Feature {
        private boolean expanded = false;
        public Feature(){ }

        public String name = "<Feature>";
        public String description = "I am a description";
    }
}
