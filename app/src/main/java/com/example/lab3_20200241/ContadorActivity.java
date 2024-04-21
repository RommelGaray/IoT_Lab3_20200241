package com.example.lab3_20200241;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3_20200241.databinding.ActivityContadorBinding;
import com.example.lab3_20200241.dto.NumeroPrimo;
import com.example.lab3_20200241.dto.Pelicula;
import com.example.lab3_20200241.modelview.ContadorViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContadorActivity extends AppCompatActivity {
    ActivityContadorBinding binding;
    TypicodeService typicodeService;
    private boolean shouldStart= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Intente usar hilos pero no me salio.
        ContadorViewModel contadorViewModel =
                new ViewModelProvider(ContadorActivity.this).get(ContadorViewModel.class);

        contadorViewModel.getContador().observe(this, contador -> {
            //aquí o2
            binding.iniciar.setText(String.valueOf(contador));
        });


        /* Mensaje bienvenida */
        Toast.makeText(this, "Bienvenido al Contador de Números Primos ", Toast.LENGTH_LONG).show();

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        typicodeService = new Retrofit.Builder()
                .baseUrl("https://prime-number-api.onrender.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TypicodeService.class);


        AtomicBoolean isAscending = new AtomicBoolean(false);

        binding.iniciar.setOnClickListener(view -> {
//            shouldStart = false;
            executorService.execute(() -> {
                typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {
                    @Override
                    public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                        if (response.isSuccessful()  ) {
                            List<NumeroPrimo> primosList = response.body();

                            int delayMillis = 1000;

                            for (int i = 0; i < primosList.size(); i++) {
                                NumeroPrimo c = primosList.get(i);
                                final int index = i;

                                binding.primosVer.postDelayed(() -> {
                                    if (shouldStart) {
                                        // Ejecutar solo si shouldStart es verdadero
                                        Log.d("msg-test", String.valueOf(c.getNumber()));
                                        binding.primosVer.setText(String.valueOf(primosList.get(index).getNumber()));
                                    }
                                }, i * delayMillis);
                            }

                        } else {
                            Log.d("msg-test", "Error en la respuesta del webservice");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NumeroPrimo>> call, Throwable t) {
                        t.printStackTrace();
                    }

                });
            });

            isAscending.set(!isAscending.get());

        });

        Button buscarButton = findViewById(R.id.buscar);
        binding.buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.primosVer.removeCallbacks(null);
                String valorBuscado = binding.editTextText.getText().toString().trim();

                int value;
                try {
                    value = Integer.parseInt(valorBuscado);
                } catch (NumberFormatException e) {
                    Toast.makeText(ContadorActivity.this, "Ingrese un número válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                buscarPrimo(value);
            }
        });


/*
        if(tengoInternet()){
            typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {
                @Override
                public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                    //aca estoy en el UI Thread

                    if(response.isSuccessful()){
                        List<NumeroPrimo> primosList = response.body();

                        executorService.execute(() ->{
                            for (NumeroPrimo c : primosList) {
                                binding.primosVer.setText(String.valueOf(c.getNumber()));
                                Log.d("msg-test", String.valueOf(c.getNumber()));
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });




                    } else {
                        Log.d("msg-test", "error en la respuesta del webservice");
                    }
                }

                @Override
                public void onFailure(Call<List<NumeroPrimo>> call, Throwable t) {

                }


            });
        }

 */
    }

    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.d("msg-test-internet", "Internet: " + tieneInternet);
        return tieneInternet;
    }

    public void buscarPrimo(int valor){
        if(tengoInternet()){
//            binding.primosVer.removeCallbacks(null);
            shouldStart = false;
            typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {
                @Override
                public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                    if(response.isSuccessful()){
                        List<NumeroPrimo> primo = response.body();

                        for(NumeroPrimo c : primo){
                            if(valor==c.getOrder()){
                                binding.primosVer.setText(String.valueOf(c.getNumber()));
                                ascender(c.getOrder());
                                break;
                            }

                        }
                    }
                }
                @Override
                public void onFailure(Call<List<NumeroPrimo>> call, Throwable t) {

                }
            });

        }
    }





    public void ascender(int valor){
        if(tengoInternet()){
            typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {
                @Override
                public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                    if(response.isSuccessful()){

                        List<NumeroPrimo> primosList = response.body();

                        int delayMillis = 1000;
                        for (int i = valor; i < primosList.size(); i++) {
                            NumeroPrimo c = primosList.get(i);
                            final int index = i;

                            binding.primosVer.postDelayed(() -> {
                                Log.d("msg-test", String.valueOf(c.getNumber()));
                                binding.primosVer.setText(String.valueOf(primosList.get(index).getNumber()));
                            }, (i - valor) * delayMillis);
                        }

                    }
                }
                @Override
                public void onFailure(Call<List<NumeroPrimo>> call, Throwable t) {

                }
            });

        }
    }





    public void descender(int valor){
        if(tengoInternet()){
            typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {
                @Override
                public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                    if(response.isSuccessful()){

                        List<NumeroPrimo> primosList = response.body();

                        int delayMillis = 1000;
                        for (int i = valor; i < primosList.size(); i--) {
                            NumeroPrimo c = primosList.get(i);
                            final int index = i;

                            binding.primosVer.postDelayed(() -> {
                                Log.d("msg-test", String.valueOf(c.getNumber()));
                                binding.primosVer.setText(String.valueOf(primosList.get(index).getNumber()));
                            }, i * delayMillis);
                        }

                    }
                }
                @Override
                public void onFailure(Call<List<NumeroPrimo>> call, Throwable t) {

                }
            });

        }
    }




}