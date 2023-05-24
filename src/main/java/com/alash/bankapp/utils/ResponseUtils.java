package com.alash.bankapp.utils;

import java.util.Random;

public class ResponseUtils {

    // Handling custom responses
    public static final String USER_EXISTS_CODE = "001";
    public static final String USER_EXISTS_MESSAGE = "User with provided email already exists!";
    public static final String SUCCESS = "002";
    public static final String SUCCESS_MESSAGE = "Successfully done";
    public static final String USER_NOT_FOUND_CODE = "003";
    public static final String USER_NOT_FOUND = "This user doesn't exist";
    public static final String USER_REGISTERED_SUCCESS = "User has been successfully registered";
    public static final String SUCCESSFUL_TRANSACTION = "004";
    public static final String ACCOUNT_CREDITED = "Account has been credited";
    public static final String ACCOUNT_DEBITED = "Account has been debited";
    public static final String UNSUCCESSFUL_TRANSACTION = "005";
    public static final String INSUFFICIENT_FUNDS = "Insufficient fund";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successful";

    public static final int LENGTH_OF_ACCOUNT_NUMBER = 10;

    public static String generateAccountNumber(int len) {
        String accountNumber = "";
        int x;
        char[] stringChars = new char[len];

        for (int i = 0; i < len; i++) {
            Random random = new Random();
            x = random.nextInt(9);

            stringChars[i] = Integer.toString(x).toCharArray()[0];
        }

        accountNumber = new String(stringChars);
        return accountNumber.trim();
    }


}
