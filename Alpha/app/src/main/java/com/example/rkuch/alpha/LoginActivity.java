package com.example.rkuch.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText nameField;
    private EditText passWordField;
    private CardView loginField;
    private TextView registerField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameField = findViewById(R.id.name);
        passWordField = findViewById(R.id.password);
        loginField = findViewById(R.id.login);
        registerField = findViewById(R.id.register);

        loginField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginToPairingIntent = new Intent(LoginActivity.this, BluetoothPairingActivity.class);
                loginToPairingIntent.putExtra("login", nameField.getText().toString());
                startActivity(loginToPairingIntent);
            }
        });

        registerField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
