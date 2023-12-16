package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Models.UserModel;

import com.example.myapp.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class DangKy extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    Button dangky;
    EditText email;
    EditText user;
    EditText mk1;
    EditText mk2;
    TextView loginLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        dangky = findViewById(R.id.btnDK);
        email = findViewById(R.id.userEmail);
        user = findViewById(R.id.user);
        mk1 = findViewById(R.id.pass);
        mk2 = findViewById(R.id.passXN);
        loginLink = findViewById(R.id.loginLink);

        auth= FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        //Login if có tk
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangKy.this,MainActivity.class));
            }
        });
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void createUser() {
        String userName=user.getText().toString().trim();
        String userEmail=email.getText().toString().trim();
        String pass1= mk1.getText().toString().trim();
        String pass2 =mk2.getText().toString().trim();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(DangKy.this,"User name is empty!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(DangKy.this,"Email is empty!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass1)){
            Toast.makeText(DangKy.this,"Password is empty!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass2)){
            Toast.makeText(DangKy.this,"Confirm Password is empty!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass1.trim().length()<6){
            Toast.makeText(DangKy.this,"Password lenght must be greater then 6 letter!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass1.trim().compareTo(pass2.trim())!=0){
            Toast.makeText(DangKy.this,"Password do not match!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(userEmail,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserModel userModel=new UserModel(userName,userEmail,pass1,"","https://firebasestorage.googleapis.com/v0/b/baitaplon-e42e3.appspot.com/o/user.png?alt=media&token=47999e85-1ab9-404f-94bd-62bc3919d040");
                    String id=task.getResult().getUser().getUid();
                    database.getReference().child("User").child(id).setValue(userModel);
                    Toast.makeText(DangKy.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DangKy.this,"Error: "+task.getException(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}