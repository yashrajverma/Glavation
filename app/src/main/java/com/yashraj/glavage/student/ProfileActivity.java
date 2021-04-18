package com.yashraj.glavage.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yashraj.glavage.R;

import java.util.HashMap;import android.app.ProgressDialog;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button GoButton;
    private TextInputEditText mobileEditText,nameEditText,hostelEditText,room_noEditText;
    private String  mobileStringText="",nameStringText="",hostelStringText="",room_noStringText="";
    DatabaseReference userDatabase;
    private String TAG=ProfileActivity.class.getName();
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        userDatabase= FirebaseDatabase.getInstance().getReference().child("userDatabase");

        progressBar=new ProgressDialog(this);
        GoButton=findViewById(R.id.go_button);
        mobileEditText=findViewById(R.id.mobile_editText);
        nameEditText=findViewById(R.id.name_editText);
        room_noEditText=findViewById(R.id.room_no_editText);
        hostelEditText=findViewById(R.id.hostel_editText);
        nameEditText.setText(mUser.getDisplayName());


        retrieveData();
        GoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.show();
//                logout();
                if(nameEditText.getText().toString().isEmpty() && mobileEditText.getText().toString().isEmpty()
                && room_noEditText.getText().toString().isEmpty() && hostelEditText.getText().toString().isEmpty()){

                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter All Fields", Toast.LENGTH_LONG).show();
                }else if(mobileEditText.getText().toString().length()<10){
                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter 10 Digit Mobile Number", Toast.LENGTH_LONG).show();
                }else{
                    registerUser();
                }
            }
        });

    }
    private void registerUser(){
        mobileStringText=mobileEditText.getText().toString();
        hostelStringText=hostelEditText.getText().toString();
        room_noStringText=room_noEditText.getText().toString();
        nameStringText=nameEditText.getText().toString();
        HashMap<String,Object> userMap=new HashMap();
        userMap.put("user_id",mUser.getUid());
        userMap.put("user_name",nameStringText);
        userMap.put("user_hostel",hostelStringText);
        userMap.put("user_mobile",mobileStringText);
        userMap.put("user_room_no",room_noStringText);
        userDatabase.child(mUser.getUid()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "Registered SuccessFully", Toast.LENGTH_SHORT).show();
                    sendUsertoMainActivity();
                    progressBar.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });
    }
    private void retrieveData() {
        progressBar.show();
        userDatabase.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.dismiss();
                    sendUsertoMainActivity();

                    mobileEditText.setText(snapshot.child("user_mobile").getValue().toString());
                    nameEditText.setText(snapshot.child("user_name").getValue().toString());
                    room_noEditText.setText(snapshot.child("user_room_no").getValue().toString());
                    hostelEditText.setText(snapshot.child("user_hostel").getValue().toString());
                }else {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void sendUsertoMainActivity() {
        Intent intent = new Intent(ProfileActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



}
