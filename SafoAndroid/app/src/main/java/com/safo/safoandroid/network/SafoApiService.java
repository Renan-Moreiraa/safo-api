package com.safo.safoandroid.network;

import com.safo.safoandroid.model.Colaborador;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SafoApiService {

    @GET("colaboradores")
    Call<List<Colaborador>> getColaboradores();

    @POST("colaboradores")
    Call<Colaborador> salvarColaborador(@Body Colaborador colaborador);
}