package com.example.lab3_20200241;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20200241.databinding.ActivityMainBinding;
import com.example.lab3_20200241.dto.Pelicula;

import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Bienvenido al menú principal ", Toast.LENGTH_LONG).show();
    }


    public void contador(View view){
        Intent intent = new Intent(this, ContadorActivity.class);
        startActivity(intent);
    }

    public void pelicula(View view){

        EditText editText =findViewById(R.id.codigo);
        String codigo = editText.getText().toString();

        Intent intent = new Intent(this, PeliculaActivity.class);
        intent.putExtra("codigo", codigo);
        startActivity(intent);
    }


}