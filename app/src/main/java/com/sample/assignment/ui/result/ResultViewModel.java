package com.sample.assignment.ui.result;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.sample.assignment.data.repository.DataRepository;

public class ResultViewModel extends AndroidViewModel {


    private DataRepository dataRepository;
    private MutableLiveData<JsonArray> liveData;

    public ResultViewModel(@NonNull Application application){
        super(application);
    }

    public LiveData<JsonArray> getPostOfficeResponseLiveData(String pincode) {
        dataRepository = new DataRepository();
        this.liveData = dataRepository.getPostOffice(pincode);
        return liveData;
    }

}