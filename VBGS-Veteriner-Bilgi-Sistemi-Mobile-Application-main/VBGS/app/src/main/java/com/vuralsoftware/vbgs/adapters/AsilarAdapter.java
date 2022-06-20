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
import com.vuralsoftware.vbgs.R;
import com.vuralsoftware.vbgs.models.asimodel;


public class AsilarAdapter extends FirestoreRecyclerAdapter<asimodel,AsilarAdapter.MyViewHolder> {

    private onItemClickListener listener;

    public AsilarAdapter(@NonNull FirestoreRecyclerOptions<asimodel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemasilar,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position,asimodel asimodel) {
        holder.textViewAsiad.setText(asimodel.getAsiad());
        holder.textViewTarih.setText(asimodel.getTarih());
        holder.textViewSaat.setText(asimodel.getSaat());
        holder.textViewDurum.setText(asimodel.getDurum());
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewAsiad,textViewTarih,textViewDurum,textViewSaat,textViewid;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewAsiad=itemView.findViewById(R.id.itemasilar_asiad);
            textViewTarih=itemView.findViewById(R.id.itemasilar_tarih);
            textViewSaat=itemView.findViewById(R.id.itemasilar_saat);
            textViewDurum=itemView.findViewById(R.id.itemasilar_durum);
            textViewid=itemView.findViewById(R.id.itemasilar_id);

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



