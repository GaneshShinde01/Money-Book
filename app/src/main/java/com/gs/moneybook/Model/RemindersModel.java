package com.gs.moneybook.Model;

public class RemindersModel {

        private int id;
        private int userId;
        private String reminderDate;
        private String description;

    public RemindersModel(int id, int userId, String reminderDate, String description) {
        this.id = id;
        this.userId = userId;
        this.reminderDate = reminderDate;
        this.description = description;
    }

    public RemindersModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
