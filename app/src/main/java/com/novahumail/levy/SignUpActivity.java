package com.novahumail.levy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameET_signUp, emailET_signUp, passwordET_signUp, confirmPasswordET_signUp;
    private Button createAccountBTN_signUp, loginBTN_signUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        usernameET_signUp = findViewById(R.id.ET_Username_signUp);
        emailET_signUp = findViewById(R.id.ET_email_signUp);
        passwordET_signUp = findViewById(R.id.ET_password_signUp);
        confirmPasswordET_signUp = findViewById(R.id.ET_confirmPassword_signUp);

        createAccountBTN_signUp = findViewById(R.id.BTN_createAccount_signUp);
        loginBTN_signUp = findViewById(R.id.BTN_login_signUp);

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        loginBTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent h = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(h);
            }
        });


        createAccountBTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String username = usernameET_signUp.getText().toString().trim();
                String email = emailET_signUp.getText().toString().trim();
                String password = passwordET_signUp.getText().toString().trim();
                String confirmPassword = confirmPasswordET_signUp.getText().toString().trim();

                // Validate input fields
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register user with Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Save user data to Firestore
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("username", username);
                                    user.put("email", email);

                                    db.collection("users")
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                                    // Redirect user to login screen
                                                    startActivity(new Intent(SignUpActivity.this, SlabsActivity.class));
                                                    finish(); // Close SignUpActivity
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUpActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Failed to register user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });




    }
}