package com.example.tripreminder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;

import java.util.List;

public class notesAdapter extends RecyclerView.Adapter<notesAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<String> notes;


    public notesAdapter(Context ctx,List<String> notes){

        inflater = LayoutInflater.from(ctx);
        this.notes = notes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.note_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(notes.get(position));
        Log.i("note","note"+notes.get(position));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            delete=(ImageView) itemView.findViewById(R.id.img_remove);


        }

    }
}