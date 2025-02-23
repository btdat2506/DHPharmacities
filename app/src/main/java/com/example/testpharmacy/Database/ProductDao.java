package com.example.testpharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testpharmacy.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private DatabaseHelper databaseHelper;

    public ProductDao(Context context) { databaseHelper = new DatabaseHelper(context); }

    public void insert(Product product) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, product.getName()); // **Use YOUR constants**
        values.put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION, ""); // Assuming description is empty in Medicine class
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY, ""); // Assuming category is empty in Medicine class
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URL, product.getImageUrl()); // Store resource ID as string
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK_QUANTITY, 100); // Example stock quantity

        db.insert(DatabaseHelper.TB_PRODUCTS, null, values); // **Use YOUR table name**
        db.close();
    }

    public void update(Product product) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, product.getName());
        values.put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION, ""); // Assuming description is empty
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY, ""); // Assuming category is empty
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URL, product.getImageUrl()); // Store resource ID as string
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK_QUANTITY, 100); // Example stock quantity

        db.update(DatabaseHelper.TB_PRODUCTS, values, DatabaseHelper.COLUMN_PRODUCT_ID + " = ?", // **Use YOUR constants**
                new String[]{String.valueOf(product.getProductId())});
        db.close();
    }

    public void delete(Product product) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TB_PRODUCTS, DatabaseHelper.COLUMN_PRODUCT_ID + " = ?", // **Use YOUR constants**
                new String[]{String.valueOf(product.getProductId())});
        db.close();
    }

    public void deleteAllMedicines() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TB_PRODUCTS, null, null); // Delete all rows // **Use YOUR table name**
        db.close();
    }

    public Product getMedicineById(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_PRODUCTS, // **Use YOUR table name**
                null, // All columns
                DatabaseHelper.COLUMN_PRODUCT_ID + " = ?", // **Use YOUR constants**
                new String[]{String.valueOf(id)},
                null, null, null);

        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = cursorToProduct(cursor);
            cursor.close();
        }
        db.close();
        return product;
    }

    public List<Product> getAllMedicines() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_PRODUCTS, // **Use YOUR table name**
                null, null, null, null, null,
                DatabaseHelper.COLUMN_PRODUCT_NAME + " ASC"); // Order by name // **Use YOUR constants**

        List<Product> medicineList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                medicineList.add(cursorToProduct(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return medicineList;
    }

    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.setProductId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)));
        product.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME)));
        product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION)));
        product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORY)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE)));
        product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URL)));
        product.setStockQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STOCK_QUANTITY)));
        return product;
    }
}
