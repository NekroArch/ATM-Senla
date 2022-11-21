package org.example.atm.Impl;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import org.example.atm.ATM;
import org.example.exception.ATMWorkException;
import org.example.exception.InvalidAmountException;
import org.example.exception.InvalidCardException;
import org.example.exception.UserBlockException;
import org.example.model.Card;
import org.example.userio.UserIO;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ATMImpl implements ATM, Closeable {
    private final static String validCardPattern = "([0-9]{4}-){3}[0-9]{4}";
    private final static List<String> cardPatterns = List.of(
            "^(5018|5020|5038|6304|6759|6761|6763)[0-9]{8,15}$",
            "^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$",
            "^4[0-9]{12}(?:[0-9]{3})?$",
            "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14})$",
            "^3[47][0-9]{13}$"
    );

    private final List<Card> cardList;
    private float atmLimit;
    private final ICSVWriter csvWriter;

    private static final long dayInMillis = 86400000;

    private final InputStreamReader inputStreamReader;

    public ATMImpl(Supplier<InputStream> inputStream, Supplier<OutputStream> outputStream, float atmLimit) {
        this.inputStreamReader = new InputStreamReader(inputStream.get());
        this.cardList = new CsvToBeanBuilder<Card>(inputStreamReader)
                .withSeparator(' ')
                .withType(Card.class)
                .build()
                .parse();
        this.csvWriter = new CSVWriterBuilder(new OutputStreamWriter(outputStream.get()))
                .withSeparator(' ')
                .withQuoteChar('\0')
                .build();
        this.atmLimit = atmLimit;
    }

    public void login(UserIO cardInput) {
        try {

            checkCardPattern(cardInput.getCardNumber());

            checkCardType(cardInput.getCardNumber());

            checkPin(cardInput);

            startSession(findCard(cardInput.getCardNumber()), cardInput);


        } finally {
            endSession();

        }

    }

    private void startSession(Card card, UserIO cardInput) {
        boolean flag = true;
        while (flag) {
            cardInput.showMessage("""
                1. Check balance.
                2. Withdraw money from the account.
                3. Deposit money into the account.
                0. Exit.
                """);

            switch (cardInput.getCommand()) {
                case BALANCE -> cardInput.showMessage(card.getBalance().toString());
                case WITHDRAW -> withdrawMoney(card, cardInput);
                case DEPOSIT -> depositMoney(card, cardInput);
                case EXIT -> flag = false;
            }
        }
    }

    private void withdrawMoney(Card card, UserIO cardInput) {
        float balance = card.getBalance();
        float amount = cardInput.getAmount();
        if (amount > atmLimit && amount > balance) {
            throw new InvalidAmountException("Unable to withdraw this amount. Please try to input less.");
        }
        atmLimit -= amount;
        card.setBalance(balance - amount);
        cardInput.showMessage("Your operation was successful");
    }

    private void depositMoney(Card card, UserIO cardInput) {
        float amount = cardInput.getAmount();
        if (amount >= 1000000) {
            throw new InvalidAmountException("Unable to deposit this amount (limit of 1,000,000). Please try to input less.");
        }
        atmLimit += amount;
        card.setBalance(card.getBalance() + amount);
        cardInput.showMessage("Your operation was successful");
    }

    private void endSession() {
        csvWriter.writeAll(
                Stream.concat(
                        Stream.<String[]>of(Card.getCsvHeader()),
                        cardList.stream().map(Card::toCsvStrings)
                ).toList()
        );
        csvWriter.flushQuietly();
    }

    private Card findCard(String cardNum) {
        return cardList.stream()
                .filter(x -> x.getCardNumber().equals(cardNum))
                .findAny()
                .orElseThrow(() -> new InvalidCardException("Card not found"));
    }

    private void checkCardPattern(String cardNum) {
        if (!cardNum.matches(validCardPattern)) {
            throw new InvalidCardException("Invalid card pattern!");
        }
    }

    private void checkCardType(String cardNum) {
        String cardDigits = cardNum.replaceAll("-", "");
        cardPatterns.stream()
                .filter(cardDigits::matches)
                .findAny().orElseThrow(() -> new InvalidCardException("Invalid card type"));
    }
    private void checkPin(UserIO cardInput) {

        Card card = findCard(cardInput.getCardNumber());

        if (card.isLocked() && new Date().getTime() - card.getDate() < dayInMillis) {
            throw new UserBlockException("Your card is blocked");
        }

        card.unlock();

        for (int i = 0; i < 3; i++) {
            if (Objects.equals(cardInput.getPin(), card.getPin())) {
                return;
            }
            if (i != 2) {
                cardInput.showMessage("Incorrect pin! Try again ");
            }
        }
        card.lock();
        throw new UserBlockException("Failed to input pin. Card is block");
    }

    @Override
    public void close() {
        try {
            inputStreamReader.close();
            csvWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
