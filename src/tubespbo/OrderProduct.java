/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubespbo;

/**
 *
 * @author RAHMITHA MAHRUL
 */
public class OrderProduct {
    private String orderID;
    private String productID;
    private String productName;
    private String date;
    private int quantity;
    private double price;
    private double totalPrice;

    public OrderProduct(String orderID, String productID, String productName, String date, int quantity, double price, double totalPrice) {
        this.orderID = orderID;
        this.productID = productID;
        this.productName = productName;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    
    
    
    
}
