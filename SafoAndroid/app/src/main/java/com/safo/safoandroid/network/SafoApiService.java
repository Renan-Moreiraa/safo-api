package com.safo.safoandroid.network;

import com.safo.safoandroid.model.Colaborador;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SafoApiService {

    @GET("colaboradores")
    Call<List<Colaborador>> getColaboradores();
}