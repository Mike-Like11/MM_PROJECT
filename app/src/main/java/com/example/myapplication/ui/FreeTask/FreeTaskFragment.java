package com.example.myapplication.ui.FreeTask;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Article;
import com.example.myapplication.Models.Like;
import com.example.myapplication.Models.Request;
import com.example.myapplication.R;
import com.example.myapplication.ui.Articles.ArticleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FreeTaskFragment extends Fragment {

    private FreeTaskModel freeTaskModel;


    private RecyclerView rv;

    private Context mContext;
    FirebaseDatabase db;
    DatabaseReference services;

    private List<Request> listData;

    private FreeTaskAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db= FirebaseDatabase.getInstance();
        services=db.getReference();
        mContext=getActivity();

        freeTaskModel = ViewModelProviders.of(this).get(FreeTaskModel.class);
        View root = inflater.inflate(R.layout.fragment_free_tasks, container, false);
        rv=(RecyclerView)root.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        listData=new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        final String email = user.getEmail();
        services.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listData.clear();
                    int i=0;
                    for (DataSnapshot npsnapshot : dataSnapshot.child("Requests").getChildren()) {
                        Request l = npsnapshot.child("Task").getValue(Request.class);
                        assert l != null;
                        if (!name.equals(l.getName_1())){
                            if (l.getName_2().equals("No") && !l.getTask().equals("") && !l.getTask().equals("В данный момент у вас нет выполняемых заданий") && !l.getTask().equals("У вас пока что нет активных просьб")) {
                                i++;
                                listData.add(l);
                            }
                        }
                    }
                    if (i==0){
                        Request r=new Request();
                        r.setName_1("Нету :(");
                        r.setName_2("1");
                        r.setEmail_2("1");
                        r.setStatus("1");
                        r.setTask("В данный момент нет доступных для выполнения просьб :(");
                        r.setDescription("Нету :(");
                        r.setAddress("Нету :(");
                        r.setData("Нету :(");
                        services.child("Requests").child(r.getTask()).child("Task").setValue(r);
                        listData.add(r);
                    }
                    adapter=new FreeTaskAdapter(listData,mContext);
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