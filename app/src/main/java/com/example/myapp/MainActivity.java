package com.example.myapp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    static GoogleSignInClient mgs;
    TextView SignUpLink,forgot;
    Button btnLogin;
    EditText email, mk;

    ProgressBar pr;

    AppBarConfiguration mAppBarConfiguration;
    public static FirebaseAuth auth;


    public static int AC_Sign_In=100;
    public static boolean loginWithGG=false;
    public static boolean loginWithAcc=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignUpLink=findViewById(R.id.signUpLink);
        btnLogin=findViewById(R.id.btnLogin);
        email=findViewById(R.id.userLogin);
        mk=findViewById(R.id.passLogin);
        pr=findViewById(R.id.progressBar1);
        pr.setVisibility(View.GONE);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

//        View headerView = navigationView.getHeaderView(0);
//        TextView headerName = headerView.findViewById(R.id.nav_header_name);
//        TextView headerEmail = headerView.findViewById(R.id.nav_header_email);
//        CircleImageView headerImg = headerView.findViewById(R.id.nav_header_img);
        forgot=findViewById(R.id.forgotPass);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogForgotPass();
            }
        });
        auth=FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
                pr.setVisibility(View.VISIBLE);
            }
        });
        SignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DangKy.class));
            }
        });

        //đăng nhập bằng GG
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mgs= GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);

        ImageView bt= findViewById(R.id.gg);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                pr.setVisibility(View.VISIBLE);
            }
        });

    }

    private void ShowDialogForgotPass() {
        Dialog dialog=new Dialog(MainActivity.this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.forgotpass);
        EditText emailRes=dialog.findViewById(R.id.userReset);
        Button backRes=dialog.findViewById(R.id.btnBack);
        Button reset=dialog.findViewById(R.id.btnReset);
        backRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPass(emailRes.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void ResetPass(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "thành công: hãy vào email để reset password", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser() {
        String userName=email.getText().toString().trim();
        String pass=mk.getText().toString().trim();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(MainActivity.this,"User name is empty!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(MainActivity.this,"Password is empty!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.trim().length()<6){
            Toast.makeText(MainActivity.this,"Password lenght must be greater then 6 letter!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        //login user
        auth.signInWithEmailAndPassword(userName,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    passs=pass;
                    DoiPassNeuReset();
                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    loginWithAcc=true;
                    pr.setVisibility(View.GONE);
                    startActivity(new Intent(MainActivity.this,TrangChu.class));
                }
                else {
                    pr.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Error: "+task.getException(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    String passs;
    private void signIn(){
        Intent signInIntent= mgs.getSignInIntent();
        startActivityForResult(signInIntent,AC_Sign_In);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==AC_Sign_In){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            pr.setVisibility(View.GONE);
        }
    }
    static String a;
    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {

            GoogleSignInAccount acc = task.getResult(ApiException.class);

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//            a=auth.getUid().;

            System.out.println(a+"   8432");
            if (account != null) {
                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personID = account.getId();
                Uri personPhoyo = account.getPhotoUrl();

//                Toast.makeText(this, "User email:" + personEmail, Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, Register.class));
                loginWithGG=true;
                System.out.println(account.getId());
//                UserModel userModel=new UserModel(personName,personEmail,"");
////                String id=task.getResult().getUser().getUid();
//                database.getReference().child("User").child(personID).setValue(userModel);
                startActivity(new Intent(MainActivity.this,TrangChu.class));

            }
        } catch (ApiException e) {
//            throw new RuntimeException(e);
            Log.d("Message", e.toString());
        }
    }


    private void DoiPassNeuReset(){
        FirebaseUser user = MainActivity.auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User").child(MainActivity.auth.getCurrentUser().getUid());
        myRef.child("password").setValue(passs);
//        Toast.makeText(profile_acc.this,"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
//        pass= pass33;
        user.updatePassword(passs)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password updated successfully
                            Log.d("PasswordChange", "Password updated");
                        } else {
                            // If updating the password fails
                            Log.e("PasswordChange", "Error updating password: " + task.getException());
                        }
                    }
                });
    }
}