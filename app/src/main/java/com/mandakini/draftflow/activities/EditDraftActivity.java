package com.mandakini.draftflow.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mandakini.draftflow.R;
import com.mandakini.draftflow.database.DraftDao;
import com.mandakini.draftflow.models.Draft;

public class EditDraftActivity extends AppCompatActivity {

    public static final String EXTRA_DRAFT_ID = "draft_id";

    private EditText edtEditTitle, edtEditContent;
    private TextView btnClose, btnDone;
    private Button btnUpdateDraft;

    private DraftDao draftDao;
    private Draft draft;
    private int draftId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_draft);

        draftDao = new DraftDao(this);

        edtEditTitle = findViewById(R.id.edtEditTitle);
        edtEditContent = findViewById(R.id.edtEditContent);
        btnClose = findViewById(R.id.btnClose);
        btnDone = findViewById(R.id.btnDone);
        btnUpdateDraft = findViewById(R.id.btnUpdateDraft);

        draftId = getIntent().getIntExtra(EXTRA_DRAFT_ID, -1);

        if (draftId == -1) {
            Toast.makeText(this, "Draft not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadDraft();

        btnClose.setOnClickListener(v -> finish());
        btnDone.setOnClickListener(v -> updateDraft());
        btnUpdateDraft.setOnClickListener(v -> updateDraft());
    }

    private void loadDraft() {
        draft = draftDao.getDraftById(draftId);

        if (draft == null) {
            Toast.makeText(this, "Draft not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtEditTitle.setText(draft.getTitle());
        edtEditContent.setText(draft.getContent());
    }

    private void updateDraft() {
        String title = edtEditTitle.getText().toString().trim();
        String content = edtEditContent.getText().toString().trim();

        if (title.isEmpty()) {
            edtEditTitle.setError("Title is required");
            return;
        }

        if (content.isEmpty()) {
            edtEditContent.setError("Content is required");
            return;
        }

        draft.setTitle(title);
        draft.setContent(content);
        draft.setSyncStatus("LOCAL");

        int result = draftDao.updateDraft(draft);

        if (result > 0) {
            Toast.makeText(this, "Draft updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}