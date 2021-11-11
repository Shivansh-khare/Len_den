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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.BTN_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ET_mail = (EditText)findViewById(R.id.ET_sign_email);
                EditText ET_pass = (EditText)findViewById(R.id.ET_sign_pass);
                EditText ET_cnfrm_pass = (EditText)findViewById(R.id.ET_sign_cnfm_pass);

//                if(ET_mail.getText().toString().length() < 5 || ET_cnfrm_pass.getText().toString() != ET_pass.getText().toString() || ET_cnfrm_pass.getText().toString().length()<5)
//                    Toast.makeText(SignupActivity.this,"not valid",Toast.LENGTH_SHORT).show();
//                else
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(ET_mail.getText().toString(),ET_cnfrm_pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("balance").setValue(0);
                        Intent intent = new Intent(SignupActivity.this, passcode.class);
                        intent.putExtra("type",1);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        findViewById(R.id.TV_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,Login_Acctivity.class));
                finish();
            }
        });
    }
}