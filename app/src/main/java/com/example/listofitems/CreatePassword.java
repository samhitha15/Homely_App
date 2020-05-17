package com.example.listofitems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreatePassword extends AppCompatActivity implements View.OnClickListener{

    EditText pass,repass;
    Button signup;
    String atval;

    DatabaseReference mDatabaseReference;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        pass=findViewById(R.id.Password);
        repass=findViewById(R.id.Repassword);
        signup=findViewById(R.id.btn_signup);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("logindetails");
        signup.setOnClickListener(this);
        Intent ii=getIntent();
        atval=ii.getStringExtra("at");
        if(atval.equals("F"))
        {
            signup.setHint("Proceed");
        }

        //Toast.makeText(CreatePassword.this,ii.getStringExtra("userid"),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
       if(atval.equals("F"))
       {
           updateUser(view);
       }
       else{

           addUser(view);
       }
    }

    private void updateUser(View view){
        progressBar = new ProgressDialog(view.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait Sign-up in process ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        if((pass.getText().toString().trim().length()>0)&&(repass.getText().toString().trim().length()>0))
        {
            if (!(pass.getText().toString().trim().equals(repass.getText().toString().trim())))
            {
                Toast.makeText(getApplicationContext(),"password mismatch", Toast.LENGTH_SHORT).show();
                progressBar.cancel();
            }

            else
            {
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Intent in=getIntent();
                        String UserID=in.getStringExtra("userid");
                        Toast.makeText(CreatePassword.this,UserID,Toast.LENGTH_SHORT).show();
                        String Name=in.getStringExtra("username");
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            SignUpDetails upload = postSnapshot.getValue(SignUpDetails.class);
                            if(upload.getUserid().equals(UserID))
                            {
                                mDatabaseReference.child(postSnapshot.getKey()).child("password").setValue(pass.getText().toString());
                                flag++;
                                //  Toast.makeText(getApplicationContext(),upload.getId()+id.getText().toString()+upload.getPhn()+phn.getText().toString(),Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }


                        if(flag==1)
                        {
                            Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                            progressBar.cancel();
                            startActivity(new Intent(CreatePassword.this,LoginPageActivity.class));
                        }

                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Empty fields",Toast.LENGTH_SHORT).show();
            progressBar.cancel();
        }


    }
    private void addUser(View view){

        progressBar = new ProgressDialog(view.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait Sign-up in process ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        if((pass.getText().toString().trim().length()>0)&&(repass.getText().toString().trim().length()>0))
        {
            if (!(pass.getText().toString().trim().equals(repass.getText().toString().trim())))
            {
                Toast.makeText(getApplicationContext(),"password mismatch", Toast.LENGTH_SHORT).show();
                progressBar.cancel();
            }

            else
            {
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Intent in=getIntent();
                        String UserID=in.getStringExtra("userid");
                        Toast.makeText(CreatePassword.this,UserID,Toast.LENGTH_SHORT).show();
                        String Name=in.getStringExtra("username");
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            SignUpDetails upload = postSnapshot.getValue(SignUpDetails.class);
                            if(upload.getUserid().equals(UserID))
                            {
                                flag++;
                                //  Toast.makeText(getApplicationContext(),upload.getId()+id.getText().toString()+upload.getPhn()+phn.getText().toString(),Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }


                        if(flag==1)
                        {
                            Toast.makeText(getApplicationContext(),"User already registered!Try login",Toast.LENGTH_SHORT).show();
                            progressBar.cancel();
                        }
                        else if(flag==0)
                        {
                            SignUpDetails u = new SignUpDetails(UserID, pass.getText().toString(),Name);
                            mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(u);
                            Toast.makeText(getApplicationContext(), "signup successfull", Toast.LENGTH_SHORT).show();
                            flag++;
                            Intent q=new Intent(CreatePassword.this, MainActivity.class);
                            q.putExtra("username",Name);
                            startActivity(q);
                        }
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Empty fields",Toast.LENGTH_SHORT).show();
            progressBar.cancel();
        }

    }
}
