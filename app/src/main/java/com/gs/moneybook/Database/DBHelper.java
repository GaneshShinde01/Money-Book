package com.gs.moneybook.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    private static final String COLUMN_CATEGORY_TYPE = "categoryType"; // Correct column name

    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CATEGORY_NAME + " TEXT NOT NULL,"
            + COLUMN_CATEGORY_TYPE + " TEXT NOT NULL" + ")"; // Add categoryType with NOT NULL constraint


    // Create Transactions Table
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_TRANSACTION_DATE = "date";
    private static final String COLUMN_TRANSACTION_CATEGORY_ID = "categoryId";
    private static final String COLUMN_TRANSACTION_USER_ID = "userId";
    private static final String COLUMN_TRANSACTION_TYPE = "type"; // "income" or "expense"

    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TRANSACTION_AMOUNT + " REAL NOT NULL,"
            + COLUMN_TRANSACTION_DATE + " TEXT NOT NULL,"  // Consider changing TEXT to DATETIME if possible
            + COLUMN_TRANSACTION_CATEGORY_ID + " INTEGER NOT NULL,"
            + COLUMN_TRANSACTION_USER_ID + " INTEGER NOT NULL,"
            + COLUMN_TRANSACTION_TYPE + " TEXT NOT NULL,"
            + "FOREIGN KEY (" + COLUMN_TRANSACTION_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "),"
            + "FOREIGN KEY (" + COLUMN_TRANSACTION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
            + ")";

    // Create Budgets Table
    private static final String COLUMN_BUDGET_ID = "id";
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
    private static final String COLUMN_SAVINGS_GOAL_ID = "id";
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
    private static final String COLUMN_RECURRING_TRANSACTION_ID = "id";
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


    // Category Table

    public long createCategory(String categoryName, String categoryType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        values.put(COLUMN_CATEGORY_TYPE, categoryType); // Category type: Income or Expense

        // Inserting Row
        long id = db.insert(TABLE_CATEGORIES, null, values);

        db.close(); // Closing database connection
        return id;
    }

    public CategoryModel getCategoryById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES,
                new String[]{COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME, COLUMN_CATEGORY_TYPE},
                COLUMN_CATEGORY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        CategoryModel category = new CategoryModel(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_TYPE)));

        cursor.close();
        return category;
    }

    public List<CategoryModel> getAllCategories() {
        List<CategoryModel> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CategoryModel category = new CategoryModel();
                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)));
                category.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)));
                category.setCategoryType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_TYPE)));

                categories.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return categories;
    }

    public int updateCategory(CategoryModel category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getCategoryName());
        values.put(COLUMN_CATEGORY_TYPE, category.getCategoryType()); // Update category type as well

        return db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
    }

    public void deleteCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }


    // Transactions Table - CRUD Operations

    // Create a new transaction
    public long createTransaction(String transactionName, double amount, String date, String category, String transactionType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", transactionName);
        values.put("amount", amount);
        values.put("date", date);
        values.put("category", category);
        values.put("type", transactionType); // Income or Expense

        return db.insert("transactions", null, values);
    }

    // Read a single transaction by ID
    // Read a single transaction by ID
    public TransactionModel readTransactionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("transactions", new String[]{"id", "amount", "date", "categoryId", "userId", "type"},
                "id=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        TransactionModel transaction = new TransactionModel(
                cursor.getInt(0),     // id
                cursor.getString(1),   // amount
                cursor.getString(2),   // date
                cursor.getInt(3),      // categoryId
                cursor.getString(4),      // userId
                cursor.getString(5)    // type
        );
        cursor.close();
        return transaction;
    }


    // Read all transactions
    public List<TransactionModel> readAllTransactions() {
        List<TransactionModel> transactionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM transactions";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TransactionModel transaction = new TransactionModel(
                        cursor.getInt(0), // id
                        cursor.getString(1), // name
                        cursor.getString(2), // amount
                        cursor.getDouble(3), // date
                        cursor.getString(4), // category
                        cursor.getString(5)  // type
                );
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactionList;
    }

    // Update a transaction
    public int updateTransaction(int id, String transactionName, double amount, String date, String category, String transactionType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", transactionName);
        values.put("amount", amount);
        values.put("date", date);
        values.put("category", category);
        values.put("type", transactionType);

        return db.update("transactions", values, "id=?", new String[]{String.valueOf(id)});
    }

    // Delete a transaction
    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("transactions", "id=?", new String[]{String.valueOf(id)});
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
