package com.sample.assignment.data.api;


import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiRequest {

    @GET("pincode/{number}")
    Call<JsonArray> getData(@Path("number") String pincode);
}
