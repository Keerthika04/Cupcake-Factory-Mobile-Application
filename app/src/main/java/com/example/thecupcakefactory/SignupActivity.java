package com.example.thecupcakefactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.model.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignupActivity extends AppCompatActivity {

    private EditText fullNameTxt, emailTxt, phoneNoTxt, addressTxt, usernameTxt, passwordTxt, reEnterPasswordTxt;
    private ProgressBar progressBar;
    private Button mainSignupBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progressBar);
        fullNameTxt = findViewById(R.id.fullNameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneNoTxt = findViewById(R.id.phoneNoTxt);
        addressTxt = findViewById(R.id.addressTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        reEnterPasswordTxt = findViewById(R.id.reEnterPasswordTxt);
        mainSignupBtn = findViewById(R.id.mainSignupBtn);

        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
                progressBar.setVisibility(View.VISIBLE);

                String name = fullNameTxt.getText().toString();
                String email = emailTxt.getText().toString();
                String phoneNo = phoneNoTxt.getText().toString();
                String address = addressTxt.getText().toString();
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                String reEnterPassword = reEnterPasswordTxt.getText().toString();

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                Query query = usersRef.orderByChild("username").equalTo(username);


                if(name.isEmpty() || email.isEmpty() || phoneNo.isEmpty() || address.isEmpty() || username.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    new AlertDialog.Builder(SignupActivity.this)
                            .setMessage("All fields are Mandatory!")
                            .setPositiveButton("Close", null)
                            .show();
                }else {
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                progressBar.setVisibility(View.GONE);
                                new AlertDialog.Builder(SignupActivity.this)
                                        .setMessage("Username already taken, Try Again!")
                                        .setPositiveButton("Close", null)
                                        .show();
                                usernameTxt.setText("");
                            } else {
                                if(!(email.contains("@") && email.contains("."))){
                                    progressBar.setVisibility(View.GONE);
                                    new AlertDialog.Builder(SignupActivity.this)
                                            .setMessage("Please enter a proper email!")
                                            .setPositiveButton("Close", null)
                                            .show();
                                }else {
                                    if(password.length() < 6){
                                        progressBar.setVisibility(View.GONE);
                                        new AlertDialog.Builder(SignupActivity.this)
                                                .setMessage("Password should be 6 charaters long!")
                                                .setPositiveButton("Close", null)
                                                .show();
                                    }else {
                                        if (!(password.equals(reEnterPassword))) {
                                            progressBar.setVisibility(View.GONE);
                                            new AlertDialog.Builder(SignupActivity.this)
                                                    .setMessage("Ops! The passwords don\'t match \nTry again!")
                                                    .setPositiveButton("Close", null)
                                                    .show();
                                            reEnterPasswordTxt.setText("");
                                        } else {
                                            UserHelperClass userHelperClass = new UserHelperClass(name, email, phoneNo, address, username);
                                            reference.child(name).setValue(userHelperClass);
                                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                        finish();
                                                    } else {
                                                        progressBar.setVisibility(View.GONE);
                                                        new AlertDialog.Builder(SignupActivity.this)
                                                                .setMessage("SignUp Failed! Try again")
                                                                .setPositiveButton("Close", null)
                                                                .show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public void toLogin(View view) {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}