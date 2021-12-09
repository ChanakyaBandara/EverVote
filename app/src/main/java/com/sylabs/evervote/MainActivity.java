package com.sylabs.evervote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sylabs.evervote.Modles.Candidate;
import com.sylabs.evervote.Modles.Post;
import com.sylabs.evervote.RecycleViews.Recycleview_Candidate_config;
import com.sylabs.evervote.RecycleViews.Recycleview_Post_config;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private RecyclerView recyclerView;
    private List<Post> Postlist = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String currentUId;
    private TextView Username;
    public static String UserName;
    private Spinner spinner;
    private List<Post> PostList = new ArrayList<>();
    private List<Post> SpinnerFilteredList = new ArrayList<>();
    private List<String> followedPostIdList = new ArrayList<>();
    private List<String> PostKeys = new ArrayList<String>();
    private List<String> SpinnerFilteredKeys = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        Username = (TextView) findViewById(R.id.txtUserName);

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        //Toast.makeText(dashboard.this, currentUId, Toast.LENGTH_LONG).show();
        spinner = findViewById(R.id.dropdown2);
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

        Query query2 = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);
        query2.addListenerForSingleValueEvent(valueEventListener2);


        Query query = FirebaseDatabase.getInstance().getReference().child("Posts");
        query.addListenerForSingleValueEvent(valueEventListener);

        Query query3 = FirebaseDatabase.getInstance().getReference().child("Followers").orderByChild("userId").equalTo(currentUId);
        query3.addListenerForSingleValueEvent(valueEventListener3);
    }

    private void spinnerChangeHandler() {
        Log.d("ABC__test11",SpinnerFilteredKeys.toString());
        Log.d("ABC__test11",PostList.toString());
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
                for (Post post : PostList){
                    SpinnerFilteredKeys.add(post.getCID());
                }
                SpinnerFilteredList = new ArrayList<>(PostList);
        }
        Log.d("ABC_test",SpinnerFilteredKeys.toString());
        updateRecycleView(SpinnerFilteredList,SpinnerFilteredKeys);
    }

    private void getFollowedList() {
        SpinnerFilteredList.clear();
        SpinnerFilteredKeys.clear();
        for (Post post : PostList){
            if(followedPostIdList.contains(post.getCID())){
                SpinnerFilteredList.add(post);
                SpinnerFilteredKeys.add(post.getCID());
            }
        }
    }

    private void getUnFollowedList() {
        SpinnerFilteredList.clear();
        SpinnerFilteredKeys.clear();
        for (Post post : PostList){
            if(!followedPostIdList.contains(post.getCID())){
                SpinnerFilteredList.add(post);
                SpinnerFilteredKeys.add(post.getCID());
            }
        }
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
                Log.d("ABC test",dataSnapshot.toString());
                Postlist.clear();
                PostKeys.clear();
                //List<String> Keys = new ArrayList<String>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
//                    Log.d("ABC",keyNode.toString());
//                    Log.d("ABC",keyNode.getKey());
                    Post post = new Post();
                    post.setID(keyNode.getKey());
                    post.setTitle(keyNode.child("Title").getValue(String.class));
                    post.setDescription(keyNode.child("Description").getValue(String.class));
                    post.setDate(keyNode.child("Date").getValue(String.class));
                    post.setTime(keyNode.child("Time").getValue(String.class));
                    post.setCandidateName(keyNode.child("CandidateName").getValue(String.class));
                    post.setCandidateParty(keyNode.child("CandidateParty").getValue(String.class));
                    post.setImgURL(keyNode.child("ImgURL").getValue(String.class));
                    post.setCID(keyNode.child("CID").getValue(String.class));
                    Postlist.add(post);
                    PostKeys.add(keyNode.getKey());
                }
                spinnerChangeHandler();
            }else{
                Toast.makeText(MainActivity.this, "No Data Found !", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                //Toast.makeText(dashboard.this, "Test Query", Toast.LENGTH_LONG).show();
                Log.d("ABC",dataSnapshot.toString());
                Username.setText("Hi Welcome\n"+dataSnapshot.child("FName").getValue(String.class)+" "+dataSnapshot.child("LName").getValue(String.class));
                UserName = dataSnapshot.child("FName").getValue(String.class)+" "+dataSnapshot.child("LName").getValue(String.class);
            }else{
                Toast.makeText(MainActivity.this, "No data Found", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener3 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("ABC test22",dataSnapshot.toString());
            if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    followedPostIdList.add(keyNode.child("CID").getValue(String.class));
                }
            }
            Log.d("ABC_test22",followedPostIdList.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void updateRecycleView(List<Post> finalList,List<String> keyss){
        new Recycleview_Post_config().setConfig(recyclerView,MainActivity.this,finalList,keyss);
    }

    public void goToSearch(View view) {
        Intent intent = new Intent(MainActivity.this, SearchCandidate.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
    }
}