package com.sylabs.evervote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sylabs.evervote.Modles.Candidate;
import com.sylabs.evervote.RecycleViews.Recycleview_Candidate_config;

import java.util.ArrayList;
import java.util.List;

public class SearchCandidate extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private RecyclerView recyclerView;
    private List<Candidate> Candidatelist = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_candidate);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Query query = FirebaseDatabase.getInstance().getReference().child("Candidates");

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
                Log.d("ABC",dataSnapshot.toString());
                Candidatelist.clear();
                List<String> Keys = new ArrayList<String>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Log.d("ABC",keyNode.toString());
                    Log.d("ABC",keyNode.getKey());
                    Candidate candidate = new Candidate();
                    candidate.setID(keyNode.getKey());
                    candidate.setName(keyNode.child("Name").getValue(String.class));
                    candidate.setParty(keyNode.child("Party").getValue(String.class));
                    candidate.setHistory(keyNode.child("History").getValue(String.class));
                    candidate.setFollowers(String.valueOf(keyNode.child("Followers").getValue(Integer.class)));
                    candidate.setImgURL(keyNode.child("ImgURL").getValue(String.class));
                    Candidatelist.add(candidate);
                    Keys.add(keyNode.getKey());
                }
                new Recycleview_Candidate_config().setConfig(recyclerView,SearchCandidate.this,Candidatelist,Keys);

            }else{
                Toast.makeText(SearchCandidate.this, "No Data Found !", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void goToHome(View view) {
        Intent intent = new Intent(SearchCandidate.this, MainActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(SearchCandidate.this, Profile.class);
        startActivity(intent);
    }
}