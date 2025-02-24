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

    // Medicines Table - Column Names (Renamed TB_PRODUCTS to TB_MEDICINES, but keeping column names for now, adjust if needed)
    public static final String COLUMN_PRODUCT_ID = "product_id"; // Alias for _id if needed
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_IMAGE_URL = "image_url"; // Or use resource ID if storing locally
    public static final String COLUMN_PRODUCT_STOCK_QUANTITY = "stock_quantity";
    public static final String COLUMN_PRODUCT_UNIT = "unit"; // Added unit column

    // Cart Items Table - Column Names
    public static final String COLUMN_CART_ITEM_ID = "cart_item_id"; // Alias for _id
    public static final String COLUMN_CART_USER_ID = "user_id"; // Foreign Key to Users table
    public static final String COLUMN_CART_PRODUCT_ID = "product_id"; // Foreign Key to Medicines table (Renamed from Products)
    public static final String COLUMN_CART_QUANTITY = "quantity";
    public static final String COLUMN_CART_ADDED_AT = "added_at";

    public static final String INSERT_ADMIN = "INSERT INTO " + TB_USERS + " (" +
            COLUMN_USER_NAME + ", " + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PASSWORD +
            ") VALUES ('admin', 'admin@gmail.com', 'admin123')";

    public static final String INSERT_MEDICINE_1 = "INSERT INTO " + TB_MEDICINES + " (" + // Renamed INSERT_PRODUCT_1 to INSERT_MEDICINE_1 and TB_PRODUCTS to TB_MEDICINES
            COLUMN_PRODUCT_NAME + ", " + COLUMN_PRODUCT_DESCRIPTION + ", " +
            COLUMN_PRODUCT_CATEGORY + ", " + COLUMN_PRODUCT_PRICE + ", " +
            COLUMN_PRODUCT_IMAGE_URL + ", " + COLUMN_PRODUCT_STOCK_QUANTITY + ", " + COLUMN_PRODUCT_UNIT + // Added unit column to insert
            ") VALUES ('Hỗn dịch uống Amoxicillin 250mg Imexpharm điều trị nhiễm khuẩn đường hô hấp, đường mật, đường tiêu hóa (12 gói)', " +
            "'Thuốc Amoxicillin của Công ty Cổ phần Dược phẩm IMEXPHARM, có thành phần chính là Amoxicillin. Đây là thuốc dùng để điều trị các nhiễm khuẩn do các vi khuẩn nhạy cảm với thuốc tại các vị trí: Đường hô hấp trên, đường hô hấp dưới, nhiễm khuẩn đường mật, đường tiêu hóa, đường tiết niệu - sinh dục, dự phòng viêm nội tâm mạc.', " +
            "'Thuốc kháng sinh', 32.400, 'https://cdn.nhathuoclongchau.com.vn/unsafe/768x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/9_528e8474cf.png', 100, 'box 12 gói')"; // Added unit value

    public static final String INSERT_MEDICINE_2 = "INSERT INTO " + TB_MEDICINES + " (" + // Renamed INSERT_PRODUCT_2 to INSERT_MEDICINE_2 and TB_PRODUCTS to TB_MEDICINES
            COLUMN_PRODUCT_NAME + ", " + COLUMN_PRODUCT_DESCRIPTION + ", " +
            COLUMN_PRODUCT_CATEGORY + ", " + COLUMN_PRODUCT_PRICE + ", " +
            COLUMN_PRODUCT_IMAGE_URL + ", " + COLUMN_PRODUCT_STOCK_QUANTITY + ", " + COLUMN_PRODUCT_UNIT + // Added unit column to insert
            ") VALUES ('Thuốc Exopadin 60mg Trường Thọ điều trị viêm mũi dị ứng, mày đay (3 vỉ x 10 viên)', " +
            "'Thuốc Exopadin là sản phẩm của Công ty Cổ phần Dược phẩm Trường Thọ có thành phần chính là Fexofenadin hydroclorid. Đây là thuốc được chỉ định để điều trị viêm mũi dị ứng theo mùa, điều trị các biểu hiện ngoài da không biến chứng của mày đay vô căn mạn tính ở người lớn và trẻ em từ 12 tuổi trở lên. Thuốc làm giảm ngứa và số lượng dát mày đay một cách đáng kể.', " +
            "'Thuốc dị ứng', 60.000, 'https://cdn.nhathuoclongchau.com.vn/unsafe/768x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/IMG_0238_fc19904162.jpg', 100, 'vỉ 30 viên')"; // Added unit value


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
        String tbMedicines = "CREATE TABLE " + TB_MEDICINES + "(" + // Renamed TB_PRODUCTS to TB_MEDICINES
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PRODUCT_NAME + " TEXT," +
                COLUMN_PRODUCT_DESCRIPTION + " TEXT," +
                COLUMN_PRODUCT_CATEGORY + " TEXT," +
                COLUMN_PRODUCT_PRICE + " REAL," + // SQLite uses REAL for floating point numbers
                COLUMN_PRODUCT_IMAGE_URL + " TEXT," +
                COLUMN_PRODUCT_STOCK_QUANTITY + " INTEGER," +
                COLUMN_PRODUCT_UNIT + " TEXT," + // Added unit column
                COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                COLUMN_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        String tbCartItems = "CREATE TABLE " + TB_CART_ITEMS + "(" +
                COLUMN_CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CART_USER_ID + " INTEGER," + // Foreign Key to Users
                COLUMN_CART_PRODUCT_ID + " INTEGER," + // Foreign Key to Medicines (Renamed from Products)
                COLUMN_CART_QUANTITY + " INTEGER," +
                COLUMN_CART_ADDED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(" + COLUMN_CART_USER_ID + ") REFERENCES " + TB_USERS + "(" + COLUMN_USER_ID + ")," +
                "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TB_MEDICINES + "(" + COLUMN_PRODUCT_ID + ")" + // Renamed TB_PRODUCTS to TB_MEDICINES
                ")";

        db.execSQL(tbUsers);
        db.execSQL(tbMedicines); // Renamed tbProducts to tbMedicines
        db.execSQL(tbCartItems);
        // You can optionally insert initial data here if needed (e.g., default medicines)
        db.execSQL(INSERT_ADMIN);
        // Medicine for test.
        db.execSQL(INSERT_MEDICINE_1); // Renamed INSERT_PRODUCT_1 to INSERT_MEDICINE_1
        db.execSQL(INSERT_MEDICINE_2); // Renamed INSERT_PRODUCT_2 to INSERT_MEDICINE_2
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_MEDICINES); // Renamed TB_PRODUCTS to TB_MEDICINES
        db.execSQL("DROP TABLE IF EXISTS " + TB_CART_ITEMS);
        onCreate(db);
    }

    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }
}