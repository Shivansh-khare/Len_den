package com.example.lenden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.passcodeview.PasscodeView;

import java.math.BigInteger;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Math.random;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i[]={3};
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().hide();
        TextView name = findViewById(R.id.Tran_name);
        CircleImageView user,send;

        user = findViewById(R.id.Trans_Image_user);
        send = findViewById(R.id.Trans_Image_send);
        final int[] n = {10};

        String userId = getIntent().getStringExtra("uid");

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        mRef.child("users").child(userId).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText("Paying to "+snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TransactionActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

        mRef.child("users").child(userId).child("pic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(TransactionActivity.this).load(snapshot.getValue()).into(send);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TransactionActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

        mRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(TransactionActivity.this).load(snapshot.getValue()).into(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TransactionActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

        mRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("transactions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                n[0] = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.fab_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double money = Double.parseDouble(((EditText)findViewById(R.id.ET_money)).getText().toString());
                        double balance = snapshot.getValue(Double.class);
                        String note = ((EditText)findViewById(R.id.note_transaction)).getText().toString();

                        if(money <= 0){
                            Toast.makeText(TransactionActivity.this,"money should be greater than zero",Toast.LENGTH_SHORT).show();
                        }
                        findViewById(R.id.form).setVisibility(View.GONE);
                        findViewById(R.id.pb_trans).setVisibility(View.VISIBLE);
                        findViewById(R.id.cnfrm_pass).setVisibility(View.VISIBLE);
                        findViewById(R.id.fab_send).setVisibility(View.GONE);
                        PasscodeView passcodeView = findViewById(R.id.cnfrm_pass);
                        String pass = getSharedPreferences("key", Context.MODE_PRIVATE).getString("pin","1234");
                        passcodeView.setPasscodeLength(4).setLocalPasscode(String.valueOf(pass)).setCorrectInputTip("welcome").setListener(new PasscodeView.PasscodeViewListener() {
                            @Override
                            public void onFail() {
                                i[0]--;
                                if(i[0]==0)
                                {finish();}
                                Toast.makeText(TransactionActivity.this,"Wrong attempt "+ (3- i[0]),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(String number) {
                                passcodeView.setVisibility(View.GONE);
                                sendMoney(balance,userId,money,note, n[0]);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        findViewById(R.id.form).setVisibility(View.GONE);
                        findViewById(R.id.iv_fail).setVisibility(View.VISIBLE);
                    }
                });
            }

            private void sendMoney(double balance, String userId, double money, String note, Integer n) {
                mRef.child("users").child(userId).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double rec_balance = snapshot.getValue(Double.class);
                        if(balance < money)
                        {
                            Toast.makeText(TransactionActivity.this,"Balance is not sufficient",Toast.LENGTH_LONG).show();
                        }else {
                            rec_balance += money;
                            mRef.child("users").child(userId).child("balance").setValue(rec_balance);
                            mRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(balance - money);
                            DatabaseReference mRef1 = mRef.child("users").child(userId).child("transactions").push();
                            DatabaseReference mRef2 = mRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("transactions").push();
                            mRef1.child("amount").setValue(money);
                            mRef1.child("recived").setValue("true");
                            mRef1.child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mRef1.child("note").setValue(note);
                            mRef2.child("amount").setValue(money);
                            mRef2.child("recived").setValue("false");
                            mRef2.child("user").setValue(userId);
                            mRef2.child("note").setValue(note);


                            BigInteger b = new BigInteger(String.valueOf(n));
                            if (n % 8 == 0 || b.isProbablePrime(1)) {
                                Double total = money * (0.6) + balance * (0.4);
                                Integer reward = (new Random()).nextInt(total.intValue())/40;
                                DatabaseReference ref = mRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rewards").push();
                                ref.child("money").setValue(Integer.parseInt(String.valueOf(reward)));
                                ref.child("visited").setValue(false);
                                Toast.makeText(TransactionActivity.this, "Earned a Reward", Toast.LENGTH_LONG).show();
                            }
                            findViewById(R.id.pb_trans).setVisibility(View.GONE);
                            findViewById(R.id.iv_pass).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        findViewById(R.id.pb_trans).setVisibility(View.GONE);
                        findViewById(R.id.iv_fail).setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


}