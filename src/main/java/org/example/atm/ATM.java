package org.example.atm;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.example.userio.UserIO;

public interface ATM {
    void login(UserIO cardInput) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;


}
