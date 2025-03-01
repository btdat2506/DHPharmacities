# The UI Classes in the Pharmacy Application

The Android app follows a standard Android structure with Activity and Fragment classes corresponding to specific XML layout files. Here's a breakdown of the main UI classes and their corresponding layouts:

## Splash Screen
- **Class**: `SplashScreenActivity.java`
- **Layout**: `activity_splash_screen.xml`

    -> **Purpose**: Initial loading screen with logo display.

## Authentication
- **Class**: `LoginSignupActivity.java`
- **Layout**: `activity_login_signup.xml`

    -> **Purpose**: Container for login and signup tabs.

- **Class**: `LoginFragment.java`
- **Layout**: `fragment_login.xml`

    -> **Purpose**: User login form.

- **Class**: `SignupFragment.java`
- **Layout**: `fragment_signup.xml`

    -> **Purpose**: New user registration form.

## Main User Interface
- **Class**: `HomeActivity.java`
- **Layout**: `activity_home.xml`

    -> **Purpose**: Main screen with category tabs and navigation.

- **Class**: `GenericCategoryFragment.java`
- **Layout**: `fragment_generic_category.xml`

    -> **Purpose**: Displays medicines by category.

## Product Details
- **Class**: `ProductDetailActivity.java`
- **Layout**: `activity_product_detail.xml`

    -> **Purpose**: Detailed medicine information and purchase options.

## Cart & Checkout
- **Class**: `CartActivity.java`
- **Layout**: `activity_cart.xml`

    -> **Purpose**: Shopping cart with item listing and modification options.

- **Class**: `CheckoutActivity.java`
- **Layout**: `activity_checkout.xml`

    -> **Purpose**: Order confirmation and payment process.

## User Profile
- **Class**: `ProfileActivity.java`
- **Layout**: `activity_profile.xml`

    -> **Purpose**: User information management.

## Admin Interface
- **Class**: `AdminDashboardActivity.java`
- **Layout**: `activity_admin_dashboard.xml`

    -> **Purpose**: Main admin control panel with tiles for different functions.

- **Class**: `CustomersListActivity.java`
- **Layout**: `activity_customers_list.xml`

    -> **Purpose**: List and manage pharmacy customers.

- **Class**: `CustomerDetailActivity.java`
- **Layout**: `activity_customer_detail.xml`

    -> **Purpose**: Detailed customer information and order history.

- **Class**: `OrdersListActivity.java`
- **Layout**: `activity_orders_list.xml`

    -> **Purpose**: Order management with filtering and sorting.

- **Class**: `OrderDetailActivity.java`
- **Layout**: `activity_order_detail.xml`
    
    -> **Purpose**: Detailed order information and status management.

- **Class**: `ProductManagementActivity.java`
- **Layout**: `activity_product_management.xml`

    -> **Purpose**: List and manage pharmacy products.

- **Class**: `ProductEditActivity.java`
- **Layout**: `activity_product_edit.xml`

    -> **Purpose**: Add or edit product information.

- **Class**: `StatisticsActivity.java`
- **Layout**: `activity_statistics.xml`

    -> **Purpose**: Sales analytics and statistical charts.

## Item Adapters and Layouts
- **Adapter**: `MedicineAdapter.java`
- **Item Layout**: `item_medicine.xml`

    -> **Purpose**: Display medicine items in grid or list.

- **Adapter**: `CartItemAdapter.java`
- **Item Layout**: `item_cart.xml`

    -> **Purpose**: Display cart items with quantity controls.

- **Adapter**: `CartItemCheckoutAdapter.java`
- **Item Layout**: `item_cart_checkout.xml`

    -> **Purpose**: Display cart items in checkout view. (simplified)

- **Adapter**: `CustomerAdapter.java`
- **Item Layout**: `item_customer.xml`

    -> **Purpose**: Display customer information in list.

- **Adapter**: `OrderAdapter.java`
- **Item Layout**: `item_order.xml`

    -> **Purpose**: Display order summary in list.

- **Adapter**: `OrderItemAdapter.java`
- **Item Layout**: `item_order_detail.xml`

    -> **Purpose**: Display items within an order.

- **Adapter**: `ProductAdapter.java`
- **Item Layout**: `item_product_admin.xml`

    -> **Purpose**: Display product information in admin view.

## Dialog Layouts
- **Class**: `CustomerOrdersDialog.java`
- **Layout**: `dialog_customer_orders.xml`

    -> **Purpose**: Display a specific customer's orders in a popup dialog.

This structure follows the MVC (Model-View-Controller) pattern commonly used in Android development, where the Activities and Fragments serve as controllers, the XML layouts are the views, and the Model classes (User, Medicine, Bill, etc.) represent the data.