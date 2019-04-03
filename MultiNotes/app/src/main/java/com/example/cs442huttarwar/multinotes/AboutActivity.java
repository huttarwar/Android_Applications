package com.example.cs442huttarwar.multinotes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView name, version, appname;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        appname = findViewById(R.id.appname);
        version = findViewById(R.id.version);
        name = findViewById(R.id.author);
    }
}
