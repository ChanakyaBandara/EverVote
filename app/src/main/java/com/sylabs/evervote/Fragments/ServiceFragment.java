package com.sylabs.evervote.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sylabs.evervote.R;

public class ServiceFragment extends Fragment {

    private String CandidateID;

    public ServiceFragment(String parmCandidateID) {
        CandidateID = parmCandidateID;
        Log.d("ABC CID ServiceFragment",CandidateID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_can_service, container, false);
        return rootView;

    }
}
