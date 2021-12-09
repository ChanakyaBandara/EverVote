package com.sylabs.evervote.RecycleViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sylabs.evervote.Modles.Candidate_Service;
import com.sylabs.evervote.R;

import java.util.List;

public class Recycleview_Service_config {
    private Context mContext;
    private Candidate_ServiceAddaptor mCandidate_ServiceAddaptor;


    public void setConfig(RecyclerView recyclerView, Context context, List<Candidate_Service> candidate_Services, List<String> keys){
        mContext = context;
        mCandidate_ServiceAddaptor = new Candidate_ServiceAddaptor(candidate_Services,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mCandidate_ServiceAddaptor);
    }

    class Candidate_ServiceItemView extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView serviceName;
        private TextView serviceDate;
        private TextView serviceDesc;
        private String SID;


        private String key;

        public Candidate_ServiceItemView(ViewGroup parent){
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service,parent,false));


            cardView = (CardView) itemView.findViewById(R.id.card_view);
            serviceName = (TextView) itemView.findViewById(R.id.service_name);
            serviceDate = (TextView) itemView.findViewById(R.id.service_date);
            serviceDesc = (TextView) itemView.findViewById(R.id.service_desc);

        }

        public void bind( Candidate_Service  candidate_Service,String key){
            serviceName.setText(candidate_Service.getTitle());
            serviceDate.setText(candidate_Service.getDate());
            serviceDesc.setText(candidate_Service.getDisc());
            SID = candidate_Service.getSID();
            this.key = key;
        }



    }

    class Candidate_ServiceAddaptor extends RecyclerView.Adapter<Candidate_ServiceItemView>{
        private List<Candidate_Service> mCandidate_Service;
        private List<String> mKey;

        public Candidate_ServiceAddaptor(List<Candidate_Service> mCandidate_Service, List<String> mKey) {
            this.mCandidate_Service = mCandidate_Service;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public Candidate_ServiceItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Candidate_ServiceItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Candidate_ServiceItemView holder, int position) {
            holder.bind(mCandidate_Service.get(position),mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mCandidate_Service.size();
        }


    }

}
