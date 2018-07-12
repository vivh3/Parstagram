package me.vivh.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // Automatically finds each field by the specified ID.
    @BindView(R.id.etUsername) TextView usernameInput;
    @BindView(R.id.etPassword) TextView passwordInput;
    @BindView(R.id.btnLogin) TextView loginBtn;
    @BindView(R.id.fakeTitle) TextView fakeTitle;
    @BindView(R.id.btnSignUp) Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // persist user if haven't logged out
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // go directly to the feed
            Log.d("LoginActivity", "Already signed in!");
            final Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        } else {
            // show the signup or login screen
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = usernameInput.getText().toString();
                    final String password = passwordInput.getText().toString();
                    login(username, password);
                }
            });

            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.d("LoginActivity", "Login successful!");
                    final Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
