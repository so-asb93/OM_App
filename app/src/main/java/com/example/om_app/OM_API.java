package com.example.om_app;

import retrofit2.Call;
import retrofit2.http.GET;
public interface OM_API {
    @GET("OM.json")
    Call<RestJoueursResponse> getJoueursResponse();

}