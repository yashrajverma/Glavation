package com.yashraj.glavage.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yashraj.glavage.R;

public class DashBoardActivity extends AppCompatActivity {
    private TextView username;
    private ImageView setting_img_button,user_image;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference userDatabase;
    private FloatingActionButton fab_clothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        userDatabase= FirebaseDatabase.getInstance().getReference().child("userDatabase").child(mUser.getUid());
        user_image=findViewById(R.id.user_dashboard_image);
        Picasso.get().load(mUser.getPhotoUrl().toString()).into(user_image);

        username=findViewById(R.id.user_name);
        setting_img_button=findViewById(R.id.settings_img_button);
        fab_clothes=findViewById(R.id.fab_add_clothes);
        fab_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this,AddClothesActivity.class));
            }
        });
        setting_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this,SettingActivity.class));
            }
        });
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username.setText("Welcome, \n"+snapshot.child("user_name").getValue().toString().toLowerCase());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}