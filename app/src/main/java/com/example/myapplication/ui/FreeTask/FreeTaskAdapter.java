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
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Models.Article;
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
            holder.txtname.setText(ld.getName_2());
            holder.txtmovie.setText("Автор: " + ld.getName_1());
            holder.myTV.setText(ld.getTask());
            holder.myAdress.setText(ld.getAddress());
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

                alertbox.setTitle("Комментарии");
                final MaterialEditText yc=comment_window.findViewById(R.id.your_Commnebt_Field);
                yc.setMinLines(2);

                final TextView tvc=comment_window.findViewById(R.id.comments);

                tvc.setText("здесь ничего нет");
                services.child("Requests").child(ld.getName_2()).child("Comment").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tvc.setText("");
                        String s = "";
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            tvc.append(ds.getValue().toString());
                            tvc.append(System.getProperty("line.separator"));

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final AppCompatImageButton acbc=comment_window.findViewById(R.id.btn_cooment_next);
                acbc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(yc.getText().toString().trim().equalsIgnoreCase("")){
                            yc.setError("This field can not be blank");
                        }
                        else {
                            services.child("Requests").child(ld.getName_2()).child("Comment").push().setValue(name + ':' + yc.getText().toString());
                            yc.setText("");
                            services.child("Requests").child(ld.getName_2()).child("Comment").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tvc.setText("");
                                    String s = "";
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        tvc.append(ds.getValue().toString());
                                        tvc.append(System.getProperty("line.separator"));
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

                alertbox.show();
            }
        });

        if(!ld.user_s(ld.getName_1(),name)){
            holder.btn_ta.setVisibility(View.VISIBLE);

            holder.btn_ta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View View) {


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {



                        }
                    },500);




                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtavtor, txtname, txtmovie, nl, myAdress;
        private EditText myTV;
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

            myTV = (EditText) itemView.findViewById(R.id.avtor_2);
            //lb = (LikeButton) itemView.findViewById(R.id.star_button);
            db = FirebaseDatabase.getInstance();
            services = db.getReference();
            services.keepSynced(true);

        }
    }
}

