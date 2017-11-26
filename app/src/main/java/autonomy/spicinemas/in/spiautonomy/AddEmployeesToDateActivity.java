package autonomy.spicinemas.in.spiautonomy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AddEmployeesToDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employees_to_date);
        Intent i = getIntent();
        String date = i.getStringExtra("date");
        LinearLayout empShift = findViewById(R.id.empshift);

    }
}
