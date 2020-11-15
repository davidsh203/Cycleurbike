package com.example.cycleurbike.ClassHelpers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cycleurbike.ClassHelpers.Route;
import com.example.cycleurbike.R;
import com.example.cycleurbike.activities.MapsActivity;
import com.example.cycleurbike.activities.RiderProfileActivity;
import com.example.cycleurbike.activities.SharedRoutesActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SharedRoutesAdapter extends RecyclerView.Adapter<SharedRoutesAdapter.SharedRoutesViewHolder> {


    Context context;
    ArrayList<Route> routes;
    Activity activity;
    SharedPreferences sharedPreferences;

    public SharedRoutesAdapter(Context context, ArrayList<Route> routes, Activity activity, SharedPreferences sharedPreferences) {
        this.context = context;
        this.routes = routes;
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
    }


    @NonNull
    @Override
    public SharedRoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_routes_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new SharedRoutesViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull final SharedRoutesViewHolder holder, int position) {


        holder.totalDistanceTextView.setText(routes.get(holder.getAdapterPosition()).getDistance());
        holder.totalTimeTextView.setText(routes.get(holder.getAdapterPosition()).getTime());
        holder.dateTextView.setText(routes.get(holder.getAdapterPosition()).getDateOfRoute());
        holder.startTimeTextView.setText(routes.get(holder.getAdapterPosition()).getHourOfRoute());
        holder.avgSpeedTextView.setText(routes.get(holder.getAdapterPosition()).getAvgSpeed());

        if (routes.get(holder.getAdapterPosition()).getRouteName() != null){
            holder.routeNameTextView.setText(routes.get(holder.getAdapterPosition()).getRouteName());
        }

        Picasso.with(context).load(routes.get(holder.getAdapterPosition()).getImageMapUri()).fit().centerCrop().into(holder.mapImageView);


        holder.sharedRoutesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "המפה נטענת", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                SharedPreferences.Editor editor = context.getSharedPreferences("file", MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String json = gson.toJson(routes.get(holder.getAdapterPosition()));
                editor.putString("myRoute", json);
                editor.commit();


                context.startActivity(intent);
                activity.finish();

            }
        });

    }


    @Override
    public int getItemCount() {
        return routes.size();
    }


    class SharedRoutesViewHolder extends RecyclerView.ViewHolder {

        TextView totalTimeTextView, totalDistanceTextView, dateTextView, startTimeTextView, avgSpeedTextView, routeNameTextView;
        ImageView mapImageView;
        LinearLayout sharedRoutesLinearLayout;

        public SharedRoutesViewHolder(@NonNull View itemView) {
            super(itemView);

            totalDistanceTextView = itemView.findViewById(R.id.total_distance_text_view_shared_routes_item);
            totalTimeTextView = itemView.findViewById(R.id.total_time_text_view_shared_routes_item);
            dateTextView = itemView.findViewById(R.id.date_text_view_shared_routes_item);
            startTimeTextView = itemView.findViewById(R.id.start_time_text_view_shared_routes_item);
            avgSpeedTextView = itemView.findViewById(R.id.avg_speed_time_text_view_shared_routes_item);
            routeNameTextView = itemView.findViewById(R.id.name_of_route_text_view_shared_routes_item);

            mapImageView = itemView.findViewById(R.id.map_image_view_shared_routes_profile);

            sharedRoutesLinearLayout = itemView.findViewById(R.id.linear_layout_shared_routes_item);

        }

    }
}
