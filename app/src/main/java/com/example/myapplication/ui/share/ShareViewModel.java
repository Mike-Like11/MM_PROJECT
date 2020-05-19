package com.example.myapplication.ui.share;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShareViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Вы сможете воспользоваться данным разделом в следующей версии приложения :)");
    }

    public LiveData<String> getText() {
        return mText;
    }
}