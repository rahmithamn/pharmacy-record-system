/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubespbo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RAHMITHA MAHRUL
 */
public class MainMenuJF extends javax.swing.JFrame {
    private DefaultTableModel modelStock = new DefaultTableModel();
    private DefaultTableModel modelProduct = new DefaultTableModel();
    private DefaultTableModel modelSupplier = new DefaultTableModel();
    private DefaultTableModel modelCategory = new DefaultTableModel();
    private DefaultTableModel modelOrder = new DefaultTableModel();
    private com.mysql.jdbc.Connection conn;
    private ArrayList<StockPharmacy> stockPh;
    private ArrayList<Product> productPh;
    private ArrayList<Supplier> supplierPh;
    private ArrayList<Category> categoryPh;
    private ArrayList<OrderProduct> orderPh;
    
    private String orderid = "1";
    /**
     * Creates new form MainMenuJF
     */
    public MainMenuJF() {
        initComponents();
        
        scrollPaneStock.getViewport().setBackground(Color.WHITE);
        scrollPaneProduct.getViewport().setBackground(Color.WHITE);
        scrollPaneSupplier.getViewport().setBackground(Color.WHITE);
        orderTableScrollPane.getViewport().setBackground(Color.WHITE);
        
        //change table stock design
        stockTable.getTableHeader().setFont(new Font("Roboto Lt", Font.PLAIN, 14));
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)stockTable.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(0);
        stockTable.getTableHeader().setPreferredSize(new Dimension(scrollPaneStock.getWidth(), 40));
        
        
         //change table product design
        productTable.getTableHeader().setFont(new Font("Roboto Lt", Font.PLAIN, 14));
        DefaultTableCellRenderer renderer2 = (DefaultTableCellRenderer)productTable.getTableHeader().getDefaultRenderer();
        renderer2.setHorizontalAlignment(0);
        productTable.getTableHeader().setPreferredSize(new Dimension(scrollPaneProduct.getWidth(), 40));
        
        
         //change table supplier design
        supplierTable.getTableHeader().setFont(new Font("Roboto Lt", Font.PLAIN, 14));
        DefaultTableCellRenderer renderer3 = (DefaultTableCellRenderer)supplierTable.getTableHeader().getDefaultRenderer();
        renderer3.setHorizontalAlignment(0);
        supplierTable.getTableHeader().setPreferredSize(new Dimension(scrollPaneProduct.getWidth(), 40));
        
        //change table category design
        categoryTable.getTableHeader().setFont(new Font("Roboto Lt", Font.PLAIN, 14));
        DefaultTableCellRenderer renderer5 = (DefaultTableCellRenderer)categoryTable.getTableHeader().getDefaultRenderer();
        renderer5.setHorizontalAlignment(0);
        categoryTable.getTableHeader().setPreferredSize(new Dimension(scrollPaneCategory.getWidth(), 40));
        
        
        //change table order design
        orderTable.getTableHeader().setFont(new Font("Roboto Lt", Font.PLAIN, 14));
        DefaultTableCellRenderer renderer4 = (DefaultTableCellRenderer)orderTable.getTableHeader().getDefaultRenderer();
        renderer4.setHorizontalAlignment(0);
        orderTable.getTableHeader().setPreferredSize(new Dimension(orderHistoryScrollPane.getWidth(), 40));
        
        
        //view Table content
       
        this.setLocationRelativeTo(null);
        
        //set Stock Table content
        loadColumnStock();
        stockTable.setModel(modelStock);
        conn = Connection.openConnection();
        loadStockPharmacy();
        viewStock();
        
        //set product Table content
        loadColumnProduct();
        productTable.setModel(modelProduct);
        loadProductPharmacy();
        viewProduct();
        
        //set supplier table content
        loadColumnSupplier();
        supplierTable.setModel(modelSupplier);
        loadSupplierPharmacy();
        viewSupplier();
        
        //set category table content
        loadColumnCategory();
        categoryTable.setModel(modelCategory);
        loadCategoryPharmacy();
        viewCategory();
        
        //set order table content
        loadColumnOrder();
        orderTable.setModel(modelOrder);
        loadOrderPharmacy();
        viewOrder();
        
        //visibility of button
        
        addSupplierBtn.setVisible(false);
        addProductBtn.setVisible(false);
        
        //visibility of panels
        cashRegisterPanel.setVisible(false);
        
        //combo box
        supplierComboBox();
        selectCategoryComboBox();
    }
private void loadColumnProduct() {
        modelProduct.addColumn("Product ID");
        modelProduct.addColumn("Supplier ID");
        modelProduct.addColumn("Product Name");
        modelProduct.addColumn("Category ID");
        modelProduct.addColumn("Price");
    }
    private void loadColumnSupplier() {
        modelSupplier.addColumn("Supplier ID");
        modelSupplier.addColumn("Supplier Name");
        modelSupplier.addColumn("Supplier Contact");
        modelSupplier.addColumn("Supplier Address");
    }
    private void loadColumnStock() {
        modelStock.addColumn("Product ID");
        modelStock.addColumn("Supplier ID");
        modelStock.addColumn("Product Name");
        modelStock.addColumn("Quantity");
        modelStock.addColumn("Buy Price/Pcs");
        modelStock.addColumn("Selling Price/Pcs");
        modelStock.addColumn("Data Supplied");
    
    }
    private void loadColumnCategory(){
        modelCategory.addColumn("Category ID");
        modelCategory.addColumn("Category Name");
    }
     private void loadColumnOrder() {
        modelOrder.addColumn("Order ID");
        modelOrder.addColumn("Product ID");
        modelOrder.addColumn("Product Name");
        modelOrder.addColumn("Date");
        modelOrder.addColumn("Quantity");
        modelOrder.addColumn("Price");
        modelOrder.addColumn("Total Price");
    }
    private void supplierComboBox() {
        for(int i = 0; i < supplierPh.size(); i++) {
                supplierIDCombo.addItem(supplierPh.get(i).getSupplierID() + " - " + supplierPh.get(i).getSupplierName());
                supplierIDCombo1.addItem(supplierPh.get(i).getSupplierID() + " - " + supplierPh.get(i).getSupplierName());
        }
    }
    private void selectCategoryComboBox() {
        for(int i = 0; i < categoryPh.size(); i++) {
                selectCategory.addItem(categoryPh.get(i).getCategoryID() + " - " + categoryPh.get(i).getCategoryName());
                selectCategory1.addItem(categoryPh.get(i).getCategoryID() + " - " + categoryPh.get(i).getCategoryName());
            
        }
    }
    private void loadStockPharmacy() {
        if(conn!=null) {
            stockPh = new ArrayList<>();
            String kueri = "SELECT * FROM stock;";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String idProduct = rs.getString("product_id");
                    String idSupplier = rs.getString("supplier_id");
                    String nameProduct = rs.getString("product_name");
                    int categoryid = rs.getInt("category_id");
                    int qty = rs.getInt("quantity");
                    double priceBuy = rs.getDouble("buying_price");
                    double priceSell = rs.getDouble("selling_price");
                    String supplyDate = rs.getString("date_supplied");
                    String batchNumber = rs.getString("batch_number");
                    String mfdDate = rs.getString("mfd_date");
                    String expDate = rs.getString("exp_date");
                    StockPharmacy stock = new StockPharmacy(idProduct, idSupplier, nameProduct, categoryid, qty, priceBuy, priceSell, supplyDate, batchNumber, mfdDate, expDate);
                    stockPh.add(stock);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
    }
    private void viewStock() {
        modelStock.setRowCount(0);
        for(StockPharmacy b:stockPh) {
            modelStock.addRow(new Object[]{b.getProductID(), b.getSupplierID(), b.getProductName(), b.getQuantity(), b.getBuyPrice(), b.getSellingPrice(), b.getDateSupplied()});
            
        }
    }
    private void loadProductPharmacy() {
        if(conn!=null) {
            productPh = new ArrayList<>();
            String kueri = "SELECT a.product_id, a.supplier_id, a.product_name, b.category_name, a.price FROM product a JOIN category b ON a.category_id = b.category_id;";
            
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                
                ResultSet rs = ps.executeQuery();
               
                while (rs.next()) {
                    String idProduct = rs.getString("product_id");
                    String idSupplier = rs.getString("supplier_id");
                    String nameProduct = rs.getString("product_name");
                    String categoryName = rs.getString("category_name");
                    
                    
                    double priceBuy = rs.getDouble("price");
                    
                    
                    Product product = new Product(idProduct, idSupplier, nameProduct, categoryName, priceBuy);
                    productPh.add(product);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
    }
    private void viewProduct() {
        modelProduct.setRowCount(0);
        for(Product p:productPh) {
            modelProduct.addRow(new Object[]{p.getProductID(), p.getSupplierID(), p.getProductName(), p.getCategoryName(), p.getPrice()});
            
        }
    }
    private void loadSupplierPharmacy() {
        if(conn!=null) {
            supplierPh = new ArrayList<>();
            String kueri = "SELECT * FROM supplier;";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String idSupplier = rs.getString("supplier_id");
                    String nameSupplier = rs.getString("supplier_name");
                    String supplierContact = rs.getString("supplier_contact");
                    String supplierAddress = rs.getString("supplier_address");
                    Supplier supplier = new Supplier(idSupplier, nameSupplier, supplierContact, supplierAddress);
                    supplierPh.add(supplier);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
    }
    private void viewSupplier() {
        modelSupplier.setRowCount(0);
        for(Supplier s:supplierPh) {
            modelSupplier.addRow(new Object[]{s.getSupplierID(), s.getSupplierName(), s.getSupplierContact(), s.getSupplierAddress()});
            
        }
    }
    private void loadCategoryPharmacy() {
        if(conn!=null) {
            categoryPh = new ArrayList<>();
            String kueri = "SELECT * FROM category;";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int idCategory = rs.getInt("category_id");
                    String nameCategory = rs.getString("category_name");
                    
                    Category category = new Category(idCategory, nameCategory);
                    categoryPh.add(category);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
    }
    private void viewCategory() {
        modelCategory.setRowCount(0);
        for(Category c:categoryPh) {
            modelCategory.addRow(new Object[]{c.getCategoryID(), c.getCategoryName()});
            
        }
    }
    private void loadOrderPharmacy() {
        if(conn!=null) {
            orderPh = new ArrayList<>();
            String kueri = "SELECT * FROM orders;";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String idOrder = rs.getString("order_id");
                    String idProduct = rs.getString("product_id");
                    String nameProduct = rs.getString("product_name");
                    int qty = rs.getInt("quantity");
                    double priceSell = rs.getDouble("price");
                    String date = rs.getString("date");
                    double totalPrice = rs.getDouble("total_price");
                  
                    OrderProduct order = new OrderProduct(idOrder, idProduct, nameProduct, date, qty, priceSell, totalPrice);
                    orderPh.add(order);
                    
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
    }
    private void viewOrder() {
        modelOrder.setRowCount(0);
        for(OrderProduct o:orderPh) {
            modelOrder.addRow(new Object[]{o.getOrderID(), o.getProductID(), o.getProductName(), o.getDate(), o.getQuantity(), o.getPrice(), o.getTotalPrice()});
            
        }
    }
    private void clickedDetailRow() {
        int clickedRow = stockTable.getSelectedRow();
        conn = Connection.openConnection();
        productIDLabel.setText(modelStock.getValueAt(clickedRow, 0).toString());
        productNameLabel.setText(modelStock.getValueAt(clickedRow, 2).toString());
        idSupplierLabel1.setText(modelStock.getValueAt(clickedRow, 1).toString());
        buyPriceLabel1.setText(modelStock.getValueAt(clickedRow, 4).toString());
        sellingPriceLabel1.setText(modelStock.getValueAt(clickedRow, 5).toString());
        quantityLabel1.setText(modelStock.getValueAt(clickedRow, 3).toString());
        dateSuppliedLabel1.setText(modelStock.getValueAt(clickedRow, 6).toString());
        
        String idSupplierStock = modelStock.getValueAt(clickedRow, 1).toString();
        String idProductStock = modelStock.getValueAt(clickedRow, 0).toString();
        
        for(int i = 0; i < supplierPh.size(); i++) {
            if(idSupplierStock.equals(supplierPh.get(i).getSupplierID())) {
                supplierNameLabel1.setText(supplierPh.get(i).getSupplierName());
                supplierContactLabel1.setText(supplierPh.get(i).getSupplierContact());
                supplierAddressLabel1.setText(supplierPh.get(i).getSupplierAddress());
            }
        }
        
        for(int i = 0; i < stockPh.size(); i++) {
            if(idProductStock.equals(stockPh.get(i).getProductID())) {
                mfdDateLabel1.setText(stockPh.get(i).getMfdDate());
                expiredDateLabel1.setText(stockPh.get(i).getExpDate());
                BatchNumberLabel.setText(stockPh.get(i).getBatchNumber());
            }
        }
        for(int i = 0; i < productPh.size(); i++) {
            if(idProductStock.equals(productPh.get(i).getProductID())) {
                categoryProductLabel1.setText(productPh.get(i).getCategoryName());
            }
        }
        
    }
    private void deleteClickedRowStock(String product_id) {
        if(conn != null) {
            try {
                String kueri = "DELETE FROM stock WHERE product_id = ?;";
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(1, product_id);
               
                
                int hasil = ps.executeUpdate();
                if(hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Stock Deleted");
                    
                    loadStockPharmacy(); viewStock();
                }
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void deleteClickedRowProduct(String product_id) {
        if(conn != null) {
            try {
                String kueri = "DELETE FROM product WHERE product_id = ?;";
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(1, product_id);
               
                
                int hasil = ps.executeUpdate();
                if(hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Product Deleted");
                    
                    loadProductPharmacy(); viewProduct();
                }
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void deleteClickedRowSupplier(String supplier_id) {
        if(conn != null) {
            try {
                String kueri = "DELETE FROM supplier WHERE supplier_id = ?;";
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(1, supplier_id);
               
                
                int hasil = ps.executeUpdate();
                if(hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Supplier Deleted");
                    
                    loadSupplierPharmacy(); viewSupplier();
                }
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void addStock(String product_id, String supplier_id, String product_name, int category_id, int quantity, double buying_price, double selling_price, String date_supplied, String batch_number, String mfd_date, String exp_date) {
        if(conn != null) {
            try {
                String kueri = "INSERT INTO stock(product_id, supplier_id, product_name, category_id, quantity, buying_price, selling_price, date_supplied, batch_number, mfd_date, exp_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(1, product_id);
                ps.setString(2, supplier_id);
                ps.setString(3, product_name);
                ps.setInt(4, category_id);
                ps.setInt(5, quantity);
                ps.setDouble(6, buying_price);
                ps.setDouble(7, selling_price);
                ps.setString(8, date_supplied);
                ps.setString(9, batch_number);
                ps.setString(10, mfd_date);
                ps.setString(11, exp_date);
                
                int hasil = ps.executeUpdate();
                if(hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Stock Inputted");
                    
                    loadStockPharmacy(); viewStock();
                }
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void addProduct(String product_id, String supplier_id, String product_name, int category_id, double price) {
        if(conn != null) {
            try {
                String kueri = "INSERT INTO product(product_id, supplier_id, product_name, category_id, price) VALUES (?, ?, ?, ?, ?);";
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(1, product_id);
                ps.setString(2, supplier_id);
                ps.setString(3, product_name);
                ps.setInt(4, category_id);
                ps.setDouble(5, price);
                
                
                int hasil = ps.executeUpdate();
                if(hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Product Inputted!");
                    
                    loadProductPharmacy(); viewProduct();
                }
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void addSupplier(String supplier_id, String supplier_name, String supplier_contact, String supplier_address) {
        if(conn != null) {
            try {
                String kueri = "INSERT INTO supplier(supplier_id, supplier_name, supplier_contact, supplier_address) VALUES (?, ?, ?, ?);";
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(1, supplier_id);
                ps.setString(2, supplier_name);
                ps.setString(3, supplier_contact);
                ps.setString(4, supplier_address);
                
                
                
                int hasil = ps.executeUpdate();
                if(hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Supplier Inputted!");
                    
                    loadSupplierPharmacy(); viewSupplier();
                }
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void resetAddStock() {
        productIDTf.setText("");
        productNameTf.setText("");
        selectCategory.setSelectedIndex(0);
        quantityTf.setText("");
        buyingPriceTf.setText("");
        sellingPriceTf.setText("");
        supplierIDCombo.setSelectedIndex(0);
        dateSuppliedPicker.setDate(null);
        mfdDatePicker.setDate(null);
        batchNumberTf.setText("");
        expiredDatePicker.setDate(null);
    }
     private void resetAddProduct() {
        productIDTf1.setText("");
        productNameTf1.setText("");
        sellingPriceTf1.setText("");
        selectCategory1.setSelectedIndex(0);
        supplierIDCombo1.setSelectedIndex(0);
    }
      private void resetAddSupplier() {
        supplierIDTf.setText("");
        supplierNameTf.setText("");
        supplierContactTf.setText("");
        supplierAddressTA.setText("");
        
    }
    private void searchStockbyKeyword(String keyword) {
        if(conn != null) {
            stockPh = new ArrayList<>();
            String kueri = "SELECT * FROM stock WHERE product_id LIKE ?";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(1, "%"+keyword+"%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String idProduct = rs.getString("product_id");
                    String idSupplier = rs.getString("supplier_id");
                    String nameProduct = rs.getString("product_name");
                    int qty = rs.getInt("quantity");
                    double priceBuy = rs.getDouble("buying_price");
                    double priceSell = rs.getDouble("selling_price");
                    String supplyDate = rs.getString("date_supplied");
                    StockPharmacy stock = new StockPharmacy(idProduct, idSupplier, nameProduct, qty, priceBuy, priceSell, supplyDate);
                    stockPh.add(stock);
                }
                rs.close();
                rs.close();
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                
    }
    private void addOrders(String order_id, String product_id, String product_name, String date, int quantity, double price, double priceTotal, int qtyAfter) {
        if(conn != null) {
            try {
                
               
                String kueri = "INSERT INTO orders(order_id, product_id, product_name, date, quantity, price, total_price) VALUES (?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement ps = conn.prepareStatement(kueri);
                
                
                
                
                ps.setString(1, order_id);
                ps.setString(2, product_id);
                ps.setString(3, product_name);
                ps.setString(4, date);
                ps.setInt(5, quantity);
                ps.setDouble(6, price);
                ps.setDouble(7, priceTotal);
                
                int hasil = ps.executeUpdate();
                if(hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Order Inputted");
                    
                    
                }
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void changeQuantity(int quantity, String product_id) {
        if(conn != null) {
            try {
                String kueri = "UPDATE stock SET quantity = ? WHERE product_id = ?;";
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(2, product_id);
                ps.setInt(1, quantity);
                
                
                int hasil = ps.executeUpdate();
                if(hasil > 0) {
                    
                    
                    loadStockPharmacy(); viewStock();
                }
            } catch(SQLException ex) {
                Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        sidePanel = new javax.swing.JPanel();
        sideMenuDatabase = new javax.swing.JPanel();
        logoDatabase = new javax.swing.JLabel();
        databaseLabel = new javax.swing.JLabel();
        pharmacyLogo = new javax.swing.JLabel();
        sideMenuCashRegister = new javax.swing.JPanel();
        logoCashRegister = new javax.swing.JLabel();
        cashRegisterLabel = new javax.swing.JLabel();
        closeMinimizePanel = new javax.swing.JPanel();
        DisplayMenu = new javax.swing.JPanel();
        DatabasePanelCard = new javax.swing.JPanel();
        DatabasePanelBox = new javax.swing.JPanel();
        upperPanel = new javax.swing.JPanel();
        searchBar = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        addStockBtn = new javax.swing.JLabel();
        addProductBtn = new javax.swing.JLabel();
        addSupplierBtn = new javax.swing.JLabel();
        viewMenuPanel = new javax.swing.JPanel();
        viewStocksBtn = new javax.swing.JLabel();
        viewProductsBtn = new javax.swing.JLabel();
        viewSupplierBtn = new javax.swing.JLabel();
        viewRowStockBtn = new javax.swing.JLabel();
        viewCategoryBtn = new javax.swing.JLabel();
        deleteStockRowBtn = new javax.swing.JLabel();
        deleteProductRowBtn = new javax.swing.JLabel();
        deleteSupplierRowBtn = new javax.swing.JLabel();
        viewTable = new javax.swing.JPanel();
        tabPanel = new javax.swing.JPanel();
        stocksPanel = new javax.swing.JPanel();
        scrollPaneStock = new javax.swing.JScrollPane();
        stockTable = new javax.swing.JTable();
        addStocksPanel = new javax.swing.JPanel();
        addStockBox = new javax.swing.JPanel();
        addStockTitle = new javax.swing.JLabel();
        sellingPriceTf = new javax.swing.JTextField();
        sellingPriceTfLabel = new javax.swing.JLabel();
        supplierIDTfLabel = new javax.swing.JLabel();
        productIDTf = new javax.swing.JTextField();
        expiredDateTfLabel = new javax.swing.JLabel();
        selectCategory = new javax.swing.JComboBox();
        productNameTf = new javax.swing.JTextField();
        dollarSignLabel2 = new javax.swing.JLabel();
        quantityTfLabel = new javax.swing.JLabel();
        buyingPriceTfLabel = new javax.swing.JLabel();
        quantityTf = new javax.swing.JTextField();
        buyingPriceTf = new javax.swing.JTextField();
        categoryIDTfLabel = new javax.swing.JLabel();
        dollarSign1Label = new javax.swing.JLabel();
        productIDTfLabel = new javax.swing.JLabel();
        supplierIDCombo = new javax.swing.JComboBox();
        productNameTfLabel = new javax.swing.JLabel();
        expiredDatePicker = new org.jdesktop.swingx.JXDatePicker();
        batchNumberTfLabel = new javax.swing.JLabel();
        batchNumberTf = new javax.swing.JTextField();
        dateSupplierTfLabel = new javax.swing.JLabel();
        mfdDateTfLabel = new javax.swing.JLabel();
        dateSuppliedPicker = new org.jdesktop.swingx.JXDatePicker();
        mfdDatePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel10 = new javax.swing.JLabel();
        addStockInputBtn = new javax.swing.JLabel();
        addStockresetBtn = new javax.swing.JLabel();
        addStockCancelBtn = new javax.swing.JLabel();
        categoryPanel = new javax.swing.JPanel();
        scrollPaneCategory = new javax.swing.JScrollPane();
        categoryTable = new javax.swing.JTable();
        addProductsPanel = new javax.swing.JPanel();
        addProductBox = new javax.swing.JPanel();
        addStockTitle1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        productIDTfLabel1 = new javax.swing.JLabel();
        productIDTf1 = new javax.swing.JTextField();
        productNameTfLabel1 = new javax.swing.JLabel();
        productNameTf1 = new javax.swing.JTextField();
        selectCategory1 = new javax.swing.JComboBox();
        categoryIDTfLabel1 = new javax.swing.JLabel();
        sellingPriceTf1 = new javax.swing.JTextField();
        dollarSignLabel3 = new javax.swing.JLabel();
        sellingPriceTfLabel1 = new javax.swing.JLabel();
        supplierIDCombo1 = new javax.swing.JComboBox();
        supplierIDTfLabel1 = new javax.swing.JLabel();
        addProductResetBtn = new javax.swing.JLabel();
        addProductCancelBtn = new javax.swing.JLabel();
        addProductInputBtn = new javax.swing.JLabel();
        productPanel = new javax.swing.JPanel();
        scrollPaneProduct = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        addSupplierPanel = new javax.swing.JPanel();
        supplierPanel = new javax.swing.JPanel();
        scrollPaneSupplier = new javax.swing.JScrollPane();
        supplierTable = new javax.swing.JTable();
        addSupplierPanel2 = new javax.swing.JPanel();
        addSupplierTitle = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        supplierIDTfLabel4 = new javax.swing.JLabel();
        supplierIDTf = new javax.swing.JTextField();
        supplierNameTfLabel4 = new javax.swing.JLabel();
        supplierNameTf = new javax.swing.JTextField();
        addSupplierResetBtn = new javax.swing.JLabel();
        addSupplierCancelBtn = new javax.swing.JLabel();
        addSupplierInputBtn = new javax.swing.JLabel();
        supplierContactTfLabel = new javax.swing.JLabel();
        supplierContactTf = new javax.swing.JTextField();
        supplierAddressTfLabel = new javax.swing.JLabel();
        supplierAddressScrollPane = new javax.swing.JScrollPane();
        supplierAddressTA = new javax.swing.JTextArea();
        detailPanel = new javax.swing.JPanel();
        DetailBox = new javax.swing.JPanel();
        DetailTop = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        productNameLabel = new javax.swing.JLabel();
        BatchNumberLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        productIDLabel = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        quantityLabel1 = new javax.swing.JLabel();
        DetailBottom = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        supplierLabel1 = new javax.swing.JLabel();
        expiredDateLabel1 = new javax.swing.JLabel();
        categoryLabel1 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        categoryProductLabel1 = new javax.swing.JLabel();
        buyPriceLabel1 = new javax.swing.JLabel();
        priceLabel1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        sellingPriceLabel1 = new javax.swing.JLabel();
        supplierAddressLabel1 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        mfdDateLabel1 = new javax.swing.JLabel();
        dateLabel1 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        dateSuppliedLabel1 = new javax.swing.JLabel();
        idSupplierLabel1 = new javax.swing.JLabel();
        supplierNameLabel1 = new javax.swing.JLabel();
        supplierContactLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cashRegisterPanel = new javax.swing.JPanel();
        cashRegisterBox = new javax.swing.JPanel();
        upperMenuCashRegister = new javax.swing.JPanel();
        viewOrderHistoryBtn = new javax.swing.JLabel();
        cashRegisterBtn = new javax.swing.JLabel();
        cashRegisterView = new javax.swing.JPanel();
        cashRegisterInputs = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        viewProducts = new javax.swing.JTextArea();
        totalPaymentBtn = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        productIDInputTf4 = new javax.swing.JTextField();
        inputValueOfProduct4 = new javax.swing.JTextField();
        inputProductBtn4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        reportPrint = new javax.swing.JTextArea();
        printReceipt = new javax.swing.JLabel();
        TaxTf = new javax.swing.JTextField();
        subTotalPaymentTf = new javax.swing.JTextField();
        totalPaymentTf = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        orderHistory = new javax.swing.JPanel();
        orderHistoryScrollPane = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        dateOfOrder = new javax.swing.JLabel();
        orderTableScrollPane = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidePanel.setBackground(new java.awt.Color(0, 157, 233));
        sidePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sideMenuDatabase.setBackground(new java.awt.Color(0, 149, 221));
        sideMenuDatabase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sideMenuDatabaseMouseClicked(evt);
            }
        });

        logoDatabase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tubespbo/images/logo-db.png"))); // NOI18N

        databaseLabel.setFont(new java.awt.Font("Roboto Lt", 0, 17)); // NOI18N
        databaseLabel.setForeground(new java.awt.Color(255, 255, 255));
        databaseLabel.setText("Database");

        javax.swing.GroupLayout sideMenuDatabaseLayout = new javax.swing.GroupLayout(sideMenuDatabase);
        sideMenuDatabase.setLayout(sideMenuDatabaseLayout);
        sideMenuDatabaseLayout.setHorizontalGroup(
            sideMenuDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideMenuDatabaseLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(logoDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(databaseLabel)
                .addContainerGap(145, Short.MAX_VALUE))
        );
        sideMenuDatabaseLayout.setVerticalGroup(
            sideMenuDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideMenuDatabaseLayout.createSequentialGroup()
                .addGroup(sideMenuDatabaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(logoDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(databaseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sidePanel.add(sideMenuDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 310, 50));

        pharmacyLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tubespbo/images/logo-pharmacy.png"))); // NOI18N
        pharmacyLogo.setText("jLabel3");
        sidePanel.add(pharmacyLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, 180, 63));

        sideMenuCashRegister.setBackground(new java.awt.Color(0, 170, 237));
        sideMenuCashRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sideMenuCashRegisterMouseClicked(evt);
            }
        });

        logoCashRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tubespbo/images/logo-tr.png"))); // NOI18N

        cashRegisterLabel.setFont(new java.awt.Font("Roboto Lt", 0, 17)); // NOI18N
        cashRegisterLabel.setForeground(new java.awt.Color(255, 255, 255));
        cashRegisterLabel.setText("Cash Register");
        cashRegisterLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cashRegisterLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout sideMenuCashRegisterLayout = new javax.swing.GroupLayout(sideMenuCashRegister);
        sideMenuCashRegister.setLayout(sideMenuCashRegisterLayout);
        sideMenuCashRegisterLayout.setHorizontalGroup(
            sideMenuCashRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideMenuCashRegisterLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(logoCashRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(cashRegisterLabel)
                .addContainerGap(111, Short.MAX_VALUE))
        );
        sideMenuCashRegisterLayout.setVerticalGroup(
            sideMenuCashRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideMenuCashRegisterLayout.createSequentialGroup()
                .addGroup(sideMenuCashRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cashRegisterLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logoCashRegister, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sidePanel.add(sideMenuCashRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 310, -1));

        bg.add(sidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 310, 730));

        closeMinimizePanel.setBackground(new java.awt.Color(235, 242, 255));

        javax.swing.GroupLayout closeMinimizePanelLayout = new javax.swing.GroupLayout(closeMinimizePanel);
        closeMinimizePanel.setLayout(closeMinimizePanelLayout);
        closeMinimizePanelLayout.setHorizontalGroup(
            closeMinimizePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1090, Short.MAX_VALUE)
        );
        closeMinimizePanelLayout.setVerticalGroup(
            closeMinimizePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        bg.add(closeMinimizePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 1090, 40));

        DisplayMenu.setBackground(new java.awt.Color(255, 255, 255));
        DisplayMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DatabasePanelCard.setBackground(new java.awt.Color(255, 255, 255));
        DatabasePanelCard.setLayout(new java.awt.CardLayout());

        DatabasePanelBox.setBackground(new java.awt.Color(255, 255, 255));
        DatabasePanelBox.setLayout(new javax.swing.BoxLayout(DatabasePanelBox, javax.swing.BoxLayout.LINE_AXIS));

        upperPanel.setBackground(new java.awt.Color(255, 255, 255));
        upperPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        upperPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        searchBar.setSelectionColor(new java.awt.Color(245, 245, 245));
        upperPanel.add(searchBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 590, 40));

        searchBtn.setBackground(new java.awt.Color(255, 255, 255));
        searchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tubespbo/images/logo-search.png"))); // NOI18N
        searchBtn.setBorder(null);
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });
        upperPanel.add(searchBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 60, 42));

        addStockBtn.setBackground(new java.awt.Color(111, 216, 101));
        addStockBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addStockBtn.setForeground(new java.awt.Color(255, 255, 255));
        addStockBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addStockBtn.setText("Add Stock");
        addStockBtn.setOpaque(true);
        addStockBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addStockBtnMouseClicked(evt);
            }
        });
        upperPanel.add(addStockBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 10, 95, 40));

        addProductBtn.setBackground(new java.awt.Color(111, 216, 101));
        addProductBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addProductBtn.setForeground(new java.awt.Color(255, 255, 255));
        addProductBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addProductBtn.setText("Add Product");
        addProductBtn.setOpaque(true);
        addProductBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addProductBtnMouseClicked(evt);
            }
        });
        upperPanel.add(addProductBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 10, 95, 40));

        addSupplierBtn.setBackground(new java.awt.Color(111, 216, 101));
        addSupplierBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addSupplierBtn.setForeground(new java.awt.Color(255, 255, 255));
        addSupplierBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addSupplierBtn.setText("Add Supplier");
        addSupplierBtn.setOpaque(true);
        addSupplierBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addSupplierBtnMouseClicked(evt);
            }
        });
        upperPanel.add(addSupplierBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 10, 95, 40));

        viewMenuPanel.setBackground(new java.awt.Color(255, 255, 255));
        viewMenuPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        viewStocksBtn.setBackground(new java.awt.Color(245, 245, 245));
        viewStocksBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        viewStocksBtn.setForeground(new java.awt.Color(51, 51, 51));
        viewStocksBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewStocksBtn.setText("View Stocks");
        viewStocksBtn.setOpaque(true);
        viewStocksBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewStocksBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewStocksBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewStocksBtnMouseExited(evt);
            }
        });
        viewMenuPanel.add(viewStocksBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 15, 100, 35));

        viewProductsBtn.setBackground(new java.awt.Color(245, 245, 245));
        viewProductsBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        viewProductsBtn.setForeground(new java.awt.Color(51, 51, 51));
        viewProductsBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewProductsBtn.setText("View Products");
        viewProductsBtn.setOpaque(true);
        viewProductsBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewProductsBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewProductsBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewProductsBtnMouseExited(evt);
            }
        });
        viewMenuPanel.add(viewProductsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(149, 15, 100, 35));

        viewSupplierBtn.setBackground(new java.awt.Color(245, 245, 245));
        viewSupplierBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        viewSupplierBtn.setForeground(new java.awt.Color(51, 51, 51));
        viewSupplierBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewSupplierBtn.setText("View Supplier");
        viewSupplierBtn.setOpaque(true);
        viewSupplierBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewSupplierBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewSupplierBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewSupplierBtnMouseExited(evt);
            }
        });
        viewMenuPanel.add(viewSupplierBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(256, 15, 100, 35));

        viewRowStockBtn.setBackground(new java.awt.Color(111, 216, 101));
        viewRowStockBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        viewRowStockBtn.setForeground(new java.awt.Color(255, 255, 255));
        viewRowStockBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewRowStockBtn.setText("View Stock Row");
        viewRowStockBtn.setOpaque(true);
        viewRowStockBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewRowStockBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewRowStockBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewRowStockBtnMouseExited(evt);
            }
        });
        viewMenuPanel.add(viewRowStockBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 15, 122, 35));

        viewCategoryBtn.setBackground(new java.awt.Color(245, 245, 245));
        viewCategoryBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        viewCategoryBtn.setForeground(new java.awt.Color(51, 51, 51));
        viewCategoryBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewCategoryBtn.setText("View Category");
        viewCategoryBtn.setOpaque(true);
        viewCategoryBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewCategoryBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewCategoryBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewCategoryBtnMouseExited(evt);
            }
        });
        viewMenuPanel.add(viewCategoryBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(363, 15, 100, 35));

        deleteStockRowBtn.setBackground(new java.awt.Color(0, 170, 237));
        deleteStockRowBtn.setFont(new java.awt.Font("Roboto Lt", 0, 16)); // NOI18N
        deleteStockRowBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteStockRowBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        deleteStockRowBtn.setText("Delete Stock Row");
        deleteStockRowBtn.setOpaque(true);
        deleteStockRowBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteStockRowBtnMouseClicked(evt);
            }
        });
        viewMenuPanel.add(deleteStockRowBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, 170, 36));

        deleteProductRowBtn.setBackground(new java.awt.Color(0, 170, 237));
        deleteProductRowBtn.setFont(new java.awt.Font("Roboto Lt", 0, 16)); // NOI18N
        deleteProductRowBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteProductRowBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        deleteProductRowBtn.setText("Delete Product Row");
        deleteProductRowBtn.setOpaque(true);
        deleteProductRowBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteProductRowBtnMouseClicked(evt);
            }
        });
        viewMenuPanel.add(deleteProductRowBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, 170, 36));

        deleteSupplierRowBtn.setBackground(new java.awt.Color(0, 170, 237));
        deleteSupplierRowBtn.setFont(new java.awt.Font("Roboto Lt", 0, 16)); // NOI18N
        deleteSupplierRowBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteSupplierRowBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        deleteSupplierRowBtn.setText("Delete Supplier Row");
        deleteSupplierRowBtn.setOpaque(true);
        deleteSupplierRowBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteSupplierRowBtnMouseClicked(evt);
            }
        });
        viewMenuPanel.add(deleteSupplierRowBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, 170, 36));

        upperPanel.add(viewMenuPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1090, 60));

        viewTable.setBackground(new java.awt.Color(255, 255, 255));

        tabPanel.setBackground(new java.awt.Color(255, 255, 255));
        tabPanel.setMinimumSize(new java.awt.Dimension(900, 525));
        tabPanel.setPreferredSize(new java.awt.Dimension(900, 525));
        tabPanel.setLayout(new java.awt.CardLayout());

        stocksPanel.setBackground(new java.awt.Color(255, 255, 255));
        stocksPanel.setLayout(new javax.swing.BoxLayout(stocksPanel, javax.swing.BoxLayout.LINE_AXIS));

        scrollPaneStock.setBackground(new java.awt.Color(255, 255, 255));
        scrollPaneStock.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        stockTable.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        stockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Supplier ID", "Product Name", "Category ID", "Quantity", "Buying Price", "Selling Price", "Date Supplied", "Batch Number", "Mfd Date", "Expired Date"
            }
        ));
        stockTable.setFocusable(false);
        stockTable.setGridColor(new java.awt.Color(204, 204, 204));
        stockTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        stockTable.setOpaque(false);
        stockTable.setRowHeight(40);
        stockTable.setSelectionBackground(new java.awt.Color(0, 170, 237));
        stockTable.setShowVerticalLines(false);
        scrollPaneStock.setViewportView(stockTable);

        stocksPanel.add(scrollPaneStock);

        tabPanel.add(stocksPanel, "card2");

        addStocksPanel.setLayout(new javax.swing.BoxLayout(addStocksPanel, javax.swing.BoxLayout.LINE_AXIS));

        addStockBox.setBackground(new java.awt.Color(255, 255, 255));
        addStockBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        addStockBox.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addStockTitle.setFont(new java.awt.Font("Roboto Lt", 1, 30)); // NOI18N
        addStockTitle.setForeground(new java.awt.Color(102, 102, 102));
        addStockTitle.setText("Add Stock");
        addStockBox.add(addStockTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, -1, -1));

        sellingPriceTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        sellingPriceTf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellingPriceTfActionPerformed(evt);
            }
        });
        addStockBox.add(sellingPriceTf, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 430, 70, 40));

        sellingPriceTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        sellingPriceTfLabel.setText("Selling Price");
        sellingPriceTfLabel.setToolTipText("");
        addStockBox.add(sellingPriceTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 410, -1, -1));

        supplierIDTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierIDTfLabel.setText("Supplier ID");
        addStockBox.add(supplierIDTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, -1, -1));

        productIDTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addStockBox.add(productIDTf, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 270, 40));

        expiredDateTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        expiredDateTfLabel.setText("Expired Date");
        addStockBox.add(expiredDateTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 320, -1, -1));

        selectCategory.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addStockBox.add(selectCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, 160, 40));

        productNameTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addStockBox.add(productNameTf, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 270, 40));

        dollarSignLabel2.setFont(new java.awt.Font("Roboto Th", 1, 18)); // NOI18N
        dollarSignLabel2.setText("$");
        dollarSignLabel2.setToolTipText("");
        addStockBox.add(dollarSignLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 440, -1, -1));

        quantityTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        quantityTfLabel.setText("Quantity");
        quantityTfLabel.setToolTipText("");
        addStockBox.add(quantityTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 320, -1, -1));

        buyingPriceTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        buyingPriceTfLabel.setText("Buying Price");
        buyingPriceTfLabel.setToolTipText("");
        addStockBox.add(buyingPriceTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 410, -1, -1));

        quantityTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addStockBox.add(quantityTf, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 340, 90, 40));

        buyingPriceTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        buyingPriceTf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyingPriceTfActionPerformed(evt);
            }
        });
        addStockBox.add(buyingPriceTf, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 430, 70, 40));

        categoryIDTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        categoryIDTfLabel.setText("Category ID");
        categoryIDTfLabel.setToolTipText("");
        addStockBox.add(categoryIDTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, -1, -1));

        dollarSign1Label.setFont(new java.awt.Font("Roboto Th", 1, 18)); // NOI18N
        dollarSign1Label.setText("$");
        dollarSign1Label.setToolTipText("");
        addStockBox.add(dollarSign1Label, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 440, -1, -1));

        productIDTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        productIDTfLabel.setText("Product ID");
        addStockBox.add(productIDTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, -1));

        supplierIDCombo.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addStockBox.add(supplierIDCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, 350, 40));

        productNameTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        productNameTfLabel.setText("Product Name");
        addStockBox.add(productNameTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, -1, -1));

        expiredDatePicker.setFont(new java.awt.Font("Roboto Lt", 0, 16)); // NOI18N
        addStockBox.add(expiredDatePicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 340, 140, 40));

        batchNumberTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        batchNumberTfLabel.setText("Batch Number");
        batchNumberTfLabel.setToolTipText("");
        addStockBox.add(batchNumberTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 230, -1, -1));

        batchNumberTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        addStockBox.add(batchNumberTf, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 250, 90, 40));

        dateSupplierTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        dateSupplierTfLabel.setText("Date Supplied");
        addStockBox.add(dateSupplierTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 230, -1, -1));

        mfdDateTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        mfdDateTfLabel.setText("Manufactured Date");
        addStockBox.add(mfdDateTfLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 320, -1, -1));

        dateSuppliedPicker.setFont(new java.awt.Font("Roboto Lt", 0, 16)); // NOI18N
        addStockBox.add(dateSuppliedPicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 250, 140, 40));

        mfdDatePicker.setFont(new java.awt.Font("Roboto Lt", 0, 16)); // NOI18N
        addStockBox.add(mfdDatePicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, 140, 40));

        jLabel10.setBackground(new java.awt.Color(0, 170, 237));
        jLabel10.setOpaque(true);
        addStockBox.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 260, 6));

        addStockInputBtn.setBackground(new java.awt.Color(111, 216, 101));
        addStockInputBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addStockInputBtn.setForeground(new java.awt.Color(255, 255, 255));
        addStockInputBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addStockInputBtn.setText("Input Data");
        addStockInputBtn.setOpaque(true);
        addStockInputBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addStockInputBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addStockInputBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addStockInputBtnMouseExited(evt);
            }
        });
        addStockBox.add(addStockInputBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 430, 90, 40));

        addStockresetBtn.setBackground(new java.awt.Color(235, 242, 255));
        addStockresetBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addStockresetBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addStockresetBtn.setText("Reset");
        addStockresetBtn.setOpaque(true);
        addStockresetBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addStockresetBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addStockresetBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addStockresetBtnMouseExited(evt);
            }
        });
        addStockBox.add(addStockresetBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 430, 90, 40));

        addStockCancelBtn.setBackground(new java.awt.Color(235, 242, 255));
        addStockCancelBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addStockCancelBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addStockCancelBtn.setText("Cancel");
        addStockCancelBtn.setOpaque(true);
        addStockCancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addStockCancelBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addStockCancelBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addStockCancelBtnMouseExited(evt);
            }
        });
        addStockBox.add(addStockCancelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 430, 90, 40));

        addStocksPanel.add(addStockBox);

        tabPanel.add(addStocksPanel, "card6");

        categoryPanel.setLayout(new javax.swing.BoxLayout(categoryPanel, javax.swing.BoxLayout.LINE_AXIS));

        scrollPaneCategory.setBackground(new java.awt.Color(255, 255, 255));
        scrollPaneCategory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        categoryTable.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        categoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        categoryTable.setRowHeight(40);
        scrollPaneCategory.setViewportView(categoryTable);

        categoryPanel.add(scrollPaneCategory);

        tabPanel.add(categoryPanel, "card7");

        addProductsPanel.setBackground(new java.awt.Color(255, 255, 255));
        addProductsPanel.setLayout(new javax.swing.BoxLayout(addProductsPanel, javax.swing.BoxLayout.LINE_AXIS));

        addProductBox.setBackground(new java.awt.Color(255, 255, 255));
        addProductBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        addStockTitle1.setFont(new java.awt.Font("Roboto Lt", 1, 30)); // NOI18N
        addStockTitle1.setForeground(new java.awt.Color(102, 102, 102));
        addStockTitle1.setText("Add Product");

        jLabel13.setBackground(new java.awt.Color(0, 170, 237));
        jLabel13.setOpaque(true);

        productIDTfLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        productIDTfLabel1.setText("Product ID");

        productIDTf1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N

        productNameTfLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        productNameTfLabel1.setText("Product Name");

        productNameTf1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N

        selectCategory1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N

        categoryIDTfLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        categoryIDTfLabel1.setText("Category ID");
        categoryIDTfLabel1.setToolTipText("");

        sellingPriceTf1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        sellingPriceTf1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellingPriceTf1ActionPerformed(evt);
            }
        });

        dollarSignLabel3.setFont(new java.awt.Font("Roboto Th", 1, 18)); // NOI18N
        dollarSignLabel3.setText("$");
        dollarSignLabel3.setToolTipText("");

        sellingPriceTfLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        sellingPriceTfLabel1.setText("Selling Price");
        sellingPriceTfLabel1.setToolTipText("");

        supplierIDCombo1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N

        supplierIDTfLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierIDTfLabel1.setText("Supplier ID");

        addProductResetBtn.setBackground(new java.awt.Color(235, 242, 255));
        addProductResetBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addProductResetBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addProductResetBtn.setText("Reset");
        addProductResetBtn.setOpaque(true);
        addProductResetBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addProductResetBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addProductResetBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addProductResetBtnMouseExited(evt);
            }
        });

        addProductCancelBtn.setBackground(new java.awt.Color(235, 242, 255));
        addProductCancelBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addProductCancelBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addProductCancelBtn.setText("Cancel");
        addProductCancelBtn.setOpaque(true);
        addProductCancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addProductCancelBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addProductCancelBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addProductCancelBtnMouseExited(evt);
            }
        });

        addProductInputBtn.setBackground(new java.awt.Color(111, 216, 101));
        addProductInputBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addProductInputBtn.setForeground(new java.awt.Color(255, 255, 255));
        addProductInputBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addProductInputBtn.setText("Input Data");
        addProductInputBtn.setOpaque(true);
        addProductInputBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addProductInputBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addProductInputBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addProductInputBtnMouseExited(evt);
            }
        });

        javax.swing.GroupLayout addProductBoxLayout = new javax.swing.GroupLayout(addProductBox);
        addProductBox.setLayout(addProductBoxLayout);
        addProductBoxLayout.setHorizontalGroup(
            addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addProductBoxLayout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productNameTfLabel1)
                    .addComponent(productNameTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productIDTfLabel1)
                            .addComponent(productIDTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(addProductBoxLayout.createSequentialGroup()
                            .addComponent(addStockTitle1)
                            .addGap(84, 84, 84))
                        .addGroup(addProductBoxLayout.createSequentialGroup()
                            .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(categoryIDTfLabel1)
                                .addComponent(selectCategory1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sellingPriceTfLabel1)
                                .addGroup(addProductBoxLayout.createSequentialGroup()
                                    .addComponent(dollarSignLabel3)
                                    .addGap(9, 9, 9)
                                    .addComponent(sellingPriceTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(2, 2, 2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addProductBoxLayout.createSequentialGroup()
                        .addComponent(addProductResetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(addProductCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(addProductInputBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(supplierIDTfLabel1)
                    .addComponent(supplierIDCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(145, 145, 145))
        );
        addProductBoxLayout.setVerticalGroup(
            addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addProductBoxLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(addProductBoxLayout.createSequentialGroup()
                        .addComponent(supplierIDTfLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(supplierIDCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addProductBoxLayout.createSequentialGroup()
                        .addComponent(addStockTitle1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(productIDTfLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(productIDTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addProductBoxLayout.createSequentialGroup()
                        .addComponent(productNameTfLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(productNameTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addProductResetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addProductInputBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addProductCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(addProductBoxLayout.createSequentialGroup()
                        .addComponent(categoryIDTfLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(selectCategory1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addProductBoxLayout.createSequentialGroup()
                        .addComponent(sellingPriceTfLabel1)
                        .addGap(3, 3, 3)
                        .addGroup(addProductBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addProductBoxLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(dollarSignLabel3))
                            .addComponent(sellingPriceTf1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        addProductsPanel.add(addProductBox);

        tabPanel.add(addProductsPanel, "card8");

        productPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        productPanel.setLayout(new javax.swing.BoxLayout(productPanel, javax.swing.BoxLayout.LINE_AXIS));

        scrollPaneProduct.setBorder(null);
        scrollPaneProduct.setForeground(new java.awt.Color(204, 204, 204));

        productTable.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Supplier ID", "Product Name", "Category", "Quantity", "Price"
            }
        ));
        productTable.setRowHeight(40);
        productTable.setShowVerticalLines(false);
        scrollPaneProduct.setViewportView(productTable);

        productPanel.add(scrollPaneProduct);

        tabPanel.add(productPanel, "card3");

        addSupplierPanel.setBackground(new java.awt.Color(255, 255, 255));
        addSupplierPanel.setLayout(new javax.swing.BoxLayout(addSupplierPanel, javax.swing.BoxLayout.LINE_AXIS));
        tabPanel.add(addSupplierPanel, "card9");

        supplierPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        supplierPanel.setLayout(new javax.swing.BoxLayout(supplierPanel, javax.swing.BoxLayout.LINE_AXIS));

        scrollPaneSupplier.setBackground(new java.awt.Color(204, 204, 204));
        scrollPaneSupplier.setBorder(null);

        supplierTable.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        supplierTable.setRowHeight(40);
        scrollPaneSupplier.setViewportView(supplierTable);

        supplierPanel.add(scrollPaneSupplier);

        tabPanel.add(supplierPanel, "card4");

        addSupplierPanel2.setBackground(new java.awt.Color(255, 255, 255));
        addSupplierPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        addSupplierTitle.setFont(new java.awt.Font("Roboto Lt", 1, 30)); // NOI18N
        addSupplierTitle.setForeground(new java.awt.Color(102, 102, 102));
        addSupplierTitle.setText("Add Supplier");

        jLabel14.setBackground(new java.awt.Color(0, 170, 237));
        jLabel14.setOpaque(true);

        supplierIDTfLabel4.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierIDTfLabel4.setText("Supplier ID");

        supplierIDTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N

        supplierNameTfLabel4.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierNameTfLabel4.setText("Supplier Name");

        supplierNameTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N

        addSupplierResetBtn.setBackground(new java.awt.Color(235, 242, 255));
        addSupplierResetBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addSupplierResetBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addSupplierResetBtn.setText("Reset");
        addSupplierResetBtn.setOpaque(true);
        addSupplierResetBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addSupplierResetBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addSupplierResetBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addSupplierResetBtnMouseExited(evt);
            }
        });

        addSupplierCancelBtn.setBackground(new java.awt.Color(235, 242, 255));
        addSupplierCancelBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addSupplierCancelBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addSupplierCancelBtn.setText("Cancel");
        addSupplierCancelBtn.setOpaque(true);
        addSupplierCancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addSupplierCancelBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addSupplierCancelBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addSupplierCancelBtnMouseExited(evt);
            }
        });

        addSupplierInputBtn.setBackground(new java.awt.Color(111, 216, 101));
        addSupplierInputBtn.setFont(new java.awt.Font("Roboto Lt", 0, 15)); // NOI18N
        addSupplierInputBtn.setForeground(new java.awt.Color(255, 255, 255));
        addSupplierInputBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        addSupplierInputBtn.setText("Input Data");
        addSupplierInputBtn.setOpaque(true);
        addSupplierInputBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addSupplierInputBtnMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addSupplierInputBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addSupplierInputBtnMouseExited(evt);
            }
        });

        supplierContactTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierContactTfLabel.setText("Supplier Contact");

        supplierContactTf.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N

        supplierAddressTfLabel.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierAddressTfLabel.setText("Supplier Address");

        supplierAddressTA.setColumns(20);
        supplierAddressTA.setRows(5);
        supplierAddressScrollPane.setViewportView(supplierAddressTA);

        javax.swing.GroupLayout addSupplierPanel2Layout = new javax.swing.GroupLayout(addSupplierPanel2);
        addSupplierPanel2.setLayout(addSupplierPanel2Layout);
        addSupplierPanel2Layout.setHorizontalGroup(
            addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addSupplierPanel2Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addGroup(addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addSupplierTitle)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(addSupplierPanel2Layout.createSequentialGroup()
                .addGroup(addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addSupplierPanel2Layout.createSequentialGroup()
                        .addComponent(addSupplierResetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addSupplierCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(addSupplierInputBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addSupplierPanel2Layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addGroup(addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplierIDTfLabel4)
                            .addComponent(supplierIDTf, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(supplierNameTfLabel4)
                        .addComponent(supplierNameTf, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                .addGroup(addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(supplierContactTfLabel)
                    .addComponent(supplierAddressTfLabel)
                    .addComponent(supplierContactTf)
                    .addComponent(supplierAddressScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                .addGap(165, 165, 165))
        );
        addSupplierPanel2Layout.setVerticalGroup(
            addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addSupplierPanel2Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(addSupplierTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(addSupplierPanel2Layout.createSequentialGroup()
                        .addComponent(supplierIDTfLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierIDTf, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addSupplierPanel2Layout.createSequentialGroup()
                        .addComponent(supplierContactTfLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierContactTf, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addGroup(addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierNameTfLabel4)
                    .addComponent(supplierAddressTfLabel))
                .addGap(3, 3, 3)
                .addGroup(addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(addSupplierPanel2Layout.createSequentialGroup()
                        .addComponent(supplierNameTf, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(addSupplierPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addSupplierInputBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addSupplierCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addSupplierResetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(supplierAddressScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        tabPanel.add(addSupplierPanel2, "card10");

        detailPanel.setLayout(new javax.swing.BoxLayout(detailPanel, javax.swing.BoxLayout.LINE_AXIS));

        DetailBox.setBackground(new java.awt.Color(255, 255, 255));
        DetailBox.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DetailTop.setBackground(new java.awt.Color(102, 102, 102));
        DetailTop.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Roboto Lt", 0, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Batch Number");
        DetailTop.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, -1, -1));

        productNameLabel.setFont(new java.awt.Font("Roboto Lt", 0, 36)); // NOI18N
        productNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        productNameLabel.setText("Name of Product");
        DetailTop.add(productNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));

        BatchNumberLabel.setFont(new java.awt.Font("Roboto Cn", 0, 16)); // NOI18N
        BatchNumberLabel.setForeground(new java.awt.Color(255, 255, 255));
        BatchNumberLabel.setText("Batch No");
        DetailTop.add(BatchNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 30, -1, -1));

        jLabel12.setFont(new java.awt.Font("Roboto Lt", 0, 13)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Product Name");
        DetailTop.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        productIDLabel.setFont(new java.awt.Font("Roboto Cn", 0, 16)); // NOI18N
        productIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        productIDLabel.setText("ProductID");
        DetailTop.add(productIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, -1));

        jLabel30.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Qty :");
        jLabel30.setOpaque(true);
        DetailTop.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 100, 40, 30));

        quantityLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        quantityLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        quantityLabel1.setText("Q");
        quantityLabel1.setOpaque(true);
        DetailTop.add(quantityLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 100, 40, 30));

        DetailBox.add(DetailTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 810, 130));

        DetailBottom.setBackground(new java.awt.Color(255, 255, 255));
        DetailBottom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        DetailBottom.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Selling Price");
        jLabel15.setOpaque(true);
        DetailBottom.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 71, 30));

        supplierLabel1.setBackground(new java.awt.Color(111, 216, 101));
        supplierLabel1.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        supplierLabel1.setForeground(new java.awt.Color(255, 255, 255));
        supplierLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        supplierLabel1.setText("Supplier");
        supplierLabel1.setOpaque(true);
        DetailBottom.add(supplierLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 71, 25));

        expiredDateLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        expiredDateLabel1.setText("date of product's expired");
        DetailBottom.add(expiredDateLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 280, -1, -1));

        categoryLabel1.setBackground(new java.awt.Color(111, 216, 101));
        categoryLabel1.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        categoryLabel1.setForeground(new java.awt.Color(255, 255, 255));
        categoryLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        categoryLabel1.setText("Category");
        categoryLabel1.setOpaque(true);
        DetailBottom.add(categoryLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 71, 25));

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Supplier Address");
        jLabel16.setOpaque(true);
        DetailBottom.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 100, 30));

        categoryProductLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        categoryProductLabel1.setText("category name");
        DetailBottom.add(categoryProductLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, -1, -1));

        buyPriceLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        buyPriceLabel1.setText("price of buy");
        DetailBottom.add(buyPriceLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, -1, -1));

        priceLabel1.setBackground(new java.awt.Color(111, 216, 101));
        priceLabel1.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        priceLabel1.setForeground(new java.awt.Color(255, 255, 255));
        priceLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        priceLabel1.setText("Price");
        priceLabel1.setOpaque(true);
        DetailBottom.add(priceLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 71, 25));

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Buying Price");
        jLabel18.setOpaque(true);
        DetailBottom.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 71, 30));

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Expired Date");
        jLabel19.setOpaque(true);
        DetailBottom.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 250, 100, 30));

        sellingPriceLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        sellingPriceLabel1.setText("price of selling");
        DetailBottom.add(sellingPriceLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 170, -1, -1));

        supplierAddressLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierAddressLabel1.setText("address of supplier");
        DetailBottom.add(supplierAddressLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 160, -1, -1));

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Mfd Date");
        jLabel24.setOpaque(true);
        DetailBottom.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, 100, 30));

        mfdDateLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        mfdDateLabel1.setText("date of manufacture");
        DetailBottom.add(mfdDateLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 280, -1, -1));

        dateLabel1.setBackground(new java.awt.Color(111, 216, 101));
        dateLabel1.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        dateLabel1.setForeground(new java.awt.Color(255, 255, 255));
        dateLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateLabel1.setText("Date");
        dateLabel1.setOpaque(true);
        DetailBottom.add(dateLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 71, 25));

        jLabel25.setBackground(new java.awt.Color(255, 255, 255));
        jLabel25.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Date Supplied");
        jLabel25.setOpaque(true);
        DetailBottom.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 100, 30));

        jLabel26.setBackground(new java.awt.Color(255, 255, 255));
        jLabel26.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setText("Supplier ID");
        jLabel26.setOpaque(true);
        DetailBottom.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 100, 30));

        jLabel27.setBackground(new java.awt.Color(255, 255, 255));
        jLabel27.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Supplier Name");
        jLabel27.setOpaque(true);
        DetailBottom.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 100, 30));

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Roboto Cn", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(102, 102, 102));
        jLabel28.setText("Supplier Contact");
        jLabel28.setOpaque(true);
        DetailBottom.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 130, 100, 30));

        dateSuppliedLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        dateSuppliedLabel1.setText("date of stock supplied");
        DetailBottom.add(dateSuppliedLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, -1, -1));

        idSupplierLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        idSupplierLabel1.setText("ID of Supplier");
        DetailBottom.add(idSupplierLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, -1, -1));

        supplierNameLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierNameLabel1.setText("name of supplier");
        DetailBottom.add(supplierNameLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, -1, -1));

        supplierContactLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        supplierContactLabel1.setText("contact of supplier");
        DetailBottom.add(supplierContactLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 160, -1, -1));

        DetailBox.add(DetailBottom, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 810, 320));

        detailPanel.add(DetailBox);

        tabPanel.add(detailPanel, "card5");

        javax.swing.GroupLayout viewTableLayout = new javax.swing.GroupLayout(viewTable);
        viewTable.setLayout(viewTableLayout);
        viewTableLayout.setHorizontalGroup(
            viewTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewTableLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1018, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        viewTableLayout.setVerticalGroup(
            viewTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewTableLayout.createSequentialGroup()
                .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );

        upperPanel.add(viewTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 1090, 510));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel1.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1088, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 58, Short.MAX_VALUE)
        );

        upperPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 60));

        DatabasePanelBox.add(upperPanel);

        DatabasePanelCard.add(DatabasePanelBox, "card2");

        DisplayMenu.add(DatabasePanelCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 810));

        cashRegisterPanel.setBackground(new java.awt.Color(255, 255, 255));
        cashRegisterPanel.setLayout(new java.awt.CardLayout());

        cashRegisterBox.setBackground(new java.awt.Color(255, 255, 255));
        cashRegisterBox.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        upperMenuCashRegister.setBackground(new java.awt.Color(255, 255, 255));

        viewOrderHistoryBtn.setBackground(new java.awt.Color(245, 245, 245));
        viewOrderHistoryBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        viewOrderHistoryBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewOrderHistoryBtn.setText("View Orders History");
        viewOrderHistoryBtn.setOpaque(true);
        viewOrderHistoryBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewOrderHistoryBtnMouseClicked(evt);
            }
        });

        cashRegisterBtn.setBackground(new java.awt.Color(245, 245, 245));
        cashRegisterBtn.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        cashRegisterBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cashRegisterBtn.setText("Cash Register");
        cashRegisterBtn.setOpaque(true);
        cashRegisterBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cashRegisterBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout upperMenuCashRegisterLayout = new javax.swing.GroupLayout(upperMenuCashRegister);
        upperMenuCashRegister.setLayout(upperMenuCashRegisterLayout);
        upperMenuCashRegisterLayout.setHorizontalGroup(
            upperMenuCashRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upperMenuCashRegisterLayout.createSequentialGroup()
                .addContainerGap(725, Short.MAX_VALUE)
                .addComponent(viewOrderHistoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cashRegisterBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        upperMenuCashRegisterLayout.setVerticalGroup(
            upperMenuCashRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upperMenuCashRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(upperMenuCashRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewOrderHistoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashRegisterBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        cashRegisterBox.add(upperMenuCashRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 70));

        cashRegisterView.setLayout(new java.awt.CardLayout());

        cashRegisterInputs.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(235, 242, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        viewProducts.setEditable(false);
        viewProducts.setColumns(20);
        viewProducts.setFont(new java.awt.Font("Roboto Lt", 0, 18)); // NOI18N
        viewProducts.setRows(5);
        jScrollPane1.setViewportView(viewProducts);

        jPanel7.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 410, 340));

        totalPaymentBtn.setBackground(new java.awt.Color(0, 170, 237));
        totalPaymentBtn.setFont(new java.awt.Font("Roboto Lt", 1, 18)); // NOI18N
        totalPaymentBtn.setForeground(new java.awt.Color(255, 255, 255));
        totalPaymentBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalPaymentBtn.setText("Total");
        totalPaymentBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        totalPaymentBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        totalPaymentBtn.setOpaque(true);
        totalPaymentBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                totalPaymentBtnMouseClicked(evt);
            }
        });
        jPanel7.add(totalPaymentBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 370, 150, 42));

        jPanel4.setBackground(new java.awt.Color(235, 242, 255));

        productIDInputTf4.setFont(new java.awt.Font("Roboto Lt", 1, 18)); // NOI18N

        inputValueOfProduct4.setFont(new java.awt.Font("Roboto Lt", 1, 14)); // NOI18N
        inputValueOfProduct4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputValueOfProduct4ActionPerformed(evt);
            }
        });

        inputProductBtn4.setBackground(new java.awt.Color(0, 170, 237));
        inputProductBtn4.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        inputProductBtn4.setForeground(new java.awt.Color(255, 255, 255));
        inputProductBtn4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inputProductBtn4.setText("Input Product");
        inputProductBtn4.setOpaque(true);
        inputProductBtn4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inputProductBtn4inputProductBtnMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        jLabel1.setText("Quantity");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputValueOfProduct4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(productIDInputTf4, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(inputProductBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(inputProductBtn4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(productIDInputTf4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inputValueOfProduct4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(235, 242, 255));

        reportPrint.setEditable(false);
        reportPrint.setColumns(20);
        reportPrint.setFont(new java.awt.Font("Roboto Lt", 0, 14)); // NOI18N
        reportPrint.setRows(5);
        jScrollPane2.setViewportView(reportPrint);

        printReceipt.setBackground(new java.awt.Color(0, 170, 237));
        printReceipt.setFont(new java.awt.Font("Roboto Lt", 1, 18)); // NOI18N
        printReceipt.setForeground(new java.awt.Color(255, 255, 255));
        printReceipt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        printReceipt.setText("Print");
        printReceipt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        printReceipt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        printReceipt.setOpaque(true);
        printReceipt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printReceiptMouseClicked(evt);
            }
        });

        TaxTf.setFont(new java.awt.Font("Roboto Lt", 1, 22)); // NOI18N

        subTotalPaymentTf.setFont(new java.awt.Font("Roboto Lt", 1, 22)); // NOI18N

        totalPaymentTf.setFont(new java.awt.Font("Roboto Lt", 1, 22)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Roboto Lt", 0, 20)); // NOI18N
        jLabel3.setText("Tax");

        jLabel4.setFont(new java.awt.Font("Roboto Lt", 0, 20)); // NOI18N
        jLabel4.setText("SubTotal");

        jLabel5.setFont(new java.awt.Font("Roboto Lt", 0, 20)); // NOI18N
        jLabel5.setText("Total");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(39, 39, 39)
                        .addComponent(totalPaymentTf, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(subTotalPaymentTf, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(47, 47, 47)
                        .addComponent(TaxTf, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                            .addGap(310, 310, 310)
                            .addComponent(printReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TaxTf, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subTotalPaymentTf, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalPaymentTf, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(printReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
        );

        jLabel2.setFont(new java.awt.Font("Roboto Lt", 1, 36)); // NOI18N
        jLabel2.setText("Cash Register");

        javax.swing.GroupLayout cashRegisterInputsLayout = new javax.swing.GroupLayout(cashRegisterInputs);
        cashRegisterInputs.setLayout(cashRegisterInputsLayout);
        cashRegisterInputsLayout.setHorizontalGroup(
            cashRegisterInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashRegisterInputsLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(cashRegisterInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(cashRegisterInputsLayout.createSequentialGroup()
                        .addGroup(cashRegisterInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        cashRegisterInputsLayout.setVerticalGroup(
            cashRegisterInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cashRegisterInputsLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cashRegisterInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(cashRegisterInputsLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        cashRegisterView.add(cashRegisterInputs, "card2");

        orderHistory.setBackground(new java.awt.Color(255, 255, 255));
        orderHistory.setLayout(new javax.swing.BoxLayout(orderHistory, javax.swing.BoxLayout.LINE_AXIS));

        orderHistoryScrollPane.setBorder(null);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        dateOfOrder.setText("Order Date");

        orderTableScrollPane.setBorder(null);

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        orderTable.setRowHeight(40);
        orderTableScrollPane.setViewportView(orderTable);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateOfOrder)
                    .addComponent(orderTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 956, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(dateOfOrder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(orderTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        orderHistoryScrollPane.setViewportView(jPanel9);

        orderHistory.add(orderHistoryScrollPane);

        cashRegisterView.add(orderHistory, "card3");

        cashRegisterBox.add(cashRegisterView, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1090, 620));

        cashRegisterPanel.add(cashRegisterBox, "card2");

        DisplayMenu.add(cashRegisterPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 810));

        bg.add(DisplayMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 1090, 810));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 1400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cashRegisterLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cashRegisterLabelMouseClicked

    }//GEN-LAST:event_cashRegisterLabelMouseClicked

    private void sideMenuCashRegisterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sideMenuCashRegisterMouseClicked
        //visibility
        DatabasePanelCard.setVisible(false);
        cashRegisterPanel.setVisible(true);

        //design
        sideMenuDatabase.setBackground(new Color(0,170,237));
        sideMenuCashRegister.setBackground(new Color(0,149,221));

    }//GEN-LAST:event_sideMenuCashRegisterMouseClicked

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        String keyword = searchBar.getText().trim();
        if(keyword.length()==0) {
            loadStockPharmacy(); viewStock();
        }else {
            searchStockbyKeyword(keyword);viewStock();
        }
    }//GEN-LAST:event_searchBtnActionPerformed

    private void addStockBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockBtnMouseClicked
        addStocksPanel.setVisible(true);
        addProductsPanel.setVisible(false);
        addSupplierPanel.setVisible(false);
        detailPanel.setVisible(false);
        stocksPanel.setVisible(false);
        productPanel.setVisible(false);
        supplierPanel.setVisible(false);
        viewRowStockBtn.setVisible(false);

        deleteStockRowBtn.setVisible(false);
        deleteProductRowBtn.setVisible(false);
        deleteSupplierRowBtn.setVisible(false);
    }//GEN-LAST:event_addStockBtnMouseClicked

    private void addProductBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductBtnMouseClicked
        addStocksPanel.setVisible(false);
        addProductsPanel.setVisible(true);
        addSupplierPanel.setVisible(false);
        detailPanel.setVisible(false);
        stocksPanel.setVisible(false);
        productPanel.setVisible(false);
        supplierPanel.setVisible(false);
        viewRowStockBtn.setVisible(false);

        deleteStockRowBtn.setVisible(false);
        deleteProductRowBtn.setVisible(false);
        deleteSupplierRowBtn.setVisible(false);
    }//GEN-LAST:event_addProductBtnMouseClicked

    private void addSupplierBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierBtnMouseClicked
        addStocksPanel.setVisible(false);
        addProductsPanel.setVisible(false);
        addSupplierPanel.setVisible(true);
        detailPanel.setVisible(false);
        stocksPanel.setVisible(false);
        productPanel.setVisible(false);
        supplierPanel.setVisible(false);
        viewRowStockBtn.setVisible(false);

        deleteStockRowBtn.setVisible(false);
        deleteProductRowBtn.setVisible(false);
        deleteSupplierRowBtn.setVisible(false);
    }//GEN-LAST:event_addSupplierBtnMouseClicked

    private void viewStocksBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewStocksBtnMouseClicked
        //view panel
        stocksPanel.setVisible(true);
        productPanel.setVisible(false);
        supplierPanel.setVisible(false);
        categoryPanel.setVisible(false);
        addStocksPanel.setVisible(false);
        addProductsPanel.setVisible(false);
        addSupplierPanel.setVisible(false);

        //btn
        addSupplierBtn.setVisible(false);
        addProductBtn.setVisible(false);
        addStockBtn.setVisible(true);
        viewRowStockBtn.setVisible(true);

        deleteStockRowBtn.setVisible(true);
        deleteProductRowBtn.setVisible(false);
        deleteSupplierRowBtn.setVisible(false);
    }//GEN-LAST:event_viewStocksBtnMouseClicked

    private void viewStocksBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewStocksBtnMouseEntered
        viewStocksBtn.setBackground(new Color(43,204,244));
        viewStocksBtn.setForeground(Color.white);
    }//GEN-LAST:event_viewStocksBtnMouseEntered

    private void viewStocksBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewStocksBtnMouseExited
        viewStocksBtn.setBackground(new Color(245,245,245));
        viewStocksBtn.setForeground(Color.black);
    }//GEN-LAST:event_viewStocksBtnMouseExited

    private void viewProductsBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProductsBtnMouseClicked
        //panel tab
        stocksPanel.setVisible(false);
        productPanel.setVisible(true);
        supplierPanel.setVisible(false);
        categoryPanel.setVisible(false);
        addStocksPanel.setVisible(false);
        addProductsPanel.setVisible(false);
        addSupplierPanel.setVisible(false);

        //view btn
        addSupplierBtn.setVisible(false);
        addProductBtn.setVisible(true);
        addStockBtn.setVisible(false);

        deleteStockRowBtn.setVisible(false);
        deleteProductRowBtn.setVisible(true);
        deleteSupplierRowBtn.setVisible(false);
    }//GEN-LAST:event_viewProductsBtnMouseClicked

    private void viewProductsBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProductsBtnMouseEntered
        viewProductsBtn.setBackground(new Color(43,204,244));
        viewProductsBtn.setForeground(Color.white);
    }//GEN-LAST:event_viewProductsBtnMouseEntered

    private void viewProductsBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProductsBtnMouseExited
        viewProductsBtn.setBackground(new Color(245,245,245));
        viewProductsBtn.setForeground(Color.black);
    }//GEN-LAST:event_viewProductsBtnMouseExited

    private void viewSupplierBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSupplierBtnMouseClicked
        //panel tab
        stocksPanel.setVisible(false);
        productPanel.setVisible(false);
        supplierPanel.setVisible(true);
        categoryPanel.setVisible(false);
        addStocksPanel.setVisible(false);
        addProductsPanel.setVisible(false);
        addSupplierPanel.setVisible(false);

        //btn
        addSupplierBtn.setVisible(true);
        addProductBtn.setVisible(false);
        addStockBtn.setVisible(false);

        deleteStockRowBtn.setVisible(false);
        deleteProductRowBtn.setVisible(false);
        deleteSupplierRowBtn.setVisible(true);
    }//GEN-LAST:event_viewSupplierBtnMouseClicked

    private void viewSupplierBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSupplierBtnMouseEntered
        viewSupplierBtn.setBackground(new Color(43,204,244));
        viewSupplierBtn.setForeground(Color.white);
    }//GEN-LAST:event_viewSupplierBtnMouseEntered

    private void viewSupplierBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSupplierBtnMouseExited
        viewSupplierBtn.setBackground(new Color(245,245,245));
        viewSupplierBtn.setForeground(Color.black);
    }//GEN-LAST:event_viewSupplierBtnMouseExited

    private void viewRowStockBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewRowStockBtnMouseClicked
        detailPanel.setVisible(true);
        stocksPanel.setVisible(false);
        productPanel.setVisible(false);
        supplierPanel.setVisible(false);
        addStocksPanel.setVisible(false);
        addProductsPanel.setVisible(false);
        addSupplierPanel.setVisible(false);

        viewRowStockBtn.setVisible(false);
        

        addSupplierBtn.setVisible(false);
        addProductBtn.setVisible(false);
        addStockBtn.setVisible(false);

        deleteStockRowBtn.setVisible(false);
        deleteProductRowBtn.setVisible(false);
        deleteSupplierRowBtn.setVisible(false);

        clickedDetailRow();
    }//GEN-LAST:event_viewRowStockBtnMouseClicked

    private void viewRowStockBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewRowStockBtnMouseEntered
        viewRowStockBtn.setBackground(new Color(43,204,116));
        viewRowStockBtn.setForeground(Color.white);
    }//GEN-LAST:event_viewRowStockBtnMouseEntered

    private void viewRowStockBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewRowStockBtnMouseExited
        viewRowStockBtn.setBackground(new Color(111,216,101));
        viewRowStockBtn.setForeground(Color.white);
    }//GEN-LAST:event_viewRowStockBtnMouseExited

    private void viewCategoryBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewCategoryBtnMouseClicked
        //panel tab
        stocksPanel.setVisible(false);
        productPanel.setVisible(false);
        supplierPanel.setVisible(false);
        categoryPanel.setVisible(true);
        addStocksPanel.setVisible(false);
        addProductsPanel.setVisible(false);
        addSupplierPanel.setVisible(false);

        //btn
        addSupplierBtn.setVisible(false);
        addProductBtn.setVisible(false);
        addStockBtn.setVisible(false);

        deleteStockRowBtn.setVisible(false);
        deleteProductRowBtn.setVisible(false);
        deleteSupplierRowBtn.setVisible(false);
    }//GEN-LAST:event_viewCategoryBtnMouseClicked

    private void viewCategoryBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewCategoryBtnMouseEntered
        viewCategoryBtn.setBackground(new Color(43,204,244));
        viewCategoryBtn.setForeground(Color.white);
    }//GEN-LAST:event_viewCategoryBtnMouseEntered

    private void viewCategoryBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewCategoryBtnMouseExited
        viewCategoryBtn.setBackground(new Color(245,245,245));
        viewCategoryBtn.setForeground(Color.black);
    }//GEN-LAST:event_viewCategoryBtnMouseExited

    private void sellingPriceTfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellingPriceTfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sellingPriceTfActionPerformed

    private void buyingPriceTfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyingPriceTfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buyingPriceTfActionPerformed

    private void addStockInputBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockInputBtnMouseClicked
        Date dateSupplied = dateSuppliedPicker.getDate();
        Date dateMfd = mfdDatePicker.getDate();
        Date dateExp = expiredDatePicker.getDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDateSupply = dateFormat.format(dateSupplied);
        String strDateMfd = dateFormat.format(dateMfd);
        String strDateExp = dateFormat.format(dateExp);

        String productID = productIDTf.getText();
        String productName = productNameTf.getText();
        String category = selectCategory.getSelectedItem().toString();
        int categorySelect = Character.getNumericValue(category.charAt(0));
        String supplierId = supplierIDCombo.getSelectedItem().toString();
        String supplierID = supplierId.substring(0, 13);
        int qty = Integer.parseInt(quantityTf.getText());
        double buyingPrice = Double.parseDouble(buyingPriceTf.getText());
        double sellingPrice = Double.parseDouble(sellingPriceTf.getText());
        String dateSupply = strDateSupply;
        String batchNumber = batchNumberTf.getText();
        String mfdDate = strDateMfd;
        String expDate = strDateExp;

        if(productID.equals("") || supplierID.equals("")) {
            JOptionPane.showMessageDialog(this, "Isi Field yang kosong");
        } else {
            addStock(productID, supplierID, productName, categorySelect, qty, buyingPrice, sellingPrice, dateSupply, batchNumber, mfdDate, expDate);
        }
        resetAddStock();
    }//GEN-LAST:event_addStockInputBtnMouseClicked

    private void addStockInputBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockInputBtnMouseEntered
        addStockInputBtn.setBackground(new Color(43,204,116));
        addStockInputBtn.setForeground(Color.white);
    }//GEN-LAST:event_addStockInputBtnMouseEntered

    private void addStockInputBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockInputBtnMouseExited
        addStockInputBtn.setBackground(new Color(111,216,101));
        addStockInputBtn.setForeground(Color.white);
    }//GEN-LAST:event_addStockInputBtnMouseExited

    private void addStockresetBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockresetBtnMouseClicked
        resetAddStock();
    }//GEN-LAST:event_addStockresetBtnMouseClicked

    private void addStockresetBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockresetBtnMouseEntered
        addStockresetBtn.setBackground(new Color(43,204,244));
        addStockresetBtn.setForeground(Color.white);
    }//GEN-LAST:event_addStockresetBtnMouseEntered

    private void addStockresetBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockresetBtnMouseExited
        addStockresetBtn.setBackground(new Color(235,242,255));
        addStockresetBtn.setForeground(Color.black);
    }//GEN-LAST:event_addStockresetBtnMouseExited

    private void addStockCancelBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockCancelBtnMouseClicked
        detailPanel.setVisible(false);
        stocksPanel.setVisible(true);
        productPanel.setVisible(false);
        supplierPanel.setVisible(false);

       
        viewRowStockBtn.setVisible(true);

        addSupplierBtn.setVisible(false);
        addProductBtn.setVisible(false);
        addStockBtn.setVisible(true);
    }//GEN-LAST:event_addStockCancelBtnMouseClicked

    private void addStockCancelBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockCancelBtnMouseEntered
        addStockCancelBtn.setBackground(new Color(43,204,244));
        addStockCancelBtn.setForeground(Color.white);
    }//GEN-LAST:event_addStockCancelBtnMouseEntered

    private void addStockCancelBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStockCancelBtnMouseExited
        addStockCancelBtn.setBackground(new Color(235,242,255));
        addStockCancelBtn.setForeground(Color.black);
    }//GEN-LAST:event_addStockCancelBtnMouseExited

    private void sellingPriceTf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellingPriceTf1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sellingPriceTf1ActionPerformed

    private void addProductResetBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductResetBtnMouseClicked
         resetAddProduct();
    }//GEN-LAST:event_addProductResetBtnMouseClicked

    private void addProductResetBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductResetBtnMouseEntered
        addProductResetBtn.setBackground(new Color(43,204,244));
        addProductResetBtn.setForeground(Color.white);
    }//GEN-LAST:event_addProductResetBtnMouseEntered

    private void addProductResetBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductResetBtnMouseExited
        addProductResetBtn.setBackground(new Color(235,242,255));
        addProductResetBtn.setForeground(Color.black);
    }//GEN-LAST:event_addProductResetBtnMouseExited

    private void addProductCancelBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductCancelBtnMouseClicked
       detailPanel.setVisible(false);
        stocksPanel.setVisible(false);
        productPanel.setVisible(true);
        supplierPanel.setVisible(false);

       
        viewRowStockBtn.setVisible(true);

        addSupplierBtn.setVisible(false);
        addProductBtn.setVisible(false);
        addStockBtn.setVisible(true);
    }//GEN-LAST:event_addProductCancelBtnMouseClicked

    private void addProductCancelBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductCancelBtnMouseEntered
        addProductCancelBtn.setBackground(new Color(43,204,244));
        addProductCancelBtn.setForeground(Color.white);
    }//GEN-LAST:event_addProductCancelBtnMouseEntered

    private void addProductCancelBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductCancelBtnMouseExited
        addProductCancelBtn.setBackground(new Color(235,242,255));
        addProductCancelBtn.setForeground(Color.black);
    }//GEN-LAST:event_addProductCancelBtnMouseExited

    private void addProductInputBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductInputBtnMouseClicked
        String productID = productIDTf1.getText();
        String productName = productNameTf1.getText();
        String category = selectCategory1.getSelectedItem().toString();
        int categorySelect = Character.getNumericValue(category.charAt(0));
        String supplierId = supplierIDCombo1.getSelectedItem().toString();
        String supplierID = supplierId.substring(0, 13);
        String price = sellingPriceTf1.getText();
        double sellingPrice = Double.parseDouble(price);

        if(productID.equals("") || supplierID.equals("") || productName.equals("") || selectCategory1.getSelectedIndex() == 0 || price.equals("")) {
            JOptionPane.showMessageDialog(this, "Fill in the empty field");
        } else {
            addProduct(productID, supplierID, productName, categorySelect, sellingPrice);
        }
        resetAddProduct();
    }//GEN-LAST:event_addProductInputBtnMouseClicked

    private void addProductInputBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductInputBtnMouseEntered
        addProductInputBtn.setBackground(new Color(43,204,116));
        addProductInputBtn.setForeground(Color.white);
    }//GEN-LAST:event_addProductInputBtnMouseEntered

    private void addProductInputBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addProductInputBtnMouseExited
        addProductInputBtn.setBackground(new Color(111,216,101));
        addProductInputBtn.setForeground(Color.white);
    }//GEN-LAST:event_addProductInputBtnMouseExited

    private void addSupplierResetBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierResetBtnMouseClicked
        resetAddSupplier();
    }//GEN-LAST:event_addSupplierResetBtnMouseClicked

    private void addSupplierResetBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierResetBtnMouseEntered
        addSupplierResetBtn.setBackground(new Color(43,204,244));
        addSupplierResetBtn.setForeground(Color.white);
    }//GEN-LAST:event_addSupplierResetBtnMouseEntered

    private void addSupplierResetBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierResetBtnMouseExited
        addProductResetBtn.setBackground(new Color(235,242,255));
        addProductResetBtn.setForeground(Color.black);
    }//GEN-LAST:event_addSupplierResetBtnMouseExited

    private void addSupplierCancelBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierCancelBtnMouseClicked
        detailPanel.setVisible(false);
        stocksPanel.setVisible(false);
        productPanel.setVisible(false);
        supplierPanel.setVisible(true);

       
        viewRowStockBtn.setVisible(true);

        addSupplierBtn.setVisible(false);
        addProductBtn.setVisible(false);
        addStockBtn.setVisible(true);
    }//GEN-LAST:event_addSupplierCancelBtnMouseClicked

    private void addSupplierCancelBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierCancelBtnMouseEntered
        addSupplierCancelBtn.setBackground(new Color(43,204,244));
        addSupplierCancelBtn.setForeground(Color.white);
    }//GEN-LAST:event_addSupplierCancelBtnMouseEntered

    private void addSupplierCancelBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierCancelBtnMouseExited
        addSupplierCancelBtn.setBackground(new Color(235,242,255));
        addSupplierCancelBtn.setForeground(Color.black);
    }//GEN-LAST:event_addSupplierCancelBtnMouseExited

    private void addSupplierInputBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierInputBtnMouseClicked

        String supplierID = supplierIDTf.getText();
        String supplierName = supplierNameTf.getText();
        String supplierContact = supplierContactTf.getText();
        String supplierAddress = supplierAddressTA.getText();

        if(supplierID.equals("") || supplierName.equals("") || supplierContact.equals("") || supplierAddress.equals("")) {
            JOptionPane.showMessageDialog(this, "Fill in the empty field");
        } else {
            addSupplier(supplierID, supplierName, supplierContact, supplierAddress);
        }
        resetAddSupplier();
    }//GEN-LAST:event_addSupplierInputBtnMouseClicked

    private void addSupplierInputBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierInputBtnMouseEntered
        addSupplierInputBtn.setBackground(new Color(43,204,116));
        addSupplierInputBtn.setForeground(Color.white);
    }//GEN-LAST:event_addSupplierInputBtnMouseEntered

    private void addSupplierInputBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSupplierInputBtnMouseExited
        addSupplierInputBtn.setBackground(new Color(111,216,101));
        addSupplierInputBtn.setForeground(Color.white);
    }//GEN-LAST:event_addSupplierInputBtnMouseExited

    private void deleteStockRowBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteStockRowBtnMouseClicked
        int clickedRow = stockTable.getSelectedRow();
        conn = Connection.openConnection();

        String idProductStock = modelStock.getValueAt(clickedRow, 0).toString();

        deleteClickedRowStock(idProductStock);

    }//GEN-LAST:event_deleteStockRowBtnMouseClicked

    private void deleteProductRowBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteProductRowBtnMouseClicked
        int clickedRow = productTable.getSelectedRow();
        conn = Connection.openConnection();

        String idProductStock = modelProduct.getValueAt(clickedRow, 0).toString();

        deleteClickedRowProduct(idProductStock);
    }//GEN-LAST:event_deleteProductRowBtnMouseClicked

    private void deleteSupplierRowBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteSupplierRowBtnMouseClicked
        int clickedRow = supplierTable.getSelectedRow();
        conn = Connection.openConnection();

        String idSupplier = modelSupplier.getValueAt(clickedRow, 0).toString();

        deleteClickedRowSupplier(idSupplier);
    }//GEN-LAST:event_deleteSupplierRowBtnMouseClicked

    private void viewOrderHistoryBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewOrderHistoryBtnMouseClicked
        cashRegisterInputs.setVisible(false);
        orderHistory.setVisible(true);
    }//GEN-LAST:event_viewOrderHistoryBtnMouseClicked

    private void cashRegisterBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cashRegisterBtnMouseClicked
        cashRegisterInputs.setVisible(false);
        orderHistory.setVisible(false);
    }//GEN-LAST:event_cashRegisterBtnMouseClicked

    private void totalPaymentBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalPaymentBtnMouseClicked

        String productID = productIDInputTf4.getText();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
         
        String strDate = dtf.format(now);
        
        

        double sum = 0;
        for(int u = 0; u < orderPh.size(); u++) {
            if(orderid.equalsIgnoreCase(orderPh.get(u).getOrderID())) {
                int qty = orderPh.get(u).getQuantity();
                String dateOrder = orderPh.get(u).getDate();
                double priceProduct = orderPh.get(u).getPrice();
                double multiplyResult = qty * priceProduct;
                sum += orderPh.get(u).getTotalPrice();

                double tax = (1.5 * sum) / 100;
                double totalAll = tax + sum;
                String totalString = Double.toString(sum);
                String totalTax = Double.toString(tax);
                String totalAllString = Double.toString(totalAll);

                totalPaymentTf.setText(totalAllString);
                TaxTf.setText(totalTax);
                subTotalPaymentTf.setText(totalString);

                for(int i = 0; i < stockPh.size(); i++) {
                    if(orderid.equalsIgnoreCase(orderPh.get(u).getOrderID())) {
                        if(orderPh.get(u).getProductID().equalsIgnoreCase(stockPh.get(i).getProductID())) {
                            int quantityBefore = stockPh.get(i).getQuantity();
                            int qtyBuy = orderPh.get(u).getQuantity();
                            int quantityAfter = quantityBefore - qtyBuy;

                            String product = orderPh.get(u).getProductID();

                            changeQuantity(quantityAfter, product);
                        }
                    }
                }

            }

        }
        reportPrint.append("\n\n" + "       RMN Pharmacy" +
            "\n\n" +
            "  ================================================  " + "\n\n"
            + "      Tax                              $ " + TaxTf.getText() + "\n"
            + "      Sub Total                  $ " + subTotalPaymentTf.getText() + "\n"
            + "      Total                           $ " + totalPaymentTf.getText() + "\n\n" +
            "  ================================================  " + "\n\n" +
            "      Date\n\n\n" + "       " + strDate +
            "      Thank You for shopping with us");

        String order = orderPh.get(orderPh.size()-1).getOrderID();
        int orderI = Integer.parseInt(order) + 1;
        orderid = String.valueOf(orderI);

    }//GEN-LAST:event_totalPaymentBtnMouseClicked

    private void inputValueOfProduct4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputValueOfProduct4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputValueOfProduct4ActionPerformed
 private void resetCashRegister() {
        productIDInputTf4.setText("");
        inputValueOfProduct4.setText("");
    }
    private void inputProductBtn4inputProductBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputProductBtn4inputProductBtnMouseClicked
        String productID = productIDInputTf4.getText();

        if(productID.equals("")) {
            JOptionPane.showMessageDialog(this, "Isi Field yang kosong");
        } else {

            for(int i = 0; i < stockPh.size(); i++) {
                if(productID.equalsIgnoreCase(stockPh.get(i).getProductID())) {

                    int quantityProduct = Integer.parseInt(inputValueOfProduct4.getText());
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                    LocalDateTime now = LocalDateTime.now();  
         
                    String strDate = dtf.format(now);

                    String productName = stockPh.get(i).getProductName();
                    double price = stockPh.get(i).getSellingPrice();
                    double total_price = quantityProduct * price;
                    int qtyAfter = stockPh.get(i).getQuantity();
                    addOrders(orderid, productID, productName, strDate, quantityProduct, price, total_price, qtyAfter);
                    resetCashRegister();
                    viewProducts.setText("");
                    loadOrderPharmacy();viewOrder();

                    for(int u = 0; u < orderPh.size(); u++) {
                        int qty = orderPh.get(u).getQuantity();
                        double priceProduct = orderPh.get(u).getPrice();
                        double multiplyResult = qty * priceProduct;

                        if(orderid.equalsIgnoreCase(orderPh.get(u).getOrderID())) {

                            viewProducts.append(orderPh.get(u).getProductName() + " X " + orderPh.get(u).getQuantity() + "\n"
                                + multiplyResult + "\n\n"
                            );

                        }

                    }
                    reportPrint.setText("");
                }

            }

        }

    }//GEN-LAST:event_inputProductBtn4inputProductBtnMouseClicked

    private void printReceiptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printReceiptMouseClicked
        try {
            boolean complete = reportPrint.print();
            if(complete) {
                JOptionPane.showMessageDialog(null, "Done Printing", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Printing!", "Printer", JOptionPane.ERROR_MESSAGE);
            }
        } catch (PrinterException ex) {
            Logger.getLogger(MainMenuJF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_printReceiptMouseClicked

    private void sideMenuDatabaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sideMenuDatabaseMouseClicked
        //visibility
        DatabasePanelCard.setVisible(true);
        cashRegisterPanel.setVisible(false);

        //design
        sideMenuCashRegister.setBackground(new Color(0,170,237));
        sideMenuDatabase.setBackground(new Color(0,149,221));
    }//GEN-LAST:event_sideMenuDatabaseMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainMenuJF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenuJF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenuJF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenuJF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenuJF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BatchNumberLabel;
    private javax.swing.JPanel DatabasePanelBox;
    private javax.swing.JPanel DatabasePanelCard;
    private javax.swing.JPanel DetailBottom;
    private javax.swing.JPanel DetailBox;
    private javax.swing.JPanel DetailTop;
    private javax.swing.JPanel DisplayMenu;
    private javax.swing.JTextField TaxTf;
    private javax.swing.JPanel addProductBox;
    private javax.swing.JLabel addProductBtn;
    private javax.swing.JLabel addProductCancelBtn;
    private javax.swing.JLabel addProductInputBtn;
    private javax.swing.JLabel addProductResetBtn;
    private javax.swing.JPanel addProductsPanel;
    private javax.swing.JPanel addStockBox;
    private javax.swing.JLabel addStockBtn;
    private javax.swing.JLabel addStockCancelBtn;
    private javax.swing.JLabel addStockInputBtn;
    private javax.swing.JLabel addStockTitle;
    private javax.swing.JLabel addStockTitle1;
    private javax.swing.JLabel addStockresetBtn;
    private javax.swing.JPanel addStocksPanel;
    private javax.swing.JLabel addSupplierBtn;
    private javax.swing.JLabel addSupplierCancelBtn;
    private javax.swing.JLabel addSupplierInputBtn;
    private javax.swing.JPanel addSupplierPanel;
    private javax.swing.JPanel addSupplierPanel2;
    private javax.swing.JLabel addSupplierResetBtn;
    private javax.swing.JLabel addSupplierTitle;
    private javax.swing.JTextField batchNumberTf;
    private javax.swing.JLabel batchNumberTfLabel;
    private javax.swing.JPanel bg;
    private javax.swing.JLabel buyPriceLabel1;
    private javax.swing.JTextField buyingPriceTf;
    private javax.swing.JLabel buyingPriceTfLabel;
    private javax.swing.JPanel cashRegisterBox;
    private javax.swing.JLabel cashRegisterBtn;
    private javax.swing.JPanel cashRegisterInputs;
    private javax.swing.JLabel cashRegisterLabel;
    private javax.swing.JPanel cashRegisterPanel;
    private javax.swing.JPanel cashRegisterView;
    private javax.swing.JLabel categoryIDTfLabel;
    private javax.swing.JLabel categoryIDTfLabel1;
    private javax.swing.JLabel categoryLabel1;
    private javax.swing.JPanel categoryPanel;
    private javax.swing.JLabel categoryProductLabel1;
    private javax.swing.JTable categoryTable;
    private javax.swing.JPanel closeMinimizePanel;
    private javax.swing.JLabel databaseLabel;
    private javax.swing.JLabel dateLabel1;
    private javax.swing.JLabel dateOfOrder;
    private javax.swing.JLabel dateSuppliedLabel1;
    private org.jdesktop.swingx.JXDatePicker dateSuppliedPicker;
    private javax.swing.JLabel dateSupplierTfLabel;
    private javax.swing.JLabel deleteProductRowBtn;
    private javax.swing.JLabel deleteStockRowBtn;
    private javax.swing.JLabel deleteSupplierRowBtn;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JLabel dollarSign1Label;
    private javax.swing.JLabel dollarSignLabel2;
    private javax.swing.JLabel dollarSignLabel3;
    private javax.swing.JLabel expiredDateLabel1;
    private org.jdesktop.swingx.JXDatePicker expiredDatePicker;
    private javax.swing.JLabel expiredDateTfLabel;
    private javax.swing.JLabel idSupplierLabel1;
    private javax.swing.JLabel inputProductBtn4;
    private javax.swing.JTextField inputValueOfProduct4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel logoCashRegister;
    private javax.swing.JLabel logoDatabase;
    private javax.swing.JLabel mfdDateLabel1;
    private org.jdesktop.swingx.JXDatePicker mfdDatePicker;
    private javax.swing.JLabel mfdDateTfLabel;
    private javax.swing.JPanel orderHistory;
    private javax.swing.JScrollPane orderHistoryScrollPane;
    private javax.swing.JTable orderTable;
    private javax.swing.JScrollPane orderTableScrollPane;
    private javax.swing.JLabel pharmacyLogo;
    private javax.swing.JLabel priceLabel1;
    private javax.swing.JLabel printReceipt;
    private javax.swing.JTextField productIDInputTf4;
    private javax.swing.JLabel productIDLabel;
    private javax.swing.JTextField productIDTf;
    private javax.swing.JTextField productIDTf1;
    private javax.swing.JLabel productIDTfLabel;
    private javax.swing.JLabel productIDTfLabel1;
    private javax.swing.JLabel productNameLabel;
    private javax.swing.JTextField productNameTf;
    private javax.swing.JTextField productNameTf1;
    private javax.swing.JLabel productNameTfLabel;
    private javax.swing.JLabel productNameTfLabel1;
    private javax.swing.JPanel productPanel;
    private javax.swing.JTable productTable;
    private javax.swing.JLabel quantityLabel1;
    private javax.swing.JTextField quantityTf;
    private javax.swing.JLabel quantityTfLabel;
    private javax.swing.JTextArea reportPrint;
    private javax.swing.JScrollPane scrollPaneCategory;
    private javax.swing.JScrollPane scrollPaneProduct;
    private javax.swing.JScrollPane scrollPaneStock;
    private javax.swing.JScrollPane scrollPaneSupplier;
    private javax.swing.JTextField searchBar;
    private javax.swing.JButton searchBtn;
    private javax.swing.JComboBox selectCategory;
    private javax.swing.JComboBox selectCategory1;
    private javax.swing.JLabel sellingPriceLabel1;
    private javax.swing.JTextField sellingPriceTf;
    private javax.swing.JTextField sellingPriceTf1;
    private javax.swing.JLabel sellingPriceTfLabel;
    private javax.swing.JLabel sellingPriceTfLabel1;
    private javax.swing.JPanel sideMenuCashRegister;
    private javax.swing.JPanel sideMenuDatabase;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JTable stockTable;
    private javax.swing.JPanel stocksPanel;
    private javax.swing.JTextField subTotalPaymentTf;
    private javax.swing.JLabel supplierAddressLabel1;
    private javax.swing.JScrollPane supplierAddressScrollPane;
    private javax.swing.JTextArea supplierAddressTA;
    private javax.swing.JLabel supplierAddressTfLabel;
    private javax.swing.JLabel supplierContactLabel1;
    private javax.swing.JTextField supplierContactTf;
    private javax.swing.JLabel supplierContactTfLabel;
    private javax.swing.JComboBox supplierIDCombo;
    private javax.swing.JComboBox supplierIDCombo1;
    private javax.swing.JTextField supplierIDTf;
    private javax.swing.JLabel supplierIDTfLabel;
    private javax.swing.JLabel supplierIDTfLabel1;
    private javax.swing.JLabel supplierIDTfLabel4;
    private javax.swing.JLabel supplierLabel1;
    private javax.swing.JLabel supplierNameLabel1;
    private javax.swing.JTextField supplierNameTf;
    private javax.swing.JLabel supplierNameTfLabel4;
    private javax.swing.JPanel supplierPanel;
    private javax.swing.JTable supplierTable;
    private javax.swing.JPanel tabPanel;
    private javax.swing.JLabel totalPaymentBtn;
    private javax.swing.JTextField totalPaymentTf;
    private javax.swing.JPanel upperMenuCashRegister;
    private javax.swing.JPanel upperPanel;
    private javax.swing.JLabel viewCategoryBtn;
    private javax.swing.JPanel viewMenuPanel;
    private javax.swing.JLabel viewOrderHistoryBtn;
    private javax.swing.JTextArea viewProducts;
    private javax.swing.JLabel viewProductsBtn;
    private javax.swing.JLabel viewRowStockBtn;
    private javax.swing.JLabel viewStocksBtn;
    private javax.swing.JLabel viewSupplierBtn;
    private javax.swing.JPanel viewTable;
    // End of variables declaration//GEN-END:variables
}
