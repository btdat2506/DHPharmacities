## Database Structure Overview

The database is managed through a `DatabaseHelper` class which extends `SQLiteOpenHelper` to handle database creation, upgrades, and basic operations. The database is named "HDPharmacities.db" and has four main tables:

### Table

1. users - Stores user account information.
2. medicines - Contains details about pharmaceutical products.
3. bills - Stores order/transaction information.
4. bill_items - Contains items within each order/bill.

### Database Access Objects (DAOs)

The app implements the DAO pattern with separate classes for each entity:

- UserDao - Handles user data operations.
- MedicineDao - Manages medicine (product) operations.
- BillDao - Handles order/billing operations.

## Table Schemas

### 1. users Table

	CREATE TABLE users (
	    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
	    name TEXT,
	    email TEXT UNIQUE,
	    phone_number TEXT UNIQUE,
	    password TEXT,
	    address TEXT,
	    medical_notice TEXT,
	    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	)

### 2. medicines Table

	CREATE TABLE medicines (
	    product_id INTEGER PRIMARY KEY AUTOINCREMENT,
	    name TEXT,
	    description TEXT,
	    category TEXT,
	    price REAL,
	    image_url TEXT,
	    stock_quantity INTEGER,
	    unit TEXT,
	    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	)

### 3. bill Table

	CREATE TABLE bills (
	    order_number TEXT PRIMARY KEY,
	    user_id INTEGER,
	    shipping_name TEXT,
	    shipping_phone TEXT,
	    shipping_address TEXT,
	    shipping_note TEXT,
	    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	    total_amount REAL,
	    status TEXT DEFAULT 'pending',
	    FOREIGN KEY(user_id) REFERENCES users(user_id)
	)

### 4. bill_items Table

	CREATE TABLE bill_items (
	    bill_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
	    order_number TEXT,
	    product_id INTEGER,
	    product_name TEXT,
	    product_image TEXT,
	    unit_price REAL,
	    quantity INTEGER,
	    total_price REAL,
	    FOREIGN KEY(order_number) REFERENCES bills(order_number),
	    FOREIGN KEY(product_id) REFERENCES medicines(product_id)
	)

## Entity Relationships

1. **User-Bill Relationship** (One-to-Many)
- A user can have multiple bills/.
- Each bill belongs to one user.
- Foreign key: `bills.user_id` references `users.user_id`.

2. **Bill-BillItem Relationship** (One-to-Many)
- A bill can have multiple bill items.
- Each bill item belongs to one bill.
- Foreign key: `bill_items.order_number` references `bills.order_number`.


3. **Medicine-BillItem Relationship** (One-to-Many)
- A medicine can appear in multiple bill items (across different orders).
- Each bill item refers to one medicine product.
- Foreign key: `bill_items.product_id` references `medicines.product_id`.

## Data Models

The application uses corresponding model classes to represent each entity:
1. **User** - Represents a user account with personal details.
2. **Medicine** - Represents pharmaceutical products.
3. **Bill** - Represents an order with shipping details and status.
4. **BillItem** - Represents individual items within an order.

## Additional Notes

1. The database includes a special handling for admin accounts:
- The first two users (user_id 1 and 2) are considered admins.
- A constant `NUM_ADMIN = 2` in `Dataset` class is used to identify admin users.

2. The application includes predefined sample data:
- Two admin users.
- Two regular users.
- Five sample medicines across different categories.

3. Order status follows a predefined workflow:\
   pending → processing → shipping → delivered/cancelled

4. The database implementation includes proper transaction handling, especially in the `BillDao` class when creating bills with multiple items.

This database structure effectively supports the pharmacy application's core functionalities: user management, product catalog, shopping cart, and order processing.