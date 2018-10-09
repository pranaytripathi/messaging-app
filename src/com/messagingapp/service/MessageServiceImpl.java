package com.messagingapp.service;

import com.messagingapp.entities.AdjustmentRecordEntity;
import com.messagingapp.entities.SalesDetailsEntity;
import com.messagingapp.exceptions.InvalidAdjustmentException;
import com.messagingapp.exceptions.InvalidMessageTypeException;
import com.messagingapp.exceptions.InvalidProductTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessageServiceImpl implements MessagingService {
    List<SalesDetailsEntity> salesDetailsEntityList = new ArrayList<SalesDetailsEntity>();
    List<AdjustmentRecordEntity> adjustmentRecordEntityList = new ArrayList<AdjustmentRecordEntity>();

    /**
     * display valid messaging configuration.
     */
    private void displayConfiguration() {
        System.out.println(
                "\n\n-------- Initiating Application ---------\n\n"
                        + "\n Following are the valid sales type messages \n\n"
                        + "Message Type 1: product_name sales_value \n"
                        + "Example: apples 20p \n\n"
                        + "Message Type 2: no_of_sales product_name sales_value \n"
                        + "Example: 20 apples 20 \n\n"
                        + "Message Type 3: adjustment product_name adjustment_value \n"
                        + "Example: Add apples 20p \n\n"
        );
    }

    /**
     * initiates messaging service once the products are stored.
     *
     * @param productList
     */
    @Override
    public void initiateMessagingService(List<String> productList, Scanner sc) {
        int countOfMessages = 1;
        displayConfiguration();
        System.out.println("\n\n ------------ Started Messaging Service---------");
        while (countOfMessages <= PAUSE_COUNT_OF_MESSAGES) {
            System.out.println("Please enter you message in the above mentioned format:");
            String message = sc.nextLine();
            try {
                recordMessage(message.trim(), getValidMessageType(message), productList);
                if (countOfMessages % UPDATE_COUNT_OF_MESSAGES == 0) {
                    showSalesDetails(productList);
                }
                System.out.println("\n Sale message processed successfully");
                if (countOfMessages == PAUSE_COUNT_OF_MESSAGES) showAdjustments();
                System.out.println("\n Total message processed: " + countOfMessages);
                countOfMessages++;
            } catch (InvalidAdjustmentException | InvalidProductTypeException | InvalidMessageTypeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * @param message Message Input
     * @return Message Type as Integer. For example if the pattern matches type 1 then returns message as type 1.
     * @throws InvalidMessageTypeException
     */
    @Override
    public int getValidMessageType(String message) throws InvalidMessageTypeException {
        if (matchMessageType(message, MESSAGE_TYPE_PATTERN_1)) return 1;
        else if (matchMessageType(message, MESSAGE_TYPE_PATTERN_2)) return 2;
        else if (matchMessageType(message, MESSAGE_TYPE_PATTERN_3)) return 3;
        else
            throw new InvalidMessageTypeException("Message type not valid. Please enter the valid type message again.");
    }

    /**
     * @param message Message Input
     * @param regex   Regular Expression matching pattern.
     * @return boolean value if the message matches the pattern.
     */
    @Override
    public boolean matchMessageType(String message, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(message);
        return m.find();
    }

    /**
     * Processes and records messages as per message type
     *
     * @param message
     */

    private void recordMessage(String message, int messageType, List<String> productList) throws InvalidProductTypeException, InvalidAdjustmentException, InvalidMessageTypeException {
        if (messageType == 3 && salesDetailsEntityList.size() < 1) {
            throw new InvalidMessageTypeException("Invalid message. Please add sales to the product before performing the adjustment");
        }
        String[] messageGroup = message.split("\\s");
        switch (messageType) {
            case 1:
                if (productList.indexOf(messageGroup[0]) == -1)
                    throw new InvalidProductTypeException("The product type does not exist. Please enter a valid product type in message");
                salesDetailsEntityList.add(new SalesDetailsEntity()
                        .setProduct(messageGroup[0])
                        .setSalesValue(Double.parseDouble(messageGroup[1].substring(0, messageGroup[1].length() - 1)))
                        .setSalesCount(1));
                break;
            case 2:
                if (productList.indexOf(messageGroup[1]) == -1)
                    throw new InvalidProductTypeException("The product type does not exist. Please enter a valid product type in message");
                salesDetailsEntityList.add(new SalesDetailsEntity()
                        .setProduct(messageGroup[1])
                        .setSalesValue(Double.parseDouble(messageGroup[2].substring(0, messageGroup[2].length() - 1)))
                        .setSalesCount(Integer.parseInt(messageGroup[0])));
                break;
            case 3:
                if (productList.indexOf(messageGroup[1]) == -1)
                    throw new InvalidProductTypeException("The product type does not exist. Please enter a valid product type in message");
                List<SalesDetailsEntity> filteredSales = salesDetailsEntityList.stream()
                        .filter(sale -> sale.getProduct().equals(messageGroup[1]))
                        .collect(Collectors.toList());
                if (filteredSales.size() == 0) {
                    throw new InvalidMessageTypeException("Invalid message. Please add sales to the product before performing the adjustment");
                }
                for (SalesDetailsEntity sale : filteredSales) {
                    switch (messageGroup[0]) {
                        case "Add":
                            sale.setSalesValue(sale.getSalesValue() + Double.parseDouble(messageGroup[2].substring(0, messageGroup[2].length() - 1)));
                            adjustmentRecordEntityList.add(new AdjustmentRecordEntity().setAdjustmentMessage("Added " + messageGroup[2] + " to product " + sale.getProduct()));
                            break;
                        case "Substract":
                            sale.setSalesValue(sale.getSalesValue() - Double.parseDouble(messageGroup[2].substring(0, messageGroup[2].length() - 1)));
                            adjustmentRecordEntityList.add(new AdjustmentRecordEntity().setAdjustmentMessage("Subtracted " + messageGroup[2] + " to product " + sale.getProduct()));
                            break;
                        case "Multiply":
                            sale.setSalesValue(sale.getSalesValue() * Double.parseDouble(messageGroup[2].substring(0, messageGroup[2].length() - 1)));
                            adjustmentRecordEntityList.add(new AdjustmentRecordEntity().setAdjustmentMessage("Multiplied " + messageGroup[2] + " to product " + sale.getProduct()));
                            break;
                        default:
                            throw new InvalidAdjustmentException("The given adjustment is not valid. Following are your adjustment options:"
                                    + "\n 1. Add"
                                    + "\n 2. Substract"
                                    + "\n 3. Multiply");

                    }
                }

        }
    }

    /**
     * @param productList Prints the details of Sales on every @UPDATE_COUNT_OF_MESSAGES
     */

    private void showSalesDetails(List<String> productList) {
        for (String product : productList) {
            double totalSalesValue = 0;
            int totalSales = 0;
            List<SalesDetailsEntity> filteredSales = salesDetailsEntityList.stream()
                    .filter(sale -> sale.getProduct().equals(product))
                    .collect(Collectors.toList());
            for (SalesDetailsEntity sale : filteredSales) {
                totalSales = totalSales + sale.getSalesCount();
                totalSalesValue = totalSalesValue + sale.getSalesValue() * sale.getSalesCount();

            }
            System.out.println("\n\n Product: " + product +
                    " Total Sales: " + totalSales +
                    " Total Sale Value: £" + totalSalesValue / 100
            );

        }
    }

    /**
     * Prints all the adjustments made when the sale counts is equal to PAUSE_COUNT_OF_MESSAGES
     */
    private void showAdjustments() {
        System.out.println("\n\n The processing has stopped now. " +
                "Here are all the adjustment made to sales");
        for (AdjustmentRecordEntity adjustment : adjustmentRecordEntityList) {
            System.out.println("\n" + adjustment.getAdjustmentMessage());
        }

    }
}
