package com.gs.moneybook.Database;

import android.annotation.SuppressLint;
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



    public UserModel getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID, COLUMN_USER_FULL_NAME, COLUMN_USER_MOBILE_NUMBER, COLUMN_USER_EMAIL,
                        COLUMN_USER_OCCUPATION, COLUMN_USER_PASSWORD, COLUMN_USER_DOB, COLUMN_USER_MONTHLY_INCOME,
                        COLUMN_USER_SAVINGS_GOAL, COLUMN_USER_CURRENCY},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

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
        return null;
    }

    public int updateUser(UserModel user) {
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
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
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

    public void defaultCategories(SQLiteDatabase db){
        // Expense categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Groceries', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Rent', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Utilities', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Transport', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Entertainment', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Dining Out', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Health', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Insurance', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Shopping', 'Expense', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Bills', 'Expense', -1)");

        // Income categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Salary', 'Income', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Investment', 'Income', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Freelancing', 'Income', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Gifts', 'Income', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Interest', 'Income', -1)");

        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " ("
                + COLUMN_CATEGORY_NAME + ", "
                + COLUMN_CATEGORY_TYPE + ", "
                + COLUMN_CATEGORY_USER_ID + ") VALUES ('Rental Income', 'Income', -1)");


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



/*    // Method to get a category by ID for a specific user
    public CategoryModel getCategoryById(long id, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES,
                new String[]{COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME, COLUMN_CATEGORY_TYPE},
                COLUMN_CATEGORY_ID + "=? AND " + COLUMN_CATEGORY_USER_ID + "=?",
                new String[]{String.valueOf(id), String.valueOf(userId)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            CategoryModel category = new CategoryModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_TYPE))
            );
            cursor.close();
            return category;
        }

        return null; // Return null if no record is found
    }*/


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
        db.close();

        return categories;
    }


    public int getCategoryIdByName(String categoryName, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CATEGORY_ID + " FROM " + TABLE_CATEGORIES + " WHERE " + COLUMN_CATEGORY_NAME + " = ? AND " + COLUMN_CATEGORY_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryName, String.valueOf(userId)});
        int categoryId = -1;

        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("categoryId"));
        }
        cursor.close();
        db.close();

        return categoryId;
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



/*
    public int updateCategory(CategoryModel category, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getCategoryName());
        values.put(COLUMN_CATEGORY_TYPE, category.getCategoryType()); // Update category type as well

        return db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = ? AND " + COLUMN_CATEGORY_USER_ID + " = ?",
                new String[]{String.valueOf(category.getId()), String.valueOf(userId)});
    }
*/


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



    // Function to retrieve all transactions with category name from the database
    // Function to retrieve all transactions for the logged-in user
   // @SuppressLint("Range")
    public List<TransactionModel> getAllTransactions(int userId) {  // Add userId as a parameter
        List<TransactionModel> transactionList = new ArrayList<>();

        // Query to get all transactions for the logged-in user with the category name
        String selectQuery = "SELECT t.*, c." + COLUMN_CATEGORY_NAME + " FROM " + TABLE_TRANSACTIONS + " t"
                + " INNER JOIN " + TABLE_CATEGORIES + " c ON t." + COLUMN_TRANSACTION_CATEGORY_ID + " = c." + COLUMN_CATEGORY_ID
                + " WHERE t." + COLUMN_TRANSACTION_USER_ID + " = " + userId;  // Add userId filter

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TransactionModel transaction = new TransactionModel();
                transaction.setAmount(cursor.getDouble(1)); // Amount
                transaction.setDate(cursor.getString(2)); // Date
                transaction.setCategoryId(cursor.getInt(3)); // Category ID
                transaction.setUserId(cursor.getInt(4)); // User ID
                transaction.setType(cursor.getString(5)); // Transaction Type
                transaction.setPaymentModeId(cursor.getInt(6)); // Payment Mode ID

                // Get the category name from the categories table
               // String categoryName = cursor.getString(COLUMN_CATEGORY_NAME);
                //transaction.setCategoryName(categoryName);

                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transactionList;
    }








    // Update a transaction
    public int updateTransaction(int id, double amount, String date, int categoryId, String transactionType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_AMOUNT, amount);
        values.put(COLUMN_TRANSACTION_DATE, date);
        values.put(COLUMN_TRANSACTION_CATEGORY_ID, categoryId);
        values.put(COLUMN_TRANSACTION_TYPE, transactionType); // Income or Expense

        return db.update(TABLE_TRANSACTIONS, values, COLUMN_TRANSACTION_ID + "=?", new String[]{String.valueOf(id)});
    }


    // Delete a transaction
    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTIONS, COLUMN_TRANSACTION_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }


    // curd for budget table

    // Create a new budget
    public long createBudget(BudgetModel budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGET_AMOUNT, budget.getAmount());
        values.put(COLUMN_BUDGET_START_DATE, budget.getStartDate());
        values.put(COLUMN_BUDGET_END_DATE, budget.getEndDate());
        values.put(COLUMN_BUDGET_CATEGORY, budget.getCategory());
        values.put(COLUMN_BUDGET_TYPE, budget.getBudgetType());

        // Insert row
        long id = db.insert(TABLE_BUDGETS, null, values);
        db.close();
        return id;
    }
    // Read a budget by ID
    public BudgetModel readBudgetById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGETS, new String[]{COLUMN_BUDGET_ID, COLUMN_BUDGET_AMOUNT, COLUMN_BUDGET_START_DATE, COLUMN_BUDGET_END_DATE, COLUMN_BUDGET_CATEGORY, COLUMN_BUDGET_TYPE},
                COLUMN_BUDGET_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        BudgetModel budget = new BudgetModel(
                cursor.getInt(0),    // id
                cursor.getDouble(1), // amount
                cursor.getString(2), // startDate
                cursor.getString(3), // endDate
                cursor.getString(4), // category
                cursor.getString(5)  // budgetType
        );
        cursor.close();
        return budget;
    }
    // Read all budgets
    public List<BudgetModel> readAllBudgets() {
        List<BudgetModel> budgetList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BUDGETS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BudgetModel budget = new BudgetModel(
                        cursor.getInt(0),    // id
                        cursor.getDouble(1), // amount
                        cursor.getString(2), // startDate
                        cursor.getString(3), // endDate
                        cursor.getString(4), // category
                        cursor.getString(5)  // budgetType
                );
                budgetList.add(budget);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return budgetList;
    }
    // Update an existing budget
    public int updateBudget(BudgetModel budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUDGET_AMOUNT, budget.getAmount());
        values.put(COLUMN_BUDGET_START_DATE, budget.getStartDate());
        values.put(COLUMN_BUDGET_END_DATE, budget.getEndDate());
        values.put(COLUMN_BUDGET_CATEGORY, budget.getCategory());
        values.put(COLUMN_BUDGET_TYPE, budget.getBudgetType());

        // Updating row
        return db.update(TABLE_BUDGETS, values, COLUMN_BUDGET_ID + " = ?",
                new String[]{String.valueOf(budget.getId())});
    }
    // Delete a budget by ID
    public void deleteBudget(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUDGETS, COLUMN_BUDGET_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    //curd for recurring transaction

    // Create a new recurring transaction
    public long createRecurringTransaction(RecurringTransactionModel transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECURRING_TRANSACTION_NAME, transaction.getTransactionName());
        values.put(COLUMN_RECURRING_TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(COLUMN_RECURRING_TRANSACTION_DATE, transaction.getTransactionDate());
        values.put(COLUMN_RECURRING_TRANSACTION_FREQUENCY, transaction.getFrequency());
        values.put(COLUMN_RECURRING_TRANSACTION_CATEGORY, transaction.getCategory());
        values.put(COLUMN_RECURRING_TRANSACTION_TYPE, transaction.getTransactionType());

        // Insert row
        long id = db.insert(TABLE_RECURRING_TRANSACTIONS, null, values);
        db.close();
        return id;
    }
    // Read a recurring transaction by ID
    public RecurringTransactionModel readRecurringTransactionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECURRING_TRANSACTIONS, new String[]{COLUMN_RECURRING_TRANSACTION_ID, COLUMN_RECURRING_TRANSACTION_NAME, COLUMN_RECURRING_TRANSACTION_AMOUNT, COLUMN_RECURRING_TRANSACTION_DATE, COLUMN_RECURRING_TRANSACTION_FREQUENCY, COLUMN_RECURRING_TRANSACTION_CATEGORY, COLUMN_RECURRING_TRANSACTION_TYPE},
                COLUMN_RECURRING_TRANSACTION_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        RecurringTransactionModel transaction = new RecurringTransactionModel(
                cursor.getInt(0),    // id
                cursor.getString(1), // transactionName
                cursor.getDouble(2), // amount
                cursor.getString(3), // transactionDate
                cursor.getString(4), // frequency
                cursor.getString(5), // category
                cursor.getString(6)  // transactionType
        );
        cursor.close();
        return transaction;
    }
    // Read all recurring transactions
    public List<RecurringTransactionModel> readAllRecurringTransactions() {
        List<RecurringTransactionModel> transactionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECURRING_TRANSACTIONS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RecurringTransactionModel transaction = new RecurringTransactionModel(
                        cursor.getInt(0),    // id
                        cursor.getString(1), // transactionName
                        cursor.getDouble(2), // amount
                        cursor.getString(3), // transactionDate
                        cursor.getString(4), // frequency
                        cursor.getString(5), // category
                        cursor.getString(6)  // transactionType
                );
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return transactionList;
    }
    // Update an existing recurring transaction
    public int updateRecurringTransaction(RecurringTransactionModel transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECURRING_TRANSACTION_NAME, transaction.getTransactionName());
        values.put(COLUMN_RECURRING_TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(COLUMN_RECURRING_TRANSACTION_DATE, transaction.getTransactionDate());
        values.put(COLUMN_RECURRING_TRANSACTION_FREQUENCY, transaction.getFrequency());
        values.put(COLUMN_RECURRING_TRANSACTION_CATEGORY, transaction.getCategory());
        values.put(COLUMN_RECURRING_TRANSACTION_TYPE, transaction.getTransactionType());

        // Updating row
        return db.update(TABLE_RECURRING_TRANSACTIONS, values, COLUMN_RECURRING_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(transaction.getId())});
    }
    // Delete a recurring transaction by ID
    public void deleteRecurringTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECURRING_TRANSACTIONS, COLUMN_RECURRING_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    //curd for savings goal table

    // Insert a new savings goal
    public long createSavingsGoal(SavingsGoalModel savingsGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goalName", savingsGoal.getGoalName());
        values.put("targetAmount", savingsGoal.getTargetAmount());
        values.put("currentAmount", savingsGoal.getCurrentAmount());
        values.put("goalDeadline", savingsGoal.getGoalDeadline());
        values.put("status", savingsGoal.getStatus());

        // Inserting Row
        long id = db.insert(TABLE_SAVINGS_GOALS, null, values);
        db.close();
        return id;
    }
    // Read a single savings goal by ID
    public SavingsGoalModel getSavingsGoalById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAVINGS_GOALS, new String[]{"id", "goalName", "targetAmount", "currentAmount", "goalDeadline", "status"},
                "id=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        SavingsGoalModel savingsGoal = new SavingsGoalModel(
                cursor.getInt(0),    // id
                cursor.getDouble(1), // goalName
                cursor.getDouble(2), // targetAmount
                cursor.getString(3), // currentAmount
                cursor.getString(4), // goalDeadline
                cursor.getString(5)  // status
        );
        cursor.close();
        return savingsGoal;
    }
    // Read all savings goals
    public List<SavingsGoalModel> getAllSavingsGoals() {
        List<SavingsGoalModel> savingsGoalList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SAVINGS_GOALS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SavingsGoalModel savingsGoal = new SavingsGoalModel(
                        cursor.getInt(0),    // id
                        cursor.getDouble(1), // goalName
                        cursor.getDouble(2), // targetAmount
                        cursor.getString(3), // currentAmount
                        cursor.getString(4), // goalDeadline
                        cursor.getString(5)  // status
                );
                savingsGoalList.add(savingsGoal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return savingsGoalList;
    }

    // Update a savings goal
    public int updateSavingsGoal(SavingsGoalModel savingsGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("goalName", savingsGoal.getGoalName());
        values.put("targetAmount", savingsGoal.getTargetAmount());
        values.put("currentAmount", savingsGoal.getCurrentAmount());
        values.put("goalDeadline", savingsGoal.getGoalDeadline());
        values.put("status", savingsGoal.getStatus());

        // Updating row
        return db.update(TABLE_SAVINGS_GOALS, values, "id = ?",
                new String[]{String.valueOf(savingsGoal.getId())});
    }
    // Delete a savings goal
    public void deleteSavingsGoal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVINGS_GOALS, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }


    //curd operations for the Reminder table

    // Insert a new reminder
    public long createReminder(RemindersModel reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", reminder.getUserId());
        values.put("reminderDate", reminder.getReminderDate());
        values.put("description", reminder.getDescription());

        // Inserting Row
        long id = db.insert(TABLE_REMINDERS, null, values);
        db.close();
        return id;
    }
    // Read a single reminder by ID
    public RemindersModel getReminderById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REMINDERS, new String[]{"id", "userId", "reminderDate", "description"},
                "id=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        RemindersModel reminder = new RemindersModel(
                cursor.getInt(0),    // id
                cursor.getInt(1),    // userId
                cursor.getString(2), // reminderDate
                cursor.getString(3)  // description
        );
        cursor.close();
        return reminder;
    }
    // Read all reminders
    public List<RemindersModel> getAllReminders() {
        List<RemindersModel> reminderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RemindersModel reminder = new RemindersModel(
                        cursor.getInt(0),    // id
                        cursor.getInt(1),    // userId
                        cursor.getString(2), // reminderDate
                        cursor.getString(3)  // description
                );
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reminderList;
    }
    // Update a reminder
    public int updateReminder(RemindersModel reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", reminder.getUserId());
        values.put("reminderDate", reminder.getReminderDate());
        values.put("description", reminder.getDescription());

        // Updating row
        return db.update(TABLE_REMINDERS, values, "id = ?",
                new String[]{String.valueOf(reminder.getId())});
    }
    // Delete a reminder
    public void deleteReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, "id = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

}
