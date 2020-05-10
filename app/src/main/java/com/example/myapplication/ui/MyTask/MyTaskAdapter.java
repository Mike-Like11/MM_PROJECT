package com.example.myapplication.ui.MyTask;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Request;
import com.example.myapplication.R;
import com.example.myapplication.ui.FreeTask.FreeTaskAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder>{

    private List<Request> listData;
    private String s;
    private Context context;
    FirebaseDatabase db;
    private  boolean buto;
    DatabaseReference services;

    public MyTaskAdapter(List<Request> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_task,parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final MyTaskAdapter.ViewHolder holder, final int position) {
        final Request ld = listData.get(position);
        if (!ld.getName_1().equals("") && ld.getName_2().equals("No") && !ld.getTask().equals("")) {
            holder.txtname.setText(ld.getTask());
            holder.myTV.setText("Описание: " + ld.getDescription());
            holder.myData.setText("Дата: " + ld.getData());
            holder.txtmovie.setText("Исполнитель: " + "ещё не найден");
        }
        else{
            holder.txtname.setText(ld.getTask());
            holder.myTV.setText("Описание: " + ld.getDescription());
            holder.myData.setText("Дата: " + ld.getData());
            holder.txtmovie.setText("Исполнитель: " + ld.getName_2());
        }

        db = FirebaseDatabase.getInstance();
        services = db.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        final String email = user.getEmail();


        holder.btn_q.setOnClickListener(new View.OnClickListener() {
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

                final TextView tvc = comment_window.findViewById(R.id.comments);

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

                final AppCompatImageButton acbc = comment_window.findViewById(R.id.btn_cooment_next);
                acbc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yc.getText().toString().trim().equalsIgnoreCase("")) {
                            yc.setError("This field can not be blank");
                        } else {
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
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

                alertbox.show();
            }
        });

        holder.btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                services.child("Requests").child(ld.getTask()).removeValue();
                removeItem(position);

            }
        });
    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtavtor, txtname, txtmovie, nl, myAdress, myTV, myData;
        private AppCompatButton btn_q, btn_d;
        private ViewSwitcher switcher;
        private LikeButton lb;
        private String s;
        FirebaseDatabase db;
        DatabaseReference services;

        public ViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.req);
            txtmovie = (TextView) itemView.findViewById(R.id.ispol);
            //myAdress = (TextView) itemView.findViewById(R.id.adress);
            btn_d=(AppCompatButton)itemView.findViewById(R.id.btn_del);
            btn_q =(AppCompatButton) itemView.findViewById(R.id.btn_questions);
            myTV = (TextView) itemView.findViewById(R.id.avtor_2);
            myData = (TextView) itemView.findViewById(R.id.myData);
            db = FirebaseDatabase.getInstance();
            services = db.getReference();
            services.keepSynced(true);
        }
    }
}
