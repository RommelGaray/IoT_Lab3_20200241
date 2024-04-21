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

            executorService.execute(() -> {
                typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {

                    @Override
                    public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                        if (response.isSuccessful()  ) {
                            binding.iniciar.setText("Descender");
                            List<NumeroPrimo> primosList = response.body();
                            int delayMillis = 1000;
                            for (int i = 0; i < primosList.size(); i++) {
                                NumeroPrimo c = primosList.get(i);
                                final int index = i;
                                binding.primosVer.postDelayed(() -> {
                                    if (shouldStart) {
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

            /** Se intento hacer una validación para el botón de Ascender/Descender
             * Sin embargo no funciona correctamente. En estas validación se hacia uso
             * de la función "ascender" y "descender". **/
            /**
            if(binding.iniciar.getText().toString().trim().equals("Iniciar")){
                executorService.execute(() -> {
                    typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {

                        @Override
                        public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                            if (response.isSuccessful()  ) {
                                binding.iniciar.setText("Descender");
                                List<NumeroPrimo> primosList = response.body();
                                int delayMillis = 1000;
                                for (int i = 0; i < primosList.size(); i++) {
                                    NumeroPrimo c = primosList.get(i);
                                    final int index = i;
                                    binding.primosVer.postDelayed(() -> {
                                        if (shouldStart) {
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

            } else if (binding.iniciar.getText().toString().trim().equals("Descender")) {
                executorService.execute(() -> {
                    typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {

                        @Override
                        public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                            if (response.isSuccessful()  ) {
                                binding.iniciar.setText("Ascender");

                                String valorBuscado = binding.primosVer.getText().toString();
                                int value = Integer.parseInt(valorBuscado);
                                descender(value);

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

            } else if (binding.iniciar.getText().toString().trim().equals("Ascender")){
                executorService.execute(() -> {
                    typicodeService.getNumeroPrimo().enqueue(new Callback<List<NumeroPrimo>>() {

                        @Override
                        public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                            if (response.isSuccessful()  ) {
                                binding.iniciar.setText("Descender");

                                String valorBuscado = binding.primosVer.getText().toString();
                                int value = Integer.parseInt(valorBuscado);
                                ascender(value);

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
            }
             **/

            isAscending.set(!isAscending.get());

        });

        binding.buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.primosVer.removeCallbacks(null);
                String valorBuscado = binding.editTextText.getText().toString().trim();

                int value;
                try {
                    value = Integer.parseInt(valorBuscado);
                } catch (NumberFormatException e) {
                    Toast.makeText(ContadorActivity.this, "Ingrese un número válido [1-999]", Toast.LENGTH_SHORT).show();
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
//                                descender(c.getOrder());
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




    /** Se crean funciones para ascender o descender, funcionan correctamente, pero no le lograron implementar correctamente,
     * para lo que se solicitaba **/

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
                        for (int i = valor; i >= 0; i--) {
                            NumeroPrimo c = primosList.get(i);
                            final int index = i;

                            binding.primosVer.postDelayed(() -> {
                                Log.d("msg-test", String.valueOf(c.getNumber()));
                                binding.primosVer.setText(String.valueOf(primosList.get(index).getNumber()));
                            }, (valor - i) * delayMillis);
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