package com.example.android.task3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.task3.models.User;
import com.example.android.task3.sharedpref.SessionManagement;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText et_email, et_pass;
    private Button btn_login;
    HttpURLConnection connection = null;
    BufferedReader reader = null;

    SessionManagement session;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_email = (EditText) findViewById(R.id.et_email);
        et_pass = (EditText) findViewById(R.id.et_pass);

        session = new SessionManagement(getApplicationContext());

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Get data User");
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                new ApiConnect().execute("https://private-ea45b-geeksfarmtask3.apiary-mock.com/account");
            }
        });
    }

    public void process_json(String json_str) throws JSONException {
        final String email = et_email.getText().toString();
        final String pass = et_pass.getText().toString();
        try {
            JSONObject api_json = new JSONObject(json_str);
            JSONArray users = api_json.getJSONArray("users");
            Gson gson_user = new Gson();
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                User model_user = gson_user.fromJson(String.valueOf(user), User.class);
                if (email.equals(model_user.getEmail()) && pass.equals(model_user.getPassword())) {
                    session.createLoginSession(String.valueOf(model_user.getId()),model_user.getEmail(), model_user.getPassword(), model_user.getToken_auth());
                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
        } catch (JSONException e) {
            Toast.makeText(MainActivity.this, "Email atau Password tidak sesuai!", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }

    public String get_data(String url_target) {
        String line = "";
        try {
            URL url = new URL(url_target);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    class ApiConnect extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return get_data(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                process_json(s);
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
