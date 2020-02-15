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
public class StockPharmacy {
    private String productID;
    private String supplierID;
    private String productName;
    private int categoryID;
    private int quantity;
    private double buyPrice, sellingPrice;
    private String dateSupplied;
    private String batchNumber,mfdDate,expDate;

    
    public StockPharmacy(String productID, String supplierID, String productName, int categoryID, int quantity, double buyPrice, double sellingPrice, String dateSupplied, String batchNumber, String mfdDate, String expDate) {
        this.productID = productID;
        this.supplierID = supplierID;
        this.productName = productName;
        this.categoryID = categoryID;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellingPrice = sellingPrice;
        this.dateSupplied = dateSupplied;
        this.batchNumber = batchNumber;
        this.mfdDate = mfdDate;
        this.expDate = expDate;
    }

    
    

    public StockPharmacy(String productID, String supplierID, String productName, int quantity, double buyPrice, double sellingPrice, String dateSupplied) {
        this.productID = productID;
        this.supplierID = supplierID;
        this.productName = productName;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellingPrice = sellingPrice;
        this.dateSupplied = dateSupplied;
    }

    public int getCategoryID() {
        return categoryID;
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

    public int getQuantity() {
        return quantity;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public String getDateSupplied() {
        return dateSupplied;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public String getMfdDate() {
        return mfdDate;
    }

    public String getExpDate() {
        return expDate;
    }

    
    
    
    
}
