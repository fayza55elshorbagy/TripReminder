package com.example.tripreminder.adapters;

import android.view.LayoutInflater;
<<<<<<< HEAD
=======
import android.view.MenuItem;
>>>>>>> activityToFragment
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
<<<<<<< HEAD
=======
import android.widget.PopupMenu;
>>>>>>> activityToFragment
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
<<<<<<< HEAD
import com.example.tripreminder.beans.HistoryListener;
import com.example.tripreminder.beans.TripListener;
import com.example.tripreminder.beans.Trips;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    List<Trips> trips=new ArrayList<>();
    private final HistoryListener tripListener;

    public HistoryAdapter(HistoryListener tripListener) {
        this.tripListener = tripListener;
=======
import com.example.tripreminder.beans.Trip;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    List<Trip> trips;

    public HistoryAdapter(List<Trip> trips) {
        this.trips = trips;
>>>>>>> activityToFragment
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
=======
        int layout = R.layout.history_item;
>>>>>>> activityToFragment
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
<<<<<<< HEAD
        Trips trip = trips.get(position);
        holder.title.setText(trip.getName());
        holder.from.setText(trip.getStartPoint());
        holder.to.setText(trip.getEndPoint());
        holder.date.setText(trip.getDate());
        holder.time.setText(trip.getTime());
        //holder.type.setText(trip.getType());
        if(trip.getStatus()==1)
            holder.status.setText("Cancelled" );
        else if(trip.getStatus()==2)
            holder.status.setText("Done" );

        holder.del.setOnClickListener(v -> tripListener.delete(trips.get(position)));
        holder.note.setOnClickListener(v -> tripListener.showNote(trips.get(position)));



    }
    public void saveTrips(List<Trips> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }
=======
        Trip trip = trips.get(position);
        holder.title.setText(trip.getTitle());
        holder.from.setText(trip.getFrom());
        holder.to.setText(trip.getTo());
        holder.date.setText(trip.getDate());
        holder.time.setText(trip.getTime());
        holder.type.setText(trip.getType());
        holder.status.setText(trip.getStatus());


    }

>>>>>>> activityToFragment
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
<<<<<<< HEAD
        Button note;
        Button del;
=======
        ImageView note;

>>>>>>> activityToFragment

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleView);
            from = itemView.findViewById(R.id.fromView);
            to = itemView.findViewById(R.id.toView);
            date = itemView.findViewById(R.id.dateView);
            time = itemView.findViewById(R.id.timeView);
<<<<<<< HEAD
            status = itemView.findViewById(R.id.statusView);
            note = itemView.findViewById(R.id.note);
            del = itemView.findViewById(R.id.remove_btn);
=======
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

>>>>>>> activityToFragment

        }

    }
}