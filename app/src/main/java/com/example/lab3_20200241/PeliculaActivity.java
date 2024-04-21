package com.example.lab3_20200241;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    TextView tituloPelicula,director,actores,fecha,genero,escritores,resumen,info1,info2,info3,info4,info5;

    private CheckBox checkBox;
    private Button button6;
    private String i;
    private String apikey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_pelicula);

        checkBox = findViewById(R.id.checkBox);
        button6 = findViewById(R.id.button6);

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        Toast.makeText(this, "¿Tiene internet? " + tengoInternet(), Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        i = intent.getStringExtra("codigo");
        apikey = "bf81d461";

        typicodeService = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TypicodeService.class);

        if(tengoInternet()){
            typicodeService.getPelicula(apikey,i).enqueue(new Callback<Pelicula>() {
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

        Toast.makeText(this, "Bienvenido a la sección de Peliculas", Toast.LENGTH_LONG).show();

        button6.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                button6.setEnabled(isChecked);
                updateButtonColor();
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button6.setEnabled(checkBox.isChecked());
            }
        });
    }

    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.d("msg-test-internet", "Internet: " + tieneInternet);
        return tieneInternet;
    }

    public void principal(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateButtonColor() {
        if (button6.isEnabled()) {
            button6.setBackgroundColor(Color.parseColor("#FE9700"));
        } else {
            button6.setBackgroundColor(Color.GRAY);
        }
    }

}