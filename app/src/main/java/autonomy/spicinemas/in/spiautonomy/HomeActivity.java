package autonomy.spicinemas.in.spiautonomy;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
            TextView viewLeaveRequests = findViewById(R.id.viewleavereq);
            viewLeaveRequests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, LeaveRequestActivity.class));
                }
            });
        }
        else
        {
            setContentView(R.layout.activity_home);
            TextView leaveRequest = findViewById(R.id.leaverequest);
            leaveRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomeActivity.this, SubmitRequestActivity.class);
                    i.putExtra("name", name);
                    i.putExtra("empID", empID);
                    startActivity(i);
                }
            });
            TextView viewLeaveRequest = findViewById(R.id.viewleaverequests);
            viewLeaveRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomeActivity.this, ViewLeaveRequestsActivity.class);
                    i.putExtra("name", name);
                    i.putExtra("empID", empID);
                    startActivity(i);
                }
            });
        }
        TextView viewSchedule = findViewById(R.id.viewschedule);
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PLjp0AEEJ0-fEuKWBJV30ebHCGoi1Ny69U")));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuhome, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout)
            logout();
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        logout();
    }
    void logout(){
        new AlertDialog.Builder(this)
                .setTitle("Really Logout?")
                .setMessage("Are you sure you want to Logout?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                }).create().show();
    }
}