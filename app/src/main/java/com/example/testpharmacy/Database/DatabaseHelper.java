package com.example.testpharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static String TB_USERS = "USERS";

    public static String TB_USER_ID = "ID";
    public static String TB_USER_MAIL = "MAIL";
    public static String TB_USER_MATKHAU = "MATKHAU";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "DrugStore.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbUser = "CREATE TABLE " + TB_USERS + " ( " + TB_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_USER_MAIL + " TEXT, " + TB_USER_MATKHAU + " TEXT )";

        db.execSQL(tbUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_USERS);
        onCreate(db);
    }

    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }

    public void addUser(String userMail, String userPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_USERS + " WHERE "
                + TB_USER_MAIL + " = ?", new String[]{userMail});

        if(cursor.getCount() > 0) {
            Toast.makeText(context, "Người dùng đã tồn tại.", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues cv = new ContentValues();

            cv.put(TB_USER_MAIL, userMail);
            cv.put(TB_USER_MATKHAU, userPass);
            long result = db.insert(TB_USERS, null, cv);
            if(result == -1) {
                Toast.makeText(context, "Signup Fail!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Signup Successful!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Boolean checkUserMail(String userMail) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_USERS + " WHERE "
                + TB_USER_MAIL + " = ?", new String[]{userMail});

        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUserMailAndMk(String userMail, String userMk) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_USERS + " WHERE "
                + TB_USER_MAIL + " = ? AND " + TB_USER_MATKHAU + " = ?", new String[]{userMail, userMk});

        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

}
