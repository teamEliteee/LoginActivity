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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private EditText fullname,age,email,password;
    private ProgressBar progressBar;
    private Button register;
    private TextView banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        mAuth = FirebaseAuth.getInstance();
        fullname = findViewById(R.id.fullname);
        age = findViewById(R.id.age);
        banner = findViewById(R.id.banner);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password1);
        progressBar = findViewById(R.id.progressBar2);
        register = findViewById(R.id.registerUser);
        banner.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this,loginActivity.class));
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String ageText = age.getText().toString().trim();
        String  fullnameText = fullname.getText().toString().trim();

        if(fullnameText.isEmpty()){
            fullname.setError("Full Name is required");
            fullname.requestFocus();
            return;
        }
        if(ageText.isEmpty()){
            age.setError("Age is required");
            age.requestFocus();
            return;
        }
        if(emailText.isEmpty()){
            email.setError("email is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Please enter valid email");
            email.requestFocus();
            return;
        }
        if(passwordText.isEmpty()){
            password.setError("Password required");
            password.requestFocus();
            return;
        }

        if(passwordText.length() < 6){
            password.setError("Password should be more than six characters");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailText,passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullnameText,ageText,emailText);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "User has been registered Successfully!!!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}