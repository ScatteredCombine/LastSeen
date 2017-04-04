package com.example.admin.lastseen;

/**
 * Created by ADMIN on 18-03-2017.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

public class ReceiveFromServer extends AsyncTask<String,String,ArrayList<String>>{

    private Context context;
    private TextView lastSeenStatus;
    public static String TAG="RECEIVE FROM SERVER";

    public ReceiveFromServer(Context context,TextView wordField) {
        this.context = context;
        this.lastSeenStatus = wordField;
    }

    protected void onPreExecute(){
        Log.i(TAG,"onPreExecute");
        MainActivity.progressBar.setProgress(30);
        this.lastSeenStatus.setText("Connecting server..");
    }


    @Override
    protected ArrayList<String> doInBackground(String... arg0) {
        Log.i(TAG,"DoinBG");
        try{
            Log.i(TAG,"In try block");
            String facName = arg0[0];
            Log.i(TAG,"Facname:"+facName);
            String link="http://randomid12321.000webhostapp.com/GetDataFS_locator.php";
            String data  = URLEncoder.encode("facName", "UTF-8") + "=" +
                    URLEncoder.encode(facName, "UTF-8");
            Log.i(TAG,"Data:"+data);
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            Log.i(TAG,"URLConn open");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.i(TAG,"Writing Data to server..");
            wr.write( data );
            wr.flush();
            Log.i(TAG,"Completed write op");
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            ArrayList<String> result = new ArrayList<String>();
            String line = null;
            result.clear();
            // Read Server Response
            Log.i(TAG,"Reading Server Response");
            while((line = reader.readLine()) != null) {
                if(line.equals("end")){
                    break;
                }
                else
                {
                    result.add(line);
                }
            }
            Log.i(TAG,"Result:"+result);
            return (result);
        }
        catch(Exception e){
            e.printStackTrace();
            Log.i(TAG,"Exception:"+e.getMessage());
            ArrayList<String> result = new ArrayList<String>();
            result.add("Exception: " + e.getMessage());
            return (result);
        }
    }




    @Override
    protected void onPostExecute(ArrayList<String> result){
        Log.i(TAG,"onPostexec");
        MainActivity.progressBar.setProgress(100);
        MainActivity.progressBar.setVisibility(View.GONE);
        if(result.get(0).equals("false")){
            Log.i(TAG,"No faculty found");
            this.lastSeenStatus.setText("No faculty with that name found!");
            MainActivity.lastUpdateStatus.setText("Last Updated on : -----");
        }
        else {
            Log.i(TAG,"Faculty found");
            String date=result.get(2).substring(0,11);
            int time_hh,time_mm,time_ss;
            time_hh=Integer.parseInt(result.get(2).substring(11,13));
            time_mm=Integer.parseInt(result.get(2).substring(14,16));
            time_ss=Integer.parseInt(result.get(2).substring(17));
            time_hh=time_hh+5;
            time_mm=time_mm+30;
            if(time_mm>60){
                time_mm=time_mm-60;
                time_hh++;
            }
            Log.i(TAG,"time_hh:"+time_hh);
            if(time_mm<10){
                this.lastSeenStatus.setText(result.get(0).toUpperCase()+" was last seen in "+result.get(1)+" at "+time_hh+":0"+time_mm+":"+time_ss);
            }
            else {
                this.lastSeenStatus.setText(result.get(0).toUpperCase()+" was last seen in "+result.get(1)+" at "+time_hh+":"+time_mm+":"+time_ss);
            }
            MainActivity.lastUpdateStatus.setText("Last updated on : "+date);
        }
    }
}
