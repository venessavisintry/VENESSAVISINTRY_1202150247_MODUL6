package com.example.android.venessavisintry_1202150247_modul66.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.venessavisintry_1202150247_modul66.CommentPostActivity;
import com.example.android.venessavisintry_1202150247_modul66.R;
import com.example.android.venessavisintry_1202150247_modul66.model.Post;

import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    ArrayList<Post> postsList; //array list postlist

    public PostAdapter(Context context, ArrayList<Post> postsList) {
        this.context = context;
        this.postsList = postsList;
    }
//menampilkan post ke layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_post, parent, false);
        return new ViewHolder(view);
    }
//menampilkan data yang di post
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Post post = postsList.get(position);

        holder.mUsername.setText(post.getUsername()); //untuk username
        Glide.with(context).load(post.getImage()).into(holder.mImagePost); //glide untuk imgae
        holder.mTitlePost.setText(post.getTitlePost()); //judul(title)
        holder.mPost.setText(post.getPost());
//masukan data ke cardview
        holder.cardViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CommentPostActivity.class);
                i.putExtra("id", post.getId());
                i.putExtra("username", post.getUsername());
                i.putExtra("image", post.getImage());
                i.putExtra("title", post.getTitlePost());
                i.putExtra("id", post.getTitlePost());
                i.putExtra("desc", post.getPost());
                context.startActivity(i);
            }
        });
    }
//recylerview
    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mUsername;
        TextView mTitlePost;
        TextView mPost;
        ImageView mImagePost;
        CardView cardViewPost;

        public ViewHolder(View itemView) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.tv_username);
            mTitlePost = itemView.findViewById(R.id.tv_title_post);
            mPost = itemView.findViewById(R.id.tv_post);
            mImagePost = itemView.findViewById(R.id.img_post);
            cardViewPost = itemView.findViewById(R.id.cvPost);
        }
    }
}
