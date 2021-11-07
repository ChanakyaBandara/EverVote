package com.sylabs.evervote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.sylabs.evervote.MainActivity.UserName;

public class Complaint extends AppCompatActivity {

//    private FirebaseAuth mAuth;
//    private DatabaseReference mUserDatabase;
//    private TextView txtComplaintTile,txtComplaintContent;
//    private String CID,CName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_complaint);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        txtComplaintTile = (TextView) findViewById(R.id.txtComplaintTile);
//        txtComplaintContent = (TextView) findViewById(R.id.txtComplaintContent);
//
//        String userId = mAuth.getCurrentUser().getUid();
//
//        CID = getIntent().getStringExtra("CID");
//        CName = getIntent().getStringExtra("CName");
//
//    }
//
//    public void sendComplainet(View view) {
//        String formattedDate ="";
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            LocalDateTime localDateTime = LocalDateTime.now();
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            formattedDate = localDateTime.format(dateTimeFormatter);
//        }
//
//        final String CTitle = txtComplaintTile.getText().toString();
//        final String CContent = txtComplaintContent.getText().toString();
//        final String timeStamp = formattedDate;
//
//
//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Complaint").push();
//        Map Complaint = new HashMap<>();
//        Complaint.put("CTitle", CTitle);
//        Complaint.put("CContent", CContent);
//        Complaint.put("DComplainer", UserName);
//        Complaint.put("CID", CID);
//        Complaint.put("CName", CName);
//        Complaint.put("timeStamp", timeStamp);
//        mUserDatabase.updateChildren(Complaint);
//
//        Toast.makeText(Complaint.this, "Successfully Added", Toast.LENGTH_SHORT).show();
//    }
}