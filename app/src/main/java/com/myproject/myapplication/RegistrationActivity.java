package com.myproject.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
       AutoCompleteTextView username,Email,password;
       Button Signup;
       TextView already_member;
    private FirebaseAuth mAuth;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registration);
            // taking FirebaseAuth instance
            mAuth = FirebaseAuth.getInstance();
            // initializing variables with ids.
            username = findViewById(R.id.atvUsernameReg);
            Email = findViewById(R.id.atvEmailReg);
            password = findViewById(R.id.atvPasswordReg);
            Signup=findViewById(R.id.btnSignUp);
            already_member=findViewById(R.id.tvSignIn);


            Signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  String  inputName=username.getText().toString();
                  String inputPw=password.getText().toString();
                  String inputEmail=Email.getText().toString();

                    if(validateInput(inputName, inputPw, inputEmail))
                    {
                       insert_Data(inputName, inputPw, inputEmail);
                        registerNewUser();
                    }
                }
            });
            already_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
                    startActivity(i);
                }
            });
        }

    private boolean validateInput(String inName, String inPw, String inEmail){

        if(inName.isEmpty()){
            username.setError("Username is empty.");
            return false;
        }
        if(inPw.isEmpty()){
            password.setError("Password is empty.");
            return false;
        }
        if(inEmail.isEmpty()){
            Email.setError("Email is empty.");
            return false;
        }

        return true;
    }


    void insert_Data(String inputName,String inputPw,String inputEmail)
    {
        // on below line storing data to our shared prefs.

        // creating a master key for
        // encryption of shared preferences.
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialize/open an instance of
        // EncryptedSharedPreferences on below line.
        try {
            // on below line initializing our encrypted
            // shared preferences and passing our key to it.
            EncryptedSharedPreferences sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    // passing a file name to share a preferences
                    "preferences",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // on below line we are storing data in shared preferences file.
            sharedPreferences.edit().putString("name", inputName).apply();
            sharedPreferences.edit().putString("email",inputEmail).apply();
            sharedPreferences.edit().putString("password",inputPw ).apply();
            sharedPreferences.edit().putBoolean("status", true).apply();
            Toast.makeText(getApplicationContext(),"Successfully Encrypted Data!",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void registerNewUser()
    {

        // Take the value of two edit texts in Strings
        String email, pass;
        email = Email.getText().toString();
        pass = password.getText().toString();


        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                            "Registration successful!",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(RegistrationActivity.this,
                                    Dashboard.class);
                            startActivity(intent);
                        }
                        else {
                        task.getException().toString();

                            // Registration failed
                            Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later"+ task.getException().toString(),
                                            Toast.LENGTH_LONG)
                                    .show();

                        }
                    }

                });
    }
    }
