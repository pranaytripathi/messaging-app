package com.messagingapp.entities;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeEntity {
    private List<String> productList = new ArrayList<String>();

    public List<String> getProductList() {
        return productList;
    }

    public void setProduct(String product) {
        productList.add(product);
    }
}
