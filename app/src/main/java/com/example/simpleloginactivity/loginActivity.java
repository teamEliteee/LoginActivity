package com.example.simpleloginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button register, signIn,forgotPassword;
    private EditText email,password;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.login);
        signIn.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.forgetPassword);
        forgotPassword.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.login:
                userLogin();

            case R.id.forgetPassword:
                startActivity(new Intent(loginActivity.this,forgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if(emailText.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(passwordText.isEmpty()){
            password.setError("password is required");
            password.requestFocus();
            return;
        }
        if(passwordText.length() < 6){
            password.setError("password should be more than 6 letters");
            password.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Enter a valid Email");
            email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        Intent intent = new Intent(loginActivity.this,profileActivity.class);
                        startActivity(intent);
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(loginActivity.this, "Check Your Email to verify your account!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(loginActivity.this, "User not Able to Login, please check your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}