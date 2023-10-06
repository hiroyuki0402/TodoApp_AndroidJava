package com.example.todoapp_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.Realm;

public class InputAvtivity extends AppCompatActivity {

    private Realm realm;
    private long id;
    private TextView dateLabel;
    private EditText titleField;
    private EditText descriptionField;
    private TodoData todoData;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        setup();

    }


    private void setup() {
        configureRealm();

        configureTodoData();


        configureTextField();

        configureButton();


        configureTextView();

    }

    private void configureRealm() {
        realm = Realm.getDefaultInstance();
    }

    private void configureButton() {
        doneButton = findViewById(R.id.doneButtone);
    }

    private void configureTextField() {
        dateLabel = findViewById(R.id.dateLabel);
        titleField = findViewById(R.id.titleField);
        descriptionField = findViewById(R.id.descriptionField);

        dateLabel.setText(dateFormat(todoData.date, "YYYY/MM/DD"));
        titleField.setText(todoData.title);
        descriptionField.setText(todoData.description);
    }

    private void configureTextView() {
        dateLabel = findViewById(R.id.dateLabel);
    }

    private void configureTodoData() {
        if (getIntent() == null) {
            return;
        }

        id = getIntent().getLongExtra("id", -1);

        todoData =  realm.where(TodoData
                        .class).equalTo("id", this.id)
                .findFirst();
    }

    private String dateFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public void didTapDoneButton(View view) {
        finish();
    }


    @Override
    protected void onDestroy() {
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
        super.onDestroy();
    }
}