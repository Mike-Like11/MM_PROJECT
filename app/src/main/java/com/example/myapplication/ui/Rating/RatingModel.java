package com.example.myapplication.ui.Rating;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RatingModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RatingModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Rating fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
