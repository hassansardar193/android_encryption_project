package com.myproject.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.Nullable;

public class LoginActivity extends AppCompatActivity {
    AutoCompleteTextView Email,password;
    ImageView signinwithgoogle;

    String EMail_user,password_user,user_name;
    Boolean status;
    TextView register;
    Button signin;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email=findViewById(R.id.atvEmailLog);
        password=findViewById(R.id.atvPasswordLog);
        signin=findViewById(R.id.btnSignIn);
        register=findViewById(R.id.tvSignIn);
        signinwithgoogle=findViewById(R.id.ivgoogle);
// taking instance of FirebaseAuth
// taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        retrieve_data();



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EMail_in=Email.getText().toString();
                String password_in=password.getText().toString();
                if(validateInput(password_in,EMail_in))
                {
                    if(EMail_in.equals(EMail_user)&&password_in.equals(password_user))
                    {

                        loginUserAccount();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Check your email and password!",Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
signinwithgoogle.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       Intent i=new Intent(LoginActivity.this,loginusinggoogle.class);
       startActivity(i);
    }
});

    }
    private void loginUserAccount()
    {


        // Take the value of two edit texts in Strings
        String email, pass;
        email = Email.getText().toString();
        pass = password.getText().toString();


        // signin existing user
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                    Intent intent
                                            = new Intent(LoginActivity.this,
                                            Dashboard.class);
                                    startActivity(intent);
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
    }
    private boolean validateInput(String inPw, String inEmail){

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

    void retrieve_data()
    {
        // on below line getting data from shared preferences.
        // creating a master key for encryption of shared preferences.
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

            // on below line creating a variable to
            // get the data from shared prefs.
            user_name = sharedPreferences.getString("name", "");
            EMail_user = sharedPreferences.getString("email", "");
            password_user = sharedPreferences.getString("password", "");
         status = sharedPreferences.getBoolean("status", false);

            // on below line we are setting data
            // to our name and age edit text.

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void insert_Data()
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

           /* // on below line we are storing data in shared preferences file.
            sharedPreferences.edit().putString("name", "inputName").apply();
            sharedPreferences.edit().putString("email",inputEmail).apply();
            sharedPreferences.edit().putString("password",inputPw ).apply();*/
            sharedPreferences.edit().putBoolean("status", true).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}