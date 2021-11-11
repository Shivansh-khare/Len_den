package com.example.lenden.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TintTypedArray;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lenden.R;
import com.example.lenden.Scrach_activity;
import com.example.lenden.models.reward;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.viewHolder>{

    ArrayList<reward> data;
    Context context;

    public RewardAdapter(ArrayList<reward> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).isVisited()?1:0;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1) return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_view,parent,false));
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.unvisited_reward,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!data.get(position).isVisited()){
                    Intent intent = new Intent(holder.itemView.getContext(), Scrach_activity.class);
                    intent.putExtra("path",data.get(position).getAdd());
                    intent.putExtra("money",data.get(position).getMoney());
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
        if (data.get(position).isVisited())
        holder.textView.setText(""+data.get(position).getMoney());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.TV_reward_money);
        }
    }
}
