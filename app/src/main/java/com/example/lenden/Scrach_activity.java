package com.example.lenden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.ScratchImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Scrach_activity extends AppCompatActivity {

    TextView money_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrach_activity);

        int M = getIntent().getIntExtra("money",0);
        String Add = getIntent().getStringExtra("path");
        money_tv = findViewById(R.id.TV_money);
        final Double[] bal = {0.0};
        ScratchImageView s = findViewById(R.id.Scratch_view_);
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bal[0] = Double.parseDouble(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        money_tv.setText(String.valueOf(M));
        Toast.makeText(this,Add,Toast.LENGTH_LONG).show();

        s.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView iv) {
            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                if(percent>0.6) {
                    Toast.makeText(Scrach_activity.this, "Congratulations", Toast.LENGTH_SHORT).show();
                    siv.setVisibility(View.GONE);
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rewards").child(Add).child("visited").setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(bal[0] + M);
                }
            }
        });
    }
}