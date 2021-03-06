package com.example.hustory.question;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hustory.R;
import com.example.hustory.userInfo.UserInfo;
import com.example.hustory.util.DataStringFormat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AnswerActivity extends AppCompatActivity {

    AnswerAdapter answerAdapter;

    private Button button_answer;
    private ImageView button_mod;
    private ImageView button_del;

    private TextView question_name;
    private TextView q_person_name;
    private TextView question_time;
    private TextView question_content;

    private Intent intent;

    private static final String TAG = "AnswerActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private String userId;

    private String q_Num;
    private String name;
    private String writerId;
    private String q_diffTime;

    // ????????? ?????? ??? ????????????
    private EditText answerETXT;
    private Button ok;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer);
        userId = currentUser.getUid();

        init();

        initOnClickListener();
    }

    public void init() {
        answerAdapter = new AnswerAdapter();
        ListView listView = (ListView) findViewById(R.id.list_answer);
        listView.setAdapter(answerAdapter);

        // ????????? ????????? ??? ??????
        question_name = findViewById(R.id.question_name);
        q_person_name = findViewById(R.id.q_person_name);
        question_time = findViewById(R.id.question_time);
        question_content = findViewById(R.id.question_content);

        intent = getIntent();

        // ?????? ?????? ????????? ????????? ??? ?????????
        q_Num = intent.getStringExtra("q_Num");
        name = intent.getStringExtra("name");
        writerId = intent.getStringExtra("writerId");

        // ????????? ????????? ?????? ???????????? ??????
        myRef.child("Question").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.i("!task.isSuccessful() ", q_Num);
                }
                else {
                    if (String.valueOf(task.getResult().getValue()).equals("null")) {
                        Log.i("null ", q_Num);
                        Log.i("null ", String.valueOf(task.getResult().getValue()));
                        answerAdapter.notifyDataSetChanged();
                        return;
                    }
                    else {
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            String q_title = member.get("q_title").toString();
                            String q_content = member.get("q_content").toString();
                            q_diffTime = member.get("q_diffTime").toString();
                            String firebase_q_Num = member.get("q_Num").toString();

                            if(q_Num.equals(firebase_q_Num)){
                                question_name.setText(q_title);
                                q_person_name.setText(name);
                                Log.i(TAG, "??? ?????? ????????? : " + name);
                                q_diffTime = DataStringFormat.CreateDataWithCheck(q_diffTime);
                                question_time.setText(q_diffTime);
                                question_content.setText(q_content);
                            }
                        }
                        answerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // ????????? ?????? ???????????? ???????????? ??????
        myRef.child("Member").child(writerId).child("Q_List").child(q_Num).child("A_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                answerAdapter.clear();

                if (snapshot != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.i(TAG, dataSnapshot.toString());
                        HashMap<String, Object> member = (HashMap<String, Object>) dataSnapshot.getValue();
                        String id = member.get("id").toString();
                        String a_Num = member.get("a_Num").toString();
                        String a_content = member.get("a_content").toString();
                        String a_date = member.get("a_date").toString();
                        String a_time = member.get("a_time").toString();
                        String a_writer = member.get("a_writer").toString();
                        String a_diffTime = member.get("a_diffTime").toString();

                        answerAdapter.addItem(a_Num, a_content, a_date, a_time, a_writer, id, a_diffTime);
                    }
                    answerAdapter.notifyDataSetChanged();
                }
                else {
                    Log.i(TAG, "AnswerActivity : onDataChange() fail Firebase null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_answer = findViewById(R.id.button_answer);
        button_mod = findViewById(R.id.button_mod);
        button_del = findViewById(R.id.button_del);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private void initAnswerDialog() {
        // ????????? ????????? ??? ??????
        answerETXT = dialog.findViewById(R.id.answerETXT);
        ok = dialog.findViewById(R.id.answer_AddBTN);

        // ???????????? ?????? ?????? ???
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????? ????????? ?????? ???????????? ?????????
                if(answerETXT.getText().length() > 0) {
                    Date date = new Date();

                    Log.i("?????? : ", String.valueOf(date.getTime()));

                    SimpleDateFormat sFormat = new SimpleDateFormat("HH??? mm??? ss???");
                    SimpleDateFormat sFormat2 = new SimpleDateFormat("yyyy??? MM??? dd???");
                    SimpleDateFormat sFormat3 = new SimpleDateFormat("yyyyMMddHHmmss");

                    String a_Num = "A" + sFormat3.format(date);

                    AnswerItem data = new AnswerItem(a_Num,
                            answerETXT.getText().toString(),
                            sFormat2.format(date),
                            sFormat.format(date),
                            UserInfo.CUR_USER_NAME,
                            userId,
                            sFormat3.format(date));

                    // ????????? ????????? ????????? ??????
                    myRef.child("Member").child(writerId).child("Q_List").child(q_Num).child("A_List").child(a_Num).setValue(data);
                    dialog.cancel();
                }
                else {
                    startToast("????????? ???????????? ????????? ???????????????!");
                }
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

    public void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void startModQuestionActivity() {
        intent = new Intent(AnswerActivity.this, ModQuestionActivity.class);
        intent.putExtra("content", question_content.getText().toString());
        intent.putExtra("title", question_name.getText().toString());
        intent.putExtra("q_Num", q_Num);
        startActivity(intent);
        finish();
    }

    public void initOnClickListener() {
        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.button_answer:
                    // ????????? ?????? ????????? ????????????
                    AlertDialog.Builder builder = new AlertDialog.Builder(AnswerActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_question, null);
                    builder.setView(view);
                    dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    // ????????? ?????? ????????? ??? ?????? ????????? ??????
                    initAnswerDialog();
                    break;
                case R.id.button_mod:
                    // ???????????? ???????????? ????????? ????????? ??? ?????? ??????
                    if(q_person_name.getText().toString().equals(UserInfo.CUR_USER_NAME)) {
                        Log.i(TAG, UserInfo.CUR_USER_NAME);
                        Log.i(TAG, q_person_name.getText().toString());
                        startModQuestionActivity();
                    }
                    break;
                case R.id.button_del:
                    if(q_person_name.getText().toString().equals(UserInfo.CUR_USER_NAME)) {
                        AlertDialog.Builder del_builder = new AlertDialog.Builder(AnswerActivity.this);
                        del_builder.setTitle("?????? ??????");
                        del_builder.setMessage("?????????????????????????");

                        del_builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef.child("Question").child(q_Num).removeValue();
                                myRef.child("Member").child(writerId).child("Q_List").child(q_Num).removeValue();
                                finish();
                            }
                        });

                        del_builder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        del_builder.show();
                    }
                    break;
//                case R.id.button_mod_answer:
//                    // ???????????? ???????????? ????????? ????????? ?????? ?????? ??????
//                    if(q_person_name.getText().toString().equals(UserInfo.CUR_USER_NAME)) {
//                        // ????????? ?????? ????????? ????????????
//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AnswerActivity.this);
//                        LayoutInflater inflater2 = getLayoutInflater();
//                        View view2 = inflater2.inflate(R.layout.dialog_question, null);
//                        builder2.setView(view2);
//                        dialog = builder2.create();
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        dialog.show();
//
//                        // ????????? ?????? ????????? ??? ?????? ????????? ??????
//                        initAnswerDialog();
//                    }
//                    break;
//                case R.id.button_del_answer:
//                    if(q_person_name.getText().toString().equals(UserInfo.CUR_USER_NAME)) {
//                        AlertDialog.Builder del_builder = new AlertDialog.Builder(AnswerActivity.this);
//                        del_builder.setTitle("?????? ??????");
//                        del_builder.setMessage("?????????????????????????");
//
//                        del_builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                myRef.child("Question").child(q_Num).child("A_List").child("").removeValue();
//                                myRef.child("Member").child(writerId).child("Q_List").child(q_Num).child("A_List").child("").removeValue();
//                                finish();
//                            }
//                        });
//
//                        del_builder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//
//                        del_builder.show();
//                    }
//                    break;

            }
        };

        button_answer.setOnClickListener(onClickListener);
        button_mod.setOnClickListener(onClickListener);
        button_del.setOnClickListener(onClickListener);
    }
}
