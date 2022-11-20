package org.example.model;

import com.opencsv.bean.CsvBindByName;

import java.util.Date;

public class Card {
    @CsvBindByName(column = "Карта")
    private String cardNumber;
    @CsvBindByName(column = "Пин")
    private String pin;
    @CsvBindByName(column = "Баланс")
    private Float balance;
    @CsvBindByName(column = "Блокировка")
    private Boolean locked;
    @CsvBindByName(column = "Разблокировка")
    private Long date;

    public Card(String cardNumber, String pin, Float balance, Boolean locked, Long date) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
        this.locked = locked;
        this.date = date;
    }

    public Card() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public Long getDate() {
        return date;
    }

    public Boolean isLocked() {
        return locked;
    }

    public Float getBalance() {
        return balance;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setLocked(Boolean status) {
        this.locked = status;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    private static final String[] csvHeader = new String[] { "Карта", "Пин", "Баланс", "Блокировка", "Разблокировка" };

    public void lock(){
        locked = true;
        date = new Date().getTime();
    }

    public void unlock(){
        locked = false;
        date = 0L;
    }
    public static String[] getCsvHeader() {
        return csvHeader;
    }

    public String[] toCsvStrings() {
        return new String[] { cardNumber, pin, balance.toString(), locked.toString(), date.toString()};
    }
}
