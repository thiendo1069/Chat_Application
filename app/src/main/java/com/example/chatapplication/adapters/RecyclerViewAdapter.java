package com.example.chatapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.activities.MainActivity;
import com.example.chatapplication.activities.MessageActivity;
import com.example.chatapplication.fragments.ListChatFragment;
import com.example.chatapplication.fragments.ListFriendFragment;
import com.example.chatapplication.fragments.ProfileFragment;
import com.example.chatapplication.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    ArrayList<UserModel> models;
    Context context;
    int MODE = 0;

    public RecyclerViewAdapter(ArrayList<UserModel> models, Context context, int mode) {
        this.models = models;
        this.context = context;
        this.MODE = mode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tvUserName.setText(models.get(position).getUserName());
        if (models.get(position).getUlrImage().equals("default")) {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Glide.with(context).load(models.get(position).getUlrImage()).into(holder.imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODE == 0) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("userId", models.get(position).getUserId());
                    intent.putExtra("userName", models.get(position).getUserName());
                    context.startActivity(intent);
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    final FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    Intent intent = new Intent(context, ProfileFragment.class);
                    intent.putExtra("userId", models.get(position).getUserId());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return models != null ? models.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        CircleImageView imageView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_userName);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
