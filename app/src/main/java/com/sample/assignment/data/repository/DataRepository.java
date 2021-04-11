package com.sample.assignment.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.sample.assignment.data.api.ApiRequest;
import com.sample.assignment.data.api.RetrofitRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DataRepository {

    private static final String TAG = DataRepository.class.getSimpleName();
    private ApiRequest apiRequest;

    public DataRepository() {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
    }

        public MutableLiveData<JsonArray> getPostOffice(String pincode) {
        final MutableLiveData<JsonArray> data = new MutableLiveData<>();
        apiRequest.getData(pincode).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                         Log.d(TAG, "onResponse response:: " + response);
                        if (response.body() != null && response.code() == 200 ) {
                             Log.d(TAG, " total result:: " + response.body());
                            data.postValue(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        data.postValue(null);
                    }
                });
        return data;
    }
}
