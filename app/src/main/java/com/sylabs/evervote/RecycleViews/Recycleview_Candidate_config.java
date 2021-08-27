package com.sylabs.evervote.RecycleViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sylabs.evervote.CandidateProfile;
import com.sylabs.evervote.Modles.Candidate;
import com.sylabs.evervote.R;

import java.util.List;

public class Recycleview_Candidate_config {
    private Context mContext;
    private CandidateAddaptor mCandidateAddaptor;


    public void setConfig(RecyclerView recyclerView, Context context, List<Candidate> candidates, List<String> keys){
        mContext = context;
        mCandidateAddaptor = new CandidateAddaptor(candidates,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mCandidateAddaptor);
    }

    class CandidateItemView extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView candidateName;
        private TextView candidateParty;
        private ImageView candidatePic;
        private Button followbtn;
        private String CID;


        private String key;

        public CandidateItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.item_cadidate,parent,false));

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            candidateName = (TextView) itemView.findViewById(R.id.candidateItemName);
            candidateParty = (TextView) itemView.findViewById(R.id.candidateItemParty);
            followbtn = (Button) itemView.findViewById(R.id.candidateItemFollowBtn);
            candidatePic = (ImageView) itemView.findViewById(R.id.candidateItemImg);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CandidateProfile.class);
                    intent.putExtra("CandidateID",CID);
                    mContext.startActivity(intent);
                }
            });

        }

        public void bind( Candidate  candidate,String key){
            candidateName.setText(candidate.getName());
            candidateParty.setText(candidate.getParty());
            CID = candidate.getID();
            Glide.clear(candidatePic);
            Glide.with(mContext).load(candidate.getImgURL()).into(candidatePic);
            this.key = key;
        }



    }

    class CandidateAddaptor extends RecyclerView.Adapter<CandidateItemView>{
        private List<Candidate> mCandidate;
        private List<String> mKey;

        public CandidateAddaptor(List<Candidate> mCandidate, List<String> mKey) {
            this.mCandidate = mCandidate;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public CandidateItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CandidateItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CandidateItemView holder, int position) {
            holder.bind(mCandidate.get(position),mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mCandidate.size();
        }


    }

}
