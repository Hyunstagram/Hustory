package com.example.hustory.reservation;

import android.content.Context;
import android.graphics.Color;
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

public class PreviousAdapter extends BaseAdapter {
    private ArrayList<PreviousItem> listViewItemList = new ArrayList<PreviousItem>();
    public PreviousAdapter() {}

    @Override
    public int getCount() { return listViewItemList.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.previous_item, parent, false);
        }

        ImageView icon_professor = (ImageView) convertView.findViewById(R.id.icon_professor);
        TextView text_professor = (TextView) convertView.findViewById(R.id.text_professor);
        TextView text_summary = (TextView) convertView.findViewById(R.id.text_summary);
        TextView reservation_date = (TextView) convertView.findViewById(R.id.reservation_date);
        TextView reservation_way = (TextView) convertView.findViewById(R.id.reservation_way);
        TextView reservation_place = (TextView) convertView.findViewById(R.id.reservation_place);
        TextView reservation_state = (TextView) convertView.findViewById(R.id.reservation_state);

        PreviousItem listViewItem = listViewItemList.get(position);

        text_professor.setText(listViewItem.getProfessorStr());
        text_summary.setText(listViewItem.getSummaryStr());
        reservation_date.setText(listViewItem.getDateStr());
        reservation_way.setText(listViewItem.getWayStr());
        reservation_place.setText(listViewItem.getPlaceStr());
        reservation_state.setText(listViewItem.getAllowStr());

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/" + listViewItem.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //????????? ?????? ?????????
                Glide.with(context)
                        .load(uri)
                        .into(icon_professor);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //????????? ?????? ?????????
                //Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
            }
        });

        if (String.valueOf(listViewItem.getAllowStr()).equals("?????? ??????")) {
            reservation_state.setTextColor(Color.parseColor("#2F5596"));
            reservation_state.setBackgroundResource(R.drawable.round_background);
        }


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

    public void addItem(String id, String ProfessorStr, String SummaryStr, String DateStr, String WayStr, String PlaceStr, String allowStr) {
        PreviousItem item = new PreviousItem( id,  ProfessorStr,  SummaryStr,  DateStr,  WayStr,  PlaceStr,  allowStr);

        item.setId(id);
        item.setProfessorStr(ProfessorStr);
        item.setSummaryStr(SummaryStr);
        item.setDateStr(DateStr);
        item.setWayStr(WayStr);
        item.setPlaceStr(PlaceStr);
        item.setAllowStr(allowStr);

        listViewItemList.add(item);
    }
    public void clear() {
        listViewItemList.clear();
    }

    public void deleteItem(int position) { listViewItemList.remove(position); }
}