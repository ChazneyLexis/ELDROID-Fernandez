package com.example.eldroid_fernandez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextInputEditText emailAddressRegister, passwordRegister;
    Button registerButton, registerLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registerLoginButton = findViewById(R.id.registerLoginButton);
        emailAddressRegister = findViewById(R.id.emailAddressRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        registerButton = findViewById(R.id.registerButton);

        registerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailAddressRegister.getText().toString().trim();
                String password = passwordRegister.getText().toString().trim();
                if (email.isEmpty()) {
                    emailAddressRegister.setError("Email is required!");
                    emailAddressRegister.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailAddressRegister.setError("Please provide valid Email!");
                    emailAddressRegister.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passwordRegister.setError("Password is required!");
                    passwordRegister.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    passwordRegister.setError("Min password length should be 6 characters!");
                    passwordRegister.requestFocus();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "You are successfully Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d("TAG", "onComplete: exist_email");
                                emailAddressRegister.setError("Email already exists");
                                emailAddressRegister.requestFocus();
                            } catch (Exception e) {
                                Toast.makeText(Register.this, "Failed to register! Try Again!", Toast.LENGTH_LONG).show();
                                Log.d("TAG", "onComplete: " + e.getMessage());
                            }
                        }
                    }
                });
            }
        });
    }
}