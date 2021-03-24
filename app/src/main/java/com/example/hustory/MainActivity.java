package com.example.hustory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hustory.managementcard.FragmentMy;
import com.example.hustory.question.FragmentQuestion;
import com.example.hustory.reservation.FragmentReservation;
import com.example.hustory.reservation.GetRole;
import com.example.hustory.userInfo.UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentMain fragmentMain = new FragmentMain();
    private FragmentReservation fragmentReservation = new FragmentReservation();
    private FragmentMy fragmentMy = new FragmentMy();
    private FragmentQuestion fragmentQuestion = new FragmentQuestion();

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Hustory);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 이메일을 보내기 위한 쓰레드 정책 설정
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .permitDiskReads()
                        .permitDiskWrites()
                        .permitNetwork().build());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        myRef.child("Member").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("role").getValue().toString().equals("학생")){
                    GetRole.FLAG = 1;
                }else {
                    GetRole.FLAG = 2;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.child("Member").child(uid).child("R_List").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null){
                    GetRole.CONTENT_FLAG = 0;
                }else {
                    GetRole.CONTENT_FLAG = 1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        init();

        // 현재 로그인한 유저 정보 저장
        saveUser();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.mainItem:
                    transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();
                    break;
                case R.id.questionItem:
                    transaction.replace(R.id.frameLayout, fragmentQuestion).commitAllowingStateLoss();
                    break;
                case R.id.reservationItem:
                    transaction.replace(R.id.frameLayout, fragmentReservation).commitAllowingStateLoss();
                    break;
                case R.id.myItem:
                    transaction.replace(R.id.frameLayout, fragmentMy).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    public void replaceQuestion() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentQuestion).commitAllowingStateLoss();
    }

    public void replaceReservation() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentReservation).commitAllowingStateLoss();
    }

    public void init() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    // 현재 로그인한 유저 정보 저장
    private void saveUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference();

        // 로그인한 유저의 이름을 저장
        myRef.child("Member").child(currentUser.getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo.CUR_USER_NAME = snapshot.getValue().toString();
                Log.i(TAG, UserInfo.CUR_USER_NAME);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}