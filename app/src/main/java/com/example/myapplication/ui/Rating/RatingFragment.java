package com.example.myapplication.ui.Rating;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Rating;
import com.example.myapplication.Models.User;
import com.example.myapplication.Models.User_name;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RatingFragment extends Fragment {
    private RatingModel ratingModel;
    private RecyclerView rv;
    private Context mContext;
    private List<Rating> listData;
    FirebaseDatabase db;
    DatabaseReference services;
    private RatingAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        db=FirebaseDatabase.getInstance();
        services=db.getReference();

        ratingModel = ViewModelProviders.of(this).get(RatingModel.class);
        View root = inflater.inflate(R.layout.fragment_rating, container, false);
        rv=(RecyclerView)root.findViewById(R.id.recycler_view_rating);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        listData=new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        services.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listData.clear();
                    int i=0;
                    for (DataSnapshot npsnapshot : dataSnapshot.child("Users").getChildren()) {
                        if (!npsnapshot.getKey().equals("User_name") && i<20) {
                            i++;
                            User U = npsnapshot.getValue(User.class);
                            Rating l = new Rating();
                            l.setName(U.getName());
                            l.setRating(U.getRating());
                            assert l != null;
                            listData.add(l);
                        }
                    }
                    Collections.sort(listData, new Comparator<Rating>() {
                        @Override
                        public int compare(Rating left, Rating right) {
                            return right.getRating().compareTo(left.getRating());
                        }
                    });
                    adapter=new RatingAdapter(listData,mContext);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
}