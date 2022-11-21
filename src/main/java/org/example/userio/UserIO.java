package org.example.userio;

import org.example.model.Card;

import java.util.List;

public interface UserIO {
    String getCardNumber();
    String getPin();
    Command getCommand();
    Float getAmount();
    void showMessage(String message);
}
