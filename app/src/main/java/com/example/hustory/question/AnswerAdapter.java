package com.example.hustory.question;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.hustory.R;
import com.example.hustory.userInfo.UserInfo;
import com.example.hustory.util.DataStringFormat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AnswerAdapter extends BaseAdapter {
    private ArrayList<AnswerItem> listViewItemList = new ArrayList<>();
    public AnswerAdapter() {}

    @Override
    public int getCount() { return listViewItemList.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.answer_item, parent, false);
        }

        TextView answer_content = (TextView) convertView.findViewById(R.id.answer_content);
        TextView answer_name = (TextView) convertView.findViewById(R.id.answer_name);
        TextView answer_time = (TextView) convertView.findViewById(R.id.answer_time);
        ImageView answer_image = convertView.findViewById(R.id.answer_image);

        AnswerItem listViewItem = listViewItemList.get(position);

        answer_content.setText(listViewItem.getA_content());
        answer_name.setText(listViewItem.getA_writer());
        answer_time.setText(listViewItem.getA_diffTime());

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/" + listViewItem.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(context)
                        .load(uri)
                        .into(answer_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                //Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(String a_Num, String a_content, String a_date, String a_time, String a_writer, String id, String a_diffTime) {
        AnswerItem item = new AnswerItem(a_Num, a_content, a_date, a_time, a_writer, id, a_diffTime);

        item.setA_Num(a_Num);
        item.setA_content(a_content);

        a_diffTime = DataStringFormat.CreateDataWithCheck(a_diffTime);

        item.setA_diffTime(a_diffTime);
        item.setA_writer(a_writer);
        item.setId(id);

        listViewItemList.add(item);
    }

    public void clear() {
        listViewItemList.clear();
    }

    public void deleteItem(int position) { listViewItemList.remove(position); }
}