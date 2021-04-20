package com.yashraj.glavage.vendor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.hbb20.CountryCodePicker;
import com.yashraj.glavage.R;

import java.util.concurrent.TimeUnit;

public class DhobiLoginActivity extends AppCompatActivity {

    EditText phonetext, codetext;
    private Button continueNextButton;
    private String phonenumber = "", checker = "";
    RelativeLayout relativeLayout;
    private CountryCodePicker ccp;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    ProgressDialog loadingbar;
    private String TAG;
    private FirebaseUser mUser;
    private DatabaseReference databaseReference;
    FirebaseOptions options;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhobi_login);
       options= new FirebaseOptions.Builder()
                .setProjectId("glavage-d3e30")
                .setApplicationId("1:921441103255:android:37c13ab34380fbecf968d2")
                .setApiKey("AIzaSyBsXj6Ve9Dp_UT_v2IaesfCH2S91C6duks")
                .build();

        FirebaseApp.initializeApp(getApplicationContext() /* Context */, options, "vendor");

        FirebaseApp secondary = FirebaseApp.getInstance("vendor");

        mAuth=FirebaseAuth.getInstance(secondary);
        mUser=mAuth.getCurrentUser();
        phonetext=findViewById(R.id.phoneEditText);
        codetext=findViewById(R.id.codeEditText);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phonetext);
        loadingbar = new ProgressDialog(this);
        continueNextButton=findViewById(R.id.continueNextButton);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    Log.d("User loggin in status..............", "User logged in!!");
                    Toast.makeText(DhobiLoginActivity.this, "Verified User", Toast.LENGTH_SHORT).show();
                    sendUserToMainActivity();
                    finish();
                }
            }
        };
        relativeLayout = findViewById(R.id.phoneAuth);
        continueNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (continueNextButton.getText().equals("Submit") || checker.equals("Code Sent")) {
                    String verificationcode = codetext.getText().toString();
                    if (verificationcode.equals("")) {
                        Toast.makeText(DhobiLoginActivity.this, "Provide verification code", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingbar.setTitle("Code Verification");
                        loadingbar.setMessage("Verifying Code provided...");
                        loadingbar.setCanceledOnTouchOutside(false);
                        loadingbar.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);
                        signInWithPhoneAuthCredential(credential);
                    }
                } else {
                    phonenumber = ccp.getFullNumberWithPlus();
                    if (!phonenumber.equals("")) {
                        loadingbar.setTitle("Phone Number Verification");
                        loadingbar.setMessage("Verifying phone number...");
                        loadingbar.setCanceledOnTouchOutside(false);
                        loadingbar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phonenumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                DhobiLoginActivity.this,               // Activity (for callback binding)
                                mCallbacks);        // OnVerificationStateChangedCallbacks

                    } else {
                        Toast.makeText(DhobiLoginActivity.this, "Please provide valid Number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

                signInWithPhoneAuthCredential(phoneAuthCredential);
                phonetext.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                relativeLayout.setVisibility(View.VISIBLE);
                loadingbar.dismiss();

                continueNextButton.setText("Continue");
                codetext.setVisibility(View.INVISIBLE);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(DhobiLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(DhobiLoginActivity.this, "You have too many attempts", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                relativeLayout.setVisibility(View.GONE);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                checker = "Code Sent";
                continueNextButton.setText("Submit");
                codetext.setVisibility(View.VISIBLE);
                // ...
                loadingbar.dismiss();
                Toast.makeText(DhobiLoginActivity.this, "Code has been sent!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void sendUserToMainActivity() {

        Intent intent=new Intent(DhobiLoginActivity.this,RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            loadingbar.dismiss();
                            sendUserToMainActivity();
                            Toast.makeText(DhobiLoginActivity.this, "Verified User ", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            loadingbar.dismiss();
                            String error = task.getException().getMessage();

                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(DhobiLoginActivity.this, "Error :" + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
       if( options.getApplicationId().equals("1:921441103255:android:37c13ab34380fbecf968d2")){
           if(mAuth!=null){
               mAuth.addAuthStateListener(authStateListener);
           }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}
