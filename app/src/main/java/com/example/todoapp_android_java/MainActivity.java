package com.example.todoapp_android_java;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * アプリのメイン画面を表すアクティビティです。
 */
public class MainActivity extends AppCompatActivity {

    /** RealmActivityへの移動ボタン */
    private Button realmButton;

    /** Realmデータベースのインスタンス */
    private Realm realm;

    /** ToDoリストを表示するリストビュー */
    private ListView listView;

    /**
     * アクティビティが生成されたときに呼ばれるメソッドです。
     * @param savedInstanceState 以前の状態が保存されたバンドル
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// レイアウトの初期設定を行うメソッドを呼び出す
        // リストビューの初期設定
        configureListView();

        // ボタンの初期設定
        configureButton();

        // Realmデータベースの初期設定
        configureRealm();
    }

    /**
     * 移動ボタンの初期設定を行うメソッドです。
     */
    private void configureButton() {
        realmButton = findViewById(R.id.realmButton);
    }

    /**
     * リストビューの初期設定を行うメソッドです。
     */
    private void configureListView() {
        listView = findViewById(R.id.listView);
    }

    /**
     * Realmデータベースの初期設定を行い、ToDoリストを表示します。
     */
    private void configureRealm() {
        realm = Realm.getDefaultInstance();

        // ToDoデータを取得してリストビューに表示
        RealmResults<TodoData> todoDatas = realm.where(TodoData.class).findAll();
        TodoDataAdapter adapter = new TodoDataAdapter(todoDatas);
        listView.setAdapter(adapter);
    }

    /**
     * 指定されたアクティビティに画面遷移するメソッドです。
     * @param targetActivity 遷移先のアクティビティクラス
     */
    public void moveToView(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    /**
     * RealmActivityに遷移するためのメソッドです。
     * @param view クリックされたビュー
     */
    public void didTpaRealmButton(View view) {
        moveToView(RealmActivity.class);
    }
}
