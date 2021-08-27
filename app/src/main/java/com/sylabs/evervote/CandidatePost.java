package com.sylabs.evervote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sylabs.evervote.Modles.Candidate;
import com.sylabs.evervote.Modles.Post;
import com.sylabs.evervote.RecycleViews.Recycleview_Candidate_config;

import java.util.ArrayList;
import java.util.List;

public class CandidatePost extends AppCompatActivity {


    private String PostID;
    private TextView candidatePostTitle,candidatePostDate,candidatePostTime,candidatePostName,candidatePostDesc,candidatePostParty,candidatePostAbout;
    private ImageView candidatePostImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_post);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                PostID= null;
            } else {
                PostID= extras.getString("PostID");
            }
        } else {
            PostID= (String) savedInstanceState.getSerializable("PostID");
        }

        candidatePostTitle = (TextView) findViewById(R.id.candidatePostTitle);
        candidatePostDate = (TextView) findViewById(R.id.candidatePostDate);
        candidatePostTime = (TextView) findViewById(R.id.candidatePostTime);
        candidatePostName = (TextView) findViewById(R.id.candidatePostName);
        candidatePostDesc = (TextView) findViewById(R.id.candidatePostDesc);
        candidatePostParty = (TextView) findViewById(R.id.candidatePostParty);
        candidatePostAbout = (TextView) findViewById(R.id.candidatePostAbout);
        candidatePostImg = (ImageView) findViewById(R.id.candidatePostImg);

        Query query = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostID);

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Log.d("ABC",dataSnapshot.toString());
                Post post = new Post();
                post.setID(dataSnapshot.getKey());
                post.setTitle(dataSnapshot.child("Title").getValue(String.class));
                post.setDescription(dataSnapshot.child("Description").getValue(String.class));
                post.setDate(dataSnapshot.child("Date").getValue(String.class));
                post.setTime(dataSnapshot.child("Time").getValue(String.class));
                post.setCandidateName(dataSnapshot.child("CandidateName").getValue(String.class));
                post.setCandidateParty(dataSnapshot.child("CandidateParty").getValue(String.class));
                post.setImgURL(dataSnapshot.child("ImgURL").getValue(String.class));

                candidatePostTitle.setText(post.getTitle());
                candidatePostDesc.setText(post.getDescription());
                candidatePostDate.setText(post.getDate());
                candidatePostTime.setText(post.getTime());
                candidatePostName.setText(post.getCandidateName());
                candidatePostParty.setText(post.getCandidateParty());

                Glide.clear(candidatePostImg);
                Glide.with(CandidatePost.this).load(post.getImgURL()).into(candidatePostImg);


            }else{
                Toast.makeText(CandidatePost.this, "No Data Found !", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    
}