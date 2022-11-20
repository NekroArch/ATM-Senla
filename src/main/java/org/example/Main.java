package org.example;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.example.atm.ATM;
import org.example.atm.Impl.ATMImpl;
import org.example.userio.Impl.UserIOImpl;
import org.example.userio.UserIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

import static org.example.atm.util.ExceptionUtil.wrapException;

public class Main {

    public static void main(String[] args) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        final String filePath = "src/main/resources/ATM-data.csv";

        UserIO cardInput = new UserIOImpl();
        Supplier<InputStream> inputStream = () -> fileInputStream(filePath);
        Supplier<OutputStream> outputStream = () -> fileOutputStream(filePath);

        ATM atm = new ATMImpl(inputStream, outputStream, 100000.234f);

        atm.login(cardInput);
    }

    private static InputStream fileInputStream(String filePath) {
        return wrapException(() ->  new FileInputStream(filePath));
    }

    private static OutputStream fileOutputStream(String filePath) {
        return wrapException(() ->  new FileOutputStream(filePath));
    }

}
