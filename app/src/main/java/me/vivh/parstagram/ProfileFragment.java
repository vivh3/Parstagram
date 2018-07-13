package me.vivh.parstagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.vivh.parstagram.model.Post;


public class ProfileFragment extends Fragment {

    @BindView(R.id.btnLogOut) Button logOutBtn;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.ivProfilePic) ImageView ivProfilePic;
    @BindView(R.id.btnEditProfile) Button editProfileBtn;
    @BindView(R.id.rvProfilePosts) RecyclerView rvProfilePosts;

    private ArrayList<Post> profilePosts;
    private ProfilePostAdapter profilePostAdapter;
    private Unbinder unbinder;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        profilePosts = new ArrayList<>();
        profilePostAdapter = new ProfilePostAdapter(profilePosts);

        int numColumns = 2;
        rvProfilePosts.setLayoutManager(new GridLayoutManager(getContext(),numColumns));
        rvProfilePosts.setAdapter(profilePostAdapter);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String currentUsername = currentUser.getUsername();
        tvUserName.setText(currentUsername);
        String profilePicUrl = "";
        if (currentUser.getParseFile("profilePic") != null) {
            profilePicUrl = currentUser.getParseFile("profilePic").getUrl();
        }
        Glide.with(getContext()).load(profilePicUrl)
                .apply(new RequestOptions().placeholder(R.drawable.instagram_user_outline_24))
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfilePic);


        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // replace existing fragment with feed inside the frame
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentPlace, editProfileFragment);
                ft.commit();
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mListener.onLogoutButtonPressed(view);
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // updated to be null
                Log.d("HomeActivity", "Log out successful!");
                final Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });


        // Define the class we would like to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Define our query conditions
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, com.parse.ParseException e) {
                if(e == null){
                    profilePosts.clear();
                    profilePosts.addAll(objects);
                    profilePostAdapter.notifyDataSetChanged();
                    Log.d("ProfileFragment", profilePosts.toString());
                } else{
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*// When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/

    // interface implemented by FeedActivity to allow an interaction in this fragment to be communicated
    // to activity and other fragments in that activity
    public interface OnFragmentInteractionListener {
        //void onLogoutButtonPressed(View v);
    }
}
