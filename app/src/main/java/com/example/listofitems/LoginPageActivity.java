package com.example.listofitems;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userId,pass;
    Button login;
    TextView signup,forgotpass;
    int flag=1;
    List<SignUpDetails> uploadList;
    String name,userid;

    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        userId=findViewById(R.id.Userid);
        pass=findViewById(R.id.Password);
        forgotpass=findViewById(R.id.forgotpassword);
        login=findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        uploadList=new ArrayList<SignUpDetails>();
        signup=findViewById(R.id.signuptext);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sp=new Intent(LoginPageActivity.this,SignUpActivity.class);
                sp.putExtra("AT","S");
                startActivity(sp);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sp=new Intent(LoginPageActivity.this,SignUpActivity.class);
                sp.putExtra("AT","F");
                startActivity(sp);
            }
        });

        if(!isConnected(LoginPageActivity.this))
        { buildDialog(LoginPageActivity.this).show();

        }
    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }
    @Override
    public void onClick(View view) {
        progressBar = new ProgressDialog(view.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait Sign-in in process ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("logindetails");
        progressBar.show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SignUpDetails upload = postSnapshot.getValue(SignUpDetails.class);
                    uploadList.add(upload);
                }

                String[] uploadid = new String[uploadList.size()];
                String[] uploadpass = new String[uploadList.size()];
                String[] uploadname = new String[uploadList.size()];

                for (int i = 0; i < uploadid.length; i++) {
                    uploadid[i] = uploadList.get(i).getUserid();
                    uploadpass[i] = uploadList.get(i).getPassword();
                    uploadname[i] = uploadList.get(i).getName();
                    // Toast.makeText(getApplicationContext(),uploadid[i]+uploadpass[i],Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < uploadid.length; i++) {
                    //Toast.makeText(getApplicationContext()," "+(lid1.getText().toString().trim()).equals(uploadid[i])+uploadid[i]+lid1.getText().toString().trim(),Toast.LENGTH_SHORT ).show();
                    if ((userId.getText().toString().trim().equals(uploadid[i].trim()) && (pass.getText().toString().trim().equals(uploadpass[i].trim())))) {
                           name=uploadname[i].trim();
                        flag = 0;
                        userid=userId.getText().toString().trim();
                        break;
                    }

                }
                if (flag == 1) {
                    Toast.makeText(getApplicationContext(), "wrong credentials", Toast.LENGTH_SHORT).show();
                    progressBar.cancel();


                } else {
                    Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();
                    Intent int1=new Intent(LoginPageActivity.this,MainActivity.class);
                    int1.putExtra("Username",name);
                    int1.putExtra("USERID",userid);
                    startActivity(int1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
