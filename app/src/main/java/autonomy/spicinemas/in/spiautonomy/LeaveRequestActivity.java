package autonomy.spicinemas.in.spiautonomy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LeaveRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);
        setTitle("Leave Requests");
        new FetchPending().execute();
    }
    private class FetchPending extends AsyncTask<Void,Void,Void> {
        String webPage="";
        String baseUrl = "http://srmvdpauditorium.in/spi/";
        ProgressDialog progressDialog;
        Context context = LeaveRequestActivity.this;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(LeaveRequestActivity.this, "Please Wait!","Fetching Data!");
            super.onPreExecute();
            LinearLayout data = findViewById(R.id.data);
            TextView label = new TextView(context);
            label.setText("Pending Requests");
            label.setTextSize(23);
            label.setPadding(0,8,0,8);
            label.setGravity(Gravity.CENTER);
            label.setTextColor(Color.WHITE);
            data.addView(label);
        }
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"viewPending.php"));
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data=br.readLine()) != null)
                    webPage=webPage+data;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (webPage.contains("<br>"))
            {
                while (webPage.contains("<br>"))
                {
                    String name = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    String empID = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    String reason = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    String startDate = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    String endDate = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    LinearLayout.LayoutParams matchParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    CardView cardView = new CardView(context);
                    cardView.setLayoutParams(matchParams);
                    LinearLayout inner = new LinearLayout(context);
                    inner.setLayoutParams(matchParams);
                    inner.setOrientation(LinearLayout.VERTICAL);
                    TextView nameLabel = new TextView(context);
                    nameLabel.setText("Name : "+name);
                    nameLabel.setTextSize(20);
                    nameLabel.setGravity(Gravity.CENTER);
                    inner.addView(nameLabel);
                    TextView empIDLabel = new TextView(context);
                    empIDLabel.setText("Employee ID : "+empID);
                    empIDLabel.setTextSize(20);
                    empIDLabel.setGravity(Gravity.CENTER);
                    inner.addView(empIDLabel);
                    TextView startateLabel = new TextView(context);
                    startateLabel.setText("Start Date : "+startDate);
                    startateLabel.setTextSize(20);
                    startateLabel.setGravity(Gravity.CENTER);
                    inner.addView(startateLabel);
                    TextView endDateLabel = new TextView(context);
                    endDateLabel.setText("End Date : "+endDate);
                    endDateLabel.setTextSize(20);
                    endDateLabel.setGravity(Gravity.CENTER);
                    inner.addView(endDateLabel);
                    TextView reasonLabel = new TextView(context);
                    reasonLabel.setText("Reason : "+reason);
                    reasonLabel.setTextSize(20);
                    reasonLabel.setGravity(Gravity.CENTER);
                    reasonLabel.setVisibility(View.GONE);
                    inner.addView(reasonLabel);
                    final int id = View.generateViewId();
                    reasonLabel.setId(id);
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View v = findViewById(id);
                            if(v.getVisibility()==View.GONE)
                                v.setVisibility(View.VISIBLE);
                            else
                                v.setVisibility(View.GONE);
                        }
                    });
                    cardView.addView(inner);
                    LinearLayout outer = new LinearLayout(context);
                    outer.setLayoutParams(matchParams);
                    outer.setOrientation(LinearLayout.VERTICAL);
                    outer.addView(cardView);
                    outer.setPadding(80,40,80,40);
                    LinearLayout data = findViewById(R.id.data);
                    data.addView(outer);
                }
            }
            else
            {
                LinearLayout data = findViewById(R.id.data);
                TextView reasonLabel = new TextView(context);
                reasonLabel.setText("No Data Found");
                reasonLabel.setTextSize(20);
                reasonLabel.setGravity(Gravity.CENTER);
                reasonLabel.setVisibility(View.GONE);
                data.addView(reasonLabel);
            }
            new FetchApproved().execute();
            progressDialog.dismiss();
        }
    }
    private class FetchApproved extends AsyncTask<Void,Void,Void> {
        String webPage="";
        String baseUrl = "http://srmvdpauditorium.in/spi/";
        ProgressDialog progressDialog;
        Context context = LeaveRequestActivity.this;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(LeaveRequestActivity.this, "Please Wait!","Fetching Data!");
            super.onPreExecute();
            LinearLayout data = findViewById(R.id.data);
            TextView label = new TextView(context);
            label.setText("Approved Requests");
            label.setTextSize(23);
            label.setPadding(0,8,0,8);
            label.setGravity(Gravity.CENTER);
            label.setTextColor(Color.WHITE);
            data.addView(label);
        }
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"viewApproved.php"));
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data=br.readLine()) != null)
                    webPage=webPage+data;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (webPage.contains("<br>"))
            {
                while (webPage.contains("<br>"))
                {
                    String name = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    String empID = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    String reason = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    String startDate = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    String endDate = webPage.substring(0, webPage.indexOf("<br>"));
                    webPage = webPage.substring(webPage.indexOf("<br>")+4);
                    LinearLayout.LayoutParams matchParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    CardView cardView = new CardView(context);
                    cardView.setLayoutParams(matchParams);
                    LinearLayout inner = new LinearLayout(context);
                    inner.setLayoutParams(matchParams);
                    inner.setOrientation(LinearLayout.VERTICAL);
                    TextView nameLabel = new TextView(context);
                    nameLabel.setText("Name : "+name);
                    nameLabel.setTextSize(20);
                    nameLabel.setGravity(Gravity.CENTER);
                    inner.addView(nameLabel);
                    TextView empIDLabel = new TextView(context);
                    empIDLabel.setText("Employee ID : "+empID);
                    empIDLabel.setTextSize(20);
                    empIDLabel.setGravity(Gravity.CENTER);
                    inner.addView(empIDLabel);
                    TextView startateLabel = new TextView(context);
                    startateLabel.setText("Start Date : "+startDate);
                    startateLabel.setTextSize(20);
                    startateLabel.setGravity(Gravity.CENTER);
                    inner.addView(startateLabel);
                    TextView endDateLabel = new TextView(context);
                    endDateLabel.setText("End Date : "+endDate);
                    endDateLabel.setTextSize(20);
                    endDateLabel.setGravity(Gravity.CENTER);
                    inner.addView(endDateLabel);
                    TextView reasonLabel = new TextView(context);
                    reasonLabel.setText("Reason : "+reason);
                    reasonLabel.setTextSize(20);
                    reasonLabel.setGravity(Gravity.CENTER);
                    reasonLabel.setVisibility(View.GONE);
                    inner.addView(reasonLabel);
                    final int id = View.generateViewId();
                    reasonLabel.setId(id);
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View v = findViewById(id);
                            if(v.getVisibility()==View.GONE)
                                v.setVisibility(View.VISIBLE);
                            else
                                v.setVisibility(View.GONE);
                        }
                    });
                    cardView.addView(inner);
                    LinearLayout outer = new LinearLayout(context);
                    outer.setLayoutParams(matchParams);
                    outer.setOrientation(LinearLayout.VERTICAL);
                    outer.addView(cardView);
                    outer.setPadding(80,40,80,40);
                    LinearLayout data = findViewById(R.id.data);
                    data.addView(outer);
                }
            }
            else
            {
                LinearLayout data = findViewById(R.id.data);
                TextView reasonLabel = new TextView(context);
                reasonLabel.setText("No Data Found");
                reasonLabel.setTextSize(20);
                reasonLabel.setGravity(Gravity.CENTER);
                reasonLabel.setVisibility(View.GONE);
                data.addView(reasonLabel);
            }
            progressDialog.dismiss();
        }
    }
    String handleSpaces(String s){
        String x="";
        for(int i=0;i<s.length();i++)
        {
            char ch= s.charAt(i);
            if(ch==' ')
                x=x+"%20";
            else
                x=x+ch;
        }
        return x;
    }
}