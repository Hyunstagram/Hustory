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
import java.util.Date;

public class CompareDate {
    private FirebaseData firebaseData;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private String other_uid;

    //    private String uid = user.getUid();
    private Long createTime;
    private Long currentTime;



    public void Compare (ArrayList<String> arr, int flag) {
        if (flag == 1){
            myRef.child("Member").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    other_uid = snapshot.child(uid).child("prof").getValue().toString();
                    Log.i("other", other_uid);
                    if(arr.size()>0 && flag == 1){
                        for (int i = 0; i<arr.size(); i++){
                            Log.i("compare", "i : " + i);
                            createTime = Long.valueOf(snapshot.child(uid).child("R_List").child(arr.get(i)).child("reservedate").getValue().toString());
                            Log.i("compare", createTime.toString());
                            currentTime = new Date().getTime();
                            Log.i("compare", "비교" + createTime + " : " + currentTime);
                            if (createTime.compareTo(currentTime) < 0){
                                myRef.child("Member").child(uid).child("R_List").child(arr.get(i)).child("before_after_data").setValue("true");
                                myRef.child("Member").child(other_uid).child("student").child(uid).child("R_List").child(arr.get(i)).child("before_after_data").setValue("true");
                                myRef.child("Member").child(other_uid).child("R_List").child(arr.get(i)).child("before_after_data").setValue("true");
                            }else{
                                return;
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
//            myRef.child("Member").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    other_uid = snapshot.child(uid).child("student").getValue().toString();
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
        }


    }
}
