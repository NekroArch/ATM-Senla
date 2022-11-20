package org.example.userio;

public interface UserIO {
    String getCardNumber();
    String getPin();
    void showMenu();
    Command getCommand();
    Float getAmount();
}
