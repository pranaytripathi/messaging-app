package com.messagingapp.entities;

public class SalesDetailsEntity {

    private String product;
    private double salesValue;
    private int salesCount;


    public double getSalesValue() {
        return salesValue;
    }

    public SalesDetailsEntity setSalesValue(double salesValue) {
        this.salesValue = salesValue;
        return this;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public SalesDetailsEntity setSalesCount(int salesCount) {
        this.salesCount = salesCount;
        return this;
    }

    public String getProduct() {
        return product;
    }

    public SalesDetailsEntity setProduct(String product) {
        this.product = product;
        return this;
    }
}
