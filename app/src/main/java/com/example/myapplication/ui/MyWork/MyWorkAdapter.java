package com.example.myapplication.ui.MyWork;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Message;
import com.example.myapplication.Models.Request;
import com.example.myapplication.R;
import com.example.myapplication.ui.MyTask.MyTaskCommentAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class MyWorkAdapter extends RecyclerView.Adapter<MyWorkAdapter.ViewHolder> {

    private List<Request> listData;
    private String s;
    private Context context;
    FirebaseDatabase db;
    DatabaseReference services;
    private MyTaskCommentAdapter madapter;
    private List<Message>MyTaskCommentData;
    public MyWorkAdapter(List<Request> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_work, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final MyWorkAdapter.ViewHolder holder, final int position) {
        final Request ld = listData.get(position);
        if (!ld.getName_1().equals("") && !ld.getName_2().equals("") && !ld.getTask().equals("")) {
            holder.txtname.setText(ld.getTask());
            holder.txtmovie.setText("Имя заказчика: " + ld.getName_1());
            holder.myTV.setText("Описание: " + ld.getDescription());
            holder.myAdress.setText("Место выполнения: " + ld.getAddress());
            holder.myData.setText("Дата: " + ld.getData());
        }

        db = FirebaseDatabase.getInstance();
        services = db.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        final String email = user.getEmail();


        holder.btn_co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                AlertDialog alert = alertbox.create();

                LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
                View comment_window = inflater.inflate(R.layout.comment_window, null);
                alertbox.setView(comment_window);

                alertbox.setTitle("Вопросы");
                final MaterialEditText yc = comment_window.findViewById(R.id.your_Commnebt_Field);
                yc.setMinLines(2);
                final RecyclerView rv_c=comment_window.findViewById(R.id.recyclerview2);
                rv_c.setHasFixedSize(true);
                rv_c.setLayoutManager(new LinearLayoutManager(v.getRootView().getContext()));
                MyTaskCommentData=new ArrayList<>();
                services.child("Requests").child(ld.getTask()).child("Messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MyTaskCommentData.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Message m=ds.getValue(Message.class);
                            MyTaskCommentData.add(m);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final AppCompatImageButton acbc = comment_window.findViewById(R.id.btn_cooment_next);
                acbc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yc.getText().toString().trim().equalsIgnoreCase("")) {
                            yc.setError("This field can not be blank");
                        } else {
                            Message m=new Message(name,yc.getText().toString());
                            services.child("Requests").child(ld.getTask()).child("Messages").push().setValue(m);
                            yc.setText("");
                            MyTaskCommentData.add(m);
                            madapter=new MyTaskCommentAdapter(MyTaskCommentData,v.getRootView().getContext());
                            rv_c.setAdapter(madapter);
                        }
                    }
                });
                madapter=new MyTaskCommentAdapter(MyTaskCommentData,v.getRootView().getContext());
                rv_c.setAdapter(madapter);
                alertbox.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

                alertbox.show();
            }
        });

        holder.btn_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ld.setName_2("No");
                services.child("Requests").child(ld.getTask()).child("Task").setValue(ld);
                services.child("Requests").child(ld.getTask()).child("Messages").removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtavtor, txtname, txtmovie, nl, myAdress, myTV, myData;
        private AppCompatButton btn_r;
        private AppCompatButton btn_co;
        private ViewSwitcher switcher;
        private LikeButton lb;
        private String s;
        FirebaseDatabase db;
        DatabaseReference services;

        public ViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.req);
            txtmovie = (TextView) itemView.findViewById(R.id.avtor);
            myAdress = (TextView) itemView.findViewById(R.id.adress);
            //btn_ta=(AppCompatButton)itemView.findViewById(R.id.btn_take);
            btn_r=(AppCompatButton) itemView.findViewById(R.id.btn_refuse);
            btn_co = (AppCompatButton) itemView.findViewById(R.id.btn_cooments);

            myTV = (TextView) itemView.findViewById(R.id.avtor_2);
            myData = (TextView) itemView.findViewById(R.id.myData);
            //lb = (LikeButton) itemView.findViewById(R.id.star_button);
            db = FirebaseDatabase.getInstance();
            services = db.getReference();
            services.keepSynced(true);

        }
    }



}