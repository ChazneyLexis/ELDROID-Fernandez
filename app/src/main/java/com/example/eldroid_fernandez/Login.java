package com.example.eldroid_fernandez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Login extends AppCompatActivity {


    TextInputEditText emailAddress;
    TextInputEditText passWord;
    Button registerTextView;
    Button loginButton;
    FirebaseAuth fauth;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = findViewById(R.id.emailAddress);
        passWord = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        fauth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }


    }

    private void userLogin() {
        String email = emailAddress.getText().toString().trim();
        String password = passWord.getText().toString().trim();

        if (email.isEmpty()) {
            emailAddress.setError("Email is required!");
            emailAddress.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddress.setError("Please provide valid Email!");
            emailAddress.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.isEmpty()) {
            passWord.setError("Password is required!");
            passWord.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.length() < 6) {
            passWord.setError("Min password length should be 6 characters!");
            passWord.requestFocus();
            progressDialog.dismiss();
            return;
        }

        progressDialog.setMessage("Logging in...Please Wait");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                            progressDialog.dismiss();

                        } else {
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail) {
                                Log.d("TAG", "onComplete: invalid_email");
                                emailAddress.setError("Invalid Email not registered");
                                emailAddress.requestFocus();
                                progressDialog.dismiss();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                Log.d("TAG", "onComplete: wrong_password");
                                passWord.setError("Wrong Password");
                                passWord.requestFocus();
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                Toast.makeText(Login.this, "Failed to login!", Toast.LENGTH_LONG).show();
                                Log.d("TAG", "onComplete: " + e.getMessage());
                                progressDialog.dismiss();
                            }
                        }
                    }
                });

    }
}