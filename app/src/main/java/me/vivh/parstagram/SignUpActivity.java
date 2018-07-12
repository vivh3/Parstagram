package me.vivh.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnSignup) Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        ParseUser.getCurrentUser().logOut();

        //getSupportActionBar().hide();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                if (!(email.isEmpty() || username.isEmpty() || password.isEmpty())){
                    ParseUser user = new ParseUser();
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Log.d("SignUpActivity", "Registration successful!");
                                Toast.makeText(getApplicationContext(), "Success! Please log in.", Toast.LENGTH_LONG).show();
                            }else{
                                Log.d("SignUpActivity", "Registration failed :(");
                                Toast.makeText(getApplicationContext(), "Registration failed :(", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                    final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

