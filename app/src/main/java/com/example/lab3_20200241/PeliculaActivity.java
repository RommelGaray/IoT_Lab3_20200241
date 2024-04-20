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

    TextView tituloPelicula;
    TextView director;
    TextView actores;
    TextView fecha;
    TextView genero;
    TextView escritores;
    TextView resumen;
    TextView info1;
    TextView info2;
    TextView info3;
    TextView info4;
    TextView info5;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_pelicula);

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        Toast.makeText(this, "Tiene internet: " + tengoInternet(), Toast.LENGTH_LONG).show();

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


                        TextView tituloPelicula = findViewById(R.id.tituloPelicula);
                        TextView director = findViewById(R.id.director);
                        TextView actores = findViewById(R.id.actores);
                        TextView fecha = findViewById(R.id.fecha);
                        TextView genero = findViewById(R.id.genero);
                        TextView escritores = findViewById(R.id.escritores);

                        TextView resumen = findViewById(R.id.resumen);

                        TextView info1 = findViewById(R.id.info1);
                        TextView info2 = findViewById(R.id.info2);
                        TextView info3 = findViewById(R.id.info3);
                        TextView info4 = findViewById(R.id.info4);
                        TextView info5 = findViewById(R.id.info5);

                        tituloPelicula.setText(pelicula.getTitle());
                        director.setText(pelicula.getDirector());
                        actores.setText(pelicula.getActors());
                        fecha.setText(pelicula.getReleased());
                        genero.setText(pelicula.getGenre());
                        escritores.setText(pelicula.getWriter());

                        resumen.setText(pelicula.getPlot());

//                        info1.setText(pelicula.getRatings().getSource());
//                        info2.setText(pelicula.getRatings().getValue());
//                        info3.setText(pelicula.getRatings().getSource());
//                        info4.setText(pelicula.getRatings().getSource());
//                        info5.setText(pelicula.getRatings().getSource());





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



}