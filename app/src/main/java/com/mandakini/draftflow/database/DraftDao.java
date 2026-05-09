package com.mandakini.draftflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mandakini.draftflow.models.Draft;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DraftDao {

    private final DraftDbHelper dbHelper;

    public DraftDao(Context context) {
        dbHelper = new DraftDbHelper(context);
    }

    /**
     * Get current date/time as string
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * Insert new draft
     */
    public long insertDraft(Draft draft) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String now = getCurrentTimestamp();

        if (draft.getCreatedAt() == null || draft.getCreatedAt().isEmpty()) {
            draft.setCreatedAt(now);
        }

        if (draft.getUpdatedAt() == null || draft.getUpdatedAt().isEmpty()) {
            draft.setUpdatedAt(now);
        }

        if (draft.getSyncStatus() == null || draft.getSyncStatus().isEmpty()) {
            draft.setSyncStatus("LOCAL");
        }

        ContentValues values = new ContentValues();
        values.put(DraftDbHelper.COLUMN_TITLE, draft.getTitle());
        values.put(DraftDbHelper.COLUMN_CONTENT, draft.getContent());
        values.put(DraftDbHelper.COLUMN_IMAGE_PATH, draft.getImagePath());
        values.put(DraftDbHelper.COLUMN_CREATED_AT, draft.getCreatedAt());
        values.put(DraftDbHelper.COLUMN_UPDATED_AT, draft.getUpdatedAt());
        values.put(DraftDbHelper.COLUMN_SYNC_STATUS, draft.getSyncStatus());

        long id = db.insert(DraftDbHelper.TABLE_DRAFTS, null, values);
        db.close();

        return id;
    }

    /**
     * Get all drafts (latest first)
     */
    public List<Draft> getAllDrafts() {
        List<Draft> drafts = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DraftDbHelper.TABLE_DRAFTS,
                null,
                null,
                null,
                null,
                null,
                DraftDbHelper.COLUMN_UPDATED_AT + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Draft draft = mapCursorToDraft(cursor);
                drafts.add(draft);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return drafts;
    }

    /**
     * Get draft by ID
     */
    public Draft getDraftById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DraftDbHelper.TABLE_DRAFTS,
                null,
                DraftDbHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        Draft draft = null;

        if (cursor != null && cursor.moveToFirst()) {
            draft = mapCursorToDraft(cursor);
            cursor.close();
        }

        db.close();
        return draft;
    }

    /**
     * Update existing draft
     */
    public int updateDraft(Draft draft) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DraftDbHelper.COLUMN_TITLE, draft.getTitle());
        values.put(DraftDbHelper.COLUMN_CONTENT, draft.getContent());
        values.put(DraftDbHelper.COLUMN_IMAGE_PATH, draft.getImagePath());
        values.put(DraftDbHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());
        values.put(DraftDbHelper.COLUMN_SYNC_STATUS, draft.getSyncStatus());

        int rows = db.update(
                DraftDbHelper.TABLE_DRAFTS,
                values,
                DraftDbHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(draft.getId())}
        );

        db.close();
        return rows;
    }

    /**
     * Delete one draft
     */
    public int deleteDraft(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(
                DraftDbHelper.TABLE_DRAFTS,
                DraftDbHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return rows;
    }

    /**
     * Search drafts by title or content
     */
    public List<Draft> searchDrafts(String query) {
        List<Draft> drafts = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String where =
                DraftDbHelper.COLUMN_TITLE + " LIKE ? OR " +
                        DraftDbHelper.COLUMN_CONTENT + " LIKE ?";

        String keyword = "%" + query + "%";

        Cursor cursor = db.query(
                DraftDbHelper.TABLE_DRAFTS,
                null,
                where,
                new String[]{keyword, keyword},
                null,
                null,
                DraftDbHelper.COLUMN_UPDATED_AT + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Draft draft = mapCursorToDraft(cursor);
                drafts.add(draft);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return drafts;
    }

    /**
     * Delete multiple drafts
     */
    public int deleteMultipleDrafts(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        StringBuilder placeholders = new StringBuilder();
        String[] args = new String[ids.size()];

        for (int i = 0; i < ids.size(); i++) {
            placeholders.append("?");
            if (i < ids.size() - 1) {
                placeholders.append(",");
            }
            args[i] = String.valueOf(ids.get(i));
        }

        int rows = db.delete(
                DraftDbHelper.TABLE_DRAFTS,
                DraftDbHelper.COLUMN_ID + " IN (" + placeholders + ")",
                args
        );

        db.close();
        return rows;
    }

    /**
     * Get total number of drafts
     */
    public int getDraftCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + DraftDbHelper.TABLE_DRAFTS,
                null
        );

        int count = 0;

        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        db.close();
        return count;
    }

    /**
     * Convert Cursor row to Draft object
     */
    private Draft mapCursorToDraft(Cursor cursor) {
        Draft draft = new Draft();

        draft.setId(
                cursor.getInt(
                        cursor.getColumnIndexOrThrow(DraftDbHelper.COLUMN_ID)
                )
        );

        draft.setTitle(
                cursor.getString(
                        cursor.getColumnIndexOrThrow(DraftDbHelper.COLUMN_TITLE)
                )
        );

        draft.setContent(
                cursor.getString(
                        cursor.getColumnIndexOrThrow(DraftDbHelper.COLUMN_CONTENT)
                )
        );

        draft.setImagePath(
                cursor.getString(
                        cursor.getColumnIndexOrThrow(DraftDbHelper.COLUMN_IMAGE_PATH)
                )
        );

        draft.setCreatedAt(
                cursor.getString(
                        cursor.getColumnIndexOrThrow(DraftDbHelper.COLUMN_CREATED_AT)
                )
        );

        draft.setUpdatedAt(
                cursor.getString(
                        cursor.getColumnIndexOrThrow(DraftDbHelper.COLUMN_UPDATED_AT)
                )
        );

        draft.setSyncStatus(
                cursor.getString(
                        cursor.getColumnIndexOrThrow(DraftDbHelper.COLUMN_SYNC_STATUS)
                )
        );

        return draft;
    }
}