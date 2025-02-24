package com.example.testpharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testpharmacy.Medicine; // Changed import to Medicine

import java.util.ArrayList;
import java.util.List;

public class MedicineDao { // Renamed class to MedicineDao
    private DatabaseHelper databaseHelper;

    public MedicineDao(Context context) { databaseHelper = new DatabaseHelper(context); }

    public void open() {
        databaseHelper.open();
    }

    public void close() {
        databaseHelper.close();
    }

    public void insert(Medicine medicine) { // Changed parameter type to Medicine
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, medicine.getName()); // **Use YOUR constants**
        values.put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION, medicine.getDescription()); // Use description from Medicine
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY, medicine.getCategory()); // Use category from Medicine
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, medicine.getPrice());
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URL, medicine.getImageUrl());
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK_QUANTITY, medicine.getStockQuantity());

        db.insert(DatabaseHelper.TB_MEDICINES, null, values); // **Use YOUR table name**
        db.close();
    }

    public void update(Medicine medicine) { // Changed parameter type to Medicine
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, medicine.getName());
        values.put(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION, medicine.getDescription()); // Use description from Medicine
        values.put(DatabaseHelper.COLUMN_PRODUCT_CATEGORY, medicine.getCategory()); // Use category from Medicine
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, medicine.getPrice());
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URL, medicine.getImageUrl());
        values.put(DatabaseHelper.COLUMN_PRODUCT_STOCK_QUANTITY, medicine.getStockQuantity());

        db.update(DatabaseHelper.TB_MEDICINES, values, DatabaseHelper.COLUMN_PRODUCT_ID + " = ?", // **Use YOUR constants**
                new String[]{String.valueOf(medicine.getProductId())});
        db.close();
    }

    public void delete(Medicine medicine) { // Changed parameter type to Medicine
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TB_MEDICINES, DatabaseHelper.COLUMN_PRODUCT_ID + " = ?", // **Use YOUR constants**
                new String[]{String.valueOf(medicine.getProductId())});
        db.close();
    }

    public void deleteAllMedicines() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TB_MEDICINES, null, null); // Delete all rows // **Use YOUR table name**
        db.close();
    }

    public Medicine getMedicineById(int id) { // Return type changed to Medicine
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_MEDICINES, // **Use YOUR table name**
                null, // All columns
                DatabaseHelper.COLUMN_PRODUCT_ID + " = ?", // **Use YOUR constants**
                new String[]{String.valueOf(id)},
                null, null, null);

        Medicine medicine = null; // Changed to Medicine
        if (cursor != null && cursor.moveToFirst()) {
            medicine = cursorToMedicine(cursor); // Changed to cursorToMedicine
            cursor.close();
        }
        db.close();
        return medicine;
    }

    public List<Medicine> getAllMedicines() { // Return type changed to List<Medicine>
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TB_MEDICINES, // **Use YOUR table name**
                null, null, null, null, null,
                DatabaseHelper.COLUMN_PRODUCT_NAME + " ASC"); // Order by name // **Use YOUR constants**

        List<Medicine> medicineList = new ArrayList<>(); // Changed to List<Medicine>
        if (cursor != null && cursor.moveToFirst()) {
            do {
                medicineList.add(cursorToMedicine(cursor)); // Changed to cursorToMedicine
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return medicineList;
    }

    private Medicine cursorToMedicine(Cursor cursor) { // Renamed method to cursorToMedicine
        Medicine medicine = new Medicine(); // Changed to Medicine
        medicine.setProductId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID)));
        medicine.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME)));
        medicine.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_DESCRIPTION)));
        medicine.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_CATEGORY)));
        medicine.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE)));
        medicine.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URL)));
        medicine.setStockQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_STOCK_QUANTITY)));
        return medicine;
    }
}