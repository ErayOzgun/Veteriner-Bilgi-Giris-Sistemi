package com.vuralsoftware.vbgs.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vuralsoftware.vbgs.models.note;
import com.vuralsoftware.vbgs.R;

public class NoteAdapter extends FirestoreRecyclerAdapter<note,NoteAdapter.MyViewHolder> {

    private onItemClickListener listener;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<note> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemnotlar,parent,false);

        return new MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position,note note) {

        holder.textViewBaslik.setText(note.getBaslik());
        holder.textViewTarih.setText(note.getTarih());

    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewBaslik,textViewTarih;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewBaslik=itemView.findViewById(R.id.itemnotlar_baslik);
            textViewTarih=itemView.findViewById(R.id.itemnotlar_tarih);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null  ){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}

