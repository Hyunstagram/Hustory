package com.example.hustory.userInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// 파이어베이스에 로그인한 유저의 로그인 정보를 저장
public class UserInfo {
    public static FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static FirebaseUser CUR_USER = AUTH.getCurrentUser();
    public static String UID = CUR_USER.getUid();
    public static String CUR_USER_NAME = "";

    public UserInfo() {
    }

}
