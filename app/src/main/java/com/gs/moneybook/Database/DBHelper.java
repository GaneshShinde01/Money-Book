package com.gs.moneybook.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gs.moneybook.Model.*;

public class DBHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "MoneyBook.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CATEGORIES = "categories";
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
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "categoryName";

    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CATEGORY_NAME + " TEXT" + ")";

    // Create Transactions Table
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_TRANSACTION_DATE = "date";
    private static final String COLUMN_TRANSACTION_CATEGORY_ID = "categoryId";
    private static final String COLUMN_TRANSACTION_USER_ID = "userId";
    private static final String COLUMN_TRANSACTION_TYPE = "type"; // "income" or "expense"

    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TRANSACTION_AMOUNT + " REAL,"
            + COLUMN_TRANSACTION_DATE + " TEXT,"
            + COLUMN_TRANSACTION_CATEGORY_ID + " INTEGER,"
            + COLUMN_TRANSACTION_USER_ID + " INTEGER,"
            + COLUMN_TRANSACTION_TYPE + " TEXT" + ")";

    // Create Budgets Table
    private static final String COLUMN_BUDGET_ID = "id";
    private static final String COLUMN_BUDGET_AMOUNT = "amount";
    private static final String COLUMN_BUDGET_CATEGORY_ID = "categoryId";
    private static final String COLUMN_BUDGET_USER_ID = "userId";
    private static final String COLUMN_BUDGET_DURATION = "duration"; // "monthly" or "yearly"

    private static final String CREATE_TABLE_BUDGETS = "CREATE TABLE " + TABLE_BUDGETS + "("
            + COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_BUDGET_AMOUNT + " REAL,"
            + COLUMN_BUDGET_CATEGORY_ID + " INTEGER,"
            + COLUMN_BUDGET_USER_ID + " INTEGER,"
            + COLUMN_BUDGET_DURATION + " TEXT" + ")";

    // Create Savings Goals Table
    private static final String COLUMN_SAVINGS_GOAL_ID = "id";
    private static final String COLUMN_SAVINGS_GOAL_AMOUNT = "goalAmount";
    private static final String COLUMN_SAVINGS_GOAL_USER_ID = "userId";

    private static final String CREATE_TABLE_SAVINGS_GOALS = "CREATE TABLE " + TABLE_SAVINGS_GOALS + "("
            + COLUMN_SAVINGS_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SAVINGS_GOAL_AMOUNT + " REAL,"
            + COLUMN_SAVINGS_GOAL_USER_ID + " INTEGER" + ")";

    // Create Recurring Transactions Table
    private static final String COLUMN_RECURRING_TRANSACTION_ID = "id";
    private static final String COLUMN_RECURRING_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_RECURRING_TRANSACTION_CATEGORY_ID = "categoryId";
    private static final String COLUMN_RECURRING_TRANSACTION_USER_ID = "userId";
    private static final String COLUMN_RECURRING_TRANSACTION_INTERVAL = "interval"; // "weekly", "monthly", etc.

    private static final String CREATE_TABLE_RECURRING_TRANSACTIONS = "CREATE TABLE " + TABLE_RECURRING_TRANSACTIONS + "("
            + COLUMN_RECURRING_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_RECURRING_TRANSACTION_AMOUNT + " REAL,"
            + COLUMN_RECURRING_TRANSACTION_CATEGORY_ID + " INTEGER,"
            + COLUMN_RECURRING_TRANSACTION_USER_ID + " INTEGER,"
            + COLUMN_RECURRING_TRANSACTION_INTERVAL + " TEXT" + ")";

    // Create Reminders Table
    private static final String COLUMN_REMINDER_ID = "id";
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
}
