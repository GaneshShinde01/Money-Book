package com.gs.moneybook.Model;

public class CategoryModel {
    private int id;
    private String categoryName;
    private String categoryType; // E.g., Income or Expense

    public CategoryModel(int id, String categoryName, String categoryType) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public CategoryModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
