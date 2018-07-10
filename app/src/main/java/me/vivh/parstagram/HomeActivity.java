package me.vivh.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.vivh.parstagram.model.Post;

public class HomeActivity extends AppCompatActivity {

    private static final String imagePath = "/storage/self/primary/DCIM/Camera/20180709_012712.jpg";
    @BindView(R.id.etDescription) TextView descriptionInput;
    @BindView(R.id.btnCreate) Button createButton;
    @BindView(R.id.btnRefresh) Button refreshButton;
    @BindView(R.id.btnLogOut) Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = new File(imagePath);
                final ParseFile parseFile = new ParseFile(file);
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("HomeActivity", "Saved parseFile!");
                            createPost(description, parseFile, user);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Log.d("HomeActivity", "Log out successful!");
                final Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create post success!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity","Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}
