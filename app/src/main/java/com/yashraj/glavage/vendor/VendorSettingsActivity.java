package com.yashraj.glavage.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.yashraj.glavage.LoginActivity;
import com.yashraj.glavage.R;

public class VendorSettingsActivity extends AppCompatActivity {
    Button button;
    FirebaseAuth mAuth;
    FirebaseApp firebaseApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_settings);
        firebaseApp=FirebaseApp.getInstance("vendor");
        mAuth=FirebaseAuth.getInstance(firebaseApp);

        button=findViewById(R.id.vendor_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(VendorSettingsActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}