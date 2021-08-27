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

public class Donation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private TextView txtDonationTitle,txtDonationContent,txtDonationAmount;
    private String CID,CName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        mAuth = FirebaseAuth.getInstance();

        txtDonationTitle = (TextView) findViewById(R.id.txtDonationTitle);
        txtDonationAmount = (TextView) findViewById(R.id.txtDonationAmount);
        txtDonationContent = (TextView) findViewById(R.id.txtDonationContent);

        String userId = mAuth.getCurrentUser().getUid();

        CID = getIntent().getStringExtra("CID");
        CName = getIntent().getStringExtra("CName");
    }

    public void makeDonaton(View view) {
        String formattedDate ="";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedDate = localDateTime.format(dateTimeFormatter);
        }

        final String DTitle = txtDonationTitle.getText().toString();
        final String DAmount = txtDonationAmount.getText().toString();
        final String DContent = txtDonationContent.getText().toString();
        final String timeStamp = formattedDate;

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Donation").push();
        Map Donation = new HashMap<>();
        Donation.put("DTitle", DTitle);
        Donation.put("DAmount", DAmount);
        Donation.put("DContent", DContent);
        Donation.put("DDonner", UserName);
        Donation.put("CID", CID);
        Donation.put("CName", CName);
        Donation.put("timeStamp", timeStamp);
        mUserDatabase.updateChildren(Donation);

        Toast.makeText(Donation.this, "Successfully Added", Toast.LENGTH_SHORT).show();
    }
}