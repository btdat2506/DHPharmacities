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

    public long insert(Medicine medicine) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MEDICINE_NAME, medicine.getName());
        values.put(DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION, medicine.getDescription());
        values.put(DatabaseHelper.COLUMN_MEDICINE_CATEGORY, medicine.getCategory());
        values.put(DatabaseHelper.COLUMN_MEDICINE_PRICE, medicine.getPrice());
        values.put(DatabaseHelper.COLUMN_MEDICINE_IMAGE_URL, medicine.getImageUrl());
        values.put(DatabaseHelper.COLUMN_MEDICINE_STOCK_QUANTITY, medicine.getStockQuantity());
        values.put(DatabaseHelper.COLUMN_MEDICINE_UNIT, medicine.getUnit());

        long result = db.insert(DatabaseHelper.TB_MEDICINES, null, values);
        db.close();
        return result;
    }

    public boolean update(Medicine medicine) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MEDICINE_NAME, medicine.getName());
        values.put(DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION, medicine.getDescription());
        values.put(DatabaseHelper.COLUMN_MEDICINE_CATEGORY, medicine.getCategory());
        values.put(DatabaseHelper.COLUMN_MEDICINE_PRICE, medicine.getPrice());
        values.put(DatabaseHelper.COLUMN_MEDICINE_IMAGE_URL, medicine.getImageUrl());
        values.put(DatabaseHelper.COLUMN_MEDICINE_STOCK_QUANTITY, medicine.getStockQuantity());
        values.put(DatabaseHelper.COLUMN_MEDICINE_UNIT, medicine.getUnit());

        int rowsAffected = db.update(DatabaseHelper.TB_MEDICINES, values,
                DatabaseHelper.COLUMN_MEDICINE_ID + " = ?",
                new String[]{String.valueOf(medicine.getProductId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean delete(Medicine medicine) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsAffected = db.delete(DatabaseHelper.TB_MEDICINES,
                DatabaseHelper.COLUMN_MEDICINE_ID + " = ?",
                new String[]{String.valueOf(medicine.getProductId())});
        db.close();
        return rowsAffected > 0;
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
                DatabaseHelper.COLUMN_MEDICINE_ID + " = ?", // **Use YOUR constants**
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
                DatabaseHelper.COLUMN_MEDICINE_NAME + " ASC"); // Order by name // **Use YOUR constants**

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

    public List<Medicine> getMedicinesByCategory(String category) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = DatabaseHelper.COLUMN_MEDICINE_CATEGORY + " = ?"; // Cột category trong bảng của bạn
        String[] selectionArgs = {category}; // Tham số category

        Cursor cursor = db.query(DatabaseHelper.TB_MEDICINES, // Tên bảng
                null, selection, selectionArgs, null, null, // Thêm điều kiện lọc theo category
                DatabaseHelper.COLUMN_MEDICINE_NAME + " ASC"); // Sắp xếp theo tên thuốc

        List<Medicine> medicineList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                medicineList.add(cursorToMedicine(cursor)); // Chuyển cursor thành đối tượng Medicine
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return medicineList;
    }

    private Medicine cursorToMedicine(Cursor cursor) { // Renamed method to cursorToMedicine
        Medicine medicine = new Medicine(); // Changed to Medicine
        medicine.setProductId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICINE_ID)));
        medicine.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICINE_NAME)));
        medicine.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION)));
        medicine.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICINE_CATEGORY)));
        medicine.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICINE_PRICE)));
        medicine.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICINE_IMAGE_URL)));
        medicine.setStockQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICINE_STOCK_QUANTITY)));
        return medicine;
    }
}