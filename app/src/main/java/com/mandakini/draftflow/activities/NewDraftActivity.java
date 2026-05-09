package com.mandakini.draftflow.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mandakini.draftflow.R;
import com.mandakini.draftflow.database.DraftDao;
import com.mandakini.draftflow.models.Draft;

public class NewDraftActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1001;

    private TextView btnBack;
    private TextView btnSave;
    private EditText edtTitle;
    private EditText edtContent;
    private Button btnAttachImage;
    private Button btnPost;
    private ImageView imgPreview;

    private DraftDao draftDao;
    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_draft);

        // Initialize DAO
        draftDao = new DraftDao(this);

        // Bind Views
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        btnAttachImage = findViewById(R.id.btnAttachImage);
        btnPost = findViewById(R.id.btnPost);
        imgPreview = findViewById(R.id.imgPreview);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Save button
        btnSave.setOnClickListener(v -> saveDraft());

        // Post button
        btnPost.setOnClickListener(v -> saveDraft());

        // Attach image button
        btnAttachImage.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {

            Uri imageUri = data.getData();

            // Persist URI permission so image can be accessed later
            final int takeFlags =
                    data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;

            try {
                getContentResolver()
                        .takePersistableUriPermission(imageUri, takeFlags);
            } catch (Exception ignored) {
            }

            // Save URI as string
            selectedImagePath = imageUri.toString();

            // Preview selected image
            imgPreview.setImageURI(imageUri);
            imgPreview.setVisibility(View.VISIBLE);
        }
    }

    private void saveDraft() {
        String title = edtTitle.getText().toString().trim();
        String content = edtContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(
                    this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Draft draft = new Draft();
        draft.setTitle(title);
        draft.setContent(content);
        draft.setImagePath(selectedImagePath);

        long result = draftDao.insertDraft(draft);

        if (result > 0) {
            Toast.makeText(
                    this,
                    "Draft saved successfully",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
        } else {
            Toast.makeText(
                    this,
                    "Failed to save draft",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}