package com.example.anika.hostelmanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SignUp extends AppCompatActivity {
    Spinner spinner1, spinner2;
    Button signup;
    EditText uiName,uiEmail,uiPassword,uiRoom,uiMobile;
    TextView uiLog;
    ArrayAdapter<CharSequence> adapter1;
    ArrayAdapter<CharSequence> adapter2;


    private class Httpcaller extends AsyncTask <URL, Void, Boolean>{
        String response;
        @Override
        protected Boolean doInBackground(URL... urls){
            try{
                try {
                    response=urls[0].getContent().toString();
                    return true;
                } catch (IOException e) {
                    Log.e("FETCHING_REGISTER", "Net_Err", e);
                    return false;
                }
            }catch (Exception e) {
                Log.e("FETCHING_REGISTER", "Gen_Err", e);
                return false;
            }
        }
    }




    protected String signuper(String name, String email, String password, int hostel, String room, String mobile, int category ) {
        String response = "";
        Httpcaller httpcaller = new Httpcaller();
        URL url = null;
        String urls = getString(R.string.signupUrl);
        urls = urls + "?name="+name+"&email="+email+"&password="+password+"&hostel="+hostel+"&room="+room+"&mobile="+mobile;
        try {
            url = new URL(urls);
        } catch (MalformedURLException e) {
            Log.e("FETCHING_REGISTER","malFormedURL",e);
        }
        try {
            httpcaller.execute(url);
            response= httpcaller.response;}
        catch (Exception e){
            Log.e("FETCHING_REGISTER", "Gen_Err", e);
        }


        return response;
    }
    protected  String signuper (String name, String email, String password, int hostel, int category){
        return signuper(name,email,password,hostel,"0","0", category);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        signup = (Button) findViewById(R.id.createAccount);
        uiName = (EditText) findViewById(R.id.name);
        uiEmail = (EditText) findViewById(R.id.email);
        uiPassword = (EditText) findViewById(R.id.password);
        uiRoom = (EditText) findViewById(R.id.room);
        uiMobile = (EditText) findViewById(R.id.mobile) ;
        uiLog = (TextView) findViewById(R.id.log);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.HostelType, android.R.layout.simple_spinner_item);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.LoginType, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,mobile,password,email,room, res;
                int category, hostel;
                name = uiName.getText().toString();
                mobile = uiMobile.getText().toString();
                password = uiPassword.getText().toString();
                email = uiEmail.getText().toString();
                room = uiRoom.getText().toString();
                category = spinner2.getSelectedItemPosition();
                hostel = spinner1.getSelectedItemPosition();
                res=signuper(name,email,password,hostel,room,mobile,category);
                uiLog.setText(res);
            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + "is selected ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}
