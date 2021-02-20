package com.example.tripreminder.adapters;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;


import com.example.tripreminder.beans.Trips;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    List<Trips> trips=new ArrayList<>();
    private final HistoryListener tripListener;

    public HistoryAdapter(HistoryListener tripListener) {
        this.tripListener = tripListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trips trip = trips.get(position);
        holder.title.setText(trip.getName());
        holder.from.setText(trip.getStartLoc());
        holder.to.setText(trip.getEndLoc());
        holder.date.setText(trip.getDate());
        holder.time.setText(trip.getTime());
        //holder.type.setText(trip.getType());
        if(trip.getStatus()==1)
            holder.status.setText("Cancelled" );
        else if(trip.getStatus()==2)
            holder.status.setText("Done" );
        else if(trip.getStatus()==3)
            holder.status.setText("Passed" );

        holder.del.setOnClickListener(v -> tripListener.delete(trips.get(position)));
        holder.note.setOnClickListener(v -> tripListener.showNote(trips.get(position)));
    }
    public void saveTrips(List<Trips> trips) {
        this.trips = trips;
        notifyDataSetChanged();
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

        Button note;
        Button del;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleView);
            from = itemView.findViewById(R.id.fromView);
            to = itemView.findViewById(R.id.toView);
            date = itemView.findViewById(R.id.dateView);
            time = itemView.findViewById(R.id.timeView);


            status = itemView.findViewById(R.id.statusView);
            note = itemView.findViewById(R.id.note);
            del = itemView.findViewById(R.id.remove_btn);


        }

    }
}