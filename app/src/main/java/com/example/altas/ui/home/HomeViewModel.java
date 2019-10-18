package com.example.altas.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mutableLiveText;

    public HomeViewModel() {
        mutableLiveText = new MutableLiveData<>();
        mutableLiveText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mutableLiveText;
    }
}