package com.example.hustory.reservation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AcceptClick {
    private FirebaseData firebaseData;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String student_uid;


    public void Click(ArrayList<String> arr, int position, String uid) {
        myRef.child("Member").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student_uid = snapshot.child(uid).child("R_List").child(arr.get(position)).child("uid").getValue().toString();
                myRef.child("Member").child(uid).child("R_List").child(arr.get(position)).child("allow").setValue("상담대기");
                myRef.child("Member").child(student_uid).child("R_List").child(arr.get(position)).child("allow").setValue("상담대기");
                myRef.child("Member").child(uid).child("student").child(student_uid).child("R_List").child(arr.get(position)).child("allow").setValue("상담대기");
                Log.i("long",student_uid);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return;
    }


}
