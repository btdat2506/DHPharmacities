# DAO and Model Classes in the Pharmacy Application

## DAO Classes
These classes handle database operations for their respective entities, providing an abstraction layer between the database and business logic.

### 1. UserDao
#### Attributes:
- `databaseHelper`: Reference to the `DatabaseHelper` instance.
- `database`: Reference to the `SQLiteDatabase` instance.

#### Methods:
- `open()`: Opens a database connection.
- `close()`: Closes the database connection.
- `createUser(User user)`: Creates a new user record, returns row ID.
- `getUserById(long userId)`: Retrieves a user by ID.
- `checkUserMail(String mail)`: Checks if an email exists in the database.
- `checkUserMailAndMk(String mail, String password)`: Validates email and password combination.
- `updateUser(User user)`: Updates user information.
- `getUserIdByEmail(String email)`: Gets user ID from email.
- `getAllUsers()`: Retrieves all users.
- `getNumAdmin()`: Gets the number of admin users.
- `getCustomerCount()`: Counts the number of non-admin users.
- `cursorToUser(Cursor cursor)`: Helper method to convert cursor to `User` object.

### 2. MedicineDao
#### Attributes:
- `databaseHelper`: Reference to the `DatabaseHelper` instance.

#### Methods:
- `open()`: Opens a database connection.
- `close()`: Closes the database connection.
- `insert(Medicine medicine)`: Inserts a new medicine record.
- `update(Medicine medicine)`: Updates medicine information.
- `delete(Medicine medicine)`: Deletes a medicine record.
- `deleteAllMedicines()`: Deletes all medicine records.
- `getMedicineById(int id)`: Retrieves a medicine by ID.
- `getAllMedicines()`: Gets all medicines.
- `getMedicinesByCategory(String category)`: Filters medicines by category.
- `cursorToMedicine(Cursor cursor)`: Helper method to convert cursor to `Medicine` object.

### 3. BillDao
#### Attributes:
- `databaseHelper`: Reference to the `DatabaseHelper` instance.
- `database`: Reference to the `SQLiteDatabase` instance.

#### Methods:
- `open()`: Opens a database connection.
- `close()`: Closes the database connection.
- `createBill(Bill bill)`: Creates a bill with all its items using a transaction.
- `getBillByOrderNumber(String orderNumber)`: Retrieves bill by order number.
- `getBillsByUserId(long userId)`: Gets all bills for a specific user.
- `updateOrderStatus(String orderNumber, String newStatus)`: Updates the status of an order.
- `getAllBills()`: Retrieves all bills.
- `generateOrderNumber()`: Generates a unique order number.
- `cursorToBill(Cursor cursor)`: Helper method to convert cursor to `Bill` object.
- `cursorToBillItem(Cursor cursor)`: Helper method to convert cursor to `BillItem` object.

## Model Classes
These classes represent the entities in the database and contain business logic related to those entities.

### 1. User
#### Attributes:
- `userId`: Unique identifier for the user.
- `name`: User's name.
- `email`: User's email address (unique).
- `phoneNumber`: User's phone number (unique).
- `password`: User's password.
- `address`: User's address.
- `medicalNotice`: User's medical information/notes.
- `isAdmin`: Boolean flag indicating admin status.

#### Methods:
- Getters and setters for all attributes.
- `isAdmin()`: Returns whether the user is an admin.

### 2. Medicine
#### Attributes:
- `productId`: Unique identifier for the medicine.
- `name`: Medicine name.
- `description`: Detailed description.
- `category`: Medicine category (e.g., "Pain Relievers", "Antibiotics").
- `price`: Medicine price.
- `imageUrl`: URL to medicine image.
- `stockQuantity`: Available quantity in stock.
- `unit`: Unit of measurement (e.g., "box", "bottle", "vial").

#### Methods:
- Getters and setters for all attributes.
- Implements `Parcelable` interface methods for passing medicine objects between activities.

### 3. Bill
#### Attributes:
- `orderNumber`: Unique identifier for the bill (format: "HD-timestamp-random").
- `userId`: ID of the user who placed the order.
- `shippingName`: Name for shipping.
- `shippingPhone`: Phone number for shipping.
- `shippingAddress`: Shipping address.
- `shippingNote`: Additional notes for shipping.
- `orderDate`: Date and time of the order.
- `totalAmount`: Total amount of the order.
- `status`: Order status (pending, processing, shipping, delivered, cancelled).
- `billItems`: List of items in the bill.

#### Constants:
- `STATUS_PENDING`, `STATUS_PROCESSING`, `STATUS_SHIPPING`, `STATUS_DELIVERED`, `STATUS_CANCELLED`

#### Methods:
- Getters and setters for all attributes.
- `addBillItem(BillItem item)`: Adds an item to the bill.
- `calculateTotal()`: Calculates the total amount based on all bill items.

### 4. BillItem
#### Attributes:
- `billItemId`: Unique identifier for the billItem.
- `orderNumber`: Reference to the parent bill.
- `productId`: ID of the product in this item.
- `productName`: Name of the product.
- `productImage`: Image URL of the product.
- `unitPrice`: Price per unit.
- `quantity`: Number of units.
- `totalPrice`: Total price for this item (`unitPrice × quantity`).

#### Methods:
- Getters and setters for all attributes.
- Constructor that creates a `BillItem` from a `CartItem`.
- `calculateTotalPrice()`: Calculates the total price for this item.

## Additional Support Classes
### 1. CartItem (UI Model)
#### Attributes:
- `medicine`: Reference to the `Medicine` object.
- `quantity`: Number of units.

#### Methods:
- Getters and setters for attributes.
- `getTotalPrice()`: Calculates the total price for this cart item.

### 2. CartManager (Singleton)
#### Attributes:
- `cartItems`: List of `CartItem` objects.

#### Methods:
- `getInstance()`: Returns the singleton instance.
- `addToCart(Medicine medicine, int quantity)`: Adds a medicine to the cart.
- `updateQuantity(long productId, int quantity)`: Updates the quantity of a cart item.
- `removeFromCart(long productId)`: Removes an item from the cart.
- `removeCartItemAt(int position)`: Removes an item at a specific position.
- `getProductIdsInCart()`: Gets all product IDs in the cart.
- `getCartItems()`: Gets all items in the cart.
- `clearCart()`: Removes all items from the cart.
- `getItemCount()`: Gets the number of different items in the cart.
- `getTotalPrice()`: Calculates the total price of all items in the cart.

These classes work together to provide a complete data management solution for the pharmacy application, with the DAO classes handling database operations and the Model classes representing the business entities and their behaviors.

