package com.yashraj.glavage.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yashraj.glavage.R;
import com.yashraj.glavage.student.Adapters.ClothREcyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {
    private TextView username;
    private ImageView setting_img_button,user_image;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference userDatabase,databaseReference;
    private FloatingActionButton fab_clothes;
    private RecyclerView dashBoardRecyclerView;
    private ClothREcyclerAdapter clothREcyclerAdapter;
    private List<Model> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        userDatabase= FirebaseDatabase.getInstance().getReference().child("userDatabase").child(mUser.getUid());
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userDatabase").child(mUser.getUid()).child(mUser.getUid());
        user_image=findViewById(R.id.user_dashboard_image);
        Picasso.get().load(mUser.getPhotoUrl().toString()).into(user_image);
        dashBoardRecyclerView=findViewById(R.id.recycler_dashboard);
        dashBoardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        modelList=new ArrayList<>();

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
        loadData();
    }
    public void loadData(){
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    Model model=snapshot.getValue(Model.class);
                    modelList.add(model);
                    clothREcyclerAdapter=new ClothREcyclerAdapter(getApplicationContext(),modelList);
                    dashBoardRecyclerView.setAdapter(clothREcyclerAdapter);
                    clothREcyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}