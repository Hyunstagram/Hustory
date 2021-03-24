package com.example.hustory.reservation;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.hustory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class SetDialog {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String professor;
    private String student;
    private String summary;
    private String date;
    private String way;
    private String place;
    private String allow;
    private String before_after_data;
    private String key;
    private String contents;
    private String firebase_key;
    private String time;
    private String verstudent;
    private String school;
    private String major, prof_uid, student_uid;
    private String company_1, company_2, company_3;
    private Button button_previous;

    public void SetDialogStudent(View view1, ArrayList<String> arr, int position, LinearLayout layout, String uid) {
        // dialog 세팅

        key = arr.get(position);

        // view 세팅
        myRef.child("Member").child(uid).child("R_List").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    if (String.valueOf(task.getResult().getValue()).equals("null")){
                        return;
                    }else{
                        layout.setVisibility(View.GONE);
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            professor = member.get("professor").toString();
                            summary = member.get("summary").toString();
                            date = member.get("date").toString();
                            way = member.get("way").toString();
                            place = member.get("place").toString();
                            allow = member.get("allow").toString();
                            contents = member.get("contents").toString();
                            before_after_data = member.get("before_after_data").toString();
                            firebase_key = member.get("key").toString();
                            time = member.get("time").toString();
                            if(before_after_data.equals("false") && key.equals(firebase_key)){
                                TextView text_professor = (TextView) view1.findViewById(R.id.text_professor);
                                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
                                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
                                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
                                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
                                TextView content = (TextView) view1.findViewById(R.id.content);
                                TextView reservation_state = (TextView) view1.findViewById(R.id.reservation_state);
                                TextView reservation_time = (TextView) view1.findViewById(R.id.reservation_time);
                                text_professor.setText(professor);
                                text_summary.setText(summary);
                                reservation_date.setText(date);
                                reservation_way.setText(way);
                                reservation_place.setText(place);
                                content.setText(contents);
                                reservation_state.setText(allow);
                                reservation_time.setText(time);
                            }else {
                            }
                        }
                    }
                }
            }
        });
    }

    public void SetDialogProfessor(View view1, ArrayList<String> arr, int position, LinearLayout layout, String uid) {
        // dialog 세팅

        key = arr.get(position);

        // view 세팅
        myRef.child("Member").child(uid).child("R_List").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    if (String.valueOf(task.getResult().getValue()).equals("null")){

                    }else{
                        layout.setVisibility(View.GONE);
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            student = member.get("student").toString();
                            summary = member.get("summary").toString();
                            date = member.get("date").toString();
                            way = member.get("way").toString();
                            place = member.get("place").toString();
                            allow = member.get("allow").toString();
                            contents = member.get("contents").toString();
                            before_after_data = member.get("before_after_data").toString();
                            firebase_key = member.get("key").toString();
                            time = member.get("time").toString();
                            student_uid = member.get("uid").toString();
                            Log.i("check", "no set : " + allow);
                            if(before_after_data.equals("false") && key.equals(firebase_key)){
                                Log.i("check", "no set : " + key);
                                ImageView icon_professor = view1.findViewById(R.id.icon_professor);
                                TextView name_student = (TextView) view1.findViewById(R.id.name_student1);
                                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
                                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
                                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
                                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
                                TextView content = (TextView) view1.findViewById(R.id.content);
                                Button button_accept = (Button) view1.findViewById(R.id.button_accept);
                                TextView reservation_time = (TextView) view1.findViewById(R.id.reservation_time);
                                FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
                                StorageReference storageRef = storage.getReference();
                                storageRef.child("images/" + student_uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //이미지 로드 성공시
                                        Glide.with(view1.getContext())
                                                .load(uri)
                                                .into(icon_professor);
                                    }});
                                name_student.setText(student);
                                text_summary.setText(summary);
                                reservation_date.setText(date);
                                reservation_way.setText(way);
                                reservation_place.setText(place);
                                content.setText(contents);
                                button_accept.setText(allow);
                                reservation_time.setText(time);
                            }else {
//                                TextView text_professor = (TextView) view1.findViewById(R.id.text_professor);
//                                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
//                                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
//                                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
//                                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
//                                TextView content = (TextView) view1.findViewById(R.id.content);
//                                text_professor.setText(professor);
//                                text_summary.setText(summary);
//                                reservation_date.setText(date);
//                                reservation_way.setText(way);
//                                reservation_place.setText(place);
//                                content.setText(contents);
                            }
                        }
                    }
                }
            }
        });
    }

    public void SetDialogCard (View view1, int position, ArrayList<String> arr, ArrayList<String> nameArr) {

        key = arr.get(position);
        student = nameArr.get(position);


        // view 세팅
        myRef.child("Member").child(key).child("management").child("myinfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ImageView icon_student = view1.findViewById(R.id.icon_student);
                TextView version_student = (TextView) view1.findViewById(R.id.version_student);
                TextView my_school = (TextView) view1.findViewById(R.id.my_school);
                TextView my_major = (TextView) view1.findViewById(R.id.my_major);
                TextView my_company_1 = (TextView) view1.findViewById(R.id.my_company_1 );
                TextView my_company_2 = (TextView) view1.findViewById(R.id.my_company_2);
                TextView my_company_3 = (TextView) view1.findViewById(R.id.my_company_3);
                TextView name_student = (TextView) view1.findViewById(R.id.name_student1);
                name_student.setText(student);

                FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
                StorageReference storageRef = storage.getReference();
                storageRef.child("images/" + key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //이미지 로드 성공시
                        Glide.with(view1.getContext())
                                .load(uri)
                                .into(icon_student);

                    }});

                if(snapshot.getValue() != null) {
                    if(snapshot.child("version_student").getValue() != null){
                        verstudent = snapshot.child("version_student").getValue().toString();
                        version_student.setText(verstudent);
                        Log.i("snap", "마제환");
                    }
                    if (snapshot.child("my_school").getValue() != null){
                        school = snapshot.child("my_school").getValue().toString();
                        Log.i("onDataChange", school);
                        my_school.setText(school);
                    }
                    if (snapshot.child("my_major").getValue() != null){
                        major = snapshot.child("my_major").getValue().toString();
                        my_major.setText(major);
                    }
                    if(snapshot.child("my_company_1").getValue() != null){
                        company_1 = snapshot.child("my_company_1").getValue().toString();
                        my_company_1.setText(company_1);
                    }
                    if(snapshot.child("my_company_2").getValue() != null){
                        company_2 = snapshot.child("my_company_2").getValue().toString();
                        my_company_2.setText(company_2);
                    }
                    if(snapshot.child("my_company_3").getValue() != null){
                        company_3 = snapshot.child("my_company_3").getValue().toString();
                        my_company_3.setText(company_3);
                    }
                }else {
                    Log.i("snap", "마제환2");
                    verstudent = "";
                    school = "";
                    major = "";
                    company_1 = "";
                    company_2 = "";
                    company_3 = "";
                    version_student.setText(verstudent);
                    my_school.setText(school);
                    my_major.setText(major);
                    my_company_1.setText(company_1);
                    my_company_2.setText(company_2);
                    my_company_3.setText(company_3);
                    name_student.setText(student);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
