package me.vivh.parstagram;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.vivh.parstagram.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

  private List<Post> mPosts;
  Context context;
  // pass in posts
  public PostAdapter(List<Post> posts) {
    mPosts = posts;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View postView = inflater.inflate(R.layout.item_post, parent, false);
    ViewHolder viewHolder = new ViewHolder(postView);
    return viewHolder;
  }

  @Override
  public int getItemCount() {
    return mPosts.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivImage;
    public TextView tvUserName;
    public TextView tvDesc;
    public TextView tvTime;
    public ImageView ivProfilePic;

    public ViewHolder(View itemView) {
      super(itemView);
      ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
      tvUserName = (TextView) itemView.findViewById(R.id.tvUser);
      tvDesc = (TextView) itemView.findViewById(R.id.tvDescription);
      tvTime = (TextView) itemView.findViewById(R.id.tvTime);
      ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
    }
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Post post = mPosts.get(position);
    String profilePicUrl = "";
    if (post.getUser().getParseFile("profilePic") != null) {
        profilePicUrl = post.getUser().getParseFile("profilePic").getUrl();
    }
    holder.tvDesc.setText(post.getDescription());
    holder.tvUserName.setText(post.getUser().getUsername());
    holder.ivImage.layout(0,0,0,0);
    Glide.with(context).load(post.getImage().getUrl()).into(holder.ivImage);
    holder.tvTime.setText(getRelativeTimeAgo(post.getTime()));
    Glide.with(context).load(profilePicUrl)
            .apply(new RequestOptions().placeholder(R.drawable.instagram_user_outline_24))
            .apply(RequestOptions.circleCropTransform())
            .into(holder.ivProfilePic);
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

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String timeFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            Log.d("PostAdapter","rawJsonDate: " + rawJsonDate);
            e.printStackTrace();
        }
        return relativeDate;
    }


}
