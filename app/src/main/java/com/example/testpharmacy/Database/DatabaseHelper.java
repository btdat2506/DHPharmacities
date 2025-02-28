package com.example.testpharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.testpharmacy.Model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String TB_USERS = "users";
    public static final String TB_MEDICINES = "medicines"; // Renamed table name
    public static final String TB_BILLS = "bills";
    public static final String TB_BILL_ITEMS = "bill_items";

    // Common Column Names
//    public static final String COLUMN_ID = "_id"; // Standard convention for primary key

    // Users Table - Column Names
    public static final String COLUMN_USER_ID = "user_id"; // Alias for _id if needed for clarity
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PHONE = "phone_number";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_ADDRESS = "address";
    public static final String COLUMN_USER_MEDICAL_NOTICE = "medical_notice";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    // Medicines Table - Column Names (Renamed TB_PRODUCTS to TB_MEDICINES, but keeping column names for now, adjust if needed)
    public static final String COLUMN_MEDICINE_ID = "product_id"; // Alias for _id if needed
    public static final String COLUMN_MEDICINE_NAME = "name";
    public static final String COLUMN_MEDICINE_DESCRIPTION = "description";
    public static final String COLUMN_MEDICINE_CATEGORY = "category";
    public static final String COLUMN_MEDICINE_PRICE = "price";
    public static final String COLUMN_MEDICINE_IMAGE_URL = "image_url"; // Or use resource ID if storing locally
    public static final String COLUMN_MEDICINE_STOCK_QUANTITY = "stock_quantity";
    public static final String COLUMN_MEDICINE_UNIT = "unit"; // Added unit column

    //public static final String COLUMN_USER_IS_ADMIN = "user_is_admin";


    // Bills master table
    public static final String COLUMN_BILL_ORDER_NUMBER = "order_number";
    public static final String COLUMN_BILL_USER_ID = "user_id";
    public static final String COLUMN_BILL_SHIPPING_NAME = "shipping_name";
    public static final String COLUMN_BILL_SHIPPING_PHONE = "shipping_phone";
    public static final String COLUMN_BILL_SHIPPING_ADDRESS = "shipping_address";
    public static final String COLUMN_BILL_SHIPPING_NOTE = "shipping_note";
    public static final String COLUMN_BILL_DATE = "order_date";
    public static final String COLUMN_BILL_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_BILL_STATUS = "status";

    // Bill items table
    public static final String COLUMN_BILL_ITEM_ID = "bill_item_id";
    public static final String COLUMN_BILL_ITEM_ORDER_NUMBER = "order_number";
    public static final String COLUMN_BILL_ITEM_PRODUCT_ID = "product_id";
    public static final String COLUMN_BILL_ITEM_PRODUCT_NAME = "product_name";
    public static final String COLUMN_BILL_ITEM_PRODUCT_IMAGE = "product_image";
    public static final String COLUMN_BILL_ITEM_UNIT_PRICE = "unit_price";
    public static final String COLUMN_BILL_ITEM_QUANTITY = "quantity";
    public static final String COLUMN_BILL_ITEM_TOTAL_PRICE = "total_price";

    // Add this constant to DatabaseHelper.java

    public DatabaseHelper(@Nullable Context context) {
        super(context, "HDPharmacities.db", null, 2); // Increment version from 1 to 2
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbUsers = "CREATE TABLE " + TB_USERS + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // SQLite uses INTEGER PRIMARY KEY AUTOINCREMENT
                COLUMN_USER_NAME + " TEXT," +
                COLUMN_USER_EMAIL + " TEXT UNIQUE," +
                COLUMN_USER_PHONE + " TEXT UNIQUE," +
                COLUMN_USER_PASSWORD + " TEXT," +
                COLUMN_USER_ADDRESS + " TEXT," +
                COLUMN_USER_MEDICAL_NOTICE + " TEXT," +
                COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                COLUMN_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        String tbMedicines = "CREATE TABLE " + TB_MEDICINES + "(" + // Renamed TB_PRODUCTS to TB_MEDICINES
                COLUMN_MEDICINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MEDICINE_NAME + " TEXT," +
                COLUMN_MEDICINE_DESCRIPTION + " TEXT," +
                COLUMN_MEDICINE_CATEGORY + " TEXT," +
                COLUMN_MEDICINE_PRICE + " REAL," + // SQLite uses REAL for floating point numbers
                COLUMN_MEDICINE_IMAGE_URL + " TEXT," +
                COLUMN_MEDICINE_STOCK_QUANTITY + " INTEGER," +
                COLUMN_MEDICINE_UNIT + " TEXT," + // Added unit column
                COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                COLUMN_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        String createBillsTable = "CREATE TABLE " + TB_BILLS + "(" +
                COLUMN_BILL_ORDER_NUMBER + " TEXT PRIMARY KEY," +
                COLUMN_BILL_USER_ID + " INTEGER," +
                COLUMN_BILL_SHIPPING_NAME + " TEXT," +
                COLUMN_BILL_SHIPPING_PHONE + " TEXT," +
                COLUMN_BILL_SHIPPING_ADDRESS + " TEXT," +
                COLUMN_BILL_SHIPPING_NOTE + " TEXT," +
                COLUMN_BILL_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                COLUMN_BILL_TOTAL_AMOUNT + " REAL," +
                COLUMN_BILL_STATUS + " TEXT DEFAULT 'pending'," +
                "FOREIGN KEY(" + COLUMN_BILL_USER_ID + ") REFERENCES " + TB_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        String createBillItemsTable = "CREATE TABLE " + TB_BILL_ITEMS + "(" +
                COLUMN_BILL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_BILL_ITEM_ORDER_NUMBER + " TEXT," +
                COLUMN_BILL_ITEM_PRODUCT_ID + " INTEGER," +
                COLUMN_BILL_ITEM_PRODUCT_NAME + " TEXT," +
                COLUMN_BILL_ITEM_PRODUCT_IMAGE + " TEXT," +
                COLUMN_BILL_ITEM_UNIT_PRICE + " REAL," +
                COLUMN_BILL_ITEM_QUANTITY + " INTEGER," +
                COLUMN_BILL_ITEM_TOTAL_PRICE + " REAL," +
                "FOREIGN KEY(" + COLUMN_BILL_ITEM_ORDER_NUMBER + ") REFERENCES " + TB_BILLS + "(" + COLUMN_BILL_ORDER_NUMBER + ")," +
                "FOREIGN KEY(" + COLUMN_BILL_ITEM_PRODUCT_ID + ") REFERENCES " + TB_MEDICINES + "(" + COLUMN_MEDICINE_ID + ")" +
                ")";

        db.execSQL(tbUsers);
        db.execSQL(tbMedicines);
        db.execSQL(createBillsTable);
        db.execSQL(createBillItemsTable);
        // Add default admin
        db.execSQL(Dataset.INSERT_ADMIN_1);
        db.execSQL(Dataset.INSERT_ADMIN_2);
        // Add user for test
        db.execSQL(Dataset.INSERT_USER_1);
        db.execSQL(Dataset.INSERT_USER_2);
        // Medicine for test.
        db.execSQL(Dataset.INSERT_MEDICINE_1);
        db.execSQL(Dataset.INSERT_MEDICINE_2);
        db.execSQL(Dataset.INSERT_MEDICINE_3);
        db.execSQL(Dataset.INSERT_MEDICINE_4);
        db.execSQL(Dataset.INSERT_MEDICINE_5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_MEDICINES);
        db.execSQL("DROP TABLE IF EXISTS " + TB_BILLS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_BILL_ITEMS);
        onCreate(db);
    }

    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }
}