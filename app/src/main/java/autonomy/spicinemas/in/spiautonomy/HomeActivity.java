package autonomy.spicinemas.in.spiautonomy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    String name, empID, admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        name = i.getStringExtra("Name");
        empID = i.getStringExtra("EmpID");
        admin = i.getStringExtra("Admin");
        if (admin.equals("yes"))
        {
            setContentView(R.layout.activity_home_admin);
            TextView updateSchedule = findViewById(R.id.updateschedule);
            updateSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, UpdateScheduleActivity.class));
                }
            });
        }
        else
        {
            setContentView(R.layout.activity_home);
            /*TextView viewSchedule = (TextView)findViewById(R.id.viewschedule);
            viewSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/
        }
        TextView viewSchedule = (TextView)findViewById(R.id.viewschedule);
        viewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ViewScheduleActivity.class));
            }
        });
        TextView trainingVideos = findViewById(R.id.trainingvideos);
        trainingVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}