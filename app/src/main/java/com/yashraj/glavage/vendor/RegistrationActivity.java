package com.yashraj.glavage.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yashraj.glavage.R;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,mobile,address;
    Button submit_button;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    FirebaseApp firebaseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name=findViewById(R.id.vendor_name);
        mobile=findViewById(R.id.vendor_mobile);
        address=findViewById(R.id.vendor_address);
        submit_button=findViewById(R.id.vendor_submit_button);
        firebaseApp=FirebaseApp.getInstance("vendor");
        mAuth=FirebaseAuth.getInstance(firebaseApp);
        mUser=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("vendorDatabase");
        mobile.setText(mUser.getPhoneNumber());
        mobile.setEnabled(false);


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || address.getText().toString().isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Add All Fields", Toast.LENGTH_SHORT).show();
                }else{
                    submitUser();
                }

            }
        });
    }

    public void submitUser(){
        HashMap<String,Object> map=new HashMap<>();
        map.put("vendor_name",name.getText().toString());
        map.put("vendor_mobile",mUser.getPhoneNumber());
        map.put("vendor_address",address.getText().toString());


        databaseReference.child(mUser.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegistrationActivity.this,VendorDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}