package com.example.chatapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.models.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private List<ChatModel> chats;
    private Context context;
    FirebaseUser fuser;
    private String imageURL;

    public ChatAdapter(List<ChatModel> chats, Context context, String imageURL) {
        this.chats = chats;
        this.context = context;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MyViewHolder(view);
        } else {
            Log.e(TAG, "onCreateViewHolder: left");
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_meassage.setText(chats.get(position).getMessage());
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(imageURL.equals("default")){
           holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }else {
            Glide.with(context).load(imageURL).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return chats == null ? 0 : chats.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_meassage;
        CircleImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_meassage = itemView.findViewById(R.id.tv_message);
            imageView = itemView.findViewById(R.id.imv_avatar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else return MSG_TYPE_LEFT;
    }
}
