package com.example.testpharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testpharmacy.Model.User;

import java.util.ArrayList;
import java.util.List;

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

    // Add to UserDao.java
    public boolean updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_USER_PHONE, user.getPhoneNumber());
        values.put(DatabaseHelper.COLUMN_USER_ADDRESS, user.getAddress());
        values.put(DatabaseHelper.COLUMN_USER_MEDICAL_NOTICE, user.getMedicalNotice());

        int rowsAffected = database.update(
                DatabaseHelper.TB_USERS,
                values,
                DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[] { String.valueOf(user.getUserId()) }
        );

        return rowsAffected > 0;
    }

    // In LoginFragment.java - add getUserIdByEmail method to UserDao.java first
    public long getUserIdByEmail(String email) {
        Cursor cursor = database.query(
                DatabaseHelper.TB_USERS,
                new String[] { DatabaseHelper.COLUMN_USER_ID },
                DatabaseHelper.COLUMN_USER_EMAIL + " = ?",
                new String[] { email },
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            long userId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            cursor.close();
            return userId;
        }
        return -1; // Not found
    }

    // Add this to UserDao.java
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.TB_USERS,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_USER_NAME + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = cursorToUser(cursor);
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return users;
    }

    // Get a number of admin
    public int getNumAdmin() {
        return DatabaseHelper.NUM_ADMIN;
    }

    public int getCustomerCount() {
        Cursor cursor = database.query(
                DatabaseHelper.TB_USERS,
                new String[] { "COUNT(*)" },
                null, // no selection condition
                null,
                null, null, null
        );

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        return count;
    }

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
