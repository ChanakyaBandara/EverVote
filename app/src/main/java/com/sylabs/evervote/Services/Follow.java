package com.sylabs.evervote.Services;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Follow {

    private static FirebaseAuth mAuth;
    private static String userId;
    private static DatabaseReference mUserDatabase;

    public Follow() {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public interface FirebaseCallBack {
        void callBack(String followID);
    }

    public String newFollower(String CID){
        DatabaseReference mFollowers = mUserDatabase.child("Followers").push();
        Map Complaint = new HashMap<>();
        Complaint.put("userId", userId);
        Complaint.put("CID", CID);
        mFollowers.updateChildren(Complaint);

        updateNoFollows(CID,1);

        return mFollowers.getKey();
    }

    public void Unfollow(String CID,String followedID){
        mUserDatabase.child("Followers").child(followedID).removeValue();
        updateNoFollows(CID,-1);
    }

    public String getNoOfFollowers(String CID){
        final String[] candidateFollowers = {"0"};
        DatabaseReference mCandidateFollower = mUserDatabase.child("Candidates").child(CID).child("Followers");;
        mCandidateFollower.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                candidateFollowers[0] = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return candidateFollowers[0];
    }

//    public Boolean isCandidateFollowed(String CID){
//        if(getFollowedID(CID)!=null){
//            return true;
//        }
//        return false;
//    }

//    public String getFollowedID(String CID){
//        getFollowedIDbyInterface(new FirebaseCallBack() {
//            @Override
//            public List callBack(List list) {
//                return list;
//            }
//        });
//        Log.d("ABC Follower",list1.toString());
//        return (String) list1.get(0);
//    }

    public void getFollowedIDbyInterface(String CID,FirebaseCallBack firebaseCallBack) {
        mUserDatabase.child("Followers").orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        if(keyNode.child("CID").getValue(String.class).equals(CID)){
                            firebaseCallBack.callBack(keyNode.getKey());
                            found = true;
                            break;
                        }
                    }
                }
                if(!found){
                    firebaseCallBack.callBack(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void updateNoFollows(String CID,int count){
        DatabaseReference mCandidateFollower = mUserDatabase.child("Candidates").child(CID).child("Followers");;
        mCandidateFollower.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int candidateFollowers = dataSnapshot.getValue(Integer.class);
                mCandidateFollower.setValue(candidateFollowers + count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
