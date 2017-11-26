package autonomy.spicinemas.in.spiautonomy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
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

public class ViewScheduleActivity extends AppCompatActivity {
    String baseUrl = "http://srmvdpauditorium.in/spi/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        setTitle("Schedule");
        new ViewSchedule().execute();
    }
    private class ViewSchedule extends AsyncTask<Void,Void,Void> {
        String webPage="";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(ViewScheduleActivity.this,
                    "Please Wait!","Fetching Schedule!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"viewroster.php"));
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
            //try
            //{
                if (webPage.contains("<br>"))
                {
                    int brI = webPage.indexOf("<br>");
                    int noOfEmp = Integer.parseInt(webPage.substring(0,brI));
                    webPage = webPage.substring(brI+4);
                    while (webPage.contains("<br>"))
                    {
                        brI = webPage.indexOf("<br>");
                        String date = webPage.substring(0, brI);
                        date = date.substring(6)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
                        webPage = webPage.substring(brI+4);
                        Context context = ViewScheduleActivity.this;
                        LinearLayout.LayoutParams matchParams = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                        LinearLayout boundary = new LinearLayout(context);
                        boundary.setLayoutParams(matchParams);
                        boundary.setOrientation(LinearLayout.VERTICAL);
                        boundary.setPadding(80,40,80,40);
                        CardView cardView = new CardView(context);
                        cardView.setLayoutParams(matchParams);
                        LinearLayout outer = new LinearLayout(context);
                        outer.setLayoutParams(matchParams);
                        outer.setOrientation(LinearLayout.VERTICAL);
                        TextView dateLabel = new TextView(context);
                        dateLabel.setText(date);
                        dateLabel.setTextSize(20);
                        dateLabel.setGravity(Gravity.CENTER);
                        outer.addView(dateLabel);
                        final int id = View.generateViewId();
                        LinearLayout mid = new LinearLayout(context);
                        mid.setLayoutParams(matchParams);
                        mid.setOrientation(LinearLayout.VERTICAL);
                        mid.setVisibility(View.GONE);
                        mid.setId(id);
                        for (int i=0;i<noOfEmp;i++)
                        {
                            LinearLayout inner = new LinearLayout(context);
                            inner.setLayoutParams(matchParams);
                            inner.setOrientation(LinearLayout.HORIZONTAL);
                            brI = webPage.indexOf("<br>");
                            String name = webPage.substring(0, brI);
                            webPage = webPage.substring(brI+4);
                            brI = webPage.indexOf("<br>");
                            String empID = webPage.substring(0, brI);
                            webPage = webPage.substring(brI+4);
                            brI = webPage.indexOf("<br>");
                            TextView nameAndID = new TextView(context);
                            nameAndID.setText(name+"\n"+empID);
                            nameAndID.setLayoutParams(matchParams);
                            nameAndID.setGravity(Gravity.CENTER_HORIZONTAL);
                            String shift = webPage.substring(0, brI);
                            webPage = webPage.substring(brI+4);
                            TextView time = new TextView(context);
                            time.setText(shift);
                            time.setGravity(Gravity.CENTER_HORIZONTAL);
                            time.setLayoutParams(matchParams);
                            inner.addView(nameAndID);
                            inner.addView(time);
                            mid.addView(inner);
                        }
                        outer.addView(mid);
                        cardView.addView(outer);
                        LinearLayout data = findViewById(R.id.data);
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
                        boundary.addView(cardView);
                        data.addView(boundary);
                    }
                }
            //}
            //catch (Exception e)
            //{
             //   Toast.makeText(ViewScheduleActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            //}
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