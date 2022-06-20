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
import com.vuralsoftware.vbgs.models.ilacmodel;
import com.vuralsoftware.vbgs.R;

public class IlaclarAdapter extends FirestoreRecyclerAdapter<ilacmodel,IlaclarAdapter.MyViewHolder> {

    private onItemClickListener listener;

    public IlaclarAdapter(@NonNull FirestoreRecyclerOptions<ilacmodel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemilaclar,parent,false);

        return new MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position,ilacmodel ilacmodel) {

        holder.textViewIlacad.setText(ilacmodel.getIlacad());
        holder.textViewTarih.setText(ilacmodel.getTarih());
        holder.textViewSaat.setText(ilacmodel.getSaat());
        holder.textViewDoz.setText(ilacmodel.getDoz());

    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewIlacad,textViewTarih,textViewDoz,textViewSaat;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewIlacad=itemView.findViewById(R.id.itemilaclar_ilacad);
            textViewTarih=itemView.findViewById(R.id.itemilaclar_tarih);
            textViewSaat=itemView.findViewById(R.id.itemilacalar_saat);
            textViewDoz=itemView.findViewById(R.id.itemilacalar_doz);


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



