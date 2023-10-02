package com.example.todoapp_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class RealmActivity extends AppCompatActivity {

    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);

        configureButton();
    }

    private void configureButton() {
        backButton = findViewById(R.id.backButton);
    }

    public void didTapBackButton() {
        finish();
    }
}