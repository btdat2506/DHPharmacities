package com.example.testpharmacy.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bill {
    private String orderNumber;
    private long userId;
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String shippingNote;
    private Date orderDate;
    private double totalAmount;
    private List<BillItem> billItems;

    public Bill() {
        billItems = new ArrayList<>();
        orderDate = new Date(); // Set current date by default
    }

    // Getters and setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingNote() {
        return shippingNote;
    }

    public void setShippingNote(String shippingNote) {
        this.shippingNote = shippingNote;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    public void addBillItem(BillItem item) {
        if (billItems == null) {
            billItems = new ArrayList<>();
        }
        billItems.add(item);
    }

    // Calculate total based on all bill items
    public void calculateTotal() {
        double total = 0;
        if (billItems != null) {
            for (BillItem item : billItems) {
                total += item.getTotalPrice();
            }
        }
        this.totalAmount = total;
    }
}