package com.example.testpharmacy.Database;

public class Dataset {
    public static final int NUM_ADMIN = 2;
    // Admins
    public static final String INSERT_ADMIN_1 = "INSERT INTO " + DatabaseHelper.TB_USERS + " (" +
            DatabaseHelper.COLUMN_USER_NAME + ", " + DatabaseHelper.COLUMN_USER_EMAIL + ", " + DatabaseHelper.COLUMN_USER_PASSWORD +
            ") VALUES ('admin1', 'admin1@gmail.com', 'admin123')";
    public static final String INSERT_ADMIN_2 = "INSERT INTO " + DatabaseHelper.TB_USERS + " (" +
            DatabaseHelper.COLUMN_USER_NAME + ", " + DatabaseHelper.COLUMN_USER_EMAIL + ", " + DatabaseHelper.COLUMN_USER_PASSWORD +
            ") VALUES ('admin2', 'admin2@gmail.com', 'admin123')";

    // Users
    public static final String INSERT_USER_1 = "INSERT INTO " + DatabaseHelper.TB_USERS + " (" +
            DatabaseHelper.COLUMN_USER_NAME + ", " + DatabaseHelper.COLUMN_USER_EMAIL + ", " + DatabaseHelper.COLUMN_USER_PASSWORD + ", " + DatabaseHelper.COLUMN_USER_PHONE +
            ") VALUES ('user1', 'user1@gmail.com', 'user123', '0357571029')";
    public static final String INSERT_USER_2 = "INSERT INTO " + DatabaseHelper.TB_USERS + " (" +
            DatabaseHelper.COLUMN_USER_NAME + ", " + DatabaseHelper.COLUMN_USER_EMAIL + ", " + DatabaseHelper.COLUMN_USER_PASSWORD + ", " + DatabaseHelper.COLUMN_USER_PHONE +
            ") VALUES ('user2', 'user2@gmail.com', 'user123', '0359126807')";

    // Medicines
    public static final String INSERT_MEDICINE_1 = "INSERT INTO " + DatabaseHelper.TB_MEDICINES + " (" + // Renamed INSERT_PRODUCT_1 to INSERT_MEDICINE_1 and DatabaseHelper.TB_PRODUCTS to DatabaseHelper.TB_MEDICINES
            DatabaseHelper.COLUMN_MEDICINE_NAME + ", " + DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION + ", " +
            DatabaseHelper.COLUMN_MEDICINE_CATEGORY + ", " + DatabaseHelper.COLUMN_MEDICINE_PRICE + ", " +
            DatabaseHelper.COLUMN_MEDICINE_IMAGE_URL + ", " + DatabaseHelper.COLUMN_MEDICINE_STOCK_QUANTITY + ", " + DatabaseHelper.COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Hỗn dịch uống Amoxicillin 250mg Imexpharm điều trị nhiễm khuẩn đường hô hấp, đường mật, đường tiêu hóa (12 gói)', " +
            "'Thuốc Amoxicillin của Công ty Cổ phần Dược phẩm IMEXPHARM, có thành phần chính là Amoxicillin. Đây là thuốc dùng để điều trị các nhiễm khuẩn do các vi khuẩn nhạy cảm với thuốc tại các vị trí: Đường hô hấp trên, đường hô hấp dưới, nhiễm khuẩn đường mật, đường tiêu hóa, đường tiết niệu - sinh dục, dự phòng viêm nội tâm mạc.', " +
            "'Antibiotics', 32.400, 'https://cms-prod.s3-sgn09.fptcloud.com/9_528e8474cf.png', 100, 'box 12 gói')"; // Added unit value
    public static final String INSERT_MEDICINE_2 = "INSERT INTO " + DatabaseHelper.TB_MEDICINES + " (" + // Renamed INSERT_PRODUCT_2 to INSERT_MEDICINE_2 and DatabaseHelper.TB_PRODUCTS to DatabaseHelper.TB_MEDICINES
            DatabaseHelper.COLUMN_MEDICINE_NAME + ", " + DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION + ", " +
            DatabaseHelper.COLUMN_MEDICINE_CATEGORY + ", " + DatabaseHelper.COLUMN_MEDICINE_PRICE + ", " +
            DatabaseHelper.COLUMN_MEDICINE_IMAGE_URL + ", " + DatabaseHelper.COLUMN_MEDICINE_STOCK_QUANTITY + ", " + DatabaseHelper.COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Thuốc Exopadin 60mg Trường Thọ điều trị viêm mũi dị ứng, mày đay (3 vỉ x 10 viên)', " +
            "'Thuốc Exopadin là sản phẩm của Công ty Cổ phần Dược phẩm Trường Thọ có thành phần chính là Fexofenadin hydroclorid. Đây là thuốc được chỉ định để điều trị viêm mũi dị ứng theo mùa, điều trị các biểu hiện ngoài da không biến chứng của mày đay vô căn mạn tính ở người lớn và trẻ em từ 12 tuổi trở lên. Thuốc làm giảm ngứa và số lượng dát mày đay một cách đáng kể.', " +
            "'Antibiotics', 60.000, 'https://cms-prod.s3-sgn09.fptcloud.com/IMG_0238_fc19904162.jpg', 100, 'vỉ 30 viên')"; // Added unit value
    public static final String INSERT_MEDICINE_3 = "INSERT INTO " + DatabaseHelper.TB_MEDICINES + " (" +
            DatabaseHelper.COLUMN_MEDICINE_NAME + ", " + DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION + ", " +
            DatabaseHelper.COLUMN_MEDICINE_CATEGORY + ", " + DatabaseHelper.COLUMN_MEDICINE_PRICE + ", " +
            DatabaseHelper.COLUMN_MEDICINE_IMAGE_URL + ", " + DatabaseHelper.COLUMN_MEDICINE_STOCK_QUANTITY + ", " + DatabaseHelper.COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Viên nén Panactol Extra Khapharco hạ sốt, giảm đau (10 vỉ x 10 viên)', " +
            "'Thuốc Panactol extra là sản phẩm của Dược phẩm Khánh Hoà, thành phần chính là Paracetamol 500mg và Cafein 65mg, là thuốc dùng hạ sốt và giảm đau (Đau đầu, đau nửa đầu, đau bụng kinh, đau họng, đau cơ, xương, sốt và đau sau tiêm vaccin, sau nhổ răng/ phẫu thuật nha khoa, đau răng, do viêm xương khớp).', " +
            "'Pain Relievers', 60.000, 'https://cms-prod.s3-sgn09.fptcloud.com/DSC_07308_99ce483742.jpg', 100, 'hộp 10 vỉ')";
    public static final String INSERT_MEDICINE_4 = "INSERT INTO " + DatabaseHelper.TB_MEDICINES + " (" +
            DatabaseHelper.COLUMN_MEDICINE_NAME + ", " + DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION + ", " +
            DatabaseHelper.COLUMN_MEDICINE_CATEGORY + ", " + DatabaseHelper.COLUMN_MEDICINE_PRICE + ", " +
            DatabaseHelper.COLUMN_MEDICINE_IMAGE_URL + ", " + DatabaseHelper.COLUMN_MEDICINE_STOCK_QUANTITY + ", " + DatabaseHelper.COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Thuốc Gofen 400 MEGA We care điều trị hạ sốt, giảm đau (5 vỉ x 10 viên)', " +
            "'Gofen 400 của Công ty Mega Lifesciences Public Company Limited, có thành phần chính ibuprofen, là một thuốc kháng viêm không steroid. Đây là thuốc dùng để điều trị hạ sốt, giảm đau do cảm cúm, đau đầu, đau răng, đau nhức cơ, đau lưng, đau nhẹ do viêm khớp, đau do bong gân, đau bụng kinh.', " +
            "'Pain Relievers', 194.000, 'https://cms-prod.s3-sgn09.fptcloud.com/00003460_gofen_400ml_2721_63db_large_627db09b10.jpg', 100, 'hộp 5 vỉ')";
    public static final String INSERT_MEDICINE_5 = "INSERT INTO " + DatabaseHelper.TB_MEDICINES + " (" +
            DatabaseHelper.COLUMN_MEDICINE_NAME + ", " + DatabaseHelper.COLUMN_MEDICINE_DESCRIPTION + ", " +
            DatabaseHelper.COLUMN_MEDICINE_CATEGORY + ", " + DatabaseHelper.COLUMN_MEDICINE_PRICE + ", " +
            DatabaseHelper.COLUMN_MEDICINE_IMAGE_URL + ", " + DatabaseHelper.COLUMN_MEDICINE_STOCK_QUANTITY + ", " + DatabaseHelper.COLUMN_MEDICINE_UNIT + // Added unit column to insert
            ") VALUES ('Siro Multi Vitamin Hatro hỗ trợ tăng cường sức khỏe, nâng cao sức đề kháng (120ml)', " +
            "'Siro Multi Vitamin Hatro bổ sung một số vitamin và kẽm cho cơ thể. Hỗ trợ tăng cường sức khỏe, nâng cao sức đề kháng.', " +
            "'Vitamins', 150.000, 'https://cms-prod.s3-sgn09.fptcloud.com/DSC_08289_8048425d59.jpg', 100, 'hộp')";

}
