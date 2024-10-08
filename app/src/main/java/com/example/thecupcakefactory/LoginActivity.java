package com.example.thecupcakefactory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInputTxt,passwordInputTxt;
    private ProgressBar progressBar;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInputTxt = findViewById(R.id.usernameInputTxt);
        passwordInputTxt = findViewById(R.id.passwordInputTxt);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar= findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = usernameInputTxt.getText().toString().trim();
                String password = passwordInputTxt.getText().toString().trim();


                if (email.isEmpty() || password.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage("All fields are Mandatory!")
                            .setPositiveButton("Close", null)
                            .show();
                    passwordInputTxt.setText("");
                } else {
                    if(email.equals("admin@factory") && password.equals("20230130")){
                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                        finish();
                    }else {
                        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            fetchUsernameAndProceed(email);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            new AlertDialog.Builder(LoginActivity.this)
                                                    .setMessage("Ops! Please check your email and password!")
                                                    .setPositiveButton("Close", null)
                                                    .show();
                                        }
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage("Please check your email address!")
                                    .setPositiveButton("Close", null)
                                    .show();
                        }
                    }
                }
            }

        });
    }

    private void fetchUsernameAndProceed(String email) {
        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String username = userSnapshot.child("username").getValue(String.class);
                        proceedToMemberHome(email, username);
                        break;
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage("User not found!")
                            .setPositiveButton("Close", null)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void proceedToMemberHome(String email, String username) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MemberHomeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("username", username);
        startActivity(intent);
    }

     public void toSignup(View view) {
        Intent i = new Intent(this,SignupActivity.class);
        startActivity(i);
        finish();
    }

}
