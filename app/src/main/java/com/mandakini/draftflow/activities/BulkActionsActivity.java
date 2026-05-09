package com.mandakini.draftflow.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mandakini.draftflow.R;
import com.mandakini.draftflow.adapters.BulkDraftAdapter;
import com.mandakini.draftflow.database.DraftDao;
import com.mandakini.draftflow.models.Draft;

import java.util.List;

public class BulkActionsActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView txtSelectedCount;
    private TextView btnDeleteSelected;
    private RecyclerView recyclerBulkDrafts;

    private DraftDao draftDao;
    private BulkDraftAdapter adapter;
    private List<Draft> draftList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_actions);

        draftDao = new DraftDao(this);

        btnBack = findViewById(R.id.btnBack);
        txtSelectedCount = findViewById(R.id.txtSelectedCount);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);
        recyclerBulkDrafts = findViewById(R.id.recyclerBulkDrafts);

        recyclerBulkDrafts.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        loadDrafts();

        btnDeleteSelected.setOnClickListener(v -> deleteSelectedDrafts());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDrafts();
    }

    private void loadDrafts() {
        draftList = draftDao.getAllDrafts();

        adapter = new BulkDraftAdapter(draftList, selectedCount ->
                txtSelectedCount.setText(selectedCount + " selected")
        );

        recyclerBulkDrafts.setAdapter(adapter);

        txtSelectedCount.setText("0 selected");
    }

    private void deleteSelectedDrafts() {
        List<Integer> selectedIds = adapter.getSelectedIds();

        if (selectedIds.isEmpty()) {
            Toast.makeText(this,
                    "Please select at least one draft",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete Selected Drafts")
                .setMessage("Are you sure you want to delete " +
                        selectedIds.size() + " draft(s)?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int deletedCount =
                            draftDao.deleteMultipleDrafts(selectedIds);

                    if (deletedCount > 0) {
                        Toast.makeText(this,
                                deletedCount + " draft(s) deleted",
                                Toast.LENGTH_SHORT).show();
                        loadDrafts();
                    } else {
                        Toast.makeText(this,
                                "Failed to delete drafts",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}