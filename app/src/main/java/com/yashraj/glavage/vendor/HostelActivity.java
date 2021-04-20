package com.yashraj.glavage.vendor;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yashraj.glavage.R;
import com.yashraj.glavage.vendor.Adapters.StudentAdapter;

import java.util.ArrayList;
import java.util.List;

public class HostelActivity extends AppCompatActivity {

    TextView textView;
    String hostelname;

    DatabaseReference vendordatabaseRefernce;
    FirebaseUser mUser;
    FirebaseApp firebaseApp;
    FirebaseAuth vendorAuth;
    RecyclerView myrecyclerView;
    StudentAdapter studentAdapter;
    List<String> studentModelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel);
        firebaseApp=FirebaseApp.getInstance("vendor");

        vendorAuth=FirebaseAuth.getInstance(firebaseApp);

        studentModelsList=new ArrayList<>();

        vendordatabaseRefernce=FirebaseDatabase.getInstance().getReference().child("vendorDatabase").child(vendorAuth.getCurrentUser().getUid());
        myrecyclerView=findViewById(R.id.students_recyclerView);
        myrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myrecyclerView.setHasFixedSize(true);

        textView=findViewById(R.id.hostel_name_hostel);
        hostelname=getIntent().getExtras().getString("hostel_name");
        textView.setText(hostelname);
        for(int i=50;i<61;i++){
            studentModelsList.add(String.valueOf(i));
        }

        studentAdapter=new StudentAdapter(studentModelsList,getApplicationContext());
        myrecyclerView.setAdapter(studentAdapter);
    }


}