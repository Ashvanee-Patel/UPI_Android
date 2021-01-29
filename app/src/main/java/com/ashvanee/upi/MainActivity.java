package com.ashvanee.upi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ashvanee.upi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

ActivityMainBinding binding;
Context context = MainActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.upiBtn.setOnClickListener(view -> {
            startActivity(new Intent(context, UpiActivity.class));
            finish();
        });
        binding.paytmBtn.setOnClickListener(view -> {
            startActivity(new Intent(context, PaytmActivity.class));
            finish();
        });
        binding.invoiceBtn.setOnClickListener(view -> {
            startActivity(new Intent(context, PdfActivity.class));
            finish();
                }
        );
    }
}