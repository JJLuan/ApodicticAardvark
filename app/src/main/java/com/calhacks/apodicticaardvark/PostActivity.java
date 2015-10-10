package com.calhacks.apodicticaardvark;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
        String title = ((EditText)findViewById(R.id.titleText)).getText().toString();
        String time = ((EditText)findViewById(R.id.timeText)).getText().toString();
        String description = ((EditText)findViewById(R.id.descText)).getText().toString();
        poster.execute(title, time, description);
    }

    public void selectTimeAndDate(View view){
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    private class Poster extends AsyncTask<String,Void,String>{
        private static final String SERVER_URL="http://10.142.37.98:5000/receive/";
        @Override
        protected String doInBackground(String... params) {
            try{
                HttpURLConnection connection=(HttpURLConnection)new URL(SERVER_URL).openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");


                Map<String,String>info = new HashMap<String,String>();
                info.put("Title",params[0]);
                info.put("Time",params[1]);
                info.put("Description",params[2]);
                Writer out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
                String json = new Gson().toJson(info, info.getClass());
                Log.d("DEBUG", "JSON content: "+json);
                Log.d("DEBUG", "Writing to stream: " + Arrays.toString(json.getBytes()));
                out.write(json);
                out.flush();
                out.close();
                int resp = connection.getResponseCode();
                if(resp!=200){
                    Log.d("DEBUG","Network error. Status: "+resp);
                }
                connection.disconnect();
            } catch(Exception e){
                e.printStackTrace();
            }
            return "whatever dude";
        }
    }
}
