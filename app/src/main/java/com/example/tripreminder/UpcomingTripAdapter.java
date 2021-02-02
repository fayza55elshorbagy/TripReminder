package com.example.tripreminder;
import com.example.tripreminder.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpcomingTripAdapter extends RecyclerView.Adapter<UpcomingTripAdapter.ViewHolder> {
    List<Trip> trips;
    ViewHolder vh;

    public UpcomingTripAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.trip_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        new Upcoming(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.title.setText(trip.getTitle());
        holder.from.setText(trip.getFrom());
        holder.to.setText(trip.getTo());
        holder.date.setText(trip.getDate());
        holder.time.setText(trip.getTime());
        holder.type.setText(trip.getType());
        holder.status.setText(trip.getStatus());


    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        TextView title;
        TextView from;
        TextView to;
        TextView date;
        TextView time;
        TextView type;
        TextView status;
        ImageView menu;
        ImageView note;
        Button startBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleView);
            from = itemView.findViewById(R.id.fromView);
            to = itemView.findViewById(R.id.toView);
            date = itemView.findViewById(R.id.dateView);
            time = itemView.findViewById(R.id.timeView);
            type = itemView.findViewById(R.id.typeView);
            status = itemView.findViewById(R.id.statusView);
            menu = itemView.findViewById(R.id.menu);
            note = itemView.findViewById(R.id.note);
            startBtn = itemView.findViewById(R.id.startBtn);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.setOnMenuItemClickListener(ViewHolder.this::onMenuItemClick);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.show();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Note Click", Toast.LENGTH_SHORT).show();
                }
            });



        }




        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.notes:
                    Toast.makeText(startBtn.getContext(), "you clicked on notes", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.edit:
                    Toast.makeText(startBtn.getContext(), "you clicked on edit", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete:
                    Toast.makeText(startBtn.getContext(), "you clicked on delete", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.cancel:
                    Toast.makeText(startBtn.getContext(), "you clicked on cancel", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }


        }
}
    }

