package com.safo.safoandroid.network;

import com.safo.safoandroid.model.Colaborador;
import com.safo.safoandroid.model.Quadra;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SafoApiService {

    @GET("colaboradores")
    Call<List<Colaborador>> getColaboradores();

    @POST("colaboradores")
    Call<Colaborador> salvarColaborador(@Body Colaborador colaborador);

    @PUT("colaboradores/{id}")
    Call<Colaborador> atualizarColaborador(@Path("id") Long id, @Body Colaborador colaborador);

    @DELETE("colaboradores/{id}")
    Call<Void> deletarColaborador(@Path("id") Long id);

    @GET("quadras")
    Call<List<Quadra>> getQuadras();

    @POST("quadras")
    Call<Quadra> salvarQuadra(@Body Quadra quadra);

    @PUT("quadras/{id}")
    Call<Quadra> atualizarQuadra(@Path("id") Long id, @Body Quadra quadra);

    @DELETE("quadras/{id}")
    Call<Void> deletarQuadra(@Path("id") Long id);
}