package com.example.android.task3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.task3.sharedpref.SessionManagement;

/**
 * Created by Luteh on 2/6/2017.
 */

public class HomeActivity extends AppCompatActivity {
    TextView tv_logout;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        session = new SessionManagement(getApplicationContext());
        Toast.makeText(this, "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        session.checkLogin();
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                finish();
            }
        });
    }
}
