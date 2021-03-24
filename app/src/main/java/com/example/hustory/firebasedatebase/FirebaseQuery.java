package com.example.hustory.firebasedatebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hustory.question.QuestionAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FirebaseQuery {
    private static final String TAG = "FirebaseQuery";
    public static FirebaseDatabase db;
    public static DatabaseReference myRef;

    public static QuestionAdapter questionAdapter = new QuestionAdapter();

    public static void new_order() {
        // 처음 리스트뷰 세팅
        myRef.child("Question").orderByChild("q_diffTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, "onDataChange()");
                if(snapshot != null) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        HashMap<String, Object> member = (HashMap<String, Object>) dataSnapshot.getValue();
                        String id = member.get("id").toString();
                        String q_Num = member.get("q_Num").toString();
                        String q_title = member.get("q_title").toString();
                        String q_content = member.get("q_content").toString();
                        String q_date = member.get("q_date").toString();
                        String q_time = member.get("q_time").toString();
                        String q_count = member.get("q_count").toString();
                        String q_diffTime = member.get("q_diffTime").toString();
                        String q_writer = member.get("q_writer").toString();

                        myRef.child("Member").child(id).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                String name = (String) task.getResult().getValue();
//                                Log.i("name ", name);
                                FirebaseUtility.name_Arr.add(0, name);
                            }
                        });
                        questionAdapter.addItemDESC(0, id, q_Num, q_title, q_content, q_date, q_time, q_count, q_diffTime, q_writer);

                        FirebaseUtility.name_Arr.add(0, q_writer);
                        FirebaseUtility.key_Arr.add(0, q_Num);
                        FirebaseUtility.count_Arr.add(0, q_count);
                        FirebaseUtility.id_Arr.add(0, id);
                    }
                }
                else {
                    Log.i(TAG, "Firebase Data 찾지 못함!!");
                    questionAdapter.clear();
                }
                questionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
