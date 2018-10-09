package com.messagingapp;

import com.messagingapp.configuration.ProductConfiguration;
import com.messagingapp.configuration.ProductConfigurationImpl;
import com.messagingapp.service.MessageServiceImpl;
import com.messagingapp.service.MessagingService;

import java.util.Scanner;


public class MessagingApplication {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            ProductConfiguration configuration = new ProductConfigurationImpl();
            MessagingService messagingService = new MessageServiceImpl();
            messagingService.initiateMessagingService(configuration.configure(sc), sc);
        }
    }
}
