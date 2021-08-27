package com.sylabs.evervote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private TextView txtProfileName,txtProfileEmail,txtProfileAge,txtProfileGender,txtProfileArea,txtProfilePhone;
    DatabaseReference currentUserDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        txtProfileName = (TextView) findViewById(R.id.txtProfileName);
        txtProfileEmail = (TextView) findViewById(R.id.txtProfileEmail);
        txtProfileAge = (TextView) findViewById(R.id.txtProfileAge);
        txtProfileGender = (TextView) findViewById(R.id.txtProfileGender);
        txtProfileArea = (TextView) findViewById(R.id.txtProfileArea);
        txtProfilePhone = (TextView) findViewById(R.id.txtProfilePhone);

        String userId = mAuth.getCurrentUser().getUid();
        currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        currentUserDb.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Log.d("ABC",dataSnapshot.toString());
                txtProfileName.setText(dataSnapshot.child("FName").getValue(String.class) +" "+dataSnapshot.child("LName").getValue(String.class));
                txtProfileEmail.setText(dataSnapshot.child("Email").getValue(String.class));
                txtProfileAge.setText(dataSnapshot.child("Age").getValue(String.class));
                txtProfileGender.setText(dataSnapshot.child("Gender").getValue(String.class));
                txtProfileArea.setText(dataSnapshot.child("Area").getValue(String.class));
                txtProfilePhone.setText(dataSnapshot.child("Phone").getValue(String.class));
            }else{
                Toast.makeText(Profile.this, "Data doesn't exist", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public void logOut(View view) {
        mAuth.signOut();
        Intent intent = new Intent(Profile.this, Login.class);
        startActivity(intent);
        finish();
    }


    public void goToSearch(View view) {
        Intent intent = new Intent(Profile.this, SearchCandidate.class);
        startActivity(intent);
    }

    public void goToHome(View view) {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
    }

}