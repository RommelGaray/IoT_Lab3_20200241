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
import com.example.lab3_20200241.dto.NumeroPrimo;
import com.example.lab3_20200241.dto.Pelicula;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContadorActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TypicodeService typicodeService;
    TextView primosVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_contador);



        Toast.makeText(this, "Bienvenido al Contador de Números Primos ", Toast.LENGTH_LONG).show();

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        typicodeService = new Retrofit.Builder()
                .baseUrl("https://prime-number-api.onrender.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TypicodeService.class);

        if(tengoInternet()){
            typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {
                @Override
                public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                    //aca estoy en el UI Thread

                    if(response.isSuccessful()){
                        List<NumeroPrimo> primosList = response.body();

                        for (NumeroPrimo c : primosList) {
                            TextView primosVer = findViewById(R.id.primosVer);
                            primosVer.setText(String.valueOf(c.getNumber()));
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }


                    } else {
                        Log.d("msg-test", "error en la respuesta del webservice");
                    }
                }

                @Override
                public void onFailure(Call<List<NumeroPrimo>> call, Throwable t) {

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