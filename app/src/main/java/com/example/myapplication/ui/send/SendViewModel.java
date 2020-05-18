package com.example.myapplication.ui.send;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Вы сможете воспользоваться данным разделом в следующей версии приложения :)");
    }

    public LiveData<String> getText() {
        return mText;
    }
}