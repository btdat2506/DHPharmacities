package com.example.testpharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testpharmacy.Model.User;

public class UserDao {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public UserDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }

    public long createUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_PHONE, user.getPhoneNumber());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_USER_ADDRESS, user.getAddress());
        values.put(DatabaseHelper.COLUMN_USER_MEDICAL_NOTICE, user.getMedicalNotice());

        return database.insert(DatabaseHelper.TB_USERS, null, values); // Returns row ID of the new row, or -1 if error
    }

    public User getUserById(long userId) {
        Cursor cursor = database.query(DatabaseHelper.TB_USERS,
                null, // all columns
                DatabaseHelper.COLUMN_USER_ID + " = ?", // selection
                new String[]{String.valueOf(userId)}, // selectionArgs
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = cursorToUser(cursor);
            cursor.close();
            return user;
        }
        return null; // User not found
    }

    public Boolean checkUserMail(String mail) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TB_USERS + " WHERE "
                + DatabaseHelper.COLUMN_USER_EMAIL + " = ?", new String[]{mail});

        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUserMailAndMk(String mail, String password) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TB_USERS + " WHERE "
                + DatabaseHelper.COLUMN_USER_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?", new String[]{mail, password});

        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    // ... (Implement other CRUD operations like updateUser, deleteUser, getUserByEmail, etc.)

    // Helper method to convert Cursor to User object
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
        user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE)));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
        user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ADDRESS)));
        user.setMedicalNotice(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_MEDICAL_NOTICE)));
        return user;
    }
}
