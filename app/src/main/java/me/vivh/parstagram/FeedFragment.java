package me.vivh.parstagram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import me.vivh.parstagram.model.Post;

public class FeedFragment extends Fragment {

    ArrayList<Post> posts;
    RecyclerView rvPost;
    PostAdapter adapter;
    AsyncHttpClient client;
    SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        client = new AsyncHttpClient();
        posts = new ArrayList<>();

        adapter = new PostAdapter(posts);
        rvPost = (RecyclerView) rootView.findViewById(R.id.rvPosts);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        rvPost.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPost.setAdapter(adapter);

        loadTopPosts();
        getConfiguration();


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                posts.clear();
                adapter.clear();
                loadTopPosts();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return rootView;
    }


    public void getConfiguration() {
    }

    public void loadTopPosts(){
        final Post.Query postsQuery = new Post.Query();
        postsQuery.orderByAscending("createdAt");
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e==null){
                    Post post = new Post();
                    for (int i = 0; i < objects.size(); i++){
                        Log.d("FeedActivity", "Post ["+i+"] = "
                                + objects.get(i).getDescription()
                                + "\n username = " + objects.get(i).getUser().getUsername()
                                + "\n timestamp = " + objects.get(i).getTime());

                        posts.add(0,objects.get(i));
                        adapter.notifyItemInserted(posts.size()-1);

                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
