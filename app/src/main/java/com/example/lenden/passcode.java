package com.example.lenden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.hanks.passcodeview.PasscodeView;

public class passcode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        getSupportActionBar().hide();
        int type = getIntent().getIntExtra("type",0);

        final int[] i = {3};

        PasscodeView passcodeView = findViewById(R.id.passcodeview);
        if(type == 0)
        {
            String pass = getSharedPreferences("key", Context.MODE_PRIVATE).getString("pin","1234");
            passcodeView.setPasscodeLength(4).setLocalPasscode(String.valueOf(pass)).setCorrectInputTip("welcome").setListener(new PasscodeView.PasscodeViewListener() {
                @Override
                public void onFail() {
                    i[0]--;
                    if(i[0]==0)
                    {finish();}
                    Toast.makeText(passcode.this,"Wrong attempt "+ (3- i[0]),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(String number) {
                    Intent intent = new Intent(passcode.this,MainActivity.class);
                    intent.putExtra("x",false);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            passcodeView.setPasscodeLength(4).setFirstInputTip("set a 4 digit passcode").setListener(new PasscodeView.PasscodeViewListener() {
                @Override
                public void onFail() {
                    Toast.makeText(passcode.this,"try again",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(String number) {
                    SharedPreferences.Editor a = getSharedPreferences("key",Context.MODE_PRIVATE).edit();
                    a.putString("pin",number);
                    a.apply();
                    Intent intent = new Intent(passcode.this,MainActivity.class);
                    intent.putExtra("x",false);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}