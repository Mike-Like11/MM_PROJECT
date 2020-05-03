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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MaiwActivity;
import com.example.myapplication.MapActivity;
import com.example.myapplication.Models.Article;
import com.example.myapplication.Models.Like;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ArticlesFragment extends Fragment {

     ArticlesModel articlesModel;
     FloatingActionButton fb;
    private Context mContext;
    FirebaseDatabase db;
    DatabaseReference services;
    private List<Article> ItemList;
    TextView txt;
    private List<Article>listData;
    private RecyclerView rv;
    private ArticleAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db= FirebaseDatabase.getInstance();
        services=db.getReference();
        mContext=getActivity();


        articlesModel =
                ViewModelProviders.of(this).get(ArticlesModel.class);
        final View root = inflater.inflate(R.layout.fragment_articles, container, false);
        fb=(FloatingActionButton) root.findViewById(R.id.fab);
        rv=(RecyclerView)root.findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        listData=new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Name, email address, and profile photo Url
        final String name = user.getDisplayName();
        final String email = user.getEmail();
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
                /*

                txt = (TextView) root.findViewById(R.id.text_bottom);
                txt.setText(null);
                for (DataSnapshot country : dataSnapshot.child("Articles").getChildren()) {
                    txt.append(country.getKey() + " " + (country.child("name_narrator").getValue()) + " " + (country.child("description").getValue()) + "\n");

                }
                */
                if (dataSnapshot.exists()){
                    listData.clear();
                  //GenericTypeIndicator<ArrayList<Article>>t=new GenericTypeIndicator<ArrayList<Article>>(){};
                    //listData=dataSnapshot.child("Articles").getValue(t);
                    for (DataSnapshot npsnapshot : dataSnapshot.child("Articles").getChildren()){
                        Article l=npsnapshot.child("Article").getValue(Article.class);
                        boolean liki=false;
                        for(DataSnapshot ds:npsnapshot.child("Likes").getChildren()) {
                         Like like=ds.getValue(Like.class);
                            if (Objects.equals(like.getAmail(), name)) {
                              l.setLike_or_not(true);
                              liki=true;
                               break;
                            }
                        }
                        if(!liki && l!=null){
                            l.setLike_or_not(false);
                        }


                        assert l != null;
                        if(!l.getName().equals("") &&!l.getDescription().equals("")) {
                            listData.add(l);
                        }

                    }


                    adapter=new ArticleAdapter(listData,mContext);
                    rv.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
    private void showArticleWindow() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("Ваш рассказ");
        dialog.setMessage("Введите все, что хотите поведать ");
        dialog.setNegativeButton("Отменить", null);
        dialog.setPositiveButton("Добавить", null);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View article_window = inflater.inflate(R.layout.article_window, null);
        dialog.setView(article_window);
        final MaterialEditText name_a = article_window.findViewById(R.id.name_article_Field);
        final MaterialEditText description = article_window.findViewById(R.id.description_article_Field);
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


            }

        });


        final AlertDialog dialog1 = dialog.create();
        dialog1.show();
        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name_a.getText().toString())) {
                    name_a.setError("Вы забыли ввести ваше название");
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Name, email address, and profile photo Url
                String name = user.getDisplayName();
                String email = user.getEmail();
                Article a = new Article();
                a.setName_narrator(name);
                a.setName(name_a.getText().toString());
                a.setDescription(description.getText().toString());
                a.setLike(0);
                final Like l = new Like();
                l.setAmail("aaaa");
                a.setLike_or_not(false);

                services.child("Articles").child(name_a.getText().toString()).child("Article").setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        services.child("Articles").child(name_a.getText().toString()).child("Likes").child(l.getAmail()).setValue(l);
                        dialog1.dismiss();
                        dialog1.cancel();

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
    }
}