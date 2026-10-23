package com.safo.safoandroid.network;

import com.safo.safoandroid.model.Agendamento;
import com.safo.safoandroid.model.Colaborador;
import com.safo.safoandroid.model.Quadra;
import com.safo.safoandroid.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SafoApiService {

    // --- COLABORADORES ---
    @GET("colaboradores")
    Call<List<Colaborador>> getColaboradores();

    @POST("colaboradores")
    Call<Colaborador> salvarColaborador(@Body Colaborador colaborador);

    @PUT("colaboradores/{id}")
    Call<Colaborador> atualizarColaborador(@Path("id") Long id, @Body Colaborador colaborador);

    @DELETE("colaboradores/{id}")
    Call<Void> deletarColaborador(@Path("id") Long id);

    // --- QUADRAS ---
    @GET("quadras")
    Call<List<Quadra>> getQuadras();

    @POST("quadras")
    Call<Quadra> salvarQuadra(@Body Quadra quadra);

    @PUT("quadras/{id}")
    Call<Quadra> atualizarQuadra(@Path("id") Long id, @Body Quadra quadra);

    @DELETE("quadras/{id}")
    Call<Void> deletarQuadra(@Path("id") Long id);

    // --- AGENDAMENTOS ---
    @GET("agendamentos")
    Call<List<Agendamento>> getAgendamentos();

    @POST("agendamentos")
    Call<Agendamento> salvarAgendamento(@Body Agendamento agendamento);

    @DELETE("agendamentos/{id}")
    Call<Void> deletarAgendamento(@Path("id") Long id);

    // --- AUTENTICAÇÃO ---
    @POST("auth/login")
    Call<Usuario> login(@Body Usuario usuario);

    @POST("auth/register")
    Call<Usuario> cadastrar(@Body Usuario usuario);

    @PUT("agendamentos/{id}")
    Call<Agendamento> atualizarAgendamento(@Path("id") Long id, @Body Agendamento agendamento);
}