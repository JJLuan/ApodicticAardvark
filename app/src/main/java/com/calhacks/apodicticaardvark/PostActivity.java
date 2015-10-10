package com.calhacks.apodicticaardvark;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by John on 10/10/2015.
 */
public class PostActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
    }

    public void post(View view){
        Poster poster = new Poster();
        poster.execute();
    }

    private class Poster extends AsyncTask<Void,Void,String>{
        private static final String SERVER_URL="http://10.142.37.98:5000/receive/";
        @Override
        protected String doInBackground(Void... params) {
            try{
                HttpURLConnection connection=(HttpURLConnection)new URL(SERVER_URL).openConnection();
                connection.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());

                

                out.write(new Gson().toJson("test").getBytes());
                connection.disconnect();
            } catch(Exception e){
                e.printStackTrace();
            }
            return "whatever dude";
        }
    }
}
