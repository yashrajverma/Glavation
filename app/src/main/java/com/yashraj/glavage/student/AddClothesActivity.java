package com.yashraj.glavage.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yashraj.glavage.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;

import static java.lang.Long.parseLong;

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

    private FirebaseRecyclerAdapter<Model,ModelViewHolder> firebaseRecyclerAdapter;



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
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userDatabase").child(userid);

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

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    public void loadData(){
        FirebaseRecyclerOptions<Model> options
            =new FirebaseRecyclerOptions.Builder<Model>().setQuery(databaseReference,Model.class).build();
            firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Model, ModelViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ModelViewHolder modelViewHolder, int i, @NonNull Model model) {
                    modelViewHolder.cloth_type.setText(model.getCloth_type());
                    DateFormat dateFormat=DateFormat.getDateInstance();
                    String formattedate=dateFormat.format(new Date(parseLong(model.getDateadded())).getTime());
                    modelViewHolder.dataAdded.setText(formattedate);
                    String image_url=model.getImage();
                    Picasso.get().load(image_url).into(modelViewHolder.image);

                }

                @NonNull
                @Override
                public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image,parent,false);
                    return new ModelViewHolder(view);
                }
            };
            firebaseRecyclerAdapter.startListening();
            recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private static class ModelViewHolder extends RecyclerView.ViewHolder{
        TextView dataAdded,cloth_type;
        ImageView image;
        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            dataAdded=itemView.findViewById(R.id.date_added_textView);
            cloth_type=itemView.findViewById(R.id.cloth_type_textView);
            image=itemView.findViewById(R.id.card_cloth_image);
        }
    }
}