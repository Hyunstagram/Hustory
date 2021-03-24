package com.example.hustory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.hustory.firebasedatebase.FirebaseUtility;
import com.example.hustory.managementcard.FragmentMy;
import com.example.hustory.question.AnswerActivity;
import com.example.hustory.question.FragmentQuestion;
import com.example.hustory.reservation.FragmentReservation;
import com.example.hustory.reservation.GetRole;
import com.example.hustory.util.DataStringFormat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class FragmentMain extends Fragment {


    EditText edit_question;
    ImageView go_question;

    View view;
    ViewPager viewPager;
    TextView text_summary;
    SwipeAdapter adapter;

    ImageView image_professor;
    TextView main_professor;
    TextView title_professor;
    TextView main_summary;
    TextView go_reservation;

    LinearLayout layout_reservation;

    private Context mContext;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    private String q_Num;
    private String q_count;
    private String writer_id;
    private String q_writer;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private  String professor, student_uid, professor_uid;
    private String student;
    private  String summary;
    private  String before_after_data;
    private  String reservedate;
    private String reserve_month;
    private String reserve_day;
    Date date = new Date(2022,12,31,00,00);
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    String testDate = format.format(date);
    private  long check_reservedate;

    private TextView see_more;

    private FragmentQuestion fragmentQuestion = new FragmentQuestion();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (GetRole.FLAG  == 1) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            init_student();
        } else if (GetRole.FLAG  == 2) {
            view = inflater.inflate(R.layout.fragment_main_professor, container, false);
            init_professor();
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init_student() {
        initQuestionSection();

        see_more = view.findViewById(R.id.see_more);

        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceReservation();
            }
        });

        check_reservedate = Long.parseLong(testDate);

        text_summary = (TextView) view.findViewById(R.id.main_summary);
        viewPager = (ViewPager) view.findViewById(R.id.image_banner);

        image_professor = (ImageView) view.findViewById(R.id.image_professor);
        main_professor = (TextView) view.findViewById(R.id.main_professor);
        title_professor = (TextView) view.findViewById(R.id.title_professor);
        main_summary = (TextView) view.findViewById(R.id.main_summary);
        go_reservation = (TextView) view.findViewById(R.id.go_reservation);


        FirebaseDatabase.getInstance().getReference().child("Member").child(uid).child("R_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> member = (HashMap<String, Object>) snapshot.getValue();
                    professor = member.get("professor").toString();
                    summary = member.get("summary").toString();
                    before_after_data = member.get("before_after_data").toString();
                    reservedate = member.get("reservedate").toString();
                    reserve_month = member.get("reserve_month").toString();
                    reserve_day = member.get("reserve_day").toString();
                    professor_uid = member.get("prof_uid").toString();
                    long currentTime = new Date().getTime();

                    if(before_after_data.equals("false") && Long.valueOf(reservedate).compareTo(check_reservedate) < 0){
                        Log.i("test", "크다"+reservedate);
                        main_professor.setText(professor);
                        main_summary.setText(summary);
                        go_reservation.setText(CreateDataWithCheck(reserve_month, reserve_day));
                        check_reservedate = Long.valueOf(reservedate);
                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
                        StorageReference storageRef = storage.getReference();
                        storageRef.child("images/" + professor_uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //이미지 로드 성공시
                                Glide.with(view.getContext())
                                        .load(uri)
                                        .into(image_professor);
                            }});

                    }else {
                        Log.i("test", "작다"+Long.valueOf(reservedate).compareTo(check_reservedate));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("remove", "없어");
            }
        });

        layout_reservation = (LinearLayout) view.findViewById(R.id.reservation);

        adapter = new SwipeAdapter(getContext());
        viewPager.setAdapter(adapter);

        int dpValue = 35;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        viewPager.getLayoutParams().height = height/3;
        viewPager.requestLayout();

        String str = "\" " + String.valueOf(text_summary.getText()) + " \"";
        text_summary.setText(str);

        edit_question = (EditText) view.findViewById(R.id.edit_question);
        go_question = (ImageView) view.findViewById(R.id.go_question);

        go_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                FragmentQuestion fragment = new FragmentQuestion();
                Bundle bundle = new Bundle();
                bundle.putString("KEY_SEARCH", String.valueOf(edit_question.getText()));
                fragment.setArguments(bundle);

                transaction.replace(R.id.frameLayout, fragment);
                transaction.commit();
            }
        });

        mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) view.findViewById(R.id.fab_sub2);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_close);
                    fab_sub2.startAnimation(fab_close);
                    fab_sub1.setClickable(false);
                    fab_sub2.setClickable(false);
                    isFabOpen = false;
                } else {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_open);
                    fab_sub2.startAnimation(fab_open);
                    fab_sub1.setClickable(true);
                    fab_sub2.setClickable(true);
                    isFabOpen = true;
                }
            }
        });

        if (GetRole.CONTENT_FLAG == 0) {
            image_professor.setVisibility(View.GONE);
            main_professor.setVisibility(View.GONE);
            title_professor.setVisibility(View.GONE);
            main_summary.setVisibility(View.GONE);
            go_reservation.setVisibility(View.GONE);

            TextView text_no = new TextView(getContext());
            text_no.setText("진행중인 예약 내역이 없습니다.");
            text_no.setTextColor(Color.parseColor("#FFFFFF"));
            text_no.setTextSize(14);
            text_no.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_no);

            TextView text_go = new TextView(getContext());
            text_go.setText("새로운 예약을 진행하세요");
            text_go.setTextSize(12);
            text_go.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_go);

            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) text_go.getLayoutParams();
            layoutParams2.bottomMargin = 100;
            text_go.setLayoutParams(layoutParams2);

            Typeface typeface = getResources().getFont(R.font.jalnan);
            text_no.setTypeface(typeface);
            text_go.setTypeface(typeface);
        }
    }

    public static String CreateDataWithCheck(String reserve_month, String reserve_day) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String msg = "D-" + (Integer.parseInt(reserve_day) - day);
        Log.i("msg", msg);


        return msg;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init_professor() {
        initQuestionSection();

        see_more = view.findViewById(R.id.see_more);

        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceReservation();
            }
        });


        check_reservedate = Long.parseLong(testDate);
        text_summary = (TextView) view.findViewById(R.id.main_summary);
        viewPager = (ViewPager) view.findViewById(R.id.image_banner);

        image_professor = (ImageView) view.findViewById(R.id.image_professor);
        main_professor = (TextView) view.findViewById(R.id.main_professor);
        title_professor = (TextView) view.findViewById(R.id.title_professor);
        main_summary = (TextView) view.findViewById(R.id.main_summary);
        go_reservation = (TextView) view.findViewById(R.id.go_reservation);


        FirebaseDatabase.getInstance().getReference().child("Member").child(uid).child("R_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> member = (HashMap<String, Object>) snapshot.getValue();
                    student_uid = member.get("uid").toString();
                    student = member.get("student").toString();
                    professor = member.get("professor").toString();
                    summary = member.get("summary").toString();
                    before_after_data = member.get("before_after_data").toString();
                    reservedate = member.get("reservedate").toString();
                    reserve_month = member.get("reserve_month").toString();
                    reserve_day = member.get("reserve_day").toString();
                    long currentTime = new Date().getTime();
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
                    StorageReference storageRef = storage.getReference();
                    storageRef.child("images/" + student_uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //이미지 로드 성공시
                            Glide.with(view.getContext())
                                    .load(uri)
                                    .into(image_professor);
                        }});

                    if(before_after_data.equals("false") && Long.valueOf(reservedate).compareTo(check_reservedate) < 0){
                        Log.i("test", "크다"+reservedate);
                        main_professor.setText(student);
                        main_summary.setText(summary);
                        go_reservation.setText(CreateDataWithCheck(reserve_month, reserve_day));
                        check_reservedate = Long.valueOf(reservedate);

                    }else {
                        Log.i("test", "작다"+Long.valueOf(reservedate).compareTo(check_reservedate));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("remove", "없어");
            }
        });

        layout_reservation = (LinearLayout) view.findViewById(R.id.reservation);

        adapter = new SwipeAdapter(getContext());
        viewPager.setAdapter(adapter);

        int dpValue = 35;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        viewPager.getLayoutParams().height = height/3;
        viewPager.requestLayout();

        String str = "\" " + String.valueOf(text_summary.getText()) + " \"";
        text_summary.setText(str);

        edit_question = (EditText) view.findViewById(R.id.edit_question);
        go_question = (ImageView) view.findViewById(R.id.go_question);

        go_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                FragmentQuestion fragment = new FragmentQuestion();
                Bundle bundle = new Bundle();
                bundle.putString("KEY_SEARCH", String.valueOf(edit_question.getText()));
                fragment.setArguments(bundle);

                transaction.replace(R.id.frameLayout, fragment);
                transaction.commit();
            }
        });

        mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_close);
                    fab_sub1.setClickable(false);
                    isFabOpen = false;
                } else {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_open);
                    fab_sub1.setClickable(true);
                    isFabOpen = true;
                }
            }
        });

        if (GetRole.CONTENT_FLAG == 0) {
            image_professor.setVisibility(View.GONE);
            main_professor.setVisibility(View.GONE);
            title_professor.setVisibility(View.GONE);
            main_summary.setVisibility(View.GONE);
            go_reservation.setVisibility(View.GONE);

            TextView text_no = new TextView(getContext());
            text_no.setText("진행중인 예약 내역이 없습니다.");
            text_no.setTextColor(Color.parseColor("#FFFFFF"));
            text_no.setTextSize(14);
            text_no.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_no);

            TextView text_go = new TextView(getContext());
            text_go.setText("새로운 예약을 기다리세요");
            text_go.setTextSize(10);
            text_go.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_go);

            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) text_go.getLayoutParams();
            layoutParams2.bottomMargin = 100;
            text_go.setLayoutParams(layoutParams2);

            Typeface typeface = getResources().getFont(R.font.jalnan);
            text_no.setTypeface(typeface);
            text_go.setTypeface(typeface);
        }
    }

    // 메인화면에 가장 최근의 질문글을 보여줌
    public void initQuestionSection() {
        TextView question_name = view.findViewById(R.id.question_name);
        TextView question_time = view.findViewById(R.id.question_time);
        TextView question_content = view.findViewById(R.id.question_content);
        TextView q_countTXT = view.findViewById(R.id.q_countTXT);

        FirebaseUtility.myRef.child("Question").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        HashMap<String, Object> member = (HashMap<String, Object>) dataSnapshot.getValue();

                        Log.i("q_num", dataSnapshot.getKey());
                        q_Num = dataSnapshot.getKey().toString();

                        q_writer = member.get("q_writer").toString();
                        String q_content = member.get("q_title").toString();
                        q_count = member.get("q_count").toString();
                        String q_diffTime = member.get("q_diffTime").toString();
                        writer_id = member.get("id").toString();

                        q_diffTime = DataStringFormat.CreateDataWithCheck(q_diffTime);

                        question_name.setText(q_writer);
                        question_time.setText(q_diffTime);
                        question_content.setText(q_content);
                        q_countTXT.setText(q_count);
                    }
                }
                else {
                    question_name.setText("");
                    question_time.setText("");
                    question_content.setText("등록된 질문이 없습니다.");
                    q_countTXT.setText("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TextView view_all = view.findViewById(R.id.view_all);

        // 전체보기 누르면 질문 프래그먼트로 전환
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceQuestion();
            }
        });


        TextView new_answer = view.findViewById(R.id.new_answer);

        // 답변하기 버튼을 누르면 답변 창으로 이동
        new_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnswerActivity.class);

                // 아이템을 클릭하면 조회수 1 증가
                int count = Integer.parseInt(q_count) + 1;
                FirebaseUtility.myRef.child("Member").child(writer_id).child("Q_List").child(q_Num).child("q_count").setValue(count);
                FirebaseUtility.myRef.child("Question").child(q_Num).child("q_count").setValue(count);

                // 현재 클릭된 아이템의 질문번호(q_Num)과 작성자 이름, id를 넘겨줌
                intent.putExtra("q_Num", q_Num);
                intent.putExtra("name", q_writer);
                intent.putExtra("writerId", writer_id);

                startActivity(intent);
            }
        });
    }
}