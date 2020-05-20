package com.example.myapplication.ui.MyTask;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Message;
import com.example.myapplication.Models.Request;
import com.example.myapplication.Models.Review;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ui.Articles.CommentAdapter;
import com.example.myapplication.ui.FreeTask.FreeTaskAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder>{

    private List<Request> listData;
    private String s;
    private Context context;
    FirebaseDatabase db;
    private  boolean buto;
    DatabaseReference services;
    private MyTaskCommentAdapter madapter;
    private List<Message>MyTaskCommentData;
    private CommentAdapter cadapter;
    private List<String>CommentData;

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
        db = FirebaseDatabase.getInstance();
        services = db.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        final String email = user.getEmail();

        if (ld.getTask().equals("У вас пока что нет активных просьб")){
            holder.txtname.setText(ld.getTask());
            holder.btn_q_a.setVisibility(View.INVISIBLE);
            holder.btn_q.setVisibility(View.INVISIBLE);
            holder.btn_d.setVisibility(View.INVISIBLE);
        }

        else if (!ld.getName_1().equals("") && ld.getName_2().equals("No") && !ld.getTask().equals("")) {
            holder.txtname.setText(ld.getTask());
            holder.myTV.setText("Описание: " + ld.getDescription());
            holder.myData.setText("Дата: " + ld.getData());
            holder.txtmovie.setText("Исполнитель: " + "ещё не найден");
            holder.txtst.setText("Статус"+ld.getStatus());
            holder.mc.setStrokeColor(Color.RED);

            holder.mc.setStrokeWidth(5);
            holder.btn_q.setVisibility(View.INVISIBLE);


        }
        else{
            holder.txtname.setText(ld.getTask());
            holder.myTV.setText("Описание: " + ld.getDescription());
            holder.myData.setText("Дата: " + ld.getData());
            holder.txtmovie.setText("Исполнитель: " + ld.getName_2()+" (смотреть профиль)");
            holder.txtst.setText("Статус: "+ld.getStatus());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date currentTime = Calendar.getInstance().getTime();
            Date strDate = currentTime;
            try {
                strDate = sdf.parse(ld.getData());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (currentTime.after(strDate)) {
                if(ld.getStatus().equals("Просьба выполнена")){
                    holder.mc.setStrokeColor(Color.GREEN);
                }
                else {
                    holder.mc.setStrokeColor(Color.YELLOW);
                }
            }
            else{
                holder.mc.setStrokeColor(Color.BLUE);
            }

            holder.btn_d.setText("Завершить");
            holder.btn_q.setVisibility(View.VISIBLE);
            holder.btn_q.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                    AlertDialog alert = alertbox.create();

                    LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
                    View comment_window = inflater.inflate(R.layout.comment_window, null);
                    alertbox.setView(comment_window);
                    final RecyclerView rv_c=comment_window.findViewById(R.id.recyclerview2);
                    rv_c.setHasFixedSize(true);
                    rv_c.setItemViewCacheSize(10);
                    rv_c.setLayoutManager(new LinearLayoutManager(v.getRootView().getContext()));
                    MyTaskCommentData=new ArrayList<>();
                    alertbox.setTitle("Вопросы");
                    final MaterialEditText yc = comment_window.findViewById(R.id.your_Commnebt_Field);
                    yc.setMinLines(2);
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
                    alertbox.setView(comment_window);
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


            holder.mc.setStrokeWidth(5);
        }
        holder.btn_q_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                AlertDialog alert = alertbox.create();

                LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
                View comment_window = inflater.inflate(R.layout.comment_window, null);
                alertbox.setView(comment_window);
                final RecyclerView rv_c=comment_window.findViewById(R.id.recyclerview2);
                rv_c.setHasFixedSize(true);
                rv_c.setItemViewCacheSize(10);
                rv_c.setLayoutManager(new LinearLayoutManager(v.getRootView().getContext()));
                CommentData=new ArrayList<>();
                alertbox.setTitle("Вопросы");
                final MaterialEditText yc = comment_window.findViewById(R.id.your_Commnebt_Field);
                yc.setMinLines(2);
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


                alertbox.setView(comment_window);

                final AppCompatImageButton acbc = comment_window.findViewById(R.id.btn_cooment_next);
                acbc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yc.getText().toString().trim().equalsIgnoreCase("")) {
                            yc.setError("This field can not be blank");
                        } else {
                            services.child("Requests").child(ld.getTask()).child("Comment").push().setValue(name + ':' + yc.getText().toString());
                            CommentData.add(name + ':' + yc.getText().toString());
                            yc.setText("");
                            cadapter=new CommentAdapter(CommentData,v.getRootView().getContext());
                            rv_c.setAdapter(cadapter);
                        }
                    }
                });
                cadapter=new CommentAdapter(CommentData,v.getRootView().getContext());
                rv_c.setAdapter(cadapter);
                alertbox.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
                alertbox.show();
            }
        });
        if (!ld.getName_1().equals("") && !ld.getName_2().equals("No") && !ld.getTask().equals("")) {
            holder.txtmovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                    dialog.setTitle("Профиль "+ld.getName_2());
                    LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
                    View profile_window = inflater.inflate(R.layout.profile, null);
                    dialog.setView(profile_window);
                    final MaterialTextView r_v= profile_window.findViewById(R.id.rating_progile);
                    final MaterialTextView t_n_v = profile_window.findViewById(R.id.tast_no_profile);
                    final MaterialTextView t_v = profile_window.findViewById(R.id.task_profile);
                    services.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if(!Objects.equals(ds.getKey(), "User_name")) {
                                    User j = ds.getValue(User.class);
                                    if (Objects.requireNonNull(j).getEmail().equals(ld.getEmail_2()) && j.getEmail() != null) {
                                        r_v.setText("Рейтинг: "+j.getRating());
                                        t_n_v.setText("Задания, от который отказался: "+j.getTask_not_done());
                                        t_v.setText("Задания, которые выполнил: "+j.getTask_done());
                                        break;
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    dialog.setPositiveButton("Понятно", null);

                    final AlertDialog dialog1 = dialog.create();
                    dialog1.show();
                    dialog.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialog1.dismiss();
                        }

                    });



                }
            });
        }


        if(holder.btn_d.getText()=="Завершить") {
            holder.btn_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());

                    dialog.setTitle("Отзыв");
                    dialog.setMessage("Оцените испольнение вашей просьбы ");
                    LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
                    View estimate_window = inflater.inflate(R.layout.estimate_task, null);
                    dialog.setView(estimate_window);
                    final MaterialTextView name_est = estimate_window.findViewById(R.id.name_estimate_Field);
                    name_est.setText(ld.getName_2());
                    final MaterialEditText description_est =estimate_window.findViewById(R.id.description_estimate_Field);
                   final RatingBar rb=estimate_window.findViewById(R.id.stars_number_estimate);

                    dialog.setNegativeButton("Отменить", null);
                    dialog.setPositiveButton("Оценить", null);
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
                            final Review r=new Review();
                            r.setDescription(description_est.getText().toString());
                            r.setName(ld.getName_2());
                            r.setRating(rb.getNumStars()*5);
                            services.child("Reviews").child(ld.getName_2()).setValue(r);
                            services.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if(!Objects.equals(ds.getKey(), "User_name")) {
                                            User j = ds.getValue(User.class);
                                            if (Objects.requireNonNull(j).getEmail().equals(ld.getEmail_2()) && j.getEmail() != null) {
                                                j.setRating(j.getRating()+r.getRating());
                                                j.setTask_done(j.getTask_done()+1);
                                                ds.getRef().setValue(j);
                                                break;
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            services.child("Requests").child(ld.getTask()).removeValue();
                            removeItem(position);
                            dialog1.dismiss();
                        }
                    });

                }
            });
        }
        else {
            holder.btn_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    services.child("Requests").child(ld.getTask()).removeValue();
                    removeItem(position);
                }
            });
        }

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
        private TextView txtavtor, txtname, txtmovie, nl, myAdress, myTV, myData,txtst;
        private MaterialCardView mc;
        private AppCompatButton btn_q, btn_d,btn_q_a,btn_e;
        private ViewSwitcher switcher;
        private LikeButton lb;
        private String s;
        FirebaseDatabase db;
        DatabaseReference services;

        public ViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.req);
            txtmovie = (AppCompatButton) itemView.findViewById(R.id.ispol);
            txtst=(TextView)itemView.findViewById(R.id.st);
            mc=(MaterialCardView)itemView.findViewById(R.id.mc);
            //myAdress = (TextView) itemView.findViewById(R.id.adress);
            btn_d=(AppCompatButton)itemView.findViewById(R.id.btn_del);
            btn_q =(AppCompatButton) itemView.findViewById(R.id.btn_questions);
            btn_q_a=(AppCompatButton)itemView.findViewById(R.id.btn_questions_all);
            myTV = (TextView) itemView.findViewById(R.id.avtor_2);
            myData = (TextView) itemView.findViewById(R.id.myData);
            db = FirebaseDatabase.getInstance();
            services = db.getReference();
            services.keepSynced(true);
        }
    }
}
