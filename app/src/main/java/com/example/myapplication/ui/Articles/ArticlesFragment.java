package com.example.myapplication.ui.Articles;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MaiwActivity;
import com.example.myapplication.MapActivity;
import com.example.myapplication.Models.Article;
import com.example.myapplication.Models.Request;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ArticlesFragment extends Fragment {

     ArticlesModel articlesModel;
     FloatingActionButton fb;
    private Context mContext;
    FirebaseDatabase db;
    DatabaseReference services;
    private List<Article> ItemList;
    TextView txt;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db= FirebaseDatabase.getInstance();
        services=db.getReference();
        mContext=getActivity();

        articlesModel =
                ViewModelProviders.of(this).get(ArticlesModel.class);
        final View root = inflater.inflate(R.layout.fragment_articles, container, false);
        fb=(FloatingActionButton) root.findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showArticleWindow();
            }
        });
        services.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                txt = (TextView) root.findViewById(R.id.text_bottom);
                txt.setText(null);
                for (DataSnapshot country : dataSnapshot.child("Articles").getChildren()) {
                    txt.append(country.getKey() + " " + (country.child("name_narrator").getValue()) + " " + (country.child("description").getValue()) + "\n");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
    private void showArticleWindow() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
        dialog.setTitle("Ваш рассказ");
        dialog.setMessage("Введите все, что хотите поведать ");
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View article_window=inflater.inflate(R.layout.article_window,null);
        dialog.setView(article_window);
        final MaterialEditText name_a=article_window.findViewById(R.id.name_article_Field);
        final MaterialEditText description=article_window.findViewById(R.id.description_article_Field);
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if (TextUtils.isEmpty(name_a.getText().toString())) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Вы забыли ввести ваше название", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Name, email address, and profile photo Url
                String name = user.getDisplayName();
                String email = user.getEmail();
                Article a=new Article();
                a.setName_narrator(name);
                a.setName(name_a.getText().toString());
                a.setDescription(description.getText().toString());
                services.child("Articles").child(name_a.getText().toString()).setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "произошла ошибка" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                    ;
                });
            }

        });


        dialog.show();
    }
}