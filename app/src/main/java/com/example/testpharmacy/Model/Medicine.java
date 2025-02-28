package com.example.testpharmacy.Model; // Assuming you want to put Model classes in Model folder

import android.os.Parcel;
import android.os.Parcelable;

public class Medicine implements Parcelable {
    private long productId;        // From Product.java - Maps to COLUMN_PRODUCT_ID
    private String name;           // Maps to COLUMN_PRODUCT_NAME
    private String description;    // Maps to COLUMN_PRODUCT_DESCRIPTION
    private String category;       // Maps to COLUMN_PRODUCT_CATEGORY
    private double price;          // Maps to COLUMN_PRODUCT_PRICE
    private String imageUrl;       // Maps to COLUMN_PRODUCT_IMAGE_URL
    private int stockQuantity;     // Maps to COLUMN_PRODUCT_STOCK_QUANTITY
    private String unit;         // Kept from original Medicine.java

    public Medicine() {
        // Default constructor
    }

    public Medicine(String name, double price, String unit, int imageResourceId) { // Kept original constructor
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public Medicine(String name, double price, int imageResourceId, String description, String category, String imageUrl, int stockQuantity) { // New Constructor with all fields
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
    }


    protected Medicine(Parcel in) {
        productId = in.readLong();
        name = in.readString();
        description = in.readString();
        category = in.readString();
        price = in.readDouble();
        imageUrl = in.readString();
        stockQuantity = in.readInt();
        unit = in.readString();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(productId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeDouble(price);
        dest.writeString(imageUrl);
        dest.writeInt(stockQuantity);
        dest.writeString(unit);
    }
}