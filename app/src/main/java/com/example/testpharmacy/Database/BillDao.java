package com.example.testpharmacy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.testpharmacy.Model.Bill;
import com.example.testpharmacy.Model.BillItem;

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

    // Create a bill with all its items
    public boolean createBill(Bill bill) {
        // Begin transaction for multiple operations
        database.beginTransaction();
        try {
            // Generate order number if not provided
            if (bill.getOrderNumber() == null || bill.getOrderNumber().isEmpty()) {
                bill.setOrderNumber(generateOrderNumber());
            }

            // Calculate the total amount if not set
            if (bill.getTotalAmount() == 0) {
                bill.calculateTotal();
            }

            // Insert the bill header
            ContentValues billValues = new ContentValues();
            billValues.put(DatabaseHelper.COLUMN_BILL_ORDER_NUMBER, bill.getOrderNumber());
            billValues.put(DatabaseHelper.COLUMN_BILL_USER_ID, bill.getUserId());
            billValues.put(DatabaseHelper.COLUMN_BILL_SHIPPING_NAME, bill.getShippingName());
            billValues.put(DatabaseHelper.COLUMN_BILL_SHIPPING_PHONE, bill.getShippingPhone());
            billValues.put(DatabaseHelper.COLUMN_BILL_SHIPPING_ADDRESS, bill.getShippingAddress());
            billValues.put(DatabaseHelper.COLUMN_BILL_SHIPPING_NOTE, bill.getShippingNote());
            billValues.put(DatabaseHelper.COLUMN_BILL_TOTAL_AMOUNT, bill.getTotalAmount());

            // Format date if needed
            if (bill.getOrderDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                billValues.put(DatabaseHelper.COLUMN_BILL_DATE, sdf.format(bill.getOrderDate()));
            }

            // Insert bill header
            long billResult = database.insert(DatabaseHelper.TB_BILLS, null, billValues);

            if (billResult == -1) {
                // Failed to insert bill header
                return false;
            }

            // Insert bill items
            for (BillItem item : bill.getBillItems()) {
                item.setOrderNumber(bill.getOrderNumber());

                ContentValues itemValues = new ContentValues();
                itemValues.put(DatabaseHelper.COLUMN_BILL_ITEM_ORDER_NUMBER, item.getOrderNumber());
                itemValues.put(DatabaseHelper.COLUMN_BILL_ITEM_PRODUCT_ID, item.getProductId());
                itemValues.put(DatabaseHelper.COLUMN_BILL_ITEM_PRODUCT_NAME, item.getProductName());
                itemValues.put(DatabaseHelper.COLUMN_BILL_ITEM_PRODUCT_IMAGE, item.getProductImage());
                itemValues.put(DatabaseHelper.COLUMN_BILL_ITEM_UNIT_PRICE, item.getUnitPrice());
                itemValues.put(DatabaseHelper.COLUMN_BILL_ITEM_QUANTITY, item.getQuantity());
                itemValues.put(DatabaseHelper.COLUMN_BILL_ITEM_TOTAL_PRICE, item.getTotalPrice());

                long itemResult = database.insert(DatabaseHelper.TB_BILL_ITEMS, null, itemValues);

                if (itemResult == -1) {
                    // Failed to insert an item
                    return false;
                }
            }

            // Everything went well, mark transaction as successful
            database.setTransactionSuccessful();
            return true;
        } finally {
            // End transaction
            database.endTransaction();
        }
    }

    // Get a bill by order number
    public Bill getBillByOrderNumber(String orderNumber) {
        // Query bill header
        Cursor billCursor = database.query(
                DatabaseHelper.TB_BILLS,
                null,
                DatabaseHelper.COLUMN_BILL_ORDER_NUMBER + " = ?",
                new String[]{orderNumber},
                null, null, null
        );

        Bill bill = null;

        if (billCursor != null && billCursor.moveToFirst()) {
            bill = cursorToBill(billCursor);
            billCursor.close();

            // Query bill items
            Cursor itemsCursor = database.query(
                    DatabaseHelper.TB_BILL_ITEMS,
                    null,
                    DatabaseHelper.COLUMN_BILL_ITEM_ORDER_NUMBER + " = ?",
                    new String[]{orderNumber},
                    null, null, null
            );

            if (itemsCursor != null && itemsCursor.moveToFirst()) {
                do {
                    BillItem item = cursorToBillItem(itemsCursor);
                    bill.addBillItem(item);
                } while (itemsCursor.moveToNext());
                itemsCursor.close();
            }
        }

        return bill;
    }

    // Get bills by user ID
    public List<Bill> getBillsByUserId(long userId) {
        List<Bill> bills = new ArrayList<>();

        // Query bills by user ID
        Cursor billCursor = database.query(
                DatabaseHelper.TB_BILLS,
                null,
                DatabaseHelper.COLUMN_BILL_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null,
                DatabaseHelper.COLUMN_BILL_DATE + " DESC" // Newest bills first
        );

        if (billCursor != null && billCursor.moveToFirst()) {
            do {
                Bill bill = cursorToBill(billCursor);

                // Query items for this bill
                Cursor itemsCursor = database.query(
                        DatabaseHelper.TB_BILL_ITEMS,
                        null,
                        DatabaseHelper.COLUMN_BILL_ITEM_ORDER_NUMBER + " = ?",
                        new String[]{bill.getOrderNumber()},
                        null, null, null
                );

                if (itemsCursor != null && itemsCursor.moveToFirst()) {
                    do {
                        BillItem item = cursorToBillItem(itemsCursor);
                        bill.addBillItem(item);
                    } while (itemsCursor.moveToNext());
                    itemsCursor.close();
                }

                bills.add(bill);
            } while (billCursor.moveToNext());
            billCursor.close();
        }

        return bills;
    }

    // Helper method to convert cursor to Bill object
    private Bill cursorToBill(Cursor cursor) {
        Bill bill = new Bill();
        bill.setOrderNumber(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ORDER_NUMBER)));
        bill.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_USER_ID)));
        bill.setShippingName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_SHIPPING_NAME)));
        bill.setShippingPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_SHIPPING_PHONE)));
        bill.setShippingAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_SHIPPING_ADDRESS)));
        bill.setShippingNote(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_SHIPPING_NOTE)));
        bill.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_TOTAL_AMOUNT)));

        // Parse date if needed
        String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_DATE));
        if (dateStr != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                bill.setOrderDate(sdf.parse(dateStr));
            } catch (Exception e) {
                bill.setOrderDate(new Date()); // Fallback to current date
            }
        }

        return bill;
    }

    // Helper method to convert cursor to BillItem object
    private BillItem cursorToBillItem(Cursor cursor) {
        BillItem item = new BillItem();
        item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ITEM_ID)));
        item.setOrderNumber(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ITEM_ORDER_NUMBER)));
        item.setProductId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ITEM_PRODUCT_ID)));
        item.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ITEM_PRODUCT_NAME)));
        item.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ITEM_PRODUCT_IMAGE)));
        item.setUnitPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ITEM_UNIT_PRICE)));
        item.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ITEM_QUANTITY)));
        item.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BILL_ITEM_TOTAL_PRICE)));
        return item;
    }

    // Generate a unique order number
    public String generateOrderNumber() {
        // Generate a unique order number combining HD- prefix with timestamp and random
        String timestamp = String.valueOf(System.currentTimeMillis() % 100000);
        String random = String.valueOf((int)(Math.random() * 1000));
        return "HD-" + timestamp + "-" + random;
    }

    // Add this to BillDao.java
    public boolean updateOrderStatus(String orderNumber, String newStatus) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BILL_STATUS, newStatus);

        int rowsAffected = database.update(
                DatabaseHelper.TB_BILLS,
                values,
                DatabaseHelper.COLUMN_BILL_ORDER_NUMBER + " = ?",
                new String[] { orderNumber }
        );

        return rowsAffected > 0;
    }

    // Also add this to BillDao.java
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.TB_BILLS,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_BILL_DATE + " DESC" // Most recent first
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Bill bill = cursorToBill(cursor);

                // Fetch bill items for this bill
                Cursor itemsCursor = database.query(
                        DatabaseHelper.TB_BILL_ITEMS,
                        null,
                        DatabaseHelper.COLUMN_BILL_ITEM_ORDER_NUMBER + " = ?",
                        new String[] { bill.getOrderNumber() },
                        null, null, null
                );

                if (itemsCursor != null && itemsCursor.moveToFirst()) {
                    do {
                        BillItem item = cursorToBillItem(itemsCursor);
                        bill.addBillItem(item);
                    } while (itemsCursor.moveToNext());
                    itemsCursor.close();
                }

                bills.add(bill);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return bills;
    }
}