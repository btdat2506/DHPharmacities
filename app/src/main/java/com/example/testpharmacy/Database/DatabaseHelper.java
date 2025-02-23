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
    public static final String TB_PRODUCTS = "products";
    public static final String TB_CART_ITEMS = "cart_items";

    // Common Column Names
    public static final String COLUMN_ID = "_id"; // Standard convention for primary key

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

    // Products Table - Column Names
    public static final String COLUMN_PRODUCT_ID = "product_id"; // Alias for _id if needed
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_IMAGE_URL = "image_url"; // Or use resource ID if storing locally
    public static final String COLUMN_PRODUCT_STOCK_QUANTITY = "stock_quantity";

    // Cart Items Table - Column Names
    public static final String COLUMN_CART_ITEM_ID = "cart_item_id"; // Alias for _id
    public static final String COLUMN_CART_USER_ID = "user_id"; // Foreign Key to Users table
    public static final String COLUMN_CART_PRODUCT_ID = "product_id"; // Foreign Key to Products table
    public static final String COLUMN_CART_QUANTITY = "quantity";
    public static final String COLUMN_CART_ADDED_AT = "added_at";

    public static final String INSERT_ADMIN = "INSERT INTO " + TB_USERS + " (" +
            COLUMN_USER_NAME + ", " + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PASSWORD +
            ") VALUES ('admin', 'admin@gmail.com', 'admin123')";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "HDPharmacities.db", null, 1);
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
        String tbProducts = "CREATE TABLE " + TB_PRODUCTS + "(" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PRODUCT_NAME + " TEXT," +
                COLUMN_PRODUCT_DESCRIPTION + " TEXT," +
                COLUMN_PRODUCT_CATEGORY + " TEXT," +
                COLUMN_PRODUCT_PRICE + " REAL," + // SQLite uses REAL for floating point numbers
                COLUMN_PRODUCT_IMAGE_URL + " TEXT," +
                COLUMN_PRODUCT_STOCK_QUANTITY + " INTEGER," +
                COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                COLUMN_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        String tbCartItems = "CREATE TABLE " + TB_CART_ITEMS + "(" +
                COLUMN_CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CART_USER_ID + " INTEGER," + // Foreign Key to Users
                COLUMN_CART_PRODUCT_ID + " INTEGER," + // Foreign Key to Products
                COLUMN_CART_QUANTITY + " INTEGER," +
                COLUMN_CART_ADDED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(" + COLUMN_CART_USER_ID + ") REFERENCES " + TB_USERS + "(" + COLUMN_USER_ID + ")," +
                "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TB_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")" +
                ")";

        db.execSQL(tbUsers);
        db.execSQL(tbProducts);
        db.execSQL(tbCartItems);
        // You can optionally insert initial data here if needed (e.g., default products)
        db.execSQL(INSERT_ADMIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_CART_ITEMS);
        onCreate(db);
    }

    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }
}
