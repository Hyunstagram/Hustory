package com.example.hustory.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hustory.MainActivity;
import com.example.hustory.R;
import com.example.hustory.reservation.GetRole;
import com.example.hustory.userInfo.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private EditText emailETXT;
    private EditText passwordETXT;

    private TextView find_Id_TXT;
    private TextView find_Pw_TXT;

    private TextView login_signUpBTN;
    private Button login_loginBTN;
    private CheckBox auto_loginBTN;

    private SharedPreferences auto;
    private SharedPreferences.Editor auto_editor;

    private Intent intent;

    private String email;
    private String password;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // 레이아웃 요소 초기화
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        emailETXT.setText(auto.getString("id", ""));
        passwordETXT.setText(auto.getString("pw", ""));

        if(!auto.getString("id", "").equals("") || !auto.getString("pw", "").equals("")){
            Log.i(TAG, auto.getString("id", ""));
            Log.i(TAG, auto.getString("pw", ""));

            auto_loginBTN.setChecked(true);
            initRole(currentUser.getUid());
            startMainActivity();
        }
    }

    private void init() {
        emailETXT = findViewById(R.id.login_emailETXT);
        passwordETXT = findViewById(R.id.login_passwordETXT);

        find_Id_TXT = findViewById(R.id.find_Id_TXT);
        find_Pw_TXT = findViewById(R.id.find_Pw_TXT);

        login_signUpBTN = findViewById(R.id.login_signUpBTN);
        login_loginBTN = findViewById(R.id.login_loginBTN);
        auto_loginBTN = findViewById(R.id.auto_loginBTN);

        auto = PreferenceManager.getDefaultSharedPreferences(this);
        auto_editor = auto.edit();

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.login_signUpBTN:
                    signUpActivity();
                    break;
                case R.id.login_loginBTN:
                    login();
                    break;
                case R.id.find_Id_TXT:
                    findIdActivity();
                    break;
                case R.id.find_Pw_TXT:
                    findPwActivity();
                    break;
                default:
                    break;
            }
        };

        login_loginBTN.setOnClickListener(onClickListener);
        login_signUpBTN.setOnClickListener(onClickListener);
        find_Id_TXT.setOnClickListener(onClickListener);
        find_Pw_TXT.setOnClickListener(onClickListener);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private void login() {
        email = emailETXT.getText().toString();
        password = passwordETXT.getText().toString();

        if(email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        if(auto_loginBTN.isChecked()) {
                            auto_login(true);
                        }
                        else {
                            auto_login(false);
                        }
                        currentUser = mAuth.getCurrentUser();
                        initRole(currentUser.getUid());
                        startToast("로그인에 성공하였습니다.");
                        startMainActivity();
                    }
                    else {
                        if(task.getException() != null) {
                            Log.i(TAG, task.getException().toString());
                            startToast("이메일 또는 비밀번호가 다릅니다.");
                        }
                    }
                }
            });
        }
        else {
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void signUpActivity() {
        intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void findIdActivity() {
        intent = new Intent(this, FindIdActivity.class);
        startActivity(intent);
    }

    private void findPwActivity() {
        intent = new Intent(this, FindPwActivity.class);
        startActivity(intent);
    }

    private void startMainActivity() {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void auto_login(boolean check) {
        email = emailETXT.getText().toString();
        password = passwordETXT.getText().toString();

        if(check) {
            auto_editor.putString("id", email);
            auto_editor.putString("pw", password);
        }
        else {
            auto_editor.clear();
        }
        auto_editor.apply();
    }

    public void initRole(String uid) {
        Log.i(TAG, "initRole: " + uid);
        myRef.child("Member").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, "onDataChange : " + snapshot.child("role").getValue().toString());
                if (snapshot.child("role").getValue().toString().equals("학생")) {
                    GetRole.FLAG = 1;
                } else {
                    GetRole.FLAG = 2;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        try {
            Thread.sleep(300);

        } catch (InterruptedException e) {

        }
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
}
