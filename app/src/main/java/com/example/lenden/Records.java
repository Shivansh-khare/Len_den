package com.example.lenden;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.lenden.Adapters.RecordsAdapter;
import com.example.lenden.models.Transaction_record;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Records extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<Transaction_record> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        rv = findViewById(R.id.rv_transactions);
        list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("transactions").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Toast.makeText(Records.this,snapshot.getKey().toString(),Toast.LENGTH_LONG).show();
//                Toast.makeText(Records.this,snapshot.getKey().toString(),Toast.LENGTH_LONG).show();
//                Toast.makeText(Records.this,snapshot.child("recived").getValue().toString(),Toast.LENGTH_LONG).show();
//                Toast.makeText(Records.this,snapshot.getKey().toString(),Toast.LENGTH_LONG).show();
                list.add(new Transaction_record(snapshot.child("user").getValue().toString(),Double.parseDouble(snapshot.child("amount").getValue().toString()),Boolean.parseBoolean(snapshot.child("recived").getValue().toString()),snapshot.child("note").getValue().toString()));
                rv.getAdapter().notifyDataSetChanged();
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
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setAdapter(new RecordsAdapter(list));
    }
}