package com.example.lab3_20200241;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20200241.databinding.ActivityMainBinding;

public class ContadorActivity extends AppCompatActivity {
    ActivityMainBinding binding;
//    TypicodeService typicodeService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_contador);

    }
}