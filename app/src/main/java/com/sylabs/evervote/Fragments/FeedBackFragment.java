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


public class FeedBackFragment extends Fragment {

    private String CandidateID;
    private Button btnSendFeedback;
    private TextView txtFeedbackTitle,txtFeedbackContent;
    private DatabaseReference mUserDatabase;

    public FeedBackFragment(String parmCandidateID) {
        CandidateID = parmCandidateID;
        Log.d("ABC CID FeedBackFragment",CandidateID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_can_feedback, container, false);

        txtFeedbackTitle = (TextView) rootView.findViewById(R.id.txtFeedbackTitle);
        txtFeedbackContent = (TextView) rootView.findViewById(R.id.txtFeedbackContent);
        btnSendFeedback = (Button) rootView.findViewById(R.id.btnSendFeedback);

        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendFeedback(view);
            }
        });

        return rootView;
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
        Feedback.put("CID", CandidateID);
        Feedback.put("CName", CPCandidateName);
        mUserDatabase.updateChildren(Feedback);

        Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
    }
}
