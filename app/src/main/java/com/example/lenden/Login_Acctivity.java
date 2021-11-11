package com.example.lenden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Acctivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__acctivity);


        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(Login_Acctivity.this,MainActivity.class));
            finish();
        }


        findViewById(R.id.BTN_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ET_mail = (EditText)findViewById(R.id.ET_log_email);
                EditText ET_pass = (EditText)findViewById(R.id.ET_log_pass);
                FirebaseAuth.getInstance().signInWithEmailAndPassword( ET_mail.getText().toString().trim(),ET_pass.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(Login_Acctivity.this, passcode.class);
                        intent.putExtra("type",1);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login_Acctivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        findViewById(R.id.TV_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Acctivity.this,SignupActivity.class));
                finish();
            }
        });
    }
}