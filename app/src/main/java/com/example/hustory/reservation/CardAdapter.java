package com.example.hustory.reservation;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.hustory.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CardAdapter extends BaseAdapter {
    private ArrayList<CardItem> listViewItemList = new ArrayList<CardItem>();
    public CardAdapter() {}

    @Override
    public int getCount() { return listViewItemList.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.card_item, parent, false);
        }

        ImageView icon_student = (ImageView) convertView.findViewById(R.id.icon_student);
        TextView name_student = (TextView) convertView.findViewById(R.id.name_student1);
        TextView version_student = (TextView) convertView.findViewById(R.id.version_student);

        CardItem listViewItem = listViewItemList.get(position);

        name_student.setText(listViewItem.getNameStr());
        version_student.setText(listViewItem.getVersionStr());

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/" + listViewItem.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(context)
                        .load(uri)
                        .into(icon_student);

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

    public void addItem(String id, String nameStr, String versionStr) {
        CardItem item = new CardItem( id,  nameStr,  versionStr);

        item.setId(id);
        item.setNameStr(nameStr);
        item.setVersionStr(versionStr);

        listViewItemList.add(item);
    }
    public void clear() {
        listViewItemList.clear();
    }

    public void deleteItem(int position) { listViewItemList.remove(position); }
}