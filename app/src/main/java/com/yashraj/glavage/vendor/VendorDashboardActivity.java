package com.yashraj.glavage.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yashraj.glavage.R;
import com.yashraj.glavage.vendor.Adapters.RecyclerAdapter;
import com.yashraj.glavage.vendor.Models.HostelModel;

import java.util.ArrayList;
import java.util.List;

public class VendorDashboardActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseApp fireabseApp;
    FirebaseUser mUser;
    DatabaseReference hostel,vendordatabaseRefernce;
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    List<HostelModel> hostelModels;
    HostelModel model;
    TextView vendor_name;
    ImageView settings_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);
        fireabseApp= FirebaseApp.getInstance("vendor");
        mAuth=FirebaseAuth.getInstance(fireabseApp);
        mUser=mAuth.getCurrentUser();
        recyclerView=findViewById(R.id.hostel_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        hostelModels=new ArrayList<>();
        vendor_name=findViewById(R.id.vendor_name);
        settings_button=findViewById(R.id.settings_vendor_button);


        hostel= FirebaseDatabase.getInstance().getReference().child("hostel");
        vendordatabaseRefernce=FirebaseDatabase.getInstance().getReference().child("vendorDatabase").child(mUser.getUid());

        hostel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    model=new HostelModel(dataSnapshot.getValue().toString());
                    hostelModels.add(model);
                    adapter=new RecyclerAdapter(getApplicationContext(),hostelModels);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorDashboardActivity.this,VendorSettingsActivity.class));
            }
        });



        vendordatabaseRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    vendor_name.setText("Welcome, \n"+snapshot.child("vendor_name").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}