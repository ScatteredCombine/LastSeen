package com.example.admin.lastseen;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText userInput;
    static TextView lastSeenStatus,lastUpdateStatus;
    static ProgressBar progressBar;
    boolean doubleBackToExitPressedOnce = false;    //Check flag for double-back-exit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInput=(EditText) findViewById(R.id.userInput);
        lastSeenStatus=(TextView) findViewById(R.id.lastSeenStatus);
        lastUpdateStatus=(TextView) findViewById(R.id.lastUpdateStatus);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
    }

    public void sendData(View view){
        //-------- this is to hide the keyboard after pressing login button
        userInput.clearFocus();
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //--------
        progressBar.setIndeterminate(true);
        progressBar.setProgress(10);
        progressBar.setVisibility(View.VISIBLE);
        String facultyName = userInput.getText().toString();
        new ReceiveFromServer(this,lastSeenStatus).execute(facultyName);
    }

    @Override
    public void onBackPressed() {
        Log.i("Main Activity","onBackPressed");
        if (doubleBackToExitPressedOnce) {      //code to handle double-back-exit
            super.onBackPressed();
            //finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Main Activity","onDestroy");
    }
}
