package com.mandakini.draftflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mandakini.draftflow.R;
import com.mandakini.draftflow.adapters.DraftAdapter;
import com.mandakini.draftflow.database.DraftDao;
import com.mandakini.draftflow.models.Draft;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private Button btnNewDraft, btnBulkActions;
    private TextView btnSearch;
    private TextView txtDraftCount;
    private RecyclerView recyclerDrafts;

    // Data
    private DraftDao draftDao;
    private DraftAdapter draftAdapter;
    private List<Draft> draftList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DAO
        draftDao = new DraftDao(this);

        // Bind Views
        btnNewDraft = findViewById(R.id.btnNewDraft);
        btnBulkActions = findViewById(R.id.btnBulkActions);
        btnSearch = findViewById(R.id.btnSearch);
        txtDraftCount = findViewById(R.id.txtDraftCount);
        recyclerDrafts = findViewById(R.id.recyclerDrafts);

        // Setup RecyclerView
        recyclerDrafts.setLayoutManager(new LinearLayoutManager(this));

        // New Draft
        btnNewDraft.setOnClickListener(v ->
                startActivity(new Intent(this, NewDraftActivity.class))
        );

        // Search
        btnSearch.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class))
        );

        // Bulk Actions
        btnBulkActions.setOnClickListener(v ->
                startActivity(new Intent(this, BulkActionsActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Update draft count
        int count = draftDao.getDraftCount();
        txtDraftCount.setText(count + " Active Drafts");

        // Load all drafts
        draftList = draftDao.getAllDrafts();

        // Bind adapter
        draftAdapter = new DraftAdapter(draftList);
        recyclerDrafts.setAdapter(draftAdapter);
    }
}