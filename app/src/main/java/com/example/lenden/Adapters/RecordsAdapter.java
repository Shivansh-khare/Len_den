package com.example.lenden.Adapters;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lenden.R;
import com.example.lenden.Records;
import com.example.lenden.models.Transaction_record;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.viewHolder> {
    ArrayList<Transaction_record> list;

    public RecordsAdapter(ArrayList<Transaction_record> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_view,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        FirebaseDatabase.getInstance().getReference().child("users").child(list.get(position).getSendId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(holder.prof.getContext()).load(snapshot.child("pic").getValue().toString()).into(holder.prof);
                holder.name.setText(snapshot.child("name").getValue().toString());
                holder.note.setText(list.get(position).getNote());
                String a;
                if(list.get(position).isRec()){
                    a = "+ ₹";
                    holder.amount.setTextColor(Color.GREEN);
                }else{
                    a = "- ₹";
                    holder.amount.setTextColor(Color.RED);
                }
                a+=list.get(position).getAmount();
                holder.amount.setText(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        CircleImageView prof;
        TextView name,amount,note;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            prof = itemView.findViewById(R.id.CIV_trans_prof);
            name = itemView.findViewById(R.id.TV_Trans_name);
            amount = itemView.findViewById(R.id.TV_trans_amm);
            note = itemView.findViewById(R.id.note);
        }
    }
}
