package com.sylabs.evervote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sylabs.evervote.Modles.Candidate;
import com.sylabs.evervote.Modles.Post;
import com.sylabs.evervote.Services.Follow;

public class CandidateProfile extends AppCompatActivity {

    private String CandidateID;
    private String CandidateName;
    private String FollowID;
    private TextView candidateProfileName,candidateProfileDesc,candidateProfileFollowers,candidateProfileParty;
    private ImageView candidateProfileImg;
    private Button candidateFollowBtn;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_profile);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_about); // change to whichever id should be default
        }

       if (savedInstanceState == null) {
           Bundle extras = getIntent().getExtras();
           if(extras == null) {
               CandidateID= null;
           } else {
               CandidateID= extras.getString("CandidateID");
           }
       } else {
           CandidateID= (String) savedInstanceState.getSerializable("CandidateID");
       }

        candidateProfileName = (TextView) findViewById(R.id.candidateProfileName);
        candidateProfileDesc = (TextView) findViewById(R.id.candidateProfileDesc);
        candidateProfileFollowers = (TextView) findViewById(R.id.candidateProfileFollowers);
        candidateProfileParty = (TextView) findViewById(R.id.candidateProfileParty);
        candidateProfileImg = (ImageView) findViewById(R.id.candidateProfileImg);
        candidateFollowBtn = (Button) findViewById(R.id.candidateFollowBtn);

        Follow follow = new Follow();
        follow.getFollowedIDbyInterface(CandidateID,followID -> {
            if (followID != null){
                FollowID = followID;
                Toast.makeText(getApplicationContext(), "Followed", Toast.LENGTH_SHORT).show();
                candidateFollowBtn.setText("Unfollow");
                candidateFollowBtn.setBackgroundResource(R.drawable.btn_unfollow);
            }else {
                Toast.makeText(getApplicationContext(), "Not Followed", Toast.LENGTH_SHORT).show();
                candidateFollowBtn.setText("Follow");
                candidateFollowBtn.setBackgroundResource(R.drawable.btn_follow);
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("Candidates").child(CandidateID);
        query.addValueEventListener(valueEventListener);


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Log.d("ABC",dataSnapshot.toString());
                Candidate candidate = new Candidate();
                candidate.setID(dataSnapshot.getKey());
                candidate.setName(dataSnapshot.child("Name").getValue(String.class));
                candidate.setParty(dataSnapshot.child("Party").getValue(String.class));
                candidate.setHistory(dataSnapshot.child("History").getValue(String.class));
                candidate.setFollowers(String.valueOf(dataSnapshot.child("Followers").getValue(Integer.class)));
                candidate.setImgURL(dataSnapshot.child("ImgURL").getValue(String.class));

                CandidateID = candidate.getID();
                CandidateName = candidate.getName();
                candidateProfileName.setText(candidate.getName());
                candidateProfileDesc.setText(candidate.getHistory());
                candidateProfileFollowers.setText(candidate.getFollowers());
                candidateProfileParty.setText(candidate.getParty());

                Glide.clear(candidateProfileImg);
                Glide.with(CandidateProfile.this).load(candidate.getImgURL()).into(candidateProfileImg);
            }else{
                Toast.makeText(CandidateProfile.this, "No Data Found !", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            selectedFragment = new AboutFragment();
            switch (item.getItemId()) {
                case R.id.nav_about:
                    selectedFragment = new AboutFragment();
                    break;
                case R.id.nav_service:
                    selectedFragment = new ServiceFragment();
                    break;
                case R.id.nav_complaint:
                    selectedFragment = new ComplaintFragment();
                    break;
                case R.id.nav_feedback:
                    selectedFragment = new FeedBackFragment();
                    break;
                case R.id.nav_donation:
                    selectedFragment = new DonationFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

    public void goToDonation(View view) {
        Intent intent = new Intent(CandidateProfile.this, Donation.class);
        intent.putExtra("CID",CandidateID);
        intent.putExtra("CName",CandidateName);
        startActivity(intent);
    }

    public void goToFeedback(View view) {
        Intent intent = new Intent(CandidateProfile.this, Feedback.class);
        intent.putExtra("CID",CandidateID);
        intent.putExtra("CName",CandidateName);
        startActivity(intent);
    }

    public void goToComplaint(View view) {
        Intent intent = new Intent(CandidateProfile.this, Complaint.class);
        intent.putExtra("CID",CandidateID);
        intent.putExtra("CName",CandidateName);
        startActivity(intent);
    }

    public void followCandidate(View view) {
        Follow follow = new Follow();
        follow.getFollowedIDbyInterface(CandidateID, followID -> {
            if (followID != null){
                FollowID = followID;
                follow.Unfollow(CandidateID,followID);
                candidateFollowBtn.setText("Follow");
                candidateFollowBtn.setBackgroundResource(R.drawable.btn_follow);
            }else {
                follow.newFollower(CandidateID);
                candidateFollowBtn.setText("Unfollow");
                candidateFollowBtn.setBackgroundResource(R.drawable.btn_unfollow);
            }
        });
    }

    public void goBack(View view) {
        Intent intent = new Intent(CandidateProfile.this, MainActivity.class);
        startActivity(intent);
    }
}