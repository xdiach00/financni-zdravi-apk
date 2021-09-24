package com.digikouc.financnizdravi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("api_token")
    private String api_token;
    private double averageSalary;
    private double otherRegularIncome;
    private double housing;
    private double services;
    private double fuel;
    private double itemsDailyConsumptions;
    private double food;
    private double loanPayment;
    private double healthCare;
    private double otherNecessaryExpenses;
    private double entertainment;
    private double restaurant;
    private double sport;
    private double shopping;
    private double hobby;
    private double travel;
    private double otherUnnecessaryExpenses;
    private double pension, emergency, children, capitalFunds, termDeposits, other, totalSaved, sum_revenues, sum_necessary_expenses, sum_unnecessary_expenses, savingsSum;
    private String firstName, lastName, email;
    private Boolean type;
    private Created created;

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public double getOtherRegularIncome() {
        return otherRegularIncome;
    }

    public void setOtherRegularIncome(double otherRegularIncome) {
        this.otherRegularIncome = otherRegularIncome;
    }

    public double getHousing() {
        return housing;
    }

    public void setHousing(double housing) {
        this.housing = housing;
    }

    public double getServices() {
        return services;
    }

    public void setServices(double services) {
        this.services = services;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getItemsDailyConsumptions() {
        return itemsDailyConsumptions;
    }

    public void setItemsDailyConsumptions(double itemsDailyConsumptions) {
        this.itemsDailyConsumptions = itemsDailyConsumptions;
    }

    public double getFood() {
        return food;
    }

    public void setFood(double food) {
        this.food = food;
    }

    public double getLoanPayment() {
        return loanPayment;
    }

    public void setLoanPayment(double loanPayment) {
        this.loanPayment = loanPayment;
    }

    public double getHealthCare() {
        return healthCare;
    }

    public void setHealthCare(double healthCare) {
        this.healthCare = healthCare;
    }

    public double getOtherNecessaryExpenses() {
        return otherNecessaryExpenses;
    }

    public void setOtherNecessaryExpenses(double otherNecessaryExpenses) {
        this.otherNecessaryExpenses = otherNecessaryExpenses;
    }

    public double getEntertainment() {
        return entertainment;
    }

    public void setEntertainment(double entertainment) {
        this.entertainment = entertainment;
    }

    public double getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(double restaurant) {
        this.restaurant = restaurant;
    }

    public double getSport() {
        return sport;
    }

    public void setSport(double sport) {
        this.sport = sport;
    }

    public double getShopping() {
        return shopping;
    }

    public void setShopping(double shopping) {
        this.shopping = shopping;
    }

    public double getHobby() {
        return hobby;
    }

    public void setHobby(double hobby) {
        this.hobby = hobby;
    }

    public double getTravel() {
        return travel;
    }

    public void setTravel(double travel) {
        this.travel = travel;
    }

    public double getOtherUnnecessaryExpenses() {
        return otherUnnecessaryExpenses;
    }

    public void setOtherUnnecessaryExpenses(double otherUnnecessaryExpenses) {
        this.otherUnnecessaryExpenses = otherUnnecessaryExpenses;
    }

    public double getPension() {
        return pension;
    }

    public void setPension(double pension) {
        this.pension = pension;
    }

    public double getEmergency() {
        return emergency;
    }

    public void setEmergency(double emergency) {
        this.emergency = emergency;
    }

    public double getChildren() {
        return children;
    }

    public void setChildren(double children) {
        this.children = children;
    }

    public double getCapitalFunds() {
        return capitalFunds;
    }

    public void setCapitalFunds(double capitalFunds) {
        this.capitalFunds = capitalFunds;
    }

    public double getTermDeposits() {
        return termDeposits;
    }

    public void setTermDeposits(double termDeposits) {
        this.termDeposits = termDeposits;
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        this.other = other;
    }

    public double getTotalSaved() {
        return totalSaved;
    }

    public void setTotalSaved(double totalSaved) {
        this.totalSaved = totalSaved;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public double getSum_revenues() {
        return sum_revenues;
    }

    public void setSum_revenues(double sum_revenues) {
        this.sum_revenues = sum_revenues;
    }

    public double getSum_necessary_expenses() {
        return sum_necessary_expenses;
    }

    public void setSum_necessary_expenses(double sum_necessary_expenses) {
        this.sum_necessary_expenses = sum_necessary_expenses;
    }

    public double getSum_unnecessary_expenses() {
        return sum_unnecessary_expenses;
    }

    public void setSum_unnecessary_expenses(double sum_unnecessary_expenses) {
        this.sum_unnecessary_expenses = sum_unnecessary_expenses;
    }

    public double getSavingsSum() {
        return savingsSum;
    }

    public void setSavingsSum(double savingsSum) {
        this.savingsSum = savingsSum;
    }

    public Created getCreated() {
        return created;
    }
}
