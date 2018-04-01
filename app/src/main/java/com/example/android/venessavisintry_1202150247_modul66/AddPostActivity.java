package com.example.android.venessavisintry_1202150247_modul66;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.venessavisintry_1202150247_modul66.model.Post;
import com.example.android.venessavisintry_1202150247_modul66.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    //variable
    EditText mTitlePost, mPost;
    ImageView imgPost;
    Button mChooseImg;
    ProgressDialog mProgress;
    //database refrense instance
    DatabaseReference databasePost;
    //firbase auth instance
    FirebaseAuth mAuth;

    private Uri imgUri;
    private StorageReference mStorage;
    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        imgUri = null;

        mProgress = new ProgressDialog(this);

        //initialize firebase instance
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        databasePost = FirebaseDatabase.getInstance().getReference(MainActivity.table_1);
        databaseUser = FirebaseDatabase.getInstance().getReference(MainActivity.table_3);
        //bind data
        mTitlePost = findViewById(R.id.et_title_post);
        mPost = findViewById(R.id.et_post);
        imgPost = findViewById(R.id.img_post);
        mChooseImg = findViewById(R.id.btn_choose_img);
        //set tombol choose image
        mChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    public void add(View view){
        mProgress.setMessage("Uploading Image...");
        mProgress.show();
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                //initialize
                final String name = user.getUsername();
                final String title = mTitlePost.getText().toString();
                final String postMsg = mPost.getText().toString();
                final String id = databasePost.push().getKey();
                final String userId = mAuth.getCurrentUser().getUid();

                if (imgUri != null && !TextUtils.isEmpty(name)){
                    StorageReference img = mStorage.child(id + ".jpg").child(imgUri.getLastPathSegment());

                    img.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                String download_url = task.getResult().getDownloadUrl().toString();
                                Post post = new Post(id, userId, name, title, postMsg, download_url);
                                databasePost.child(id).setValue(post);
                            }else {
                                toastMessage("Error : " + task.getException().getMessage());
                            }
                        }
                    });
                    mProgress.dismiss();
                    //display toast
                    toastMessage("Uploaded");
                    //objek intent
                    Intent i = new Intent(AddPostActivity.this, MainActivity.class);
                    //pindah intent
                    startActivity(i);
                    finish();
                }else{
                    //display toast
                    toastMessage("Please fill the form and Choose image");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //jika suda pilih image, set imageview dengan imagenya
        if (requestCode == PICK_IMAGE){
            imgUri = data.getData();
            imgPost.setImageURI(imgUri);
        }
    }

    //method toast pesan
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
