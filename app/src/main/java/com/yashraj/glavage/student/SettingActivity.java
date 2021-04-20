package com.yashraj.glavage.student;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yashraj.glavage.R;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    Button button,done_button;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    ImageView imageView;
    TextInputEditText mobilesettings_,namesettings_,room_nosettings_;

    private Spinner hostel_spinner;
    String[] hostel_array = {"A Block","B Block","C Block","D Block","E Block","F Block","G Block","H Block","I Block","J Block","Wing 1",
            "Wing 2","Wing 3","Wing 4","Wing 5"};
    private ArrayAdapter<String> adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userDatabase").child(mUser.getUid());
        imageView=findViewById(R.id.user_setting_image);
        Picasso.get().load(mUser.getPhotoUrl().toString()).into(imageView);
        mobilesettings_=findViewById(R.id.settings_mobile_editText);
        namesettings_=findViewById(R.id.settings_name_editText);
        room_nosettings_=findViewById(R.id.settings_room_no_editText);
        hostel_spinner=findViewById(R.id.hostel_spinner_settings);
        adp = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1, hostel_array);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hostel_spinner.setAdapter(adp);

        done_button=findViewById(R.id.ok_setting_button);
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });


        button=findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        getData();

    }
    private void logout(){
        mAuth.signOut();
        Intent intent=new Intent(SettingActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void getData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    room_nosettings_.setText(snapshot.child("user_room_no").getValue().toString());
                    mobilesettings_.setText(snapshot.child("user_mobile").getValue().toString());
                    namesettings_.setText(snapshot.child("user_name").getValue().toString());
                    hostel_spinner.setTooltipText((CharSequence) snapshot.child("user_hostel").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void submitData(){
        HashMap<String,Object> map=new HashMap<>();
        map.put("user_name",namesettings_.getText().toString());
        map.put("user_mobile",mobilesettings_.getText().toString());
        map.put("user_hostel",hostel_spinner.getSelectedItem().toString());
        map.put("user_room_no",room_nosettings_.getText().toString());

        databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SettingActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingActivity.this,DashBoardActivity.class));
                }
            }
        });

    }
}