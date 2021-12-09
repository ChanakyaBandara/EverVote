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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sylabs.evervote.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class DonationFragment extends Fragment {

    private String CandidateID;
    private Button btnMakeDonaton;
    private TextView txtDonationTitle,txtDonationAmount;
    private DatabaseReference mUserDatabase;

    public DonationFragment(String parmCandidateID) {
        CandidateID = parmCandidateID;
        Log.d("ABC CID DonationFragment",CandidateID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_can_donation, container, false);

        txtDonationTitle = (TextView) rootView.findViewById(R.id.txtDonationTitle);
        txtDonationAmount = (TextView) rootView.findViewById(R.id.txtDonationAmount);
        btnMakeDonaton = (Button) rootView.findViewById(R.id.btnMakeDonation);

        btnMakeDonaton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                makeDonaton(view);
            }
        });

        return rootView;
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
        final String timeStamp = formattedDate;

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Donation").push();
        Map Donation = new HashMap<>();
        Donation.put("DTitle", DTitle);
        Donation.put("DAmount", DAmount);
        Donation.put("DDonner", UserName);
        Donation.put("CID", CandidateID);
        Donation.put("CName", CPCandidateName);
        Donation.put("timeStamp", timeStamp);
        mUserDatabase.updateChildren(Donation);

        Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
    }
}
