package com.example.anika.hostelmanagement;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginPage extends AppCompatActivity {
    Button login;
    private EditText uiEmail,uiPassword;

    private class Initlogin extends AsyncTask<Context,Void,Void> {
        protected Void doInBackground(android.content.Context... context) {
            login = (Button) findViewById(R.id.login);
            uiEmail = (EditText) findViewById(R.id.logemail);
            uiPassword = (EditText) findViewById(R.id.logpassword);

            return null;
        }
        protected void onPostExecute(Void v){
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //reads inputs and send them to server when create account button is clicked
                    String password,email;
                    password = uiPassword.getText().toString();
                    email = uiEmail.getText().toString();
                    loginer(email,password);//stores output from server to res, see signuper function.
                }
            });
        }
    }





    protected String loginer(String email, String password ) {
        String response = "";//initiates response string
        SignUp.Httpcaller httpcaller = new SignUp.Httpcaller();//creates Httpcaller object to make a http call to server
        URL url = null;//Creates a URL object to be passed to httpcaller
        String urls = getString(R.string.loginUrl);//fetches address of server, which is stored in string.xml, its a string which will later be converted into URL object.
        urls = urls + "?&email="+email+"&password="+password;//adds GET parameters(google it if you dont know what it means, in short arguments for server side script) to URL.
        try {
            url = new URL(urls);//converts urls into URL object from string
        } catch (MalformedURLException e) {
            Log.e("FETCHING_login","malFormedURL",e);//checks for any inconsistency in url and logs error.
        }
        try {
            httpcaller.execute(url);//makes http call
            response= httpcaller.response;//stores response from server in response
        }
        catch (Exception e){
            Log.e("FETCHING_login", "Gen_Err", e);//catches any exception and logs them
        }


        return response;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Initlogin setval = new Initlogin();
        setval.execute(this);
    }
}
