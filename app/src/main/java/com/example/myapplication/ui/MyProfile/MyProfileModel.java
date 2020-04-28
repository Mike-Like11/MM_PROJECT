package com.example.myapplication.ui.MyProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyProfileModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyProfileModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Настройки вашей учётной записи");
    }

    public LiveData<String> getText() {
        return mText;
    }
}