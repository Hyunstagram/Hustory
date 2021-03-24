package com.example.hustory.managementcard;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hustory.R;
import com.example.hustory.user.m_User;
import com.example.hustory.userInfo.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class CardActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText name, birth, email, education, certificate, experience, carrer;
    Button button_modify;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private FirebaseUser cuurentUser;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    View view;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);


        readUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_card);

        imageView = findViewById(R.id.icon_profile);


        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/" + uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                //Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });


        button_modify= (Button)findViewById(R.id.button_modify);

        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = (EditText)findViewById(R.id.name);
                birth =(EditText)findViewById(R.id.birth);
                email = (EditText)findViewById(R.id.email);
                education =(EditText)findViewById(R.id.education);
                certificate =(EditText)findViewById(R.id.certificate);
                experience =(EditText)findViewById(R.id.experience);
                carrer = (EditText)findViewById(R.id.career);

                String getUsername = name.getText().toString();
                String getUserbirth = birth.getText().toString();
                String getUseremail = email.getText().toString();
                String getUsereducation = education.getText().toString();
                String getUsercertificate = certificate.getText().toString();
                String getUserexpercience = experience.getText().toString();
                String getUsercarrer = carrer.getText().toString();

                Log.i("write 전", getUsername);


                writeNewUser(uid,getUsername,getUseremail,getUsercertificate,getUserbirth,getUserexpercience,getUsereducation,getUsercarrer);

            }
        });


    }

    private void writeNewUser(String userId, String name, String email , String certificate,String birth, String experience, String education,String carrer) {
        //hashmap 만들기
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("birth", birth);
        result.put("email", email);
        result.put("education", education);
        result.put("certificate", certificate);
        result.put("experience", experience);
        result.put("carrer", carrer);

        mDatabase.child("Member").child(userId).child("management").child("managecard").setValue(result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(CardActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(CardActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void readUser(){

        Log.i("uid", uid);
        mDatabase.child("Member").child(uid).child("management").child("managecard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {

                    String birth1 = dataSnapshot.child("birth").getValue().toString();
                    Log.i("onDataChange", birth1);
                    String carrer1 = dataSnapshot.child("carrer").getValue().toString();
                    String certificate1 = dataSnapshot.child("certificate").getValue().toString();
                    String education1 = dataSnapshot.child("education").getValue().toString();
                    String email1 = dataSnapshot.child("email").getValue().toString();
                    String experience1= dataSnapshot.child("experience").getValue().toString();
                    String name1 = dataSnapshot.child("name").getValue().toString();

                    name = (EditText)findViewById(R.id.name);
                    birth =(EditText)findViewById(R.id.birth);
                    email = (EditText)findViewById(R.id.email);
                    education =(EditText)findViewById(R.id.education);
                    certificate =(EditText)findViewById(R.id.certificate);
                    experience =(EditText)findViewById(R.id.experience);
                    carrer = (EditText)findViewById(R.id.career);

                    birth.setText(birth1);
                    name.setText(name1);
                    email.setText(email1);
                    education.setText(education1);
                    certificate.setText(certificate1);
                    experience.setText(experience1);
                    carrer.setText(carrer1);



                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}