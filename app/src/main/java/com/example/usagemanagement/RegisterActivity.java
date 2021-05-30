package com.example.usagemanagement;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegisterActivity.class.getName();


    EditText description;
    EditText usageType;
    Spinner usageStatusType;

    private FirebaseFirestore mStore;
    private CollectionReference mItems;
    private NotificationHandler notificationHandler;

    private LocalDate Date;
    private String usageDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        if(secret_key != 99)
            finish();

        description = findViewById(R.id.description);
        usageType = findViewById(R.id.usageType);
        usageStatusType = findViewById(R.id.usageStatusType);
        FirebaseFirestore.setLoggingEnabled(true);


        usageStatusType.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usageStatus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        usageStatusType.setAdapter(adapter);

        mStore = FirebaseFirestore.getInstance();
        mItems = mStore.collection("Usages");
        Date = LocalDate.now();
        usageDate = Date.toString();
    }



    public void register(View view) {
        String Description = description.getText().toString();
        String UsageType = usageType.getText().toString();
        String UsageStatus = usageStatusType.getSelectedItem().toString();


        mItems.add(new Usage(Description, UsageStatus, usageDate  ,UsageType));
        //startUsageActivity(UsageType);
        finish();

        Log.i(LOG_TAG,  Description + UsageStatus + usageDate + UsageType);
    }


    public void cancel(View view) {
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