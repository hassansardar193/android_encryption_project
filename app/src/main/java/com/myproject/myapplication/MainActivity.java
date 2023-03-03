package com.myproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
public class MainActivity extends AppCompatActivity {
TextView next_activity;
boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next_activity=findViewById(R.id.textView_next);
        retrieve_data();
        if(status)
        {
            Intent i=new Intent(MainActivity.this,Dashboard.class);
            startActivity(i);
        }




        next_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });

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
            /*user_name = sharedPreferences.getString("name", "");
            EMail_user = sharedPreferences.getString("email", "");
            password_user = sharedPreferences.getString("password", "");*/
            status = sharedPreferences.getBoolean("status", false);

            // on below line we are setting data
            // to our name and age edit text.

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}