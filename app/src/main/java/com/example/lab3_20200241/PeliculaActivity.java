package com.example.lab3_20200241;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

public class PeliculaActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TypicodeService typicodeService;

    TextView textView13;
    TextView textView14;
    TextView textView15;
    TextView textView16;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_pelicula);

        // Usando ExecutorService
        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;
        // **********************

        // Verificar conexion a internet
        Toast.makeText(this, "Tiene internet: " + tengoInternet(), Toast.LENGTH_LONG).show();

        /* La llamada de la API - Utilizando Retrofit */
        typicodeService = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TypicodeService.class);

        if(tengoInternet()){
            typicodeService.getPelicula().enqueue(new Callback<Pelicula>() {
                @Override
                public void onResponse(Call<Pelicula> call, Response<Pelicula> response) {
                    //aca estoy en el UI Thread
                    if(response.isSuccessful()){
                        Pelicula pelicula = response.body();

                        Intent intent = getIntent();
                        String codigo = intent.getStringExtra("codigo");


                        TextView textView = findViewById(R.id.textView13);
                        textView.setText(codigo);


                        TextView titulo = findViewById(R.id.textView14);
                        titulo.setText(pelicula.getTitle());

//                        binding.textView.setText(pelicula.getTitle());
                        Log.d("msg-test-ws-profile","titulo: " + pelicula.getTitle());

                    }
                }

                @Override
                public void onFailure(Call<Pelicula> call, Throwable t) {

                }

            });
        }

    }

    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.d("msg-test-internet", "Internet: " + tieneInternet);
        return tieneInternet;
    }

/*
    private void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        String titulo = extras.getString("Director");
        Log.d("msg-test-ws-profile","name: " + titulo);
        Log.d("msg-test-ws-profile","name: " + titulo);
        String director = extras.getString("Director");

        textView12 = (TextView) findViewById(R.id.textView12);
        textView12.setText(titulo);

    }

 */


}