package com.example.the_java_bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";

    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";

    public static final String ACCOUNT_CREATION_CODE = "002";

    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been created";

    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";

    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with the provided Account Number does not exist";

    public static final String ACCOUNT_FOUND_CODE = "004";

    public static final String ACCOUNT_FOUND_MESSAGE = "User Account Found";

    public static final String ACCOUNT_CREDIT_CODE = "005";

    public static final String ACCOUNT_CREDIT_MESSAGE = "User Account Credit";

    public static final String INSUFFICIENT_BALANCE_CODE = "006";

    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";

    public static final String ACCOUNT_DEBITED_CODE = "007";

    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been sucessfully debited";

    public static final String TRANSFER_SUCCESSFUL_CODE = "008";

    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer succesfully";

    public static final String INVALID_TRANSFER_CODE = "009";

    public static final String INVALID_TRANSFER_MESSAGE = "Transfer failed";

    public static String generateAccountNumber() {
        /*
         * 2023 + ramdomSixDigits
         * 
         */

        Year currentYear = Year.now();

        int min = 100000;

        int max = 999999;

        // generate a random number between min and max

        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);

        String randomNumber = String.valueOf(randNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();

    }

}
