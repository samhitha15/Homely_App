package com.example.listofitems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
public class SignUpActivity extends AppCompatActivity {
    Button btnGenerateOTP, btnSignIn;

    EditText etPhoneNumber, etOTP,username;
    String phoneNumber, otp,name,AT;
    FirebaseAuth auth;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    int flag=0;
    TextView login;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etOTP = findViewById(R.id.et_otp);
        btnGenerateOTP = findViewById(R.id.btn_generate_otp);
        btnSignIn = findViewById(R.id.btn_sign_in);
        username=findViewById(R.id.Username);
        login=findViewById(R.id.logintext);
        Intent ip=getIntent();
        AT=ip.getStringExtra("AT");
        if(AT.equals("F"))
        {
            btnSignIn.setHint("Verify OTP");
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sp=new Intent(SignUpActivity.this,LoginPageActivity.class);
                startActivity(sp);
            }
        });


        StartFirebaseLogin();

        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber=etPhoneNumber.getText().toString();
                name=username.getText().toString();
                checkifNumberExists(v,AT);
                         }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = etOTP.getText().toString();




                try {
    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);

    SigninWithPhone(credential);
}
catch (Exception e){
    Toast toast = Toast.makeText(SignUpActivity.this, "Verification Code is wrong", Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER,0,0);
    toast.show();
}
            }
        });

    }


    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            name=username.getText().toString();
                            Intent i=new Intent(SignUpActivity.this,CreatePassword.class);
                            i.putExtra("username",name);
                            i.putExtra("userid",phoneNumber);
                            i.putExtra("at",AT);
                            startActivity(i);

                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(SignUpActivity.this, "verification completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(SignUpActivity.this, "verification fialed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(SignUpActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }
    private void checkifNumberExists(View v, final String AT){
    progressBar = new ProgressDialog(v.getContext());
    progressBar.setCancelable(true);
    progressBar.setMessage("Requesting OTP for the above number");
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    progressBar.show();
    progressBarStatus = 0;
    if((phoneNumber.trim().length()>0) && (name.trim().length()>0))
    {
        DatabaseReference mDatabaseReference= FirebaseDatabase.getInstance().getReference("logindetails");
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        SignUpDetails upload = postSnapshot.getValue(SignUpDetails.class);
                        if(upload.getUserid().equals(phoneNumber))
                        {
                            flag++;
                            break;
                        }


                    }
                    if(flag==1 && AT.equals("S"))
                    {
                        Toast.makeText(getApplicationContext(),"Empty fields",Toast.LENGTH_SHORT).show();
                    }

                    else if(flag<=0 && AT.equals("F"))
                    {
                        Toast.makeText(getApplicationContext(),"Check the Number and Try Again!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        phoneNumber = etPhoneNumber.getText().toString();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,                     // Phone number to verify
                                60,                           // Timeout duration
                                TimeUnit.SECONDS,                // Unit of timeout
                                SignUpActivity.this,        // Activity (for callback binding)
                                mCallback);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
    else
    {
        Toast.makeText(getApplicationContext(),"Empty fields",Toast.LENGTH_SHORT).show();

    }
    progressBar.cancel();

}

}