package com.example.todoapp_android_java;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * アプリケーション全体の初期化を行うクラス
 * Realmデータベースの初期化を主な目的
 */
public class TodoApplication extends Application {

    /**
     * アプリケーションが起動した際に呼び出されるメソッド
     * Realmデータベースの初期化を行う
     */
    @Override
    public void onCreate() {
        super.onCreate();
        /// Realmデータベースの初期化
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
