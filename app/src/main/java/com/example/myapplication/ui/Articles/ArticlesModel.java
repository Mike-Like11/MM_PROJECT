package com.example.myapplication.ui.Articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArticlesModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ArticlesModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is articles fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}