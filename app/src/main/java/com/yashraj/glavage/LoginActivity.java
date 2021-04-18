package com.yashraj.glavage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.yashraj.glavage.student.MainActivity;
import com.yashraj.glavage.vendor.DhobiLoginActivity;

public class LoginActivity extends AppCompatActivity {
    Button dhobi_button,glavaasi_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        dhobi_button=findViewById(R.id.dhobi_button);
        glavaasi_button=findViewById(R.id.student_button);

        glavaasi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(LoginActivity.this, MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
               finish();
            }
        });

        dhobi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, DhobiLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}