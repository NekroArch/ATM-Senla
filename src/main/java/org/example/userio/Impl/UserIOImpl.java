package org.example.userio.Impl;

import org.example.userio.Command;
import org.example.userio.UserIO;

import java.util.List;
import java.util.Scanner;

public class UserIOImpl implements UserIO {
    static Scanner terminal = new Scanner(System.in);

    private String cardNumber;

    @Override
    public String getCardNumber() {

        if(cardNumber == null){
            System.out.print("Input your card number : ");
            cardNumber = terminal.nextLine();
        }
        return cardNumber;
    }

    @Override
    public String getPin() {
        System.out.print("Input your pin : ");
        return terminal.nextLine();
    }

    @Override
    public void showMessage(String message){
        System.out.println(message);
    }

    @Override
    public Command getCommand() {
        System.out.print("> ");
        return Command.of(terminal.nextInt());
    }

    @Override
    public Float getAmount() {
        System.out.print("""
                Input the desired amount.
                >\s""");
        return terminal.nextFloat();
    }


}
