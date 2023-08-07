package ir.mahchegroup.tickvision.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vision_tbl")
public class ModelVision {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title, date_vision, amount, day_vision, day_amount, day_pass, day_rest, income_amount, rest_amount, income, payment, profit, rest, milli_sec, is_tick;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_vision() {
        return date_vision;
    }

    public void setDate_vision(String date_vision) {
        this.date_vision = date_vision;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDay_vision() {
        return day_vision;
    }

    public void setDay_vision(String day_vision) {
        this.day_vision = day_vision;
    }

    public String getDay_amount() {
        return day_amount;
    }

    public void setDay_amount(String day_amount) {
        this.day_amount = day_amount;
    }

    public String getDay_pass() {
        return day_pass;
    }

    public void setDay_pass(String day_pass) {
        this.day_pass = day_pass;
    }

    public String getDay_rest() {
        return day_rest;
    }

    public void setDay_rest(String day_rest) {
        this.day_rest = day_rest;
    }

    public String getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(String income_amount) {
        this.income_amount = income_amount;
    }

    public String getRest_amount() {
        return rest_amount;
    }

    public void setRest_amount(String rest_amount) {
        this.rest_amount = rest_amount;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public String getMilli_sec() {
        return milli_sec;
    }

    public void setMilli_sec(String milli_sec) {
        this.milli_sec = milli_sec;
    }

    public String getIs_tick() {
        return is_tick;
    }

    public void setIs_tick(String is_tick) {
        this.is_tick = is_tick;
    }
}
