package edu.hku.comp2506.warehouse.app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import edu.hku.comp2506.warehouse.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the Title bar on welcome page
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        setContentView(R.layout.activity_welcome);
        Handler handler=new Handler();
        // After 2 seconds, show the Login Page
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}