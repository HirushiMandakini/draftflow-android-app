package com.mandakini.draftflow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DraftDbHelper extends SQLiteOpenHelper {

    // Database Info
    public static final String DATABASE_NAME = "draftflow.db";
    public static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_DRAFTS = "drafts";

    // Column Names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_SYNC_STATUS = "sync_status";

    // Create Table SQL
    private static final String CREATE_TABLE_DRAFTS =
            "CREATE TABLE " + TABLE_DRAFTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_CONTENT + " TEXT NOT NULL, " +
                    COLUMN_IMAGE_PATH + " TEXT, " +
                    COLUMN_CREATED_AT + " TEXT NOT NULL, " +
                    COLUMN_UPDATED_AT + " TEXT NOT NULL, " +
                    COLUMN_SYNC_STATUS + " TEXT DEFAULT 'LOCAL'" +
                    ");";

    public DraftDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DRAFTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRAFTS);
        onCreate(db);
    }
}