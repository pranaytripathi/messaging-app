package com.messagingapp.configuration;

import com.messagingapp.entities.ProductTypeEntity;

import java.util.List;
import java.util.Scanner;

public class ProductConfigurationImpl implements ProductConfiguration {
    @Override
    public List<String> configure(Scanner sc) {
        System.out.println("---------Initiating Configuration-----------------\n\n");
        ProductTypeEntity productList = new ProductTypeEntity();
        System.out.println("Enter the number of products");
        while(!sc.hasNextInt()) {
            System.out.println("Please enter as a number.");
            sc.nextLine();
        }
        Integer noOfProducts = sc.nextInt();
        sc.nextLine();
        for (int i = 1; i <= noOfProducts; i++) {
            System.out.println("Enter the name of product " + i);
            productList.setProduct(sc.nextLine().trim());
        }
        System.out.println("\n\n---------------\n\nApplication configured successfully" + productList.getProductList());
        System.out.println("\n\n---------------\n\nYour products are:" + productList.getProductList());
        return productList.getProductList();
    }
}
