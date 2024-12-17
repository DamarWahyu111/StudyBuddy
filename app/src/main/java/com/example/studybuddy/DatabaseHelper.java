package com.example.studybuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final String TABLE_NAME = "tasks";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TITLE";
    private static final String COL_3 = "DESCRIPTION";
    private static final String COL_4 = "DEADLINE";
    private static final String COL_5 = "PRIORITY";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to store task data
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, DESCRIPTION TEXT, DEADLINE TEXT, PRIORITY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                                            //oldVersion is the database version that was being used by the application before the update.
                                                            // newVersion is the newer database version defined in the current application code.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllData() { // getAllData to retrieve all the data that will go into the HomeFragment
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    public Cursor getTaskById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tasks WHERE id = ?", new String[]{String.valueOf(id)});
    }

    // Delete tasks by ID
    public boolean deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DatabaseHelper", "Deleting ID: " + id);
        int result = db.delete(TABLE_NAME, "ID = ?", new String[]{id});

        // Check if the table is empty after deletion
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {
                // Reset autoincrement if the table is empty
                db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_NAME + "'");
                Log.d("DatabaseHelper", "Autoincrement reset for table " + TABLE_NAME);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return result > 0;
    }

    public boolean insertTask(String title, String description, String deadline, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, title);
        contentValues.put(COL_3, description);
        contentValues.put(COL_4, deadline);
        contentValues.put(COL_5, priority);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Mengembalikan true jika berhasil
    }

    public boolean updateTask(String id, String title, String description, String deadline, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", title);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("DEADLINE", deadline);
        contentValues.put("PRIORITY", priority);
        Log.d("DatabaseHelper", "Update ID: " + id);
        Log.d("DatabaseHelper", "New Title: " + title);
        Log.d("DatabaseHelper", "New Description: " + description);
        Log.d("DatabaseHelper", "New Deadline: " + deadline);
        Log.d("DatabaseHelper", "New Priority: " + priority);

        int result = db.update("tasks", contentValues, "ID=?", new String[]{id});
        return result > 0;
    }
}