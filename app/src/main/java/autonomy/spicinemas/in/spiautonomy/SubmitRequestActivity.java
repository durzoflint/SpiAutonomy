package autonomy.spicinemas.in.spiautonomy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubmitRequestActivity extends AppCompatActivity {
    String sdate = "" , edate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_request);
        Intent i = getIntent();
        final String name = i.getStringExtra("name");
        final String empID = i.getStringExtra("empID");
        final TextView startDate = findViewById(R.id.startdate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(SubmitRequestActivity.this);
                final View selectDate = inflater.inflate(R.layout.selectdate, null);
                new AlertDialog.Builder(SubmitRequestActivity.this)
                        .setTitle("Select Start Date")
                        .setView(selectDate)
                        .setIcon(android.R.drawable.ic_menu_agenda)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                DatePicker pickDate = selectDate.findViewById(R.id.pickdate);
                                String year = "" + pickDate.getYear();
                                String month = "" + (pickDate.getMonth() + 1);
                                String day = "" + pickDate.getDayOfMonth();
                                if (month.length() == 1)
                                    month = "0" + month;
                                if (day.length() == 1)
                                    day = "0" + day;
                                sdate = year + month + day;
                                startDate.setText(day + "/" + month + "/" + year);
                            }
                        })
                        .create().show();
            }
        });
        final TextView endDate = findViewById(R.id.enddate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(SubmitRequestActivity.this);
                final View selectDate = inflater.inflate(R.layout.selectdate, null);
                new AlertDialog.Builder(SubmitRequestActivity.this)
                        .setTitle("Select End Date")
                        .setView(selectDate)
                        .setIcon(android.R.drawable.ic_menu_agenda)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                DatePicker pickDate = selectDate.findViewById(R.id.pickdate);
                                String year = "" + pickDate.getYear();
                                String month = "" + (pickDate.getMonth() + 1);
                                String day = "" + pickDate.getDayOfMonth();
                                if (month.length() == 1)
                                    month = "0" + month;
                                if (day.length() == 1)
                                    day = "0" + day;
                                edate = year + month + day;
                                endDate.setText(day + "/" + month + "/" + year);
                            }
                        })
                        .create().show();
            }
        });
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView reasonText = findViewById(R.id.reason);
                String reason = reasonText.getText().toString();
                if(sdate.length() == 8 && edate.length() == 8)
                {
                    new SubmitRequest().execute(name, empID, sdate, edate, reason);
                    onBackPressed();
                }
                else
                    Toast.makeText(SubmitRequestActivity.this, "Select Dates", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class SubmitRequest extends AsyncTask<String,Void,Void> {
        String webPage="";
        String baseUrl = "http://srmvdpauditorium.in/spi/";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(SubmitRequestActivity.this, "Please Wait!","Processing!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"insertleave.php?Name="+strings[0]+"&EmpID="+ strings[1] +
                        "&sdate="+strings[2]+"&edate="+strings[3]+"&reason="+strings[4]));
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
            progressDialog.dismiss();
            if(webPage.contains("Success"))
            {
                webPage = webPage.substring(webPage.indexOf("Success<br>")+11);
                String refID = webPage;
                Toast.makeText(SubmitRequestActivity.this,
                        "Request submitted successfully. Reference ID : " + refID, Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(SubmitRequestActivity.this, "Error while submitting request. Please try again.", Toast.LENGTH_LONG).show();
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