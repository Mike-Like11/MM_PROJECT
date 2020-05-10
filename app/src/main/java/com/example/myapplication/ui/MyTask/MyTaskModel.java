package com.example.myapplication.ui.MyTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyTaskModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyTaskModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is free task fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
