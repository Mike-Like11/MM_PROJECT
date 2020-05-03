package com.example.myapplication.ui.Articles;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.example.myapplication.Models.Article;
import com.example.myapplication.Models.Like;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{
private List<Article>listData;
private boolean liki;
private boolean likos;
private  boolean buto;
private  String s;
private Context context;
    FirebaseDatabase db;
    DatabaseReference services;
public ArticleAdapter(List<Article> listData,Context context) {
        this.listData = listData;
        this.context =context;
        }


@Override
public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article,parent,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Article ld =listData.get(position);
        if(!ld.getName_narrator().equals("") &&!ld.getName().equals("") &&!ld.getDescription().equals("")) {
            holder.txtname.setText(ld.getName());
            holder.txtmovie.setText("Автор: " + ld.getName_narrator());
            holder.myTV.setText(ld.getDescription());
        }
            holder.nl.setText(String.valueOf(ld.getLike()));

    holder.myTV.setEnabled(false);

   setliki(holder.getAdapterPosition(),ld.getName(),holder);
    db= FirebaseDatabase.getInstance();
    services=db.getReference();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // Name, email address, and profile photo Url
    final String name = user.getDisplayName();
    final String email = user.getEmail();
    holder.lb.setOnLikeListener(new OnLikeListener() {
                                    @Override
                                    public void liked(LikeButton likeButton) {
                                        liki = true;
                                        services.addValueEventListener(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (liki) {
                                                    boolean yes = false;
                                                    Like like2 = new Like();
                                                    for (DataSnapshot ds : dataSnapshot.child("Articles").child(ld.getName()).child("Likes").getChildren()) {
                                                        Like like = ds.getValue(Like.class);
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                            if (Objects.equals(like.getAmail(), name)) {
                                                                like2 = like;
                                                                yes = true;

                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (yes) {
                                                        services.child("Articles").child(ld.getName()).child("Likes").child(like2.getAmail()).removeValue();
                                                        ld.setLike(ld.getLike() - 1);
                                                        holder.nl.setText(String.valueOf(ld.getLike()));

                                                        services.child("Articles").child(ld.getName()).child("Article").child("like").setValue(ld.getLike());
                                                        liki = false;
                                                    } else {
                                                        ld.setLike(ld.getLike() + 1);
                                                        holder.nl.setText(String.valueOf(ld.getLike()));
                                                        like2.setAmail(name);
                                                        holder.lb.setLiked(false);


                                                        services.child("Articles").child(ld.getName()).child("Article").child("like").setValue(ld.getLike());
                                                        services.child("Articles").child(ld.getName()).child("Likes").child(like2.getAmail()).setValue(like2);

                                                        liki = false;
                                                        notifyItemChanged(holder.getAdapterPosition());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void unLiked(LikeButton likeButton) {
                                        liki = true;
                                        services.addValueEventListener(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (liki) {
                                                    boolean yes = false;
                                                    Like like2 = new Like();
                                                    for (DataSnapshot ds : dataSnapshot.child("Articles").child(ld.getName()).child("Likes").getChildren()) {
                                                        Like like = ds.getValue(Like.class);
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                            if (Objects.equals(like.getAmail(), name)) {
                                                                like2 = like;
                                                                yes = true;

                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (yes) {
                                                        services.child("Articles").child(ld.getName()).child("Likes").child(like2.getAmail()).removeValue();
                                                        ld.setLike(ld.getLike() - 1);
                                                        holder.lb.setLiked(false);
                                                        notifyItemChanged(holder.getAdapterPosition());

                                                        holder.nl.setText(String.valueOf(ld.getLike()));
                                                        services.child("Articles").child(ld.getName()).child("Article").child("like").setValue(ld.getLike());
                                                        liki = false;
                                                    } else {

                                                        like2.setAmail(name);
                                                        services.child("Articles").child(ld.getName()).child("Likes").child(like2.getAmail()).setValue(like2);
                                                        ld.setLike(ld.getLike() + 1);

                                                        holder.nl.setText(String.valueOf(ld.getLike()));
                                                        services.child("Articles").child(ld.getName()).child("Article").child("like").setValue(ld.getLike());
                                                        liki = false;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });


    holder.btn_c.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder alertbox=new  AlertDialog.Builder(v.getRootView().getContext());
            AlertDialog alert = alertbox.create();

            LayoutInflater inflater=LayoutInflater.from(v.getRootView().getContext());
            View comment_window=inflater.inflate(R.layout.comment_window,null);
            alertbox.setView(comment_window);

            alertbox.setTitle("Комментарии");
            final MaterialEditText yc=comment_window.findViewById(R.id.your_Commnebt_Field);
            yc.setMinLines(2);

            final TextView tvc=comment_window.findViewById(R.id.comments);

            tvc.setText("здесь ничего нет");
              services.child("Articles").child(ld.getName()).child("Comment").addValueEventListener(new ValueEventListener() {
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
                        services.child("Articles").child(ld.getName()).child("Comment").push().setValue(name + ':' + yc.getText().toString());
                        yc.setText("");
                        services.child("Articles").child(ld.getName()).child("Comment").addValueEventListener(new ValueEventListener() {
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


        if(ld.user_s(ld.getName_narrator(),name)){
            holder.btn_d.setVisibility(View.VISIBLE);
            holder.btn_r.setVisibility(View.VISIBLE);

            holder.btn_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View View) {
                    //showArticle_Rewrite_Window(ld);
                     //or switcher.showPrevious();
                    buto=true;



                        holder.myTV.setEnabled(true);
                        holder.btn_d.setText("Отменить изменения");
                        holder.btn_r.setText("Сохранить изменения");


                    new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        holder.btn_d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(android.view.View v) {
                                holder.myTV.setEnabled(false);
                                holder.myTV.setText(ld.getDescription());
                                holder.btn_d.setText("Удалить");
                                holder.btn_r.setText("Изменить");
                                try {
                                    TimeUnit.SECONDS.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                buto=false;

                            }
                        });
                        holder.btn_r.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(android.view.View v) {
                            /*

                            Map map = new HashMap<>();
                            map.put("name",ld.getName());
                            map.put("name_narrator", ld.getName_narrator());
                            map.put("description",holder.myTV.getText().toString());
                            ld.setDescription(holder.myTV.getText().toString());



                            services.child("Articles").child(ld.getName()).updateChildren(map);
                            holder.myTV.setText(ld.getDescription());
                            */
                               /* services.child("Articles").child(ld.getName()).removeValue();
                                int position = holder.getAdapterPosition();
                                removeItem(position);
                                */
                                ld.setDescription(holder.myTV.getText().toString());
                                services.child("Articles").child(ld.getName()).child("Article").setValue(ld);
                                /*
                                final Like l = new Like();
                                l.setAmail("aaaa");
                                services.child("Articles").child(ld.getName()).child("Likes").child(l.getAmail()).setValue(l);
                                */
                                holder.myTV.setEnabled(false);
                                holder.btn_d.setText("Удалить");
                                holder.btn_r.setText("Изменить");
                                try {
                                    TimeUnit.SECONDS.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                buto=false;

                            }
                        });
        }
},500);




                }
            });
            holder.btn_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    services.child("Articles").child(ld.getName()).removeValue();
                    ld.setName("");
                    ld.setDescription("");
                    ld.setName_narrator("");
                    holder.txtname.setText(ld.getName());
                    int position=holder.getAdapterPosition();
                    removeItem(position);

                }
            });

        }
        else{
            holder.btn_d.setVisibility(View.INVISIBLE);
            holder.btn_r.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            holder.btn_c.setLayoutParams(params);

        }


        }

    public void removeItem(int position) {
        listData.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
    public void setliki(final int postion, String ld, @NonNull final ViewHolder holder) {
        db = FirebaseDatabase.getInstance();
        services = db.getReference().child("Articles").child(ld);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Name, email address, and profile photo Url
        final String name = user.getDisplayName();
        final String email = user.getEmail();
        likos = false;
        s = ld;
        services.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                s = "aaaaaa";
                for (DataSnapshot ds : dataSnapshot.child("Likes").getChildren()) {
                    Like like = ds.getValue(Like.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Objects.equals(like.getAmail(), name)) {
                            s=like.getAmail();
                            likos = true;
                            break;
                        }
                    }
                }
                if (likos && dataSnapshot.exists() && !s.equals("aaaaaa")) {
                    holder.lb.setLiked(true);
                } else {
                    holder.lb.setLiked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    @Override
public int getItemCount() {
        return listData.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView txtavtor,txtname,txtmovie,nl;
    private EditText myTV;
    private AppCompatButton btn_d,btn_r;
    private AppCompatButton btn_c;
    private   ViewSwitcher switcher;
    private LikeButton lb;
    private String s;
    FirebaseDatabase db;
    DatabaseReference services;
    public ViewHolder(View itemView) {
        super(itemView);
        txtname=(TextView)itemView.findViewById(R.id.nametxt);
        txtmovie=(TextView)itemView.findViewById(R.id.avtortxt);
        btn_d=(AppCompatButton)itemView.findViewById(R.id.btn_a_d);
        btn_r=(AppCompatButton) itemView.findViewById(R.id.btn_a_r);
        btn_c= (AppCompatButton) itemView.findViewById(R.id.btn_cooment);
        myTV = (EditText) itemView.findViewById(R.id.avtor_2txt);
        lb=(LikeButton)itemView.findViewById(R.id.star_button);
        nl=(TextView)itemView.findViewById(R.id.number_liketxt);
        db= FirebaseDatabase.getInstance();
        services=db.getReference();
        services.keepSynced(true);


    }
}
}