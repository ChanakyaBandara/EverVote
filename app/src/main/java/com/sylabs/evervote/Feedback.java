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

public class Feedback extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private TextView txtFeedbackTitle,txtFeedbackContent;
    private String CID,CName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAuth = FirebaseAuth.getInstance();

        txtFeedbackTitle = (TextView) findViewById(R.id.txtFeedbackTitle);
        txtFeedbackContent = (TextView) findViewById(R.id.txtFeedbackContent);

        String userId = mAuth.getCurrentUser().getUid();

        CID = getIntent().getStringExtra("CID");
        CName = getIntent().getStringExtra("CName");
    }

    public void sendFeedback(View view) {
        String formattedDate ="";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedDate = localDateTime.format(dateTimeFormatter);
        }

        final String FTitle = txtFeedbackTitle.getText().toString();
        final String FContent = txtFeedbackContent.getText().toString();
        final String timeStamp = formattedDate;

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Feedback").push();
        Map Feedback = new HashMap<>();
        Feedback.put("FTitle", FTitle);
        Feedback.put("FContent", FContent);
        Feedback.put("DComplainer", UserName);
        Feedback.put("timeStamp", timeStamp);
        Feedback.put("CID", CID);
        Feedback.put("CName", CName);
        mUserDatabase.updateChildren(Feedback);

        Toast.makeText(Feedback.this, "Successfully Added", Toast.LENGTH_SHORT).show();
    }
}