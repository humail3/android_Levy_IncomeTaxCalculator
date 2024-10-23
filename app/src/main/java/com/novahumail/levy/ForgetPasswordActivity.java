package com.novahumail.levy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailET_forget;
    private Button recoverBTN_forget, loginBTN_forget;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        emailET_forget = findViewById(R.id.ET_email_forget);
        recoverBTN_forget = findViewById(R.id.BTN_recover_forget);
        loginBTN_forget = findViewById(R.id.BTN_login_forget);

        firebaseAuth = FirebaseAuth.getInstance();


        loginBTN_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent h = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(h);
                finish();
            }
        });


        recoverBTN_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = emailET_forget.getText().toString().trim();
                if (mail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter your Email first", Toast.LENGTH_SHORT).show();
                } else {
//                    we have to send recover password email
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Email sent, You can recover your password using email", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "Email is wrong or account not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}