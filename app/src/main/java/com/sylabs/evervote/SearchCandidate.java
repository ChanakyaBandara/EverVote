package com.sylabs.evervote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class SearchCandidate extends AppCompatActivity{

    private RecyclerView recyclerView;
    private List<Candidate> CandidateList = new ArrayList<>();
    private List<Candidate> SpinnerFilteredList = new ArrayList<>();
    private List<Candidate> SearchFilteredList = new ArrayList<>();
    private List<String> followedCandidateIdList = new ArrayList<>();
    private List<String> CandidateKeys = new ArrayList<String>();
    private List<String> SpinnerFilteredKeys = new ArrayList<String>();
    private List<String> SearchFilteredKeys = new ArrayList<String>();
    private FirebaseAuth mAuth;
    private String currentUId;
    private int item;
    Spinner spinner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_candidate);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        Query query = FirebaseDatabase.getInstance().getReference().child("Candidates");
        Query query2 = FirebaseDatabase.getInstance().getReference().child("Followers").orderByChild("userId").equalTo(currentUId);
        query.addListenerForSingleValueEvent(valueEventListener);
        query2.addListenerForSingleValueEvent(valueEventListener2);

        spinner = findViewById(R.id.dropdown1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.searchCategories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerChangeHandler();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void spinnerChangeHandler() {
        int items = spinner.getSelectedItemPosition();
        switch(items) {
            case 1:
                getFollowedList();
                break;
            case 2:
                getUnFollowedList();
                break;
            case 0:
            default:
                SpinnerFilteredKeys = new ArrayList<>(CandidateKeys);
                SpinnerFilteredList = new ArrayList<>(CandidateList);
        }
        updateRecycleView(SpinnerFilteredList,SpinnerFilteredKeys);
    }

    private void getFollowedList() {
        SpinnerFilteredList.clear();
        SpinnerFilteredKeys.clear();
        for (Candidate candidate : CandidateList){
            if(followedCandidateIdList.contains(candidate.getID())){
                SpinnerFilteredList.add(candidate);
                SpinnerFilteredKeys.add(candidate.getID());
            }
        }
    }

    private void getUnFollowedList() {
        SpinnerFilteredList.clear();
        SpinnerFilteredKeys.clear();
        for (Candidate candidate : CandidateList){
            if(!followedCandidateIdList.contains(candidate.getID())){
                SpinnerFilteredList.add(candidate);
                SpinnerFilteredKeys.add(candidate.getID());
            }
        }
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                CandidateList.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Candidate candidate = new Candidate();
                    candidate.setID(keyNode.getKey());
                    candidate.setName(keyNode.child("Name").getValue(String.class));
                    candidate.setParty(keyNode.child("Party").getValue(String.class));
                    candidate.setHistory(keyNode.child("History").getValue(String.class));
                    candidate.setFollowers(String.valueOf(keyNode.child("Followers").getValue(Integer.class)));
                    candidate.setImgURL(keyNode.child("ImgURL").getValue(String.class));
                    CandidateList.add(candidate);
                    CandidateKeys.add(keyNode.getKey());
                }
                spinnerChangeHandler();
            }else{
                Toast.makeText(SearchCandidate.this, "No Data Found !", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    followedCandidateIdList.add(keyNode.child("CID").getValue(String.class));
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void updateRecycleView(List<Candidate> finalList,List<String> keyss){
        new Recycleview_Candidate_config().setConfig(recyclerView,SearchCandidate.this,finalList,keyss);
    }

    public void goToHome(View view) {
        Intent intent = new Intent(SearchCandidate.this, MainActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(SearchCandidate.this, Profile.class);
        startActivity(intent);
    }

}