package com.gs.moneybook.Database;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gs.moneybook.Model.*;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "MoneyBook.db";
    private static final int DATABASE_VERSION = 1;

    // Singleton instance
    private static DBHelper instance;

    // Static method to get the single instance of DBHelper
    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_PAYMENT_MODES = "payment_modes";
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TABLE_BUDGETS = "budgets";
    private static final String TABLE_SAVINGS_GOALS = "savings_goals";
    private static final String TABLE_RECURRING_TRANSACTIONS = "recurring_transactions";
    private static final String TABLE_REMINDERS = "reminders";

    // User Table Columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_FULL_NAME = "fullName";
    private static final String COLUMN_USER_MOBILE_NUMBER = "mobileNumber";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_OCCUPATION = "occupation";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_DOB = "dob";
    private static final String COLUMN_USER_MONTHLY_INCOME = "monthlyIncome";
    private static final String COLUMN_USER_SAVINGS_GOAL = "savingsGoal";
    private static final String COLUMN_USER_CURRENCY = "currency";

    // Create Users Table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_FULL_NAME + " TEXT,"
            + COLUMN_USER_MOBILE_NUMBER + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_OCCUPATION + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_DOB + " TEXT,"
            + COLUMN_USER_MONTHLY_INCOME + " REAL,"
            + COLUMN_USER_SAVINGS_GOAL + " REAL,"
            + COLUMN_USER_CURRENCY + " TEXT" + ")";

    // Create Categories Table
    private static final String COLUMN_CATEGORY_ID = "categoryId";
    private static final String COLUMN_CATEGORY_NAME = "categoryName";
    private static final String COLUMN_CATEGORY_TYPE = "categoryType"; // e.g., Income/Expense
    private static final String COLUMN_CATEGORY_USER_ID = "userId";

    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CATEGORY_NAME + " TEXT NOT NULL,"
            + COLUMN_CATEGORY_TYPE + " TEXT NOT NULL,"
            + COLUMN_CATEGORY_USER_ID + " INTEGER DEFAULT -1, "
            + "FOREIGN KEY(" + COLUMN_CATEGORY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") ON DELETE CASCADE ON UPDATE CASCADE"
            + ")";

    //Crate Payment Modes Table

    private static final String COLUMN_PAYMENT_MODE_ID = "paymentModeId";
    private static final String COLUMN_PAYMENT_MODE_NAME = "paymentModeName";
    private static final String COLUMN_PAYMENT_MODE_USER_ID = "userId"; // New column for userId

    // Updated table creation SQL to include the userId column
    private static final String CREATE_TABLE_PAYMENT_MODES = "CREATE TABLE " + TABLE_PAYMENT_MODES + "("
            + COLUMN_PAYMENT_MODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PAYMENT_MODE_NAME + " TEXT NOT NULL,"
            + COLUMN_PAYMENT_MODE_USER_ID + " INTEGER NOT NULL"  // Add this line to associate payment modes with a user
            + ")";


    // Create Transactions Table
    private static final String COLUMN_TRANSACTION_ID = "transactionId";
    private static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_TRANSACTION_DATE = "date";
    private static final String COLUMN_TRANSACTION_CATEGORY_ID = "categoryId";
    private static final String COLUMN_TRANSACTION_USER_ID = "userId";
    private static final String COLUMN_TRANSACTION_TYPE = "type"; // "Income" or "Expense"
    private static final String COLUMN_TRANSACTION_PAYMENT_MODE_ID = "paymentModeId"; // Foreign key to payment_modes table

    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TRANSACTION_AMOUNT + " REAL NOT NULL,"
            + COLUMN_TRANSACTION_DATE + " TEXT NOT NULL,"  // Consider changing TEXT to DATETIME for better date handling
            + COLUMN_TRANSACTION_CATEGORY_ID + " INTEGER NOT NULL,"
            + COLUMN_TRANSACTION_USER_ID + " INTEGER NOT NULL,"
            + COLUMN_TRANSACTION_TYPE + " TEXT NOT NULL,"  // Type will be "Income" or "Expense"
            + COLUMN_TRANSACTION_PAYMENT_MODE_ID + " INTEGER,"  // Foreign key to reference the payment mode
            + "FOREIGN KEY (" + COLUMN_TRANSACTION_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "),"
            + "FOREIGN KEY (" + COLUMN_TRANSACTION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
            + "FOREIGN KEY (" + COLUMN_TRANSACTION_PAYMENT_MODE_ID + ") REFERENCES " + TABLE_PAYMENT_MODES + "(" + COLUMN_PAYMENT_MODE_ID + ")"
            + ")";


    // Create Budgets Table
    private static final String COLUMN_BUDGET_ID = "budgetId";
    private static final String COLUMN_BUDGET_AMOUNT = "amount";
    private static final String COLUMN_BUDGET_START_DATE = "startDate";
    private static final String COLUMN_BUDGET_END_DATE = "endDate";
    private static final String COLUMN_BUDGET_CATEGORY = "category";
    private static final String COLUMN_BUDGET_TYPE = "budgetType";

    private static final String CREATE_TABLE_BUDGETS = "CREATE TABLE " + TABLE_BUDGETS + "("
            + COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_BUDGET_AMOUNT + " REAL,"
            + COLUMN_BUDGET_START_DATE + " TEXT,"
            + COLUMN_BUDGET_END_DATE + " TEXT,"
            + COLUMN_BUDGET_CATEGORY + " TEXT,"
            + COLUMN_BUDGET_TYPE + " TEXT" + ")";


    // Create Savings Goals Table
    private static final String COLUMN_SAVINGS_GOAL_ID = "svaingGoalId";
    private static final String COLUMN_SAVINGS_GOAL_NAME = "goalName";
    private static final String COLUMN_SAVINGS_GOAL_TARGET_AMOUNT = "targetAmount";
    private static final String COLUMN_SAVINGS_GOAL_CURRENT_AMOUNT = "currentAmount";
    private static final String COLUMN_SAVINGS_GOAL_DEADLINE = "goalDeadline";
    private static final String COLUMN_SAVINGS_GOAL_STATUS = "status";

    private static final String CREATE_TABLE_SAVINGS_GOALS = "CREATE TABLE " + TABLE_SAVINGS_GOALS + "("
            + COLUMN_SAVINGS_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SAVINGS_GOAL_NAME + " TEXT NOT NULL,"
            + COLUMN_SAVINGS_GOAL_TARGET_AMOUNT + " REAL,"
            + COLUMN_SAVINGS_GOAL_CURRENT_AMOUNT + " REAL,"
            + COLUMN_SAVINGS_GOAL_DEADLINE + " TEXT,"
            + COLUMN_SAVINGS_GOAL_STATUS + " TEXT" + ")";


    // Create Recurring Transactions Table
    private static final String COLUMN_RECURRING_TRANSACTION_ID = "recurringTransactionId";
    private static final String COLUMN_RECURRING_TRANSACTION_NAME = "transactionName";
    private static final String COLUMN_RECURRING_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_RECURRING_TRANSACTION_DATE = "transactionDate";
    private static final String COLUMN_RECURRING_TRANSACTION_FREQUENCY = "frequency"; // Weekly, Monthly, Yearly
    private static final String COLUMN_RECURRING_TRANSACTION_CATEGORY = "category";
    private static final String COLUMN_RECURRING_TRANSACTION_TYPE = "transactionType"; // Income or Expense

    private static final String CREATE_TABLE_RECURRING_TRANSACTIONS = "CREATE TABLE " + TABLE_RECURRING_TRANSACTIONS + "("
            + COLUMN_RECURRING_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_RECURRING_TRANSACTION_NAME + " TEXT,"
            + COLUMN_RECURRING_TRANSACTION_AMOUNT + " REAL,"
            + COLUMN_RECURRING_TRANSACTION_DATE + " TEXT,"
            + COLUMN_RECURRING_TRANSACTION_FREQUENCY + " TEXT,"
            + COLUMN_RECURRING_TRANSACTION_CATEGORY + " TEXT,"
            + COLUMN_RECURRING_TRANSACTION_TYPE + " TEXT" + ")";

    // Create Reminders Table
    private static final String COLUMN_REMINDER_ID = "reminderId";
    private static final String COLUMN_REMINDER_USER_ID = "userId";
    private static final String COLUMN_REMINDER_DATE = "reminderDate";
    private static final String COLUMN_REMINDER_DESCRIPTION = "description";

    private static final String CREATE_TABLE_REMINDERS = "CREATE TABLE " + TABLE_REMINDERS + "("
            + COLUMN_REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_REMINDER_USER_ID + " INTEGER,"
            + COLUMN_REMINDER_DATE + " TEXT,"
            + COLUMN_REMINDER_DESCRIPTION + " TEXT" + ")";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        defaultCategories(db);
        db.execSQL(CREATE_TABLE_PAYMENT_MODES);
        // Insert default payment modes
        insertDefaultPaymentModes(db);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_BUDGETS);
        db.execSQL(CREATE_TABLE_SAVINGS_GOALS);
        db.execSQL(CREATE_TABLE_RECURRING_TRANSACTIONS);
        db.execSQL(CREATE_TABLE_REMINDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_MODES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVINGS_GOALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECURRING_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    // CRUD operations for Users table
    public long insertUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FULL_NAME, user.getFullName());
        values.put(COLUMN_USER_MOBILE_NUMBER, user.getMobileNumber());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_OCCUPATION, user.getOccupation());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_DOB, user.getDob());
        values.put(COLUMN_USER_MONTHLY_INCOME, user.getMonthlyIncome());
        values.put(COLUMN_USER_SAVINGS_GOAL, user.getSavingsGoal());
        values.put(COLUMN_USER_CURRENCY, user.getCurrency());
        return db.insert(TABLE_USERS, null, values);
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public UserModel getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            UserModel user = new UserModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_MOBILE_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_OCCUPATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_DOB)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_USER_MONTHLY_INCOME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_USER_SAVINGS_GOAL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_CURRENCY))
            );
            cursor.close();
            return user;
        }

        cursor.close();
        return null; // No user found
    }

    // Method to verify if the user's DOB matches the stored DOB
    public boolean verifyUserDob(String email, String dob) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ? AND dob = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, dob});
        boolean isDobCorrect = cursor.getCount() > 0;
        cursor.close();
        return isDobCorrect;
    }

    // Method to update the user's password
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        int rowsUpdated = db.update("users", contentValues, "email = ?", new String[]{email});
        return rowsUpdated > 0;
    }



    // Helper method to insert a category
    public boolean insertCategory(SQLiteDatabase db, String categoryName, String categoryType, int userId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        values.put(COLUMN_CATEGORY_TYPE, categoryType);
        values.put(COLUMN_CATEGORY_USER_ID, userId);  // Store the valid userId

        // Insert into the categories table
        long result = db.insert(TABLE_CATEGORIES, null, values);

        if (result == -1) {
            Log.e("DBHelper", "Error inserting category: " + categoryName);
            return false;  // Return false if insertion fails
        } else {
            Log.d("DBHelper", "Category inserted: " + categoryName);
            return true;  // Return true if insertion is successful
        }
    }
    public void defaultCategories(SQLiteDatabase db) {
        // Define categories as arrays
        String[] expenseCategories = {"Groceries", "Rent", "Utilities", "Transport", "Entertainment", "Dining Out", "Health", "Insurance", "Shopping", "Bills"};
        String[] incomeCategories = {"Salary", "Investment", "Freelancing", "Gifts", "Interest", "Rental Income"};

        // Insert expense categories
        for (String category : expenseCategories) {
            insertCategory(db, category, "Expense");
        }

        // Insert income categories
        for (String category : incomeCategories) {
            insertCategory(db, category, "Income");
        }
    }

    private void insertCategory(SQLiteDatabase db, String name, String type) {
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                        + COLUMN_CATEGORY_NAME + ", "
                        + COLUMN_CATEGORY_TYPE + ", "
                        + COLUMN_CATEGORY_USER_ID + ") VALUES (?, ?, -1)",
                new Object[]{name, type});
    }


    // Method to check if a category already exists (for the user only)
    public boolean checkCategoryExists(String categoryName, String categoryType, int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            // Check if the category exists for the given userId
            cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM categories WHERE categoryName = ? AND categoryType = ? AND (userId = -1 OR userId = ?)",
                    new String[]{categoryName, categoryType, String.valueOf(userId)}
            );
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0) > 0;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }


    public List<String> getCategoriesByTypeAndUserId(String categoryType, int userId) {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get categories by type and user ID
        String query = "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_TYPE + " = ? AND " + COLUMN_CATEGORY_USER_ID + " = - 1 OR " + COLUMN_CATEGORY_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryType, String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME));
                categories.add(categoryName);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return categories;
    }



    public List<String> getAllCategories(int userId) {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        // Query for categories with userId = -1 (global categories) and categories for the specific user
        String query = "SELECT categoryName, categoryType FROM categories WHERE  (userId = -1 OR userId = ?)";

        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                // Ensure columns exist before accessing them
                int categoryNameIndex = cursor.getColumnIndex("categoryName");
                int categoryTypeIndex = cursor.getColumnIndex("categoryType");

                if (categoryNameIndex >= 0 && categoryTypeIndex >= 0) {
                    // Loop through the cursor and retrieve the category names and types
                    do {
                        String categoryName = cursor.getString(categoryNameIndex);
                        String categoryType = cursor.getString(categoryTypeIndex);
                        categories.add(categoryName + " (" + categoryType + ")");
                    } while (cursor.moveToNext());
                } else {
                    Log.e("DBHelper", "Columns 'categoryName' or 'categoryType' not found in the query result.");
                }
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error fetching categories: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // Avoid closing the database connection here as it might be reused in the session
        }

        return categories;
    }


    public List<String> getUserDefinedCategories(int userId) {
        List<String> userCategories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT categoryName FROM categories WHERE userId = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                userCategories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userCategories;
    }

    public boolean deleteCategory(String categoryName, String categoryType, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("categories", "categoryName = ? AND categoryType = ? AND userId = ?",
                new String[]{categoryName, categoryType, String.valueOf(userId)});
        return rowsAffected > 0;
    }


    // Payment mode table curd

    // Method to insert default payment modes for system (userId = -1)
    private void insertDefaultPaymentModes(SQLiteDatabase db) {
        String[] defaultPaymentModes = {"Cash", "Credit Card", "Debit Card", "Bank Transfer", "Mobile Payment"};

        for (String paymentMode : defaultPaymentModes) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PAYMENT_MODE_NAME, paymentMode);
            values.put(COLUMN_PAYMENT_MODE_USER_ID, -1);  // Insert default payment modes for user -1 (system-wide)
            db.insert(TABLE_PAYMENT_MODES, null, values);
        }
    }


    // Method to insert user-defined payment mode for a specific user
    public boolean insertPaymentMode(String paymentModeName, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_MODE_NAME, paymentModeName);
        values.put(COLUMN_PAYMENT_MODE_USER_ID, userId); // Store the userId for the payment mode

        long result = db.insert(TABLE_PAYMENT_MODES, null, values);
        return result != -1; // Returns true if inserted successfully
    }


    // Check if the payment mode already exists for the user or system-wide
    public boolean checkPaymentModeExists(String paymentModeName, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if the payment mode exists for the user or in the system-wide modes
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_PAYMENT_MODE_NAME + " FROM " + TABLE_PAYMENT_MODES +
                        " WHERE " + COLUMN_PAYMENT_MODE_NAME + " = ? AND (" + COLUMN_PAYMENT_MODE_USER_ID + " = ? OR " + COLUMN_PAYMENT_MODE_USER_ID + " = -1)",
                new String[]{paymentModeName, String.valueOf(userId)}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    // Method to retrieve payment modes for a specific user, including system-wide modes
    public List<String> getPaymentModes(int userId) {
        List<String> paymentModes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get both user-defined and system-defined payment modes
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PAYMENT_MODE_NAME + " FROM " + TABLE_PAYMENT_MODES
                        + " WHERE " + COLUMN_PAYMENT_MODE_USER_ID + " = ? OR " + COLUMN_PAYMENT_MODE_USER_ID + " = -1",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                paymentModes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return paymentModes;
    }



    // Method to get user-defined payment modes for a specific user
    public List<String> getUserDefinedPaymentModes(int userId) {
        List<String> userDefinedPaymentModes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Assuming system-defined modes have IDs <= 5 (i.e., first 5 payment modes are system-defined)
        String query = "SELECT " + COLUMN_PAYMENT_MODE_NAME + " FROM " + TABLE_PAYMENT_MODES +
                " WHERE " + COLUMN_PAYMENT_MODE_ID + " > 5 AND " + COLUMN_PAYMENT_MODE_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                userDefinedPaymentModes.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_MODE_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return userDefinedPaymentModes;
    }

    // Method to delete a user-defined payment mode
    public boolean deletePaymentMode(String paymentModeName, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_PAYMENT_MODES,
                COLUMN_PAYMENT_MODE_NAME + " = ? AND " + COLUMN_PAYMENT_MODE_USER_ID + " = ?",
                new String[]{paymentModeName, String.valueOf(userId)});

        return deletedRows > 0; // Returns true if the payment mode was deleted
    }


    // Helper method to get paymentModeId by its name
    private Integer getPaymentModeIdByName(String paymentModeName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PAYMENT_MODE_ID + " FROM " + TABLE_PAYMENT_MODES
                + " WHERE " + COLUMN_PAYMENT_MODE_NAME + " = ?", new String[]{paymentModeName});

        if (cursor != null && cursor.moveToFirst()) {
            int paymentModeId = cursor.getInt(0);
            cursor.close();
            return paymentModeId;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null; // Return null if the payment mode is not found
    }




    // Transactions Table - CRUD Operations

    // Create a new transaction
    public long createTransaction(double amount, String date, String categoryId, int userId, String transactionType, @Nullable String paymentModeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_AMOUNT, amount);
        values.put(COLUMN_TRANSACTION_DATE, date);
        values.put(COLUMN_TRANSACTION_CATEGORY_ID, categoryId);
        values.put(COLUMN_TRANSACTION_USER_ID, userId); // Associate with logged-in user
        values.put(COLUMN_TRANSACTION_TYPE, transactionType); // Income or Expense

        // If the transaction is an expense and a payment mode is provided
        if (transactionType.equalsIgnoreCase("Expense") && paymentModeName != null) {
            // Fetch paymentModeId from the payment_modes table
            Integer paymentModeId = getPaymentModeIdByName(paymentModeName);
            if (paymentModeId != null) {
                values.put(COLUMN_TRANSACTION_PAYMENT_MODE_ID, paymentModeId); // Insert the paymentModeId
            } else {
                // Handle case where payment mode is not found
                throw new IllegalArgumentException("Invalid payment mode: " + paymentModeName);
            }
        }

        return db.insert(TABLE_TRANSACTIONS, null, values);
    }


    // Function to get transaction data
// Function to get transaction data
    public List<TransactionModel> getTransactions(int userId) {
        List<TransactionModel> transactionsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query to retrieve transaction data including related tables for category, user, and payment mode
            String selectQuery = "SELECT " +
                    "t." + COLUMN_TRANSACTION_ID + ", " +
                    "t." + COLUMN_TRANSACTION_AMOUNT + ", " +
                    "t." + COLUMN_TRANSACTION_DATE + ", " +
                    "t." + COLUMN_TRANSACTION_TYPE + ", " +
                    "c." + COLUMN_CATEGORY_NAME + " AS categoryName " +
                    "FROM " + TABLE_TRANSACTIONS + " t " +
                    "INNER JOIN " + TABLE_CATEGORIES + " c ON t." + COLUMN_TRANSACTION_CATEGORY_ID + " = c." + COLUMN_CATEGORY_ID + " " +
                    "WHERE t." + COLUMN_TRANSACTION_USER_ID + " = ?";

            // Execute the query
            cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

            // Get the column indexes to avoid calling getColumnIndexOrThrow repeatedly
            int transactionIdIdx = cursor.getColumnIndexOrThrow(COLUMN_TRANSACTION_ID);
            int transactionAmountIdx = cursor.getColumnIndexOrThrow(COLUMN_TRANSACTION_AMOUNT);
            int transactionDateIdx = cursor.getColumnIndexOrThrow(COLUMN_TRANSACTION_DATE);
            int transactionTypeIdx = cursor.getColumnIndexOrThrow(COLUMN_TRANSACTION_TYPE);
            int categoryNameIdx = cursor.getColumnIndexOrThrow("categoryName");

            // Iterate through the cursor and build the list
            if (cursor.moveToFirst()) {
                do {
                    TransactionModel transaction = new TransactionModel();

                    // Set values to TransactionModel
                    transaction.setTransactionId(cursor.getInt(transactionIdIdx));
                    transaction.setTransactionAmount(cursor.getDouble(transactionAmountIdx));
                    transaction.setTransactionDate(cursor.getString(transactionDateIdx));
                    transaction.setTransactionType(cursor.getString(transactionTypeIdx));
                    transaction.setCategoryName(cursor.getString(categoryNameIdx));

                    // Add to list
                    transactionsList.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();  // Handle or log exception
        } finally {
            // Ensure cursor is closed
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();  // Close the database
        }

        return transactionsList;
    }


}
