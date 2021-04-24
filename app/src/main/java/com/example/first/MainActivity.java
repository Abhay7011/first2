package com.example.first;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {


    private CountryCodePicker countryCodePicker;
    private EditText number;
    private ImageButton next;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);


        countryCodePicker = findViewById(R.id.ccp);
        number = findViewById(R.id.editTextPhone);
        next = findViewById(R.id.button);
        countryCodePicker.registerCarrierNumberEditText(number);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(number.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter No.....", Toast.LENGTH_SHORT).show();

                } else if (number.getText().toString().replace("", "").length() != 10) {
                    Toast.makeText(MainActivity.this, "Enter Correct No.....", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(MainActivity.this, otp.class);
                    intent.putExtra("number", countryCodePicker.getFullNumberWithPlus().replace("", ""));
                    startActivity(intent);
                    finish();

                }

            }

        });


    }
}