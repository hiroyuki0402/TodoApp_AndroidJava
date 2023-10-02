package com.example.todoapp_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button createButton;
    private Button updateButton;
    private Button getButton;
    private Button deleteButton;
    private Button realmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureButton();
    }

    private void configureButton() {
        createButton = findViewById(R.id.createButton);
        updateButton = findViewById(R.id.upDateButton);
        getButton = findViewById(R.id.getButton);
        deleteButton = findViewById(R.id.deleteButton);
        realmButton = findViewById(R.id.backButton);

    }

    private void moveToView(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
    }


    public void didTpaCreateButton(View view) {

    }

    public void didTpaUpdateButton(View view) {

    }

    public void didTpaGetButton(View view) {

    }

    public void didTpaDeleteButton(View view) {

    }

    public void didTpaRealmButton(View view) {
        moveToView(RealmActivity.class);
    }
}
