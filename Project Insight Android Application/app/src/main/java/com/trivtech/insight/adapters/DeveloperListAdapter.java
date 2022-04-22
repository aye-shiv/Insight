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

public class DeveloperListAdapter extends RecyclerView.Adapter<DeveloperListAdapter.ViewHolder> {

    List<Developer> list;

    public DeveloperListAdapter(List<Developer> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_developerlist_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Developer developer = list.get(position);

        holder.name.setText(developer.name);
        holder.id.setText(developer.id);
        holder.role.setText(developer.role);

        holder.containerExpanded.setVisibility(developer.expanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout container, containerExpanded;

        public TextView name;
        public TextView id;
        public TextView role;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.about_developerlist_container);
            containerExpanded = itemView.findViewById(R.id.about_developerlist_containerExpanded);

            name = itemView.findViewById(R.id.about_developerlist_name);
            id = itemView.findViewById(R.id.about_developerlist_id_text);
            role = itemView.findViewById(R.id.about_developerlist_role_text);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Developer developer = list.get(getAbsoluteAdapterPosition());
                    developer.expanded = !developer.expanded;
                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            });
        }
    }

    //======================================

    public static class Developer {
        private boolean expanded = false;
        public Developer(){ }

        public String name = "<NAME>";
        public String id = "<STUDENT ID>";
        public String role = "<ROLE>";
    }
}
