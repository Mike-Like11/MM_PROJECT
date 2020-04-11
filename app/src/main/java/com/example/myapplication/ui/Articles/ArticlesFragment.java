package com.example.myapplication.ui.Articles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class ArticlesFragment extends Fragment {

    private ArticlesModel articlesModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        articlesModel =
                ViewModelProviders.of(this).get(ArticlesModel.class);
        View root = inflater.inflate(R.layout.fragment_articles, container, false);
        final TextView textView = root.findViewById(R.id.text_articles);
        articlesModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}