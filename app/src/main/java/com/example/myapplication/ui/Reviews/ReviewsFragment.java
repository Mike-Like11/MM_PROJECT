package com.example.myapplication.ui.Reviews;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MapActivity;
import com.example.myapplication.Models.Request;
import com.example.myapplication.Models.Review;
import com.example.myapplication.Models.Reviews;
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

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {
    private ReviewsModel reviewsModel;
    private RecyclerView rv;
    private Context mContext;
    FloatingActionButton fbb;
    private List<Reviews> listData;
    FirebaseDatabase db;
    DatabaseReference services;
    private ReviewsAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        db=FirebaseDatabase.getInstance();
        services=db.getReference();

        reviewsModel = ViewModelProviders.of(this).get(ReviewsModel.class);
        View root = inflater.inflate(R.layout.fragment_reviews, container, false);;
        fbb=(FloatingActionButton) root.findViewById(R.id.fab_reviews);
        rv=(RecyclerView)root.findViewById(R.id.recycler_view_reviews);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        listData=new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        fbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewWindow();
            }
        });

        services.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listData.clear();
                    for (DataSnapshot npsnapshot : dataSnapshot.child("Reviews about us").getChildren()) {
                        Reviews l = npsnapshot.getValue(Reviews.class);
                        assert l != null;
                        listData.add(l);
                    }
                    adapter=new ReviewsAdapter(listData,mContext);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }


    private void showReviewWindow() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);

        dialog.setTitle("Ваша отзыв");
        dialog.setMessage("Здесь вы можете оставить отзыв о нашем проекте :)");
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View review_window=inflater.inflate(R.layout.review_window,null);
        dialog.setView(review_window);
        final MaterialEditText review=review_window.findViewById(R.id.review_field);

        dialog.setPositiveButton("Добавить", null);
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface , int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


            }

        });

        final AlertDialog dialog1=dialog.create();
        dialog1.show();
        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(review.getText().toString())) {
                    review.setError("Поле отзыва пустое") ;
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = user.getDisplayName();
                String email = user.getEmail();
                Reviews r=new Reviews();
                r.setName(name);
                r.setDescription(review.getText().toString());
                services.child("Reviews about us").child(name+" "+r.getDescription()).setValue(r).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Отзыв успешно добавлен!"+"\n"+"Спасибо, что пользуетесь нашим сервисом!", Snackbar.LENGTH_SHORT).show();
                        dialog1.dismiss();
                        dialog1.cancel();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Ошибка произошла " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }


                });
            }
        });
    }
}
