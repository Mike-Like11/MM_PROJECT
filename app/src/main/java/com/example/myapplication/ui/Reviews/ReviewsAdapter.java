package com.example.myapplication.ui.Reviews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Reviews;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<Reviews> listData;
    private Context context;
    FirebaseDatabase db;
    DatabaseReference services;

    public ReviewsAdapter(List<Reviews> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reviews,parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final ReviewsAdapter.ViewHolder holder, final int position) {
        final Reviews ld = listData.get(position);
        db = FirebaseDatabase.getInstance();
        services = db.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        final String email = user.getEmail();

        holder.revname.setText("Пользователь "+ld.getName());
        holder.revTV.setText("Содержание: " + ld.getDescription());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView revname, revTV;
        FirebaseDatabase db;
        DatabaseReference services;

        public ViewHolder(View itemView) {
            super(itemView);
            revTV = (TextView) itemView.findViewById(R.id.review);
            revname = (TextView) itemView.findViewById(R.id.user);
            db = FirebaseDatabase.getInstance();
            services = db.getReference();
            services.keepSynced(true);
        }
    }
}
