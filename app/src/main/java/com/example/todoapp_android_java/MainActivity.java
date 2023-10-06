package com.example.todoapp_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * アプリのメイン画面を表すアクティビティです。
 */
public class MainActivity extends AppCompatActivity {

    /**
     * RealmActivityへの移動ボタン
     */
    private Button realmButton;

    FloatingActionButton add;

    /**
     * Realmデータベースのインスタンス
     */
    private Realm realm;

    /**
     * ToDoリストを表示するリストビュー
     */
    private ListView listView;

    /**
     * アクティビティが生成されたときに呼ばれるメソッドです。
     *
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
        add = findViewById(R.id.addButton);
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
     *
     * @param targetActivity 遷移先のアクティビティクラス
     */
    public void moveToView(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }

    private void addTodo() {
        final long[] newId = new long[1];

        // UIスレッド内でRealmのトランザクションを実行
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number max = realm.where(TodoData.class).max("id");
                newId[0] = (max == null) ?  1 :  max.longValue() + 1;

                TodoData data = realm.createObject(TodoData.class, newId[0]);
                data.date = new Date();
                data.title = "";
                data.description = "";

                intent(newId[0]);
            }
        });
    }


    private void intent(long targetId) {
        Intent intent = new Intent(MainActivity.this, InputAvtivity.class);
        intent.putExtra("id", targetId);
        startActivity(intent);
    }


    /**
     * RealmActivityに遷移するためのメソッドです。
     *
     * @param view クリックされたビュー
     */
    public void didTpaRealmButton(View view) {
        moveToView(RealmActivity.class);
    }


    public void didTapAddButton(View view) {
        addTodo();
    }
}
