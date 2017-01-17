package com.example.anika.hostelmanagement;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class SignUp extends AppCompatActivity {
    Spinner spinner1, spinner2;
    Button signup;
    private EditText uiName,uiEmail,uiPassword,uiRoom,uiMobile;

    ArrayAdapter<CharSequence> adapter1;
    ArrayAdapter<CharSequence> adapter2;


    private class Initsignup extends  AsyncTask<android.content.Context,Void,Void>{
        protected Void doInBackground(android.content.Context... context) {
            spinner1 = (Spinner) findViewById(R.id.spinner1);
            spinner2 = (Spinner) findViewById(R.id.spinner2);
            signup = (Button) findViewById(R.id.createAccount);
            uiName = (EditText) findViewById(R.id.name); //identifier for Name field, similarly all elements starting with ui are identifiers for respective input/output fields
            uiEmail = (EditText) findViewById(R.id.email);
            uiPassword = (EditText) findViewById(R.id.password);
            uiRoom = (EditText) findViewById(R.id.room);
            uiMobile = (EditText) findViewById(R.id.mobile);
            adapter1 = ArrayAdapter.createFromResource(context[0], R.array.HostelType, android.R.layout.simple_spinner_item);
            adapter2 = ArrayAdapter.createFromResource(context[0], R.array.LoginType, android.R.layout.simple_spinner_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            return null;
        }
        protected void onPostExecute(Void v){
            spinner1.setAdapter(adapter1);
            spinner2.setAdapter(adapter2);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //reads inputs and send them to server when create account button is clicked
                    String name,mobile,password,email,room;
                    int category, hostel;
                    name = uiName.getText().toString();//reads Name input, similarly other similar lines followed by this do the same for thier respective fields.
                    mobile = uiMobile.getText().toString();

                    password = uiPassword.getText().toString();
                    email = uiEmail.getText().toString();
                    room = uiRoom.getText().toString();
                    category = spinner2.getSelectedItemPosition();
                    hostel = spinner1.getSelectedItemPosition();
                    if (room.isEmpty()||mobile.isEmpty()) signuper(name,email,password,hostel,category);
                    else signuper(name,email,password,hostel,room,mobile,category);//stores output from server to res, see signuper function.
                }
            });
        }
    }

    public static class Httpcaller extends AsyncTask <URL, Void, Boolean>{//class for making async(google for details, in short runs something on a separate thread from main thread to stop slowing down of main thread) http call.
        String response;//response from http calls are stored in this property of Httpcaller object.
        @Override
        protected Boolean doInBackground(URL... urls){//this method(function) is called in addition to other functions when execute method(a method form AsyncTask) is called on a Httpcaller object
            try{
                try {
                    URLConnection conn = urls[0].openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder res = new StringBuilder();
                    String inlines;
                    while ((inlines = br.readLine()) != null) res.append(inlines);
                    br.close();
                    response = res.toString();
                    Log.d ("URLCalled",urls[0].toString());
                    Log.d("Response", response);
                    return true;//indicates success
                } catch (IOException e) {
                    Log.e("FETCHING_REGISTER", "Net_Err", e);//logs any IOException
                    return false;
                }
            }catch (Exception e) {
                Log.e("FETCHING_REGISTER", "Gen_Err", e);//logs any general exception
                return false;
            }
        }
    }




    protected String signuper(String name, String email, String password, int hostel, String room, String mobile, int category ) {
        String response = "";//initiates response string
        Httpcaller httpcaller = new Httpcaller();//creates Httpcaller object to make a http call to server
        URL url = null;//Creates a URL object to be passed to httpcaller
        String urls = getString(R.string.signupUrl);//fetches address of server, which is stored in string.xml, its a string which will later be converted into URL object.
        urls = urls + "?name="+name+"&email="+email+"&password="+password+"&hostel="+hostel+"&room="+room+"&mobile="+mobile+"&category="+category;//adds GET parameters(google it if you dont know what it means, in short arguments for server side script) to URL.
        try {
            url = new URL(urls);//converts urls into URL object from string
        } catch (MalformedURLException e) {
            Log.e("FETCHING_REGISTER","malFormedURL",e);//checks for any inconsistency in url and logs error.
        }
        try {
            httpcaller.execute(url);//makes http call
            response= httpcaller.response;//stores response from server in response
        }
        catch (Exception e){
            Log.e("FETCHING_REGISTER", "Gen_Err", e);//catches any exception and logs them
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
        Initsignup setval = new Initsignup();
        setval.execute(this);
}}
