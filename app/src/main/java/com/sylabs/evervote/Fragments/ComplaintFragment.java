package com.sylabs.evervote.Fragments;

import static com.sylabs.evervote.CandidateProfile.CPCandidateName;
import static com.sylabs.evervote.MainActivity.UserName;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sylabs.evervote.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ComplaintFragment extends Fragment {

    private String CandidateID;
    private TextView txtComplaintTile,txtComplaintContent;
    private Button btnSendComplaint;
    private DatabaseReference mUserDatabase;

    public ComplaintFragment(String parmCandidateID) {
        CandidateID = parmCandidateID;
        Log.d("ABC CID ComplaintFragment",CandidateID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_can_complaint, container, false);
        txtComplaintTile = (TextView) rootView.findViewById(R.id.txtComplaintTile);
        txtComplaintContent = (TextView) rootView.findViewById(R.id.txtComplaintContent);
        btnSendComplaint = (Button) rootView.findViewById(R.id.btnSendComplaint);

        btnSendComplaint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendComplainet(view);
            }
        });
        return rootView;
    }

    public void sendComplainet(View view) {
        String formattedDate ="";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedDate = localDateTime.format(dateTimeFormatter);
        }

        final String CTitle = txtComplaintTile.getText().toString();
        final String CContent = txtComplaintContent.getText().toString();
        final String timeStamp = formattedDate;


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Complaint").push();
        Map Complaint = new HashMap<>();
        Complaint.put("CTitle", CTitle);
        Complaint.put("CContent", CContent);
        Complaint.put("DComplainer", UserName);
        Complaint.put("CID", CandidateID);
        Complaint.put("CName", CPCandidateName);
        Complaint.put("timeStamp", timeStamp);
        mUserDatabase.updateChildren(Complaint);

        Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
    }
}
