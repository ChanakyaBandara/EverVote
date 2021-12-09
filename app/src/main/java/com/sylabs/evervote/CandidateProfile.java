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
import com.sylabs.evervote.Fragments.AboutFragment;
import com.sylabs.evervote.Fragments.ComplaintFragment;
import com.sylabs.evervote.Fragments.DonationFragment;
import com.sylabs.evervote.Fragments.FeedBackFragment;
import com.sylabs.evervote.Fragments.ServiceFragment;
import com.sylabs.evervote.Modles.Candidate;
import com.sylabs.evervote.Services.Follow;

public class CandidateProfile extends AppCompatActivity {

    private String CandidateID;
    public static String CPCandidateName;
    private Fragment selectedFragment = null;

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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_about); // change to whichever id should be default

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        switch (item.getItemId()) {
            case R.id.nav_about:
                selectedFragment = new AboutFragment(CandidateID);
                break;
            case R.id.nav_service:
                selectedFragment = new ServiceFragment(CandidateID);
                break;
            case R.id.nav_complaint:
                selectedFragment = new ComplaintFragment(CandidateID);
                break;
            case R.id.nav_feedback:
                selectedFragment = new FeedBackFragment(CandidateID);
                break;
            case R.id.nav_donation:
                selectedFragment = new DonationFragment(CandidateID);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();
        return true;
    };

    public void goBack(View view) {
        Intent intent = new Intent(CandidateProfile.this, MainActivity.class);
        startActivity(intent);
    }
}