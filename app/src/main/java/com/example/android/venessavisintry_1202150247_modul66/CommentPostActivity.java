package com.example.android.venessavisintry_1202150247_modul66;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.android.venessavisintry_1202150247_modul66.adapter.CommentAdapter;
import com.example.android.venessavisintry_1202150247_modul66.model.Comment;
import com.example.android.venessavisintry_1202150247_modul66.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentPostActivity extends AppCompatActivity {
    //variable
    TextView mUsername, mTitlePost, mPost;
    ImageView mImgPost;
    EditText comment;
    //objek instance
    FirebaseAuth mAuth;
    DatabaseReference databaseComments;
    DatabaseReference databaseUser;

    private RecyclerView recyclerView;
    private ArrayList<Comment> listComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);

        mAuth = FirebaseAuth.getInstance();
        //getintent
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String username = intent.getStringExtra("username");
        String img = intent.getStringExtra("image");
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        databaseComments = FirebaseDatabase.getInstance().getReference(MainActivity.table_2).child(id);
        databaseUser = FirebaseDatabase.getInstance().getReference(MainActivity.table_3);

        recyclerView = findViewById(R.id.rv_comment);
        comment = findViewById(R.id.et_comment);
        mUsername = findViewById(R.id.tv_username);
        mImgPost = findViewById(R.id.post_img);
        mTitlePost = findViewById(R.id.tv_title_post);
        mPost = findViewById(R.id.tv_post);

        listComments = new ArrayList<>();

        Glide.with(CommentPostActivity.this).load(img).into(mImgPost);

        mUsername.setText(username);
        mTitlePost.setText(title);
        mPost.setText(desc);
    }

    public void addComment(View view){
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                String review = comment.getText().toString().trim();
                if (!TextUtils.isEmpty(review)){
                    String id = databaseComments.push().getKey();
                    Comment track = new Comment(id, user.getUsername(), review);
                    databaseComments.child(id).setValue(track);
                    toastMessage("Comment sent");
                    comment.setText("");
                }else{
                    toastMessage("Please enter comment first");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listComments.clear();

                for (DataSnapshot comments : dataSnapshot.getChildren()){
                    Comment comment = comments.getValue(Comment.class);
                    listComments.add(comment);
                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(CommentPostActivity.this, 2));
                CommentAdapter commentAdapter = new CommentAdapter(CommentPostActivity.this, listComments);
                recyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //method toast pesan
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
