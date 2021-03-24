package com.example.hustory.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hustory.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ReservationActivity extends AppCompatActivity {
    // 요일 변수
    private String[] dayArray;
    private boolean before_after_data;
    private long id;
    private String reservedate;
    private int reserve_hour;
    private int reserve_time;
    private int reserve_year;
    private int reserve_month;
    private int reserve_day;

    // 예약수를 세는 변수
    private int i;

    // view 변수
    private TextView text_professor;
    private EditText text_summary;
    private TextView select_date;
    private TextView select_time;
    private EditText select_place;
    private EditText select_content;
    private Spinner spinner;
    private String key;


    // firebase
    private FirebaseData firebaseData;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private String profUid;
    private String professor;
    private String student;
    private String version_student;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reservation);
        init();
    }


    public void init() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //요일 변수 선언
        dayArray = getResources().getStringArray(R.array.day_of_week);

        //view 변수 선언
        text_professor = findViewById(R.id.text_professor);
        text_summary = findViewById(R.id.text_summary);
        select_date = findViewById(R.id.select_date);
        select_time = findViewById(R.id.select_time);
        select_place = findViewById(R.id.select_place);
        select_content = findViewById(R.id.select_content);
        spinner = (Spinner)findViewById(R.id.onOffSpiner);

        // 이름 설정 함수
        myRef.child("Member").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profUid = snapshot.child(uid).child("prof").getValue().toString();
                professor = snapshot.child(profUid).child("name").getValue().toString();
                student = snapshot.child(uid).child("name").getValue().toString();
                version_student = snapshot.child(uid).child("management").child("myinfo").child("version_student").getValue().toString();
                Log.i("version", version_student);
                text_professor.setText(professor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        // 스피너 데이터 선언
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.on_off_line, R.layout.spinner_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // spinner.getSelectedItem().toString() : 스피너 값 가져오는 함수
        // 교수 정보 가져오기
//        FirebaseDatabase.getInstance().getReference().child("Member").child("v5N3FZkAIxTS2IpVV4ki2yUbRwJ2").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.child("role").getValue());
//                    profUid = dataSnapshot.child("prof").getValue().toString();
//                    Log.i("profUid", profUid);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("remove", "없어");
//            }
//        });
    }

    // 날짜 정보 고르는 함수.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePicker(View view) {
        // Calendar class 변수 선언 년, 월, 일, 요일 후 Calender class에 날짜 set
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        c.set(year, month, day);
        // 지나간 날짜를 선택하지 못하게 하기위해 오늘 날짜를 받아옴.
        long minDate = c.getTime().getTime();

        // DatePickerDialog 선언
        DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int DOF = (dayOfMonth - day) % 7;
                reserve_year = year;
                reserve_month = month;
                reserve_day = dayOfMonth;
                select_date.setText((month+1)+"."+dayOfMonth+"("+dayArray[dayOfWeek+DOF]+")");
                view.setMinDate(minDate);
            }
        },year,month,day);

        // 지나간 날짜 선택 못하게 min date 설정
        dateDialog.getDatePicker().setMinDate(minDate);
        // dialog 를 띄움.
        dateDialog.show();
    }


    // 시간 정보 고르는 함수.
    public void showTimePicker(View v){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                select_time.setText(hourOfDay + ":" + minute);
//                showToast(hourOfDay + ":" + minute);
                reserve_time = minute;
                reserve_hour = hourOfDay;
            }
        }, hour,minute, false);
        timeDialog.show();
    }

    // 예약버튼 클릭 이벤트
    public void onReserve(View v){

        // 날짜 데이터 만들기


//         예약하기 버튼 클릭시 예약 해서 firebase 에 데이터 전송
        if(!select_date.getText().equals("날짜 설정") && !select_time.getText().equals("시간 설정")  && select_place.getText().length() != 0 && select_content.getText().length() !=0 && text_summary.getText().length() != 0) {
//             로그인한 유저의 이름을 저장
            Date date = new Date();
            Calendar reserve = Calendar.getInstance();
            reserve.set(reserve_year,reserve_month,reserve_day,reserve_hour,reserve_time);
            reservedate = String.valueOf(reserve.getTimeInMillis());
            Log.i("reserve",reservedate);

            id = System.currentTimeMillis();
            key =  "" + id;
            // 학생 데이터 저장
            firebaseData = new FirebaseData(uid, professor, text_summary.getText().toString(), select_date.getText().toString(), select_time.getText().toString(), spinner.getSelectedItem().toString(), select_place.getText().toString(), "수락대기", select_content.getText().toString(), false, key, student, reservedate, Integer.toString(reserve_day), Integer.toString(reserve_month), profUid);
            Map<String, Object> postValue = firebaseData.toMap();
            myRef.child("Member").child(uid).child("R_List").child(key).setValue(postValue);

            // 교수 데이터 저장
            myRef.child("Member").child(profUid).child("R_List").child(key).setValue(postValue);
            myRef.child("Member").child(profUid).child("student").child(uid).child("R_List").child(key).setValue(postValue);
            myRef.child("Member").child(profUid).child("student").child(uid).child("name").setValue(student);
            myRef.child("Member").child(profUid).child("student").child(uid).child("version_student").setValue(version_student);
            myRef.child("Member").child(profUid).child("student").child(uid).child("uid").setValue(uid);

            // e-mail 보내기
            SendMail mailServer = new SendMail();

            // 강사의 이메일을 찾아서 보냄
            myRef.child("Member").child(profUid).child("email").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Log.i(TAG, snapshot.getValue().toString());
                    String prof_email = snapshot.getValue().toString();
                    mailServer.sendSecurityCode(getApplicationContext(), prof_email);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            finish();
        }

        // 미입력 항목 있을 시 토스트
//        if (dateTXT.getText().equals(date)){
//            showToast("날짜를 설정해 주세요");
//        }else if(timeTXT.getText().equals(time)){
//            showToast("시간을 설정해 주세요");
//        }else if(titleETXT.getText().length() == 0){
//            showToast("제목을 입력해 주세요");
//        }else if(contentsETXT.getText().length() == 0){
//            showToast("내용을 입력해 주세요");
//        }


    }
}