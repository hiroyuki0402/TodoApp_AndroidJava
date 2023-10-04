package com.example.todoapp_android_java;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TodoData extends RealmObject {
    /// ID
    @PrimaryKey
    public long id;

    /// 日付
    public Date date;

    /// タイトル
    public String title;

    /// 詳細
    public String description;
}

