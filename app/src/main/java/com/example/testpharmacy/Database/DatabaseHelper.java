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

    public static final int NUM_ADMIN = 2;
    public static final String INSERT_ADMIN_1 = "INSERT INTO " + TB_USERS + " (" +
            COLUMN_USER_NAME + ", " + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PASSWORD +
            ") VALUES ('admin1', 'admin1@gmail.com', 'admin123')";
    public static final String INSERT_ADMIN_2 = "INSERT INTO " + TB_USERS + " (" +
            COLUMN_USER_NAME + ", " + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PASSWORD +
            ") VALUES ('admin2', 'admin2@gmail.com', 'admin123')";
    public static final String INSERT_USER_1 = "INSERT INTO " + TB_USERS + " (" +
            COLUMN_USER_NAME + ", " + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PASSWORD + ", " + COLUMN_USER_PHONE +
            ") VALUES ('user1', 'user1@gmail.com', 'user123', '0357571029')";
    public static final String INSERT_USER_2 = "INSERT INTO " + TB_USERS + " (" +
            COLUMN_USER_NAME + ", " + COLUMN_USER_EMAIL + ", " + COLUMN_USER_PASSWORD + ", " + COLUMN_USER_PHONE +
            ") VALUES ('user2', 'user2@gmail.com', 'user123', '0359126807')";

    public static final String INSERT_MEDICINE_1 = "INSERT INTO " + TB_MEDICINES + " (" + // Renamed INSERT_PRODUCT_1 to INSERT_MEDICINE_1 and TB_PRODUCTS to TB_MEDICINES
            COLUMN_MEDICINE_NAME + ", " + COLUMN_MEDICINE_DESCRIPTION + ", " +
            COLUMN_MEDICINE_CATEGORY + ", " + COLUMN_MEDICINE_PRICE + ", " +
            COLUMN_MEDICINE_IMAGE_URL + ", " + COLUMN_MEDICINE_STOCK_QUANTITY + ", " + COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Hỗn dịch uống Amoxicillin 250mg Imexpharm điều trị nhiễm khuẩn đường hô hấp, đường mật, đường tiêu hóa (12 gói)', " +
            "'Thuốc Amoxicillin của Công ty Cổ phần Dược phẩm IMEXPHARM, có thành phần chính là Amoxicillin. Đây là thuốc dùng để điều trị các nhiễm khuẩn do các vi khuẩn nhạy cảm với thuốc tại các vị trí: Đường hô hấp trên, đường hô hấp dưới, nhiễm khuẩn đường mật, đường tiêu hóa, đường tiết niệu - sinh dục, dự phòng viêm nội tâm mạc.', " +
            "'Antibiotics', 32.400, 'https://cms-prod.s3-sgn09.fptcloud.com/9_528e8474cf.png', 100, 'box 12 gói')"; // Added unit value

    public static final String INSERT_MEDICINE_2 = "INSERT INTO " + TB_MEDICINES + " (" + // Renamed INSERT_PRODUCT_2 to INSERT_MEDICINE_2 and TB_PRODUCTS to TB_MEDICINES
            COLUMN_MEDICINE_NAME + ", " + COLUMN_MEDICINE_DESCRIPTION + ", " +
            COLUMN_MEDICINE_CATEGORY + ", " + COLUMN_MEDICINE_PRICE + ", " +
            COLUMN_MEDICINE_IMAGE_URL + ", " + COLUMN_MEDICINE_STOCK_QUANTITY + ", " + COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Thuốc Exopadin 60mg Trường Thọ điều trị viêm mũi dị ứng, mày đay (3 vỉ x 10 viên)', " +
            "'Thuốc Exopadin là sản phẩm của Công ty Cổ phần Dược phẩm Trường Thọ có thành phần chính là Fexofenadin hydroclorid. Đây là thuốc được chỉ định để điều trị viêm mũi dị ứng theo mùa, điều trị các biểu hiện ngoài da không biến chứng của mày đay vô căn mạn tính ở người lớn và trẻ em từ 12 tuổi trở lên. Thuốc làm giảm ngứa và số lượng dát mày đay một cách đáng kể.', " +
            "'Antibiotics', 60.000, 'https://cms-prod.s3-sgn09.fptcloud.com/IMG_0238_fc19904162.jpg', 100, 'vỉ 30 viên')"; // Added unit value

    public static final String INSERT_MEDICINE_3 = "INSERT INTO " + TB_MEDICINES + " (" +
            COLUMN_MEDICINE_NAME + ", " + COLUMN_MEDICINE_DESCRIPTION + ", " +
            COLUMN_MEDICINE_CATEGORY + ", " + COLUMN_MEDICINE_PRICE + ", " +
            COLUMN_MEDICINE_IMAGE_URL + ", " + COLUMN_MEDICINE_STOCK_QUANTITY + ", " + COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Viên nén Panactol Extra Khapharco hạ sốt, giảm đau (10 vỉ x 10 viên)', " +
            "'Thuốc Panactol extra là sản phẩm của Dược phẩm Khánh Hoà, thành phần chính là Paracetamol 500mg và Cafein 65mg, là thuốc dùng hạ sốt và giảm đau (Đau đầu, đau nửa đầu, đau bụng kinh, đau họng, đau cơ, xương, sốt và đau sau tiêm vaccin, sau nhổ răng/ phẫu thuật nha khoa, đau răng, do viêm xương khớp).', " +
            "'Pain Relievers', 60.000, 'https://cms-prod.s3-sgn09.fptcloud.com/DSC_07308_99ce483742.jpg', 100, 'hộp 10 vỉ')";

    public static final String INSERT_MEDICINE_4 = "INSERT INTO " + TB_MEDICINES + " (" +
            COLUMN_MEDICINE_NAME + ", " + COLUMN_MEDICINE_DESCRIPTION + ", " +
            COLUMN_MEDICINE_CATEGORY + ", " + COLUMN_MEDICINE_PRICE + ", " +
            COLUMN_MEDICINE_IMAGE_URL + ", " + COLUMN_MEDICINE_STOCK_QUANTITY + ", " + COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Thuốc Gofen 400 MEGA We care điều trị hạ sốt, giảm đau (5 vỉ x 10 viên)', " +
            "'Gofen 400 của Công ty Mega Lifesciences Public Company Limited, có thành phần chính ibuprofen, là một thuốc kháng viêm không steroid. Đây là thuốc dùng để điều trị hạ sốt, giảm đau do cảm cúm, đau đầu, đau răng, đau nhức cơ, đau lưng, đau nhẹ do viêm khớp, đau do bong gân, đau bụng kinh.', " +
            "'Pain Relievers', 194.000, 'https://cms-prod.s3-sgn09.fptcloud.com/00003460_gofen_400ml_2721_63db_large_627db09b10.jpg', 100, 'hộp 5 vỉ')";

    public static final String INSERT_MEDICINE_5 = "INSERT INTO " + TB_MEDICINES + " (" +
            COLUMN_MEDICINE_NAME + ", " + COLUMN_MEDICINE_DESCRIPTION + ", " +
            COLUMN_MEDICINE_CATEGORY + ", " + COLUMN_MEDICINE_PRICE + ", " +
            COLUMN_MEDICINE_IMAGE_URL + ", " + COLUMN_MEDICINE_STOCK_QUANTITY + ", " + COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Siro Multi Vitamin Hatro hỗ trợ tăng cường sức khỏe, nâng cao sức đề kháng (120ml)', " +
            "'Siro Multi Vitamin Hatro bổ sung một số vitamin và kẽm cho cơ thể. Hỗ trợ tăng cường sức khỏe, nâng cao sức đề kháng.', " +
            "'Vitamins', 150.000, 'https://cms-prod.s3-sgn09.fptcloud.com/DSC_08289_8048425d59.jpg', 100, 'hộp')";

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
        db.execSQL(INSERT_ADMIN_1);
        db.execSQL(INSERT_ADMIN_2);
        // Add user for test
        db.execSQL(INSERT_USER_1);
        db.execSQL(INSERT_USER_2);
        // Medicine for test.
        db.execSQL(INSERT_MEDICINE_1);
        db.execSQL(INSERT_MEDICINE_2);
        db.execSQL(INSERT_MEDICINE_3);
        db.execSQL(INSERT_MEDICINE_4);
        db.execSQL(INSERT_MEDICINE_5);
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