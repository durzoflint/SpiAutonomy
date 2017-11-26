package autonomy.spicinemas.in.spiautonomy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddEmployeesToDateActivity extends AppCompatActivity {
    String baseUrl = "http://srmvdpauditorium.in/spi/", date;
    LinearLayout empShift;
    ArrayList<Integer> spinnerIds = new ArrayList<>();
    ArrayList<String> empIds = new ArrayList<>();
    ArrayList<String> spinnerArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employees_to_date);
        Intent i = getIntent();
        date = i.getStringExtra("date");
        empShift = findViewById(R.id.empshift);
        spinnerArray.add("OFF");
        spinnerArray.add("9AM");        spinnerArray.add("10AM");
        spinnerArray.add("11AM");        spinnerArray.add("12PM");
        spinnerArray.add("1PM");        spinnerArray.add("2PM");
        spinnerArray.add("3PM");        spinnerArray.add("4PM");
        spinnerArray.add("5PM");        spinnerArray.add("6PM");
        spinnerArray.add("7PM");        spinnerArray.add("8PM");
        spinnerArray.add("9PM");        spinnerArray.add("10PM");
        new AddEmp().execute();
        Button updateSchedule = findViewById(R.id.update);
        updateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = baseUrl+"insertroster.php?Date="+date+"&";
                for (int i=0;i<spinnerIds.size();i++)
                {
                    Spinner temp = findViewById(spinnerIds.get(i));
                    String value = temp.getSelectedItem().toString();
                    url = url + empIds.get(i)+"="+value+"&";
                }
                new UpdateSchedule().execute(url.substring(0,url.length()-1));
            }
        });
    }
    private class UpdateSchedule extends AsyncTask<String,Void,Void> {
        String webPage="";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(AddEmployeesToDateActivity.this, "Please Wait!","Updating Schedule!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(strings[0]));
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
            if (webPage.contains("Update Successful"))
            {
                Toast.makeText(AddEmployeesToDateActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            else
                Toast.makeText(AddEmployeesToDateActivity.this, "Some Error Occurred.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
    private class AddEmp extends AsyncTask<Void,Void,Void> {
        String webPage="";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(AddEmployeesToDateActivity.this, "Please Wait!","Fetching Employee Data!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(handleSpaces(baseUrl+"allemp.php"));
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
            LinearLayout.LayoutParams matchParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            while (webPage.contains("<br>"))
            {
                int brI = webPage.indexOf("<br>");
                String name = webPage.substring(0, brI);
                webPage = webPage.substring(brI+4);
                brI = webPage.indexOf("<br>");
                String empID = webPage.substring(0, brI);
                webPage = webPage.substring(brI+4);
                brI = webPage.indexOf("<br>");
                String admin = webPage.substring(0, brI);
                webPage = webPage.substring(brI+4);
                if(admin.equals("no"))
                {
                    Context context = AddEmployeesToDateActivity.this;
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView emp = new TextView(context);
                    emp.setGravity(Gravity.CENTER_HORIZONTAL);
                    emp.setLayoutParams(matchParams);
                    emp.setText(name+"\n"+empID);
                    linearLayout.addView(emp);
                    Spinner spinner = new Spinner(context);
                    spinner.setLayoutParams(matchParams);
                    spinner.setGravity(Gravity.CENTER_HORIZONTAL);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                            (context, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    spinner.setAdapter(spinnerArrayAdapter);
                    int id = View.generateViewId();
                    spinner.setId(id);
                    empIds.add(empID);
                    spinnerIds.add(id);
                    linearLayout.addView(spinner);
                    empShift.addView(linearLayout);
                }
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