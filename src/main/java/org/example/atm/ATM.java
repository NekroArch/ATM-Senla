package org.example.atm;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.example.userio.UserIO;

import java.io.Closeable;

public interface ATM extends Closeable {
    void login(UserIO cardInput) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;


}
