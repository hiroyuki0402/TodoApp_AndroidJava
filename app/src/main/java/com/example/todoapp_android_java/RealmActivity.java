package com.example.todoapp_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Realmデータベースの操作やUIの制御を行うクラス
 */
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


    // property

    private long id;


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
        /* バックグラウンドスレッドを新しく作成してトランザクションを実行 */
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
                        /*現在DBに登録されている最大IDの数字*/
                        Number idMaxNumber = realm.where(TodoData.class)
                                .max("id");
                        /*ID初期化 (初期位置を最大値加算する数値)*/
                        id = 1;

                        /*idMaxNumberのnilチェック*/
                        if (idMaxNumber != null) {
                            id += idMaxNumber.longValue();
                        } else {
                            id -= 1;
                        }

                        /*値の設定*/
                        TodoData data = realm.createObject(TodoData.class, id);
                        data.date = new Date();
                        data.title = "テスト" + idMaxNumber;
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
                        String result = "テスト" + id;
                        descriptionText.setText(result);
                    }
                });
            }
            /*バックグラウンドスレッドを開始する*/
        }).start();
    }

    /*ロード*/
    private void load() {
        /*RealmデータベースからTodoDataの全データを検索する*/
        RealmResults<TodoData> datas = realm.where(TodoData.class).findAll();

        /*UI上に「配列」というテキストを一時的に表示する*/
        descriptionText.setText("配列");

        /* 取得した全てのTodoデータに対してループを実行*/
        for (TodoData data : datas) {
            String txt = descriptionText.getText() + data.toString();
            descriptionText.setText(txt);
        }
    }

    /*更新*/
    private void update() {
        /* バックグラウンドスレッドを新しく作成してトランザクションを実行 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                /* バックグラウンドスレッドで新しいRealmインスタンスを取得 */
                Realm bgRealm = Realm.getDefaultInstance();

                /* Realmのトランザクションをバックグラウンドで開始 */
                bgRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        /* idが0のTodoDataオブジェクトを検索 */
                        TodoData data = realm.where(TodoData.class).equalTo("id", 0).findFirst();
                        if (data != null) {
                            /* データの更新 */
                            data.title += "テスト";
                            data.description += "テスト";
                        }
                    }
                });

                /* トランザクション終了後、Realmインスタンスを閉じる */
                bgRealm.close();

                /* UIスレッドでテキストビューを更新 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        descriptionText.setText("アップデート完了");
                    }
                });
            }
        }).start(); /* バックグラウンドスレッドを開始 */
    }


    /*削除*/
    private void delete() {
        /* バックグラウンドスレッドでの操作を新しく作成 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                /* バックグラウンドスレッドで新しいRealmインスタンスを取得 */
                Realm bgRealm = Realm.getDefaultInstance();

                /* Realmのトランザクションをバックグラウンドで開始 */
                bgRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number min = realm.where(TodoData.class)
                                .min("id");
                        /*nullCheck*/
                        if (min == null) { return; }

                        /* idが0のTodoDataオブジェクトを検索 */
                        TodoData data = realm.where(TodoData.class)
                                .equalTo("id", min.longValue())
                                .findFirst();

                        if (data != null) {
                            /* TodoDataオブジェクトを削除 */
                            data.deleteFromRealm();
                        }
                    }
                });

                /* トランザクション終了後、Realmインスタンスを閉じる */
                bgRealm.close();

                /* UIスレッドでテキストビューを更新 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        descriptionText.setText("削除完了");
                    }
                });
            }
        }).start(); /* バックグラウンドスレッドを開始 */
    }


    // アクション

    /*戻る*/
    public void didTapBackButton(View view) {
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
