package autonomy.spicinemas.in.spiautonomy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;

public class UpdateScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule);
        setTitle("Select Date");
        final DatePicker datePicker = findViewById(R.id.datePicker);
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                Intent intent = new Intent(UpdateScheduleActivity.this, AddEmployeesToDateActivity.class);
                if(day>10)
                    intent.putExtra("date",""+year+month+day);
                else
                    intent.putExtra("date",""+year+month+"0"+day);
                startActivity(intent);
            }
        });
    }
}
