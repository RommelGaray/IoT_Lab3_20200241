package com.example.lab3_20200241;

import com.example.lab3_20200241.dto.Pelicula;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TypicodeService {
    @GET("/?apikey=bf81d461&i=tt3896198")
    Call<Pelicula> getPelicula();
}
