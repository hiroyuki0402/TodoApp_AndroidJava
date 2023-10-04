package com.example.todoapp_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class RealmActivity extends AppCompatActivity {

    // DB

    /*レルム*/
    private Realm realm;

    // ボタン

    /*作成*/
    private Button createButton;

    /*更新*/
    private Button updateButton;

    /*取得*/
    private Button getButton;

    /*削除*/
    private Button deleteButton;

    private Button backButton;


    // テキストView

    /*詳細*/
    private TextView descriptionText;


    /*一番最初に呼ばれる処理*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);

        /*セットアップ*/
        configureRealmActivity();
    }


    // メソッド

    /*Activityの構築*/
    private void configureRealmActivity() {

        /*ボタンの構築*/
        configureButton();

        /*レルムの構築*/
        configureRealm();

        /*TextViewの構築*/
        configureTextView();
    }

    /*ボタンの構築*/
    private void configureButton() {
        createButton = findViewById(R.id.createButton);
        updateButton = findViewById(R.id.upDateButton);
        getButton = findViewById(R.id.getButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
    }

    /*レルムの構築*/
    private void configureRealm() {
        realm = Realm.getDefaultInstance();
    }

    /*テキストビューの構築*/
    private void configureTextView() {
        descriptionText = findViewById(R.id.textView);
    }

    /*登録*/
    private void create() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                /*バックグラウンドスレッドで新しいRealmインスタンスを取得
                 * 理由 - Realmのインスタンスは、それが作成されたスレッドでのみ使用することができる。
                 * UIスレッドでのデータベース操作は避けるべき。
                 */
                Realm bgRealm = Realm.getDefaultInstance();

                /*トランザクションを開始してデータベースの変更を行う*/
                bgRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    /*
                     * トランザクション内でのデータベース操作を定義
                     * @param realm このトランザクション内で使用するRealmインスタンス
                     */
                    public void execute(Realm realm) {
                        /*値の設定*/
                        TodoData data = realm.createObject(TodoData.class, 1);
                        data.date = new Date();
                        data.title = "テスト";
                        data.description = "詳細";
                    }
                });

                /*トランザクション終了後、Realmインスタンスを閉じる
                 * 理由 - Realmインスタンスはリソースを消費するため、不要になったらクローズすることでリソースを解放することが推奨されている。
                 */
                bgRealm.close();

                /*UIレッドでUI更新
                 * 理由 - AndroidではUI関連の変更はメインスレッドで行う必要があり、runOnUiThreadを使って、
                 * バックグラウンドスレッドからUIスレッドで操作を行う。
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        descriptionText.setText("data: テスト");
                    }
                });
            }
            /*バックグラウンドスレッドを開始する*/
        }).start();
    }


    /*取得*/
    private void load() {
        /*トランザクションを渡す*/
        realm.executeTransaction(new Realm.Transaction() {
            /*ロード処理*/
            @Override
            public void execute(Realm realm) {

                /*検索*/
                RealmResults<TodoData> datas = realm.where(TodoData.class).findAll();

                descriptionText.setText("配列");

                for (TodoData data : datas
                ) {
                    String txt = data.toString();
                    descriptionText.setText(txt);

                }
            }
        });
    }

    /* アップデート*/
    private void update() {
        /*トランザクションを渡す*/
        realm.executeTransaction(new Realm.Transaction() {
            /*アップデート処理*/
            @Override
            public void execute(Realm realm) {

                /*検索*/
                TodoData data = realm.where(TodoData.class).equalTo("id", 0).findFirst();

                descriptionText.setText("配列");
                data.title += "テスト";
                data.description += "テスト";


            }
        });
    }

    /*削除*/
    private void delete() {
        /*トランザクションを渡す*/
        realm.executeTransaction(new Realm.Transaction() {
            /*削除処理*/
            @Override
            public void execute(Realm realm) {
                TodoData data = realm.where(TodoData.class).equalTo("id", 0).findFirst();

                descriptionText.setText("削除");

                data.deleteFromRealm();
            }
        });
    }


    // アクション

    /*戻る*/
    public void didTapBackButton() {
        finish();
    }

    /*作成*/
    public void didTpaCreateButton(View view) {
        create();
    }

    /*アップデート*/
    public void didTpaUpdateButton(View view) {
        update();
    }

    /*ロード(取得)*/
    public void didTpaGetButton(View view) {
        load();
    }

    /*削除*/
    public void didTpaDeleteButton(View view) {
        delete();
    }

    // 画面破棄

    /*アクティヴィティが破棄される時に呼ばれる*/
    @Override
    protected void onDestroy() {
        super.onDestroy();

        /// レルむを閉じる
        realm.close();
    }
}
