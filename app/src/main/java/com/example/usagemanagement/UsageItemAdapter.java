package com.example.usagemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class UsageItemAdapter extends RecyclerView.Adapter<UsageItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<Usage> mUsageData;
    private ArrayList<Usage> mUsageDataAll;
    private Context mContext;
    private int lastPosition = -1;

    UsageItemAdapter(Context context, ArrayList<Usage> usageData) {
        this.mUsageData = usageData;
        this.mUsageDataAll = usageData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_usage, parent, false));
    }

    @Override
    public void onBindViewHolder(UsageItemAdapter.ViewHolder holder, int position) {
        Usage currentUsage = mUsageData.get(position);

        holder.bindTo(currentUsage);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mUsageData.size();
    }

    @Override
    public Filter getFilter() {
        return UsageFilter;
    }

    private Filter UsageFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Usage> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.count = mUsageDataAll.size();
                results.values = mUsageDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Usage item : mUsageDataAll) {
                    if (item.getUsageType().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUsageData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsageType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mUsageType = itemView.findViewById(R.id.usagetype);
        }

        public void bindTo(@NonNull Usage currentUsage) {
            mUsageType.setText(currentUsage.getUsageType());

            itemView.findViewById(R.id.information).setOnClickListener(view -> ((UsageListActivity)mContext).moreInformation(currentUsage));

        }
    }
}


