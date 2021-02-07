package com.example.tripreminder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.beans.TripListener;
import com.example.tripreminder.beans.Trips;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UpcomingTripAdapter extends RecyclerView.Adapter<UpcomingTripAdapter.ViewHolder> {
    List<Trips> trips=new ArrayList<>();
    private final TripListener tripListener;


    public UpcomingTripAdapter(TripListener tripListener) {
        this.tripListener = tripListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
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
       // holder.type.setText(trip.getType());
      //  holder.status.setText(String.valueOf(trip.getStatus()));
       // holder.from.setText(trip.getNotes());

        holder.menu.setOnClickListener(v -> showMenu(holder.menu, trips.get(position)));
        holder.note.setOnClickListener(v -> tripListener.showNote(trips.get(position)));
        holder.startBtn.setOnClickListener(v -> tripListener.startNav(trips.get(position)));

    }
    public void saveTrips(List<Trips> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView from;
        TextView to;
        TextView date;
        TextView time;
        TextView type;
        TextView status;
        Button menu;
        Button note;
        Button startBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleView);
            from = itemView.findViewById(R.id.fromView);
            to = itemView.findViewById(R.id.toView);
            date = itemView.findViewById(R.id.dateView);
            time = itemView.findViewById(R.id.timeView);
           // type = itemView.findViewById(R.id.typeView);
           // status = itemView.findViewById(R.id.statusView);
            menu = itemView.findViewById(R.id.menu);
            note = itemView.findViewById(R.id.note);
            startBtn = itemView.findViewById(R.id.startBtn);

        }
    }
    private void showMenu(View v, Trips trip) {

        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        setForceShowIcon(popupMenu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    tripListener.edit(trip);
                    return true;
                case R.id.delete:
                    tripListener.delete(trip);
                    return true;
                case R.id.cancel:
                    tripListener.cancel(trip);
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = null;
                    if (menuPopupHelper != null) {
                        classPopupHelper = Class.forName(menuPopupHelper
                                .getClass().getName());
                    }
                    Method setForceIcons = null;
                    if (classPopupHelper != null) {
                        setForceIcons = classPopupHelper.getMethod(
                                "setForceShowIcon", boolean.class);
                    }
                    if (setForceIcons != null) {
                        setForceIcons.invoke(menuPopupHelper, true);
                    }
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
