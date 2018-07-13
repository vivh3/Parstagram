package me.vivh.parstagram;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.parstagram.model.Post;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {

    private List<Post> mPosts;
    Context context;
    public ProfilePostAdapter(List<Post> posts){ mPosts = posts; }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProfilePostImage) ImageView ivProfilePostImage;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_profile_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProfilePostAdapter.ViewHolder holder, int position) {
        Post post = mPosts.get(position);

        Glide.with(context)
            .load(post.getImage().getUrl())
            .into(holder.ivProfilePostImage);
    }

    @Override
    public int getItemCount() {
            return mPosts.size();
        }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }


}