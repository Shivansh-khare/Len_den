package com.example.lenden;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.InetAddress;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    AppCompatButton btn_scan;
    TextView tv_balance;
    FirebaseDatabase mRef;
    double blance ;
    boolean bl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        btn_scan = findViewById(R.id.btn_scan);
        tv_balance = findViewById(R.id.balance_text);
        Timer timer = new Timer();
        blance = 0.0;
        bl = getIntent().getBooleanExtra("x",true);
        mRef = FirebaseDatabase.getInstance();

        //Toast.makeText(MainActivity.this, FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();

        {
            mRef.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    blance = (double) snapshot.getValue(Double.class);
//                Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this,"Bank server down Try again later",Toast.LENGTH_SHORT).show();
                }
            });

            mRef.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pic").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Glide.with(MainActivity.this).load(snapshot.getValue(String.class)).into((CircleImageView) findViewById(R.id.CIV_home_profile));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            btn_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                    intentIntegrator.setPrompt("Scan a QR Code");
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.initiateScan();
                }
            });

            findViewById(R.id.show_qr_lay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,Show_QR.class));
                }
            });
            findViewById(R.id.Transactions).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,Records.class));
                }
            });

            findViewById(R.id.reward_lay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,RewardsActivity.class));
                }
            });

            findViewById(R.id.balance).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    findViewById(R.id.pb_balance).setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.pb_balance).setVisibility(View.GONE);
                            tv_balance.setText("₹ "+blance);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    tv_balance.setText("Balance");

                                }
                            },1300);
                        }},500);

                }
            });

            findViewById(R.id.CIV_home_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,Login_Acctivity.class));
                    finish();
                }
            });
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        bl = false;
//        bl = true;
//        Intent intent = new Intent(MainActivity.this, passcode.class);
//        intent.putExtra("type",0);
//        startActivity(intent);
//        finish();
    }

    @Override
    protected void onResume() {
//        bl = false;
        super.onResume();
        if(bl) {
            Intent intent = new Intent(MainActivity.this, passcode.class);
            intent.putExtra("type",0);
            startActivity(intent);
//            finish();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                if(!(intentResult.getContents().contains(".") || intentResult.getContents().contains("#") || intentResult.getContents().contains("$") || intentResult.getContents().contains("[") || intentResult.getContents().contains("]"))){
                    mRef.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(""+intentResult.getContents().toString())) {
                            if(intentResult.getContents().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                            {
                                Toast.makeText(MainActivity.this,"Can't send money to your self",Toast.LENGTH_SHORT).show();
                            }else{
                            Intent intent = new Intent(MainActivity.this,TransactionActivity.class);
                            intent.putExtra("uid",intentResult.getContents());
                            startActivity(intent);}
                        }else {
                            Toast.makeText(getBaseContext(), "Wrong QR code", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                }else{

                Toast.makeText(MainActivity.this,"Wrong QR code",Toast.LENGTH_SHORT).show();
                }
//
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}