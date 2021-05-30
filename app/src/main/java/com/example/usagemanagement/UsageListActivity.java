package com.example.usagemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UsageListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Usage> mUsageList;
    private UsageItemAdapter mAdapter;

    private int gridNumber = 1;
    private int usageLimit = 10;

    private FirebaseFirestore mFirestore;
    private CollectionReference mUsages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_list);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);
        if(secret_key != 99)
            finish();


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mUsageList = new ArrayList<>();

        mAdapter = new UsageItemAdapter(this, mUsageList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mUsages = mFirestore.collection("Usages");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver, filter);
    }

    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null)
                return;

            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    usageLimit = 10;
                    queryData();
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    usageLimit = 5;
                    queryData();
                    break;
            }
        }
    };

    private void queryData() {
        mUsageList.clear();

       mUsages.orderBy("description").limit(usageLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
           for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
               Usage item = document.toObject(Usage.class);
               mUsageList.add(item);
           }

           if (mUsageList.size() == 0) {
               queryData();
           }

           mAdapter.notifyDataSetChanged();
       });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.usage_list_menu, menu);
        MenuItem menuUsage = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuUsage);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    public void moreInformation(Usage usage) {
        String currentType = usage.getUsageType();
        String currentDescription = usage.getDescription();
        Intent intent = new Intent(this, UsageActivity.class);
        intent.putExtra("type", currentType);
        intent.putExtra("description", currentDescription);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }
}