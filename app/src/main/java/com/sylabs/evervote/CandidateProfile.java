package com.sylabs.evervote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class CandidateProfile extends AppCompatActivity {

    private String CandidateID;
    private String CandidateName;
    private TextView candidateProfileName,candidateProfileDesc,candidateProfileFollowers,candidateProfileParty;
    private ImageView candidateProfileImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_profile);

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

        Query query = FirebaseDatabase.getInstance().getReference().child("Candidates").child(CandidateID);;

        query.addListenerForSingleValueEvent(valueEventListener);
        
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
}