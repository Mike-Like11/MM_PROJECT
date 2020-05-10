package com.example.myapplication.ui.FreeTask;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Models.Article;
import com.example.myapplication.Models.Request;
import com.example.myapplication.R;
import com.example.myapplication.ui.Articles.ArticleAdapter;
import com.example.myapplication.ui.Articles.CommentAdapter;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.concurrent.TimeUnit;

public class FreeTaskAdapter extends RecyclerView.Adapter<FreeTaskAdapter.ViewHolder> {

    private List<Request> listData;
    private String s;
    private Context context;
    FirebaseDatabase db;
    private  boolean buto;
    DatabaseReference services;
    private CommentAdapter cadapter;
    private List<String>CommentData;
    public FreeTaskAdapter(List<Request> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_free_task,parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final FreeTaskAdapter.ViewHolder holder, final int position) {
        final Request ld = listData.get(position);
        if (!ld.getName_1().equals("") && !ld.getName_2().equals("") && !ld.getTask().equals("")) {
            holder.txtname.setText(ld.getTask());
            holder.txtmovie.setText("Имя заказчика: " + ld.getName_1());
            holder.myTV.setText("Описание: "+ld.getDescription());
            holder.myAdress.setText("Место выполнения: "+ld.getAddress());
            holder.myData.setText("Дата: "+ld.getData());
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

                LayoutInflater inflater=LayoutInflater.from(v.getRootView().getContext());
                View comment_window=inflater.inflate(R.layout.comment_window,null);
                alertbox.setView(comment_window);

                alertbox.setTitle("Вопросы");
                final MaterialEditText yc=comment_window.findViewById(R.id.your_Commnebt_Field);
                yc.setMinLines(2);
                RecyclerView rv_c=comment_window.findViewById(R.id.recyclerview2);
                rv_c.setHasFixedSize(true);
                rv_c.setLayoutManager(new LinearLayoutManager(v.getRootView().getContext()));
                CommentData=new ArrayList<>();
                alertbox.setView(comment_window);

                services.child("Requests").child(ld.getTask()).child("Comment").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        CommentData.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            CommentData.add(ds.getValue().toString());

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                cadapter=new CommentAdapter(CommentData,v.getRootView().getContext());
                rv_c.setAdapter(cadapter);
                final AppCompatImageButton acbc=comment_window.findViewById(R.id.btn_cooment_next);
                acbc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(yc.getText().toString().trim().equalsIgnoreCase("")){
                            yc.setError("This field can not be blank");
                        }
                        else {
                            services.child("Requests").child(ld.getTask()).child("Comment").push().setValue(name + ':' + yc.getText().toString());
                            yc.setText("");
                            services.child("Requests").child(ld.getTask()).child("Comment").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    CommentData.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        CommentData.add(ds.getValue().toString());
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

                alertbox.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
                cadapter=new CommentAdapter(CommentData,v.getRootView().getContext());
                rv_c.setAdapter(cadapter);
                alertbox.show();
            }
        });

        holder.btn_ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ld.setName_2(name);
                services.child("Requests").child(ld.getTask()).child("Task").setValue(ld);
                Snackbar.make(v, "Вы успешно взяли задание на выполнение!"+"\n"+"Вы можете найти его в разделе 'Мои выполняемые задания'", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtavtor, txtname, txtmovie, nl, myAdress, myTV, myData;
        private AppCompatButton btn_ta;
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
            btn_ta=(AppCompatButton)itemView.findViewById(R.id.btn_take);
            //btn_r=(AppCompatButton) itemView.findViewById(R.id.btn_a_r);*/
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

