package com.example.usagemanagement;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;

public class UsageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore mFirestore;
    private CollectionReference mUsages;
    private Usage usage;
    private String id;
    private String date;
    private LocalDate tmpDate;

    private NotificationHandler notificationHandler;

    EditText usageType;
    EditText Date;
    TextView textView;
    Spinner Status;
    EditText Description;
    String type;
    String description;
    CalendarView calendarView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);

        type = getIntent().getExtras().getString("type");
        description = getIntent().getExtras().getString("description");

        tmpDate = LocalDate.now();
        date = tmpDate.toString();

        notificationHandler = new NotificationHandler(this);

        if (type != null) {
            mFirestore = FirebaseFirestore.getInstance();
            mUsages = mFirestore.collection("Usages");

            mUsages.limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Usage item = document.toObject(Usage.class);
                    if (type.equals(item.getUsageType()) && description.equals(item.getDescription())) {
                        usage = document.toObject(Usage.class);
                        id = document.getId();
                    }
                }

                usageType = findViewById(R.id.Type);
                Date = findViewById(R.id.Date);
                Description = findViewById(R.id.Description);
                Status = findViewById(R.id.Status);
                textView = findViewById(R.id.textView);
                calendarView = findViewById(R.id.CalendarView);

                usageType.setText(usage.getUsageType());
                Date.setText(usage.getUsageDate());
                Description.setText(usage.getDescription());
                textView.setText(usage.getStatus());

                Status.setOnItemSelectedListener(this);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usageStatus, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Status.setAdapter(adapter);


                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        month += 1;
                        String  curDate = String.valueOf(dayOfMonth);
                        String  Year = String.valueOf(year);
                        String  Month = String.valueOf(month);

                        date = Year + "-" + Month + "-" + curDate;
                    }
                });



            });
        }
    }

    public void update(View view) {
        mFirestore.collection("Usages").document(id).update("description", Description.getText().toString(),"status", Status.getSelectedItem().toString(), "usageDate", date, "usageType", usageType.getText().toString());
        notificationHandler.update(usage.getUsageType());
        finish();

        Intent intent = new Intent(this, UsageActivity.class);
        intent.putExtra("type", usageType.getText().toString());
        intent.putExtra("description", Description.getText().toString());
        startActivity(intent);
    }

    public void delete(View view) {
        mFirestore.collection("Usages").document(id).delete();
        notificationHandler.delete(usage.getUsageType());
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}