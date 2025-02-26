package com.example.testpharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.example.testpharmacy.Model.Bill;

public class BillDao {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public BillDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }

    public long createBill(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CART_USER_ID, bill.getUserId());
        values.put(DatabaseHelper.COLUMN_CART_PRODUCT_ID, bill.getProductId());
        values.put(DatabaseHelper.COLUMN_CART_QUANTITY, bill.getQuantity());

        return database.insert(DatabaseHelper.TB_CART_ITEMS, null, values);
    }

    public List<Bill> getBillsByUserId(long userId) {
        List<Bill> bills = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.TB_CART_ITEMS,
                null,
                DatabaseHelper.COLUMN_CART_USER_ID + " = ?",
                new String[] { String.valueOf(userId) },
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setCartItemId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_ITEM_ID)));
                bill.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_USER_ID)));
                bill.setProductId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_PRODUCT_ID)));
                bill.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CART_QUANTITY)));
                bills.add(bill);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return bills;
    }

    public String generateOrderNumber() {
        // Generate a unique order number combining HD- prefix with timestamp and random
        String timestamp = String.valueOf(System.currentTimeMillis() % 100000);
        String random = String.valueOf((int)(Math.random() * 1000));
        return "HD-" + timestamp + "-" + random;
    }
}