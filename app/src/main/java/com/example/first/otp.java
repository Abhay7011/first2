package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    private EditText otp;
    private ImageButton submit;
    private TextView resend;
    private MKLoader loader;
    private String number,id;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_otp);


        otp = findViewById(R.id.otpenter);
        submit = findViewById(R.id.otpsubmit);
        resend = findViewById(R.id.resend);
        loader = findViewById(R.id.loader);

        mAuth = FirebaseAuth.getInstance();
        number = getIntent().getStringExtra("number");

        sendVerificationCode();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(otp.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                }
                else if (otp.getText().toString().replace("","").length()!=6){
                    Toast.makeText(otp.this, "Enter Right OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    loader.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,otp.getText().toString().replace("",""));
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });


    }

    private void sendVerificationCode() {

        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                resend.setText(""+1/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText(" Resend");
                resend.setEnabled(true);
            }
        }.start();



        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,      // Phone number to verify
                60,          // Timeout and unit
                TimeUnit.SECONDS,        // Unit of timeout
                this,        // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        com.example.first.otp.this.id = id;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(otp.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                });// OnVerificationStateChangedCallbacks





    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loader.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            startActivity(new Intent(com.example.first.otp.this,LoginActivity.class));
                            finish();
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            Toast.makeText(otp.this, "Verification Field", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}