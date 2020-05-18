package com.example.myapplication.ui.Rating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Rating;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private List<Rating> listData;
    private Context context;
    FirebaseDatabase db;
    DatabaseReference services;

    public RatingAdapter(List<Rating> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rating,parent,false);
        return new ViewHolder(view);
}

    public void onBindViewHolder(@NonNull final RatingAdapter.ViewHolder holder, final int position) {
        final Rating ld = listData.get(position);
        db = FirebaseDatabase.getInstance();
        services = db.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.ratename.setText("Пользователь "+ld.getName());
        holder.raterate.setText("Рейтинг: "+ld.getRating());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ratename, raterate;
        FirebaseDatabase db;
        DatabaseReference services;

        public ViewHolder(View itemView) {
            super(itemView);
            ratename = (TextView) itemView.findViewById(R.id.user_rate);
            raterate = (TextView) itemView.findViewById(R.id.user_rating);
            db = FirebaseDatabase.getInstance();
            services = db.getReference();
            services.keepSynced(true);
        }
    }
}
