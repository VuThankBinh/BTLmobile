package com.example.myapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class profile_acc extends AppCompatActivity {

    ImageView img;
    String imaUrl;
    TextView accName,accUser,sdt,accemail,btnPass;
    String pass,nameA,sdtA,emalA;
    ImageView backhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acc);
        img=findViewById(R.id.anhAvt);
        accName=findViewById(R.id.accName);
        accUser=findViewById(R.id.accUser);
        sdt=findViewById(R.id.sdt);
        accemail=findViewById(R.id.accemail);
        backhome=findViewById(R.id.backhome);
        btnPass=findViewById(R.id.btnPass);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile_acc.this,TrangChu.class));
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        System.out.println("id sign in: "+MainActivity.auth.getCurrentUser().getUid());
        DatabaseReference myRef = database.getReference("User");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Duyệt qua từng đối tượng con trong nút "User"
                    String userId=userSnapshot.getKey();
//                    String userId = userSnapshot.child("id").getValue(String.class);
//                    System.out.println("ID: " + userId);
                    // Kiểm tra xem trường "id" có trùng với số cụ thể không
                    if (MainActivity.auth.getCurrentUser().getUid().equals(userId)) {
                        // In ra thông tin của đối tượng
                        String imgUrl=userSnapshot.child("img").getValue(String.class);
                        String name = userSnapshot.child("name").getValue(String.class);

                        pass=userSnapshot.child("password").getValue(String.class);
                        String dt=userSnapshot.child("sdt").getValue(String.class);
                        String email=userSnapshot.child("email").getValue(String.class);
//                        String email = userSnapshot.child("email").getValue(String.class);
//                        System.out.println("ID: " + userId + ", Name: " + name);

                        imaUrl=imgUrl;
                        nameA=name;
                        sdtA=dt;
                        emalA=email;
                        System.out.println("anh"+imgUrl);
                        if(imgUrl.trim()!=""){
                            Glide.with(profile_acc.this).load(imgUrl).into(img);
                        }
                        accName.setText(name);
                        accUser.setText(name);
                        sdt.setText(dt);
                        accemail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        TextView editin4=findViewById(R.id.edit_in4);
        editin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEdit();
            }
        });
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDoiPass();
            }
        });
    }
    final static int PICK_IMAGE_REQUEST=1;
    public void onImageViewClick(View view) {
        // Khi ImageView được bấm, mở Intent để chọn ảnh từ thư viện
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Khi ảnh được chọn từ thư viện, hiển thị nó trong ImageView
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                img2.setImageBitmap(bitmap);
                Glide.with(profile_acc.this).load(bitmap).fitCenter().into(img2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void upLoadImage(String name, String sdt, String email) {
        // Tham chiếu đến Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

// Tạo tên duy nhất cho hình ảnh
        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("images/" + imageName);

// Chuyển đổi ImageView thành Bitmap
        img2.setDrawingCacheEnabled(true);
        img2.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img2.getDrawable()).getBitmap();

// Chuyển đổi Bitmap thành byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        Toast.makeText(profile_acc.this, "Đợi 1 xíu nha", Toast.LENGTH_SHORT).show();
// Tải hình ảnh lên Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Hình ảnh đã được tải lên thành công
                // Lấy đường dẫn của hình ảnh
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        // Lấy đường dẫn thành công
                        String imageUrl = downloadUri.toString();

                        // Gọi hàm để lưu thông tin cửa hàng vào Firestore với đường dẫn ảnh
                        addDataToFirestore(name,sdt,email,imageUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi tải lên thất bại
            }
        });

    }
    private void addDataToFirestore(String name, String sdt, String email,String imageUrl) {
        // Tham chiếu đến collection trong Firestore
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User").child(MainActivity.auth.getCurrentUser().getUid());
        myRef.child("img").setValue(imageUrl);
        myRef.child("sdt").setValue(sdt);
        myRef.child("name").setValue(name);
        myRef.child("email").setValue(email);
        Toast.makeText(profile_acc.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
    }

    LinearLayout chooseImg;
    ImageView img2;
    EditText nameN,sdtN,emailN;
    ImageView back;
    TextView save;
    String n,s,e;
    private void DialogEdit(){
        Dialog dialog = new Dialog(profile_acc.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.infouser);

        chooseImg=dialog.findViewById(R.id.chooseImg);
        nameN=dialog.findViewById(R.id.accUserNew);
        sdtN=dialog.findViewById(R.id.sdtNew);
        emailN=dialog.findViewById(R.id.accemailNew);
        emailN.setEnabled(false);
        img2=dialog.findViewById(R.id.anhAvtNew);
        save=dialog.findViewById(R.id.SaveHoSo);
        back=dialog.findViewById(R.id.backHoSo);
        if(imaUrl.trim()!=""){
            Glide.with(profile_acc.this).load(imaUrl).into(img2);
        }
        nameN.setText(nameA);
        sdtN.setText(sdtA);
        emailN.setText(emalA);
        n=nameN.getText().toString();
        s=sdtN.getText().toString();
        e=emailN.getText().toString();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameN.getText().toString())||TextUtils.isEmpty(sdtN.getText().toString())||TextUtils.isEmpty(emailN.getText().toString())){
                    Toast.makeText(profile_acc.this, "Bạn chưa nhập thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                upLoadImage(nameN.getText().toString(),sdtN.getText().toString(),emailN.getText().toString());
            }
        });


        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageViewClick(v);
            }
        });
        dialog.show();

    }
    private void doipass2(String pass33) {
        // Tham chiếu đến collection trong Firestore
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User").child(MainActivity.auth.getCurrentUser().getUid());
        myRef.child("password").setValue(pass33);
        Toast.makeText(profile_acc.this,"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
        pass= pass33;
        user.updatePassword(pass33)
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
    FirebaseUser user = MainActivity.auth.getCurrentUser();

    EditText pass1,pass2,pass3;
    TextView savee;
    ImageView img3,backkk;
    private void DialogDoiPass(){

        Dialog dialog = new Dialog(profile_acc.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.doipass);
        img3=dialog.findViewById(R.id.anhAvtNew3);
        pass1=dialog.findViewById(R.id.passCu);
        pass2=dialog.findViewById(R.id.passNew);
        pass3=dialog.findViewById(R.id.passXN);
        backkk=dialog.findViewById(R.id.backHoSo2);
        savee=dialog.findViewById(R.id.SaveHoSo2);
//
        backkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if(imaUrl.trim()!=""){
            Glide.with(profile_acc.this).load(imaUrl).into(img3);
        }
        savee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(pass1.getText().toString().trim())){
                    Toast.makeText(profile_acc.this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass2.getText().toString().trim())){
                    Toast.makeText(profile_acc.this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass3.getText().toString().trim())){
                    Toast.makeText(profile_acc.this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass1.getText().toString().trim().compareTo(pass)!=0){
                    Toast.makeText(profile_acc.this, "Mật khẩu cũ không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass2.getText().toString().trim().length()<6){
                    Toast.makeText(profile_acc.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass2.getText().toString().trim().compareTo(pass3.getText().toString().trim())!=0){
                    Toast.makeText(profile_acc.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                doipass2(pass2.getText().toString().trim());
            }
        });
        dialog.show();
    }
}