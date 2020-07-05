package com.example.om_app;

import retrofit2.Call;
import retrofit2.http.GET;
public interface OM_API {
    @GET("/api/v2/pokemon")
    Call<RestJoueursResponse> getJoueursResponse();

}