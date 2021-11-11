package com.example.lenden;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.lenden.Adapters.RewardAdapter;
import com.example.lenden.models.reward;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RewardsActivity extends AppCompatActivity {

    RecyclerView rv;
    String data;
    ArrayList<reward> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        rv = findViewById(R.id.rec_rewards);
        lst = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rewards").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                lst.add(new reward(Integer.parseInt(snapshot.child("money").getValue().toString()),Boolean.parseBoolean(snapshot.child("visited").getValue().toString()),snapshot.getKey().toString()));
                findViewById(R.id.empty_layout_rewards).setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this,2));
        rv.setAdapter(new RewardAdapter(lst,this));


    }
}