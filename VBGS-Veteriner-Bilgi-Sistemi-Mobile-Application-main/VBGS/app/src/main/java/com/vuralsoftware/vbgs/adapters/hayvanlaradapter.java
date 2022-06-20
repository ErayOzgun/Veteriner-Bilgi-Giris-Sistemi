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
import com.vuralsoftware.vbgs.models.hayvaninfo;
import com.vuralsoftware.vbgs.R;


public class hayvanlaradapter extends FirestoreRecyclerAdapter<hayvaninfo,hayvanlaradapter.MyViewHolder> {

    private onItemClickListener listener;

    public hayvanlaradapter(@NonNull FirestoreRecyclerOptions<hayvaninfo> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemhayvanlar,parent,false);

        return new MyViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position,hayvaninfo hay) {


        holder.textViewname.setText(hay.getHayvanad());
        holder.textViewkupe.setText(hay.getKupeno());
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewname;
        TextView textViewkupe;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewname=itemView.findViewById(R.id.itemhayvanlar_txt);
            textViewkupe=itemView.findViewById(R.id.itemhayvanlar_txtkupe);
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
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}
