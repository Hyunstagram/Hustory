package com.example.hustory.question;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hustory.R;
import com.example.hustory.util.DataStringFormat;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {
    private static final String TAG = "QuestionAdapter";
    private ArrayList<QuestionItem> listViewItemList = new ArrayList<QuestionItem>();
    public QuestionAdapter() {
        listViewItemList.clear();
    }

    @Override
    public int getCount() { return listViewItemList.size(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.question_item, parent, false);
        }

        TextView question_content = (TextView) convertView.findViewById(R.id.question_content);
        TextView question_name = (TextView) convertView.findViewById(R.id.question_name);
        TextView question_time = (TextView) convertView.findViewById(R.id.question_time);
        TextView q_countTXT = (TextView) convertView.findViewById(R.id.q_countTXT);

        QuestionItem listViewItem = listViewItemList.get(position);

        question_name.setText(listViewItem.getQ_writer());
        question_content.setText(listViewItem.getQ_title());
        question_time.setText(listViewItem.getQ_diffTime());
        q_countTXT.setText(String.valueOf(listViewItem.getQ_count()));

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

    public void addItemDESC(int idx, String id, String q_Num, String q_title, String q_content, String q_date, String q_time, String q_count, String q_diffTime, String q_writer) {
        QuestionItem item = new QuestionItem(id, q_Num, q_title, q_content, q_date, q_time, q_count, q_diffTime, q_writer);

        item.setQ_title(q_title);
        item.setQ_content(q_content);
        item.setQ_writer(q_writer);

        // 작성 시간과 현재 시간의 차이를 계산하여 값을 넘겨줌
        q_diffTime = DataStringFormat.CreateDataWithCheck(q_diffTime);

        item.setQ_diffTime(q_diffTime);
        item.setQ_count(q_count);

        listViewItemList.add(idx, item);
    }

    public void deleteItem(int position) { listViewItemList.remove(position); }

    public void clear() {
        Log.i(TAG, "clear 전 list 크기 : " + listViewItemList.size());
        listViewItemList.clear();
        Log.i(TAG, "clear 후 list 크기 : " + listViewItemList.size());
    }
}