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
public class Product {
    private String productID, supplierID, productName, categoryName;
    
    private double price;

    public Product(String productID, String supplierID, String productName, String categoryName, double price) {
        this.productID = productID;
        this.supplierID = supplierID;
        this.productName = productName;
        this.categoryName = categoryName;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getPrice() {
        return price;
    }

   
    
}
