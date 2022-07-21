package com.example.simpleloginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {
    private EditText emailEntered;
    private Button resetPassword;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEntered = findViewById(R.id.emailId);
        resetPassword = findViewById(R.id.resetpassword);

        auth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }

            private void resetPassword() {
                String email = emailEntered.getText().toString().trim();

                if(email.isEmpty()){
                    emailEntered.setError("Email required");
                    emailEntered.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEntered.setError("Provide a valid Email");
                    emailEntered.requestFocus();
                    return;
                }

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(forgotPassword.this, "Email is Sent Succesfully!!! reset through the link", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(forgotPassword.this, "Something wrong has Happened!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });
    }
}