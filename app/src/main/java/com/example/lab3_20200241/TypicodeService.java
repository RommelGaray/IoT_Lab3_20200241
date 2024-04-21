package com.example.lab3_20200241;

import com.example.lab3_20200241.dto.NumeroPrimo;
import com.example.lab3_20200241.dto.Pelicula;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TypicodeService {
    @GET("/")
    Call<Pelicula> getPelicula( @Query("apikey") String apikey,
                                @Query("i") String i);

    @GET("/primeNumbers?len=999&order=1")
    Call<List<NumeroPrimo>> getNumeroPrimo();

//    Call<NumeroPrimo> getNumeroPrimo( @Query("apikey") String apikey,
//                                      @Query("i") String i);
}
