package com.yashraj.glavage.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yashraj.glavage.R;
import com.yashraj.glavage.student.Adapters.ClothREcyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClothesActivity extends AppCompatActivity {
    Spinner sp;
    String[] bloodgroup = {"Shirt","Pant","Trousers","Jeans","T-shirt"};
    private ArrayAdapter<String> adp;
    Button add_button;
    ImageButton imageButton;
    private Uri ImageUri;
    private static  final int GALLERY_CODE=1;
    private DatabaseReference databaseReference;
    private StorageReference mStorage;
    private FirebaseUser muser;
    private FirebaseAuth mAuth;
    private ProgressDialog progressBar;
    private RecyclerView recyclerView;
    private List<Model> modelList;
    private ClothREcyclerAdapter clothREcyclerAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);
        sp=findViewById(R.id.spinner_type_cloth);
        imageButton=findViewById(R.id.add_image_cloth);
        mAuth=FirebaseAuth.getInstance();
        muser=mAuth.getCurrentUser();
        String userid=muser.getUid();
        progressBar=new ProgressDialog(this);
        mStorage= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userDatabase").child(userid).child(userid);

        modelList=new ArrayList<>();

        recyclerView=findViewById(R.id.cloth_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adp = new ArrayAdapter<String>(AddClothesActivity.this, android.R.layout.simple_list_item_1, bloodgroup);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);

        add_button=findViewById(R.id.add_image_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startposting();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });
        loadData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            ImageUri=data.getData();
            imageButton.setImageURI(ImageUri);

        }
    }

    private void startposting() {

        if(ImageUri!=null ){

            final StorageReference filepath=mStorage.child("Cloth_Images").child(ImageUri.getLastPathSegment());
            progressBar.setMessage("Adding Clothes...");
            progressBar.show();

            filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference newCloth=databaseReference.push();
                            Map<String,String> datatosave=new HashMap<>();

                            datatosave.put("cloth_type",sp.getSelectedItem().toString());
                            datatosave.put("dateadded", String.valueOf(System.currentTimeMillis()));
                            datatosave.put("image",uri.toString());
                            datatosave.put("userid",muser.getUid());
                           newCloth.setValue(datatosave);

                            progressBar.dismiss();
                            Toast.makeText(getApplicationContext(),"Cloth's Added",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });

        }

    }

public void loadData(){
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    Model model=snapshot.getValue(Model.class);
                    modelList.add(model);
                    clothREcyclerAdapter=new ClothREcyclerAdapter(getApplicationContext(),modelList);
                    recyclerView.setAdapter(clothREcyclerAdapter);
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