package com.sylabs.evervote.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sylabs.evervote.CandidateProfile;
import com.sylabs.evervote.Modles.Candidate;
import com.sylabs.evervote.R;
import com.sylabs.evervote.Services.Follow;

public class AboutFragment extends Fragment {

    private String CandidateID;
    private String CandidateName;
    private String FollowID;
    private TextView candidateProfileName,candidateProfileDesc,candidateProfileFollowers,candidateProfileParty;
    private ImageView candidateProfileImg;
    private Button candidateFollowBtn;

    public AboutFragment(String parmCandidateID) {
        CandidateID = parmCandidateID;
        Log.d("ABC CID AboutFragment",CandidateID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_can_about, container, false);

        candidateProfileName = (TextView) rootView.findViewById(R.id.fragCandidateProfileName);
        candidateProfileDesc = (TextView) rootView.findViewById(R.id.fragCandidateProfileDesc);
        candidateProfileFollowers = (TextView) rootView.findViewById(R.id.fragCandidateProfileFollowers);
        candidateProfileParty = (TextView) rootView.findViewById(R.id.fragCandidateProfileParty);
        candidateProfileImg = (ImageView) rootView.findViewById(R.id.fragCandidateProfileImg);
        candidateFollowBtn = (Button) rootView.findViewById(R.id.fragCandidateFollowBtn);

        candidateFollowBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                followCandidate(view);
            }
        });

        Follow follow = new Follow();
        follow.getFollowedIDbyInterface(CandidateID,followID -> {
            if (followID != null){
                FollowID = followID;
                Toast.makeText(getContext(), "Followed", Toast.LENGTH_SHORT).show();
                candidateFollowBtn.setText("Unfollow");
                candidateFollowBtn.setBackgroundResource(R.drawable.btn_unfollow);
            }else {
                Toast.makeText(getContext(), "Not Followed", Toast.LENGTH_SHORT).show();
                candidateFollowBtn.setText("Follow");
                candidateFollowBtn.setBackgroundResource(R.drawable.btn_follow);
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("Candidates").child(CandidateID);
        query.addValueEventListener(aboutValueEventListener);

        return rootView;
    }

    ValueEventListener aboutValueEventListener = new ValueEventListener() {
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

                CandidateProfile.CPCandidateName = candidate.getName();

                Glide.clear(candidateProfileImg);
                Glide.with(getContext()).load(candidate.getImgURL()).into(candidateProfileImg);
            }else{
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

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
}
