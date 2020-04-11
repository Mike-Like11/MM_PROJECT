package com.example.myapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FreeTaskModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FreeTaskModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is free task fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}