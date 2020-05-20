package com.example.myapplication.ui.MyWork;

import android.content.Context;
import android.content.Intent;
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

import com.example.myapplication.MapActivity;
import com.example.myapplication.Models.Request;
import com.example.myapplication.R;
import com.example.myapplication.ui.MyTask.MyTaskAdapter;
import com.example.myapplication.ui.MyTask.MyTaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyWorkFragment extends Fragment {

    private MyWorkModel myWorkModel;
    private RecyclerView rv;
    private Context mContext;
    FloatingActionButton fb;
    private List<Request> listData;
    FirebaseDatabase db;
    DatabaseReference services;
    private MyWorkAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myWorkModel = ViewModelProviders.of(this).get(MyWorkModel.class);
        View root = inflater.inflate(R.layout.fragment_my_work, container, false);
        Intent intent=new Intent(getContext(), MapActivity.class);
        mContext=getActivity();
        db= FirebaseDatabase.getInstance();
        services=db.getReference();
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
                        if (name.equals(l.getName_2())){
                            if (!l.getName_2().equals("") && !l.getTask().equals("")) {
                                i++;
                                listData.add(l);
                            }
                        }
                    }
                    if (i==0){
                        Request r=new Request();
                        r.setName_1(name);
                        r.setName_2("No");
                        r.setEmail_2("No");
                        r.setStatus("Исполнитель не найден");
                        r.setTask("В данный момент у вас нет выполняемых заданий");
                        r.setDescription("Исполнитель не найден");
                        r.setAddress(" Исполнитель не найден");
                        r.setData("Исполнитель не найден");
                        services.child("Requests").child(r.getTask()).child("Task").setValue(r);
                        listData.add(r);
                    }
                    adapter=new MyWorkAdapter(listData,mContext);
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