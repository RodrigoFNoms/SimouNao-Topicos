package com.example.simounao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestService {

    @GET(".")
    Call<Resposta> consultar(@Query("question") String pergunta);
}
