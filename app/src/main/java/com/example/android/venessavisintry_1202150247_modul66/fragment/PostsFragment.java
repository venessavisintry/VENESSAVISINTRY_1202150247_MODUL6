package com.example.android.venessavisintry_1202150247_modul66.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.android.venessavisintry_1202150247_modul66.MainActivity;
import com.example.android.venessavisintry_1202150247_modul66.R;
import com.example.android.venessavisintry_1202150247_modul66.adapter.PostAdapter;
import com.example.android.venessavisintry_1202150247_modul66.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Post> listPosts;

    DatabaseReference database;

    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        database = FirebaseDatabase.getInstance().getReference(MainActivity.table_1);

        recyclerView = view.findViewById(R.id.rv_posts);
        listPosts = new ArrayList<>();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPosts.clear();

                for (DataSnapshot posts : dataSnapshot.getChildren()){
                    Post post = posts.getValue(Post.class);
                    listPosts.add(post);
                }
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                PostAdapter postAdapter = new PostAdapter(getContext(), listPosts);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
