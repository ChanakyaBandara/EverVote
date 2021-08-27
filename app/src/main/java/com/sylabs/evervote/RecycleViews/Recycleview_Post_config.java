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

import com.bumptech.glide.Glide;
import com.sylabs.evervote.CandidatePost;
import com.sylabs.evervote.Modles.Post;
import com.sylabs.evervote.R;

import java.util.List;

public class Recycleview_Post_config {
    private Context mContext;
    private PostAddaptor mPostAddaptor;


    public void setConfig(RecyclerView recyclerView, Context context, List<Post> posts, List<String> keys){
        mContext = context;
        mPostAddaptor = new PostAddaptor(posts,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mPostAddaptor);
    }

    class PostItemView extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView postTitle;
        private TextView postCandidateName;
        private TextView postCandidateParty;
        private TextView postDate;
        private TextView postTime;
        private ImageView postCoverPic;
        private String PID;


        private String key;

        public PostItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.item_post,parent,false));

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postCandidateName = (TextView) itemView.findViewById(R.id.postCandidateName);
            postCandidateParty = (TextView) itemView.findViewById(R.id.postCandidateParty);
            postDate = (TextView) itemView.findViewById(R.id.postDate);
            postTime = (TextView) itemView.findViewById(R.id.postTime);
            postCoverPic = (ImageView) itemView.findViewById(R.id.postCoverPic);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CandidatePost.class);
                    intent.putExtra("PostID",PID);
                    mContext.startActivity(intent);
                }
            });

        }

        public void bind( Post  post,String key){
            postTitle.setText(post.getTitle());
            postCandidateName.setText(post.getCandidateName());
            postCandidateParty.setText(post.getCandidateParty());
            postDate.setText(post.getDate());
            postTime.setText(post.getTime());
            PID = post.getID();
            Glide.clear(postCoverPic);
            Glide.with(mContext).load(post.getImgURL()).into(postCoverPic);
            this.key = key;
        }



    }

    class PostAddaptor extends RecyclerView.Adapter<PostItemView>{
        private List<Post> mPost;
        private List<String> mKey;

        public PostAddaptor(List<Post> mPost, List<String> mKey) {
            this.mPost = mPost;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public PostItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostItemView holder, int position) {
            holder.bind(mPost.get(position),mKey.get(position));
        }

        @Override
        public int getItemCount() {
            return mPost.size();
        }


    }

}
