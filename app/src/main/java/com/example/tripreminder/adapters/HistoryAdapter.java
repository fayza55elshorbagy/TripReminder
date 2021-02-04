package com.example.tripreminder.adapters;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.beans.Trip;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    List<Trip> trips;

    public HistoryAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.history_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView from;
        TextView to;
        TextView date;
        TextView time;
        TextView type;
        TextView status;
        ImageView note;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleView);
            from = itemView.findViewById(R.id.fromView);
            to = itemView.findViewById(R.id.toView);
            date = itemView.findViewById(R.id.dateView);
            time = itemView.findViewById(R.id.timeView);
            type = itemView.findViewById(R.id.typeView);
            status = itemView.findViewById(R.id.statusView);
            note = itemView.findViewById(R.id.note);

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

    }
}