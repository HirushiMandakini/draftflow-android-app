package com.mandakini.draftflow.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mandakini.draftflow.R;
import com.mandakini.draftflow.database.DraftDao;
import com.mandakini.draftflow.models.Draft;

public class DraftDetailActivity extends AppCompatActivity {

    private TextView btnBack;
    private TextView btnEdit;

    private TextView txtDetailStatus;
    private TextView txtDetailTitle;
    private TextView txtDetailDate;
    private TextView txtDetailContent;

    private ImageView imgDetailPreview;

    private Button btnDeleteDraft;

    private DraftDao draftDao;
    private Draft draft;
    private int draftId;
    private Button btnShareDraft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_detail);
        btnShareDraft = findViewById(R.id.btnShareDraft);
        btnShareDraft.setOnClickListener(v -> shareDraft());

        // Initialize DAO
        draftDao = new DraftDao(this);

        // Get draft ID from intent
        draftId = getIntent().getIntExtra("draft_id", -1);

        if (draftId == -1) {
            Toast.makeText(this, "Invalid draft ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Bind Views
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);

        txtDetailStatus = findViewById(R.id.txtDetailStatus);
        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        txtDetailDate = findViewById(R.id.txtDetailDate);
        txtDetailContent = findViewById(R.id.txtDetailContent);

        imgDetailPreview = findViewById(R.id.imgDetailPreview);

        btnDeleteDraft = findViewById(R.id.btnDeleteDraft);

        // Load draft data
        loadDraft(draftId);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Edit button
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditDraftActivity.class);
            intent.putExtra("draft_id", draftId);
            startActivity(intent);
        });

        // Delete button
        btnDeleteDraft.setOnClickListener(v -> deleteDraft());
    }
    private void shareDraft() {
        if (draft == null) {
            Toast.makeText(this, "No draft to share", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailBody =
                draft.getTitle() + "\n\n" +
                        draft.getContent() + "\n\n" +
                        "Shared from DraftFlow";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, draft.getTitle());
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        if (draft.getImagePath() != null && !draft.getImagePath().isEmpty()) {
            Uri imageUri = Uri.parse(draft.getImagePath());
            emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivity(Intent.createChooser(emailIntent, "Send Draft via Email"));
    }
    private void loadDraft(int draftId) {
        draft = draftDao.getDraftById(draftId);

        if (draft == null) {
            Toast.makeText(this, "Draft not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set text data
        txtDetailStatus.setText(draft.getSyncStatus());
        txtDetailTitle.setText(draft.getTitle());
        txtDetailDate.setText("Last edited: " + draft.getUpdatedAt());
        txtDetailContent.setText(draft.getContent());

        // Show image if available
        if (draft.getImagePath() != null && !draft.getImagePath().isEmpty()) {
            try {
                imgDetailPreview.setImageURI(Uri.parse(draft.getImagePath()));
                imgDetailPreview.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                imgDetailPreview.setVisibility(View.GONE);
            }
        } else {
            imgDetailPreview.setVisibility(View.GONE);
        }
    }

    private void deleteDraft() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Draft")
                .setMessage("Are you sure you want to delete this draft?")
                .setPositiveButton("Delete", (DialogInterface dialog, int which) -> {
                    int result = draftDao.deleteDraft(draftId);

                    if (result > 0) {
                        Toast.makeText(
                                this,
                                "Draft deleted successfully",
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();
                    } else {
                        Toast.makeText(
                                this,
                                "Failed to delete draft",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDraft(draftId);
    }
}