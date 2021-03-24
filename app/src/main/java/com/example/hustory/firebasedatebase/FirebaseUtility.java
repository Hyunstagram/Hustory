package com.example.hustory.firebasedatebase;

import com.example.hustory.question.QuestionAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtility {
    public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = db.getReference();

    public static QuestionAdapter questionAdapter = new QuestionAdapter();

    public static ArrayList<String> name_Arr = new ArrayList<>();
    public static ArrayList<String> key_Arr = new ArrayList<>();
    public static ArrayList<String> count_Arr = new ArrayList<>();
    public static ArrayList<String> id_Arr = new ArrayList<>();

}
