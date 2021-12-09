package com.sylabs.evervote.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sylabs.evervote.Modles.Candidate_Service;
import com.sylabs.evervote.R;
import com.sylabs.evervote.RecycleViews.Recycleview_Service_config;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends Fragment {

    private String CandidateID;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private RecyclerView recyclerView;
    private List<Candidate_Service> Candidate_Servicelist = new ArrayList<>();
    private FirebaseAuth mAuth;

    public ServiceFragment(String parmCandidateID) {
        CandidateID = parmCandidateID;
        Log.d("ABC CID ServiceFragment",CandidateID);

        Query query = FirebaseDatabase.getInstance().getReference().child("Candidates").child(CandidateID).child("Service");

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
                Log.d("ABC",dataSnapshot.toString());
                Candidate_Servicelist.clear();
                List<String> Keys = new ArrayList<String>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Log.d("ABC",keyNode.toString());
                    Log.d("ABC",keyNode.getKey());
                    Candidate_Service candidate_Service = new Candidate_Service();
                    candidate_Service.setSID(keyNode.getKey());
                    candidate_Service.setTitle(keyNode.child("Title").getValue(String.class));
                    candidate_Service.setDisc(keyNode.child("Description").getValue(String.class));
                    candidate_Service.setDate(keyNode.child("Date").getValue(String.class));
                    Candidate_Servicelist.add(candidate_Service);
                    Keys.add(keyNode.getKey());
                }
                new Recycleview_Service_config().setConfig(recyclerView, getContext(),Candidate_Servicelist,Keys);
            }else{
                Toast.makeText(getContext(), "No Data Found !", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_can_service, container, false);
        return rootView;

    }
}
