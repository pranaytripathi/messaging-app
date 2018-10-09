package com.messagingapp.service;

import com.messagingapp.exceptions.InvalidMessageTypeException;

import java.util.List;
import java.util.Scanner;

public interface MessagingService {
    String MESSAGE_TYPE_PATTERN_1 = "^([a-zA-Z]+\\s[0-9]+p)$";

    String MESSAGE_TYPE_PATTERN_2 = "^([0-9]+\\s[a-zA-Z]+\\s[0-9]+p)$";

    String MESSAGE_TYPE_PATTERN_3 = "^([a-zA-Z]+\\s[a-zA-Z]+\\s[0-9]+p)$";

    int PAUSE_COUNT_OF_MESSAGES = 50;

    int UPDATE_COUNT_OF_MESSAGES = 10;

    void initiateMessagingService(List<String> productList, Scanner sc);

    int getValidMessageType(String message) throws InvalidMessageTypeException;

    boolean matchMessageType(String message, String regex);
}
