package me.vivh.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {

    Button logOutButton;
    TextView tvUserName;
    ImageView ivProfilePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        logOutButton = rootView.findViewById(R.id.btnLogout);
        tvUserName = rootView.findViewById(R.id.tvUserName);
        ivProfilePic = rootView.findViewById(R.id.ivProfilePic);

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

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // updated to be null
                Log.d("HomeActivity", "Log out successful!");
                final Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        return rootView;
    }
}
