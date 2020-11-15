package com.example.cycleurbike.ClassHelpers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cycleurbike.ClassHelpers.Route;
import com.example.cycleurbike.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class RoutesProfileAdapter extends RecyclerView.Adapter<RoutesProfileAdapter.RoutesProfileViewHolder> {


    Context context;
    ArrayList<Route> routes;


    public RoutesProfileAdapter(Context context, ArrayList<Route> routes) {
        this.context = context;
        this.routes = routes;
    }


    @NonNull
    @Override
    public RoutesProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_item_profile_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new RoutesProfileViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull final RoutesProfileViewHolder holder, int position) {


        holder.totalDistanceTextView.setText(routes.get(holder.getAdapterPosition()).getDistance());
        holder.totalTimeTextView.setText(routes.get(holder.getAdapterPosition()).getTime());
        holder.dateTextView.setText(routes.get(holder.getAdapterPosition()).getDateOfRoute());
        holder.startTimeTextView.setText(routes.get(holder.getAdapterPosition()).getHourOfRoute());
        holder.avgSpeedTextView.setText(routes.get(holder.getAdapterPosition()).getAvgSpeed());


        Picasso.with(context).load(routes.get(holder.getAdapterPosition()).getImageMapUri()).fit().centerCrop().into(holder.mapImageView);

    }


    @Override
    public int getItemCount() {
        return routes.size();
    }


    class RoutesProfileViewHolder extends RecyclerView.ViewHolder {

        TextView totalTimeTextView, totalDistanceTextView, dateTextView, startTimeTextView, avgSpeedTextView;
        ImageView mapImageView;

        public RoutesProfileViewHolder(@NonNull View itemView) {
            super(itemView);

            totalDistanceTextView = itemView.findViewById(R.id.total_distance_text_view_routs_item_profile);
            totalTimeTextView = itemView.findViewById(R.id.total_time_text_view_routs_item_profile);
            dateTextView = itemView.findViewById(R.id.date_text_view_routs_item_profile);
            startTimeTextView = itemView.findViewById(R.id.start_time_text_view_routs_item_profile);
            avgSpeedTextView = itemView.findViewById(R.id.avg_speed_time_text_view_routs_item_profile);

            mapImageView = itemView.findViewById(R.id.map_image_view_routes_profile_item);

        }
    }
}
