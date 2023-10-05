package com.example.todoapp_android_java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Realmデータベースから取得したTodoDataオブジェクトを表示用のビューにバインドするクラス
 */
public class TodoDataAdapter extends RealmBaseAdapter<TodoData> {
    public TodoDataAdapter(@Nullable OrderedRealmCollection<TodoData> data) {
        super(data);
    }

    /**
     * ビューを再利用してTodoDataオブジェクトを表示用のビューにバインドします。
     *
     * @param position    ビューの位置
     * @param convertView ビューが再利用可能な場合は再利用されたビュー、それ以外の場合はnull
     * @param parent      親ビュー
     * @return バインドされたビュー
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            /* 新しいViewを作成する場合*/
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_cell, parent, false);
        }

        /*TodoDataを取得*/
        TodoData todoData = adapterData.get(position);

        TextView dateTextView = convertView.findViewById(R.id.date);
        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView detailTextView = convertView.findViewById(R.id.detail);

        /*仮想データ（実際にはデータベースから取得したデータを使用）*/
        dateTextView.setText(todoData.date.toString());
        titleTextView.setText(todoData.title);
        detailTextView.setText(todoData.description);

        return convertView;
    }
}
