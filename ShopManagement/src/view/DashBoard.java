/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultButtonModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.StringContent;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import util.DatabageConnection;

/**
 *
 * @author UseR
 */
public class DashBoard extends javax.swing.JFrame {

    DatabageConnection connection = new DatabageConnection();
    PreparedStatement ps;
    ResultSet rs;
    String sql = "";

    PdfFormate show = new PdfFormate();
    private Date sqlDate;

    /**
     * Creates new form DashBoard
     */
    public DashBoard() {
        initComponents();
        setTime();
        getAllSelsData();
        getAllEmployeeData();
        getAllsupplierData();
        getAllStockData();
        getAllCustomarData();
        getAllProductName();
        getCustomarNameToSell();
        getAllsupplierNameToPurches();
        getAllEmployeeNameToSeles();
        getAllselsToReturn();
        getAllAddToCard();
        totalPurchaseReport();
        totalSelesReport();
        homeTodaysSales();
        homeTodaysPurchase();
        totalSelesProfit();
       
        

        AutoCompleteDecorator.decorate(ComboSupplierName);
        AutoCompleteDecorator.decorate(txtSelesProductName);
        AutoCompleteDecorator.decorate(txtSalesSelerName);

        // date for report-----
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);

    }

    //all method  
    public void setTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    java.util.Date date = new java.util.Date();
                    SimpleDateFormat tf = new SimpleDateFormat("h:mm:ss: aa");
                    SimpleDateFormat df = new SimpleDateFormat("EEEE, dd-MM-yyyy");
                    String time = tf.format(date);
                    timeShow.setText(time.split(" ")[0] + " " + time.split(" ")[1]);
                    dateShow.setText(df.format(date));
                }
            }
        }).start();
    }

    public void totalPurchaseReport() {
        sql = "select sum(totalprice) from purchasetable";

        try {
            ps = connection.getcon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                Float totalPrice = rs.getFloat("sum(totalprice)");
                lblTotalPurchase.setText(totalPrice + "");
            }

            // lblTodaysPurchase.setText(rs.getFloat("totalprice")+"");
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void totalSelesReport() {
        sql = "select sum(actualPrice) from salestable";

        try {
            ps = connection.getcon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                Float totalPrice = rs.getFloat("sum(actualPrice)");
                lblTotalSele.setText(totalPrice + "");
            }

            // lblTodaysPurchase.setText(rs.getFloat("totalprice")+"");
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   
    public void totalSelesProfit() {
        sql = "select sum(actualPrice*.09) from salestable";

        try {
            ps = connection.getcon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                Float totalPrice = rs.getFloat("sum(actualPrice*.09)");
                lblTotalProfit.setText(totalPrice + "");
            }

            // lblTodaysPurchase.setText(rs.getFloat("totalprice")+"");
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    public float getSalesTotalPrice() {
        float quantity = Float.parseFloat(txtSelesQuantity.getText().trim());
        float unitPrice = Float.parseFloat(txtSelesUnitPrice.getText().trim());
        float totalPrice = quantity * unitPrice;

        return totalPrice;
    }

    public float getStockTotalPrice() {
        float quantity = Float.parseFloat(txtStockQuantity.getText().trim());
        float unitPrice = Float.parseFloat(txtStockUnitPrice.getText().trim());
        float totalPrice = quantity * unitPrice;

        return totalPrice;
    }

    public float getPurchaseTotalPrice() {
        float quantity = Float.parseFloat(txtPurchaseQuantity.getText().trim());
        float unitPrice = Float.parseFloat(txtPurchaseUnitPrice.getText().trim());
        float totalPrice = quantity * unitPrice;
        return totalPrice;
    }

    //Start All method--------
    public float getActualPrice() {
        float grandTotal = Float.parseFloat(txtSelesGrandTotal.getText().trim());
        float totalPrice = grandTotal;
        float discount = Float.parseFloat(txtSelesDiscount.getText().trim());

        float actualPrice = totalPrice - (totalPrice * discount / 100);

        return actualPrice;
    }

    public void setQunantitySales(String productName, float quantity) {
        sql = "update  stocktable  set quantity=quantity-? where name=?";

        try {
            ps = connection.getcon().prepareStatement(sql);

            ps.setFloat(1, quantity);
            ps.setString(2, productName);

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Quantity does not Updated");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Date convertUtilDateToSqlDate(java.util.Date utilDate) {

        if (utilDate != null) {
            return new Date(utilDate.getTime());
        }
        return null;
    }

    public static java.util.Date convertStringToDate(String dateString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            System.err.println("date error");
            return null;
        }
    }

    public void getCustomarNameToSell() {
        comboSelesCustomarName.removeAllItems();
        sql = "select customarname from customar";
        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("customarname");
                comboSelesCustomarName.addItem(name);
            }
            ps.close();
            rs.close();
            connection.getcon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // public void getAllNameTo

    public void getAllsupplierNameToPurches() {
        ComboSupplierName.removeAllItems();
        sql = "select name from suppliertable";
        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                ComboSupplierName.addItem(name);
            }
            ps.close();
            rs.close();
            connection.getcon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getAllEmployeeNameToSeles() {
        txtSalesSelerName.removeAllItems();
        sql = "select name from employeetable";
        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                txtSalesSelerName.addItem(name);

            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getAllProductName() {
        txtSelesProductName.removeAllItems();
        ComboSupplierProductName.removeAllItems();

        // sql = "select name from suppliertable";
        sql = "select name from stocktable";
        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                txtSelesProductName.addItem(name);
                ComboSupplierProductName.addItem(name);
                // ComboSupplierName.addItem(name);

            }
            ps.close();
            rs.close();
            connection.getcon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getAllAddToCard() {
        String[] columns = {"Product Name", "Unit Price", "Quantity", "Total Amount"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        tblCardTable.setModel(model);

    }

    public void getAllCustomarData() {
        String[] columns = {"ID", "Customar Name", "Phone Number", "Address"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        tblCustomarTable.setModel(model);
        sql = "select * from customar";

        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("customarname");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                model.addRow(new Object[]{id, name, phone, address});

            }
            ps.close();
            rs.close();
            connection.getcon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getAllStockData() {
        float totalQuantity = 0;
        float Amount = 0;
        String[] columns = {"Id", "Name", "Unit Price", "Quantity", "Total Amount"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        tblStockTable.setModel(model);
        tblReportView.setModel(model);
        sql = "select * from stocktable";

        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String unitprice = rs.getString("unitprice");
                float quantity = rs.getFloat("quantity");
                float totalAmount = rs.getFloat("totalamount");
                totalQuantity += quantity;
                Amount += totalAmount;
                model.addRow(new Object[]{id, name, unitprice, quantity, totalAmount});
            }

            ps.close();
            rs.close();
            connection.getcon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        TotalReportQuantity.setText(String.valueOf(totalQuantity));
        TotalReportAmount.setText(String.valueOf(Amount));

    }

    public void getAllEmployeeData() {
        String[] columns = {"Id", "Name", "Phone Number", "Address", "salary", "Joning Date"};
        DefaultTableModel dtm3 = new DefaultTableModel();
        dtm3.setColumnIdentifiers(columns);
        tblEmployeeList.setModel(dtm3);
        sql = "select * from employeetable";
        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phonenumber = rs.getString("phonenumber");
                String address = rs.getString("address");
                String salary = rs.getString("salary");
                Date date = rs.getDate("date");

                dtm3.addRow(new Object[]{id, name, phonenumber, address, salary, date});
            }

            ps.close();
            rs.close();
            connection.getcon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getAllselsToReturn() {
        float actualPrice = 0;
        float tQuantity = 0;
        float profit = 0;
        
        String[] coloumns = {"Id","Customar Name", "name", "Unit Price", "Quantity", "Total Amount"};
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(coloumns);
        tblShowSelesReturn.setModel(tableModel);
        tblReportView.setModel(tableModel);

        sql = "select id,name,unit,quantity,actualPrice from salestable";

        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String customarName = rs.getString("name");
                String name = rs.getString("name");
                float unit = rs.getFloat("unit");
                float quantity = rs.getFloat("quantity");
                float total = rs.getFloat("actualPrice");
                actualPrice += total;
                tQuantity += quantity;
                profit += total *0.05;
                tableModel.addRow(new Object[]{id,customarName, name, unit, quantity, total});

            }
            ps.close();
            rs.close();
            connection.getcon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        TotalReportAmount.setText(String.valueOf(actualPrice));
        TotalReportQuantity.setText(String.valueOf(tQuantity));
        lblTodaysProfit.setText(String.valueOf(profit));
        
    }

    public void getAllsupplierData() {
        String[] columns = {"Id", "Supplier Name", "Phone Number", "Address", "Product Name", "Quatity"};
        DefaultTableModel dtm2 = new DefaultTableModel();
        dtm2.setColumnIdentifiers(columns);
        tblSupplierDeteils.setModel(dtm2);

        sql = "select * from suppliertable";

        try {
            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String number = rs.getString("number");
                String address = rs.getString("address");
                String productname = rs.getString("productname");
                float quantity = rs.getFloat("quantity");

                dtm2.addRow(new Object[]{id, name, number, address, productname, quantity});
            }

            ps.close();
            rs.close();
            connection.getcon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getAllPurchaseData() {
        float totalQuantity = 0;
        float amount = 0;

        String[] columns = {"Id", "Product Name", "Unit Price", "Quantity", "Total Price", "Purchase Date", "Supplier"};
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setColumnIdentifiers(columns);
        tblReportView.setModel(dtm);
        sql = "select * from purchasetable";

        try {

            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("purchaseid");
                String name = rs.getString("productname");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalprice");
                Date date = rs.getDate("purchasedate");
                String supplier = rs.getString("supplier");
                totalQuantity += quantity;
                amount += totalPrice;

                dtm.addRow(new Object[]{id, name, unitPrice, quantity, totalPrice, date, supplier});
            }

            ps.close();
            connection.getcon().close();
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

        TotalReportAmount.setText(String.valueOf(amount));
        TotalReportQuantity.setText(String.valueOf(totalQuantity));

    }

    public void getAllSelsData() {
        
        String[] columns = {"Id", "Product Name", "Unit Price", "Quantity", "Total Price", "Purchase Date", "Supplier"};
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setColumnIdentifiers(columns);
        tblPurchaseTable.setModel(dtm);

        sql = "select * from purchasetable";

        try {

            ps = connection.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("purchaseid");
                String name = rs.getString("productname");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalprice");
                Date date = rs.getDate("purchasedate");
                String supplier = rs.getString("supplier");

                dtm.addRow(new Object[]{id, name, unitPrice, quantity, totalPrice, date, supplier});
            }

            ps.close();
            connection.getcon().close();
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPurchaseDataToStock() {

        try {
            sql = "insert into stocktable (name,unitprice,quantity,totalamount)value(?,?,?,?) ON DUPLICATE KEY UPDATE quantity = quantity +values(quantity)";

            ps = connection.getcon().prepareStatement(sql);
            ps.setString(1, txtPurchaseProductName.getText().trim());
            ps.setFloat(2, Float.parseFloat(txtPurchaseUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtPurchaseTotalPrice.getText().trim()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();
        } catch (SQLException ex) {

            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //all Reset Method 
    public void selesReset() {
        txtSelesProductName.setSelectedItem("");
        txtSelesUnitPrice.setText("");
        txtSelesQuantity.setText("");
        txtSelesTotalPrice.setText("");
        //txtSelesGrandTotal.setText("");
        txtSelesDiscount.setText("");
        txtSelesActualPrice.setText("");
        txtSelesReceiveAmount.setText("");
        txtSelesReturnAmount.setText("");
        comboSelesCustomarName.setSelectedItem("");
        txtSalesSelerName.setSelectedItem("");
        //DefaultTableModel model = (DefaultTableModel) tblCardTable.getModel();
        // model.setRowCount(0);

    }

    public void supplierReset() {
        txtSupplierID.setText(null);
        txtSupplierName.setText(null);
        txtSupplierAddress.setText(null);
        txtSupplierphoneNumber.setText(null);
        ComboSupplierProductName.setSelectedItem(null);
        txtSupplierProductQuantity.setText(null);
    }

    public void purchaseReset() {
        txtPurchaseID.setText("");
        txtPurchaseProductName.setText("");
        txtPurchaseUnitPrice.setText("");
        txtPurchaseQuantity.setText("");
        txtPurchaseTotalPrice.setText("");
        txtPurchaseDate.setDate(null);
        ComboSupplierName.setSelectedItem(null);
    }

    public void employeeReset() {
        txtEmployeeID.setText(null);
        txtEmployeeName.setText(null);
        txtEmployeePhoneNumber.setText(null);
        txtEmployeeAddress.setText(null);
        txtEmployeeSalary.setText(null);
        dateEmployeeJoin.setDate(null);
    }

    public void stockReset() {
        txtStockID.setText(null);
        txtStockName.setText(null);
        txtStockUnitPrice.setText(null);
        txtStockQuantity.setText(null);
        txtStockTotalAmount.setText(null);
    }

    public void customarReset() {
        txtCustomarID.setText(null);
        txtCustomarName.setText(null);
        txtCustomarMobileNmber.setText(null);
        txtCustomarAddress.setText(null);
    }
      public void ReturnReset() {
        txtReturnProductID.setText(null);
        txtReturnCustomarName.setText(null);
        txtReturnProductName.setText(null);
        txtReturnProductUnitPrice.setText(null);
        txtReturnProductQuantity.setText(null);
        txtReturnSelesAmount.setText(null);
    }
    

    //   Todays Sales
    public void homeTodaysSales() {

        long currentTimeMillis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example format
        String formattedDate = dateFormat.format(timestamp);

        String sql = "SELECT actualPrice FROM salestable WHERE date = ?";
        float actualPrice = 0;
        try {
            ps = connection.getcon().prepareStatement(sql);
            ps.setDate(1, Date.valueOf(formattedDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                actualPrice += rs.getFloat("actualprice");

            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQLException appropriately
        } finally {
            // Close resources (ResultSet, PreparedStatement, Connection) in a finally block
            // This ensures that resources are closed even if an exception occurs
        }
        lblTodaysSale.setText(String.valueOf(actualPrice));

    }

    public void homeTodaysPurchase() {
// Format the current system date as a string
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Example format
        String formattedDate = dateFormat.format(timestamp);

        //  System.out.println("Date "+formattedDate);
// Prepare the SQL query and set the date parameter
        String sql = "SELECT totalprice FROM purchasetable WHERE purchasedate = ?";
        float todaysTotal = 0;
        try {
            ps = connection.getcon().prepareStatement(sql);
            ps.setDate(1, Date.valueOf(formattedDate));
            rs = ps.executeQuery();

            while (rs.next()) {

                todaysTotal += rs.getFloat("totalprice");

            }

            // Process the ResultSet as needed
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQLException appropriately
            System.err.println("SQL Error: " + e.getMessage());
        } finally {
            // Close resources (ResultSet, PreparedStatement, Connection) in a finally block
            // This ensures that resources are closed even if an exception occurs
        }
        lblTodaysPurchase.setText(String.valueOf(todaysTotal));

    }
   
    
    
    
    // End All method-------
    
    
    
    
    
    
    
    
    
    
    // start all button method
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        dateShow = new javax.swing.JLabel();
        timeShow = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSales = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnPurchase = new javax.swing.JButton();
        btnStock = new javax.swing.JButton();
        btnCustomarDateils = new javax.swing.JButton();
        btnSupplierDateils = new javax.swing.JButton();
        btnEmployeeList = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
        btnReturn1 = new javax.swing.JButton();
        btnLogOut = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        menu = new javax.swing.JTabbedPane();
        home = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        lblTodaysSale = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        lblTotalSele = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        lblTodaysPurchase = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        lblTodaysProfit = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        lblTotalPurchase = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        lblTotalProfit = new javax.swing.JLabel();
        sales = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtSelesDate = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSelesID = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtSelesDiscount = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtSelesActualPrice = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtSelesReceiveAmount = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtSelesUnitPrice = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtSelesQuantity = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtSelesReturnAmount = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCardTable = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        txtSelesTotalPrice = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        btnSalesPrint = new javax.swing.JButton();
        btnSalesReset = new javax.swing.JButton();
        btnSelesAddToCard = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtSelesProductName = new javax.swing.JComboBox<>();
        btnSalesSaved = new javax.swing.JButton();
        comboSelesCustomarName = new javax.swing.JComboBox<>();
        txtSalesSelerName = new javax.swing.JComboBox<>();
        jLabel74 = new javax.swing.JLabel();
        txtSelesGrandTotal = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        purchase = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        btnPurchaseSave = new javax.swing.JButton();
        btnPurchaseDelete = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblPurchaseTable = new javax.swing.JTable();
        btnPurchaseUpdate = new javax.swing.JButton();
        btnPurchaseReset = new javax.swing.JButton();
        txtPurchaseID = new javax.swing.JTextField();
        txtPurchaseUnitPrice = new javax.swing.JTextField();
        txtPurchaseQuantity = new javax.swing.JTextField();
        txtPurchaseDate = new com.toedter.calendar.JDateChooser();
        jLabel49 = new javax.swing.JLabel();
        txtPurchaseTotalPrice = new javax.swing.JTextField();
        ComboSupplierName = new javax.swing.JComboBox<>();
        txtPurchaseProductName = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        stock = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStockTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtStockQuantity = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        btnStockDelete = new javax.swing.JButton();
        btnStockUpdate = new javax.swing.JButton();
        btnStockReset = new javax.swing.JButton();
        txtStockID = new javax.swing.JTextField();
        txtStockName = new javax.swing.JTextField();
        txtStockUnitPrice = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        txtStockTotalAmount = new javax.swing.JTextField();
        customar = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblCustomarTable = new javax.swing.JTable();
        jLabel32 = new javax.swing.JLabel();
        txtCustomarID = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtCustomarName = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtCustomarMobileNmber = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        btnCustomarSave = new javax.swing.JButton();
        btnCustomardelete = new javax.swing.JButton();
        btnCustomarUpdate = new javax.swing.JButton();
        btnCustomarReset = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtCustomarAddress = new javax.swing.JTextArea();
        vendor = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSupplierDeteils = new javax.swing.JTable();
        jLabel36 = new javax.swing.JLabel();
        txtSupplierID = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtSupplierName = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtSupplierProductQuantity = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtSupplierAddress = new javax.swing.JTextArea();
        ComboSupplierProductName = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtSupplierphoneNumber = new javax.swing.JTextField();
        btnSupplierSave = new javax.swing.JButton();
        btnSupplierUpdate = new javax.swing.JButton();
        btnSupplierDelete = new javax.swing.JButton();
        btnSupplierReset = new javax.swing.JButton();
        employee = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblEmployeeList = new javax.swing.JTable();
        jLabel43 = new javax.swing.JLabel();
        txtEmployeeID = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txtEmployeePhoneNumber = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtEmployeeSalary = new javax.swing.JTextField();
        dateEmployeeJoin = new com.toedter.calendar.JDateChooser();
        jLabel48 = new javax.swing.JLabel();
        btnEmployeeSave = new javax.swing.JButton();
        btnEmployeeUpdate = new javax.swing.JButton();
        btnEmployeeReset = new javax.swing.JButton();
        btnEmployeeDelete = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtEmployeeAddress = new javax.swing.JTextArea();
        jLabel64 = new javax.swing.JLabel();
        Return = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        txtReturnProductID = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblShowSelesReturn = new javax.swing.JTable();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        txtReturnSelesAmount = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtReturnProductQuantity = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtReturnCustomarName = new javax.swing.JTextField();
        txtReturnProductName = new javax.swing.JTextField();
        btnReturnUpdate = new javax.swing.JButton();
        btnReturnReset = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        txtReturnProductUnitPrice = new javax.swing.JTextField();
        Report = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        radioSelesReport = new javax.swing.JRadioButton();
        radiopurchaseReport = new javax.swing.JRadioButton();
        radioStockReport = new javax.swing.JRadioButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblReportView = new javax.swing.JTable();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        TotalReportQuantity = new javax.swing.JLabel();
        TotalReportAmount = new javax.swing.JLabel();
        printPanel = new javax.swing.JPanel();
        printPanale = new javax.swing.JPanel();
        pdfShow = new javax.swing.JPanel();
        txtUnitPrice = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        txtInvoiceNumber = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        txtCustomarName1 = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        txtProductDiscount = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        txtProductTtalAmount = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        brnprintview = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 153, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 120, 90));

        date.setBackground(new java.awt.Color(127, 178, 251));
        date.setFont(new java.awt.Font("Perpetua Titling MT", 3, 36)); // NOI18N
        date.setForeground(new java.awt.Color(255, 255, 255));
        date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        date.setText("Gadget  Galaxy");
        jPanel2.add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 90));

        dateShow.setFont(new java.awt.Font("Rockwell", 1, 12)); // NOI18N
        dateShow.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(dateShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 10, 200, 30));

        timeShow.setFont(new java.awt.Font("Rockwell", 1, 14)); // NOI18N
        timeShow.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(timeShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 50, 170, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 90));

        jPanel3.setBackground(new java.awt.Color(51, 153, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSales.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnSales.setText("Sales");
        btnSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesActionPerformed(evt);
            }
        });
        jPanel3.add(btnSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 150, 40));

        btnHome.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnHome.setText("Home");
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        jPanel3.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 150, 50));

        btnPurchase.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnPurchase.setText("Purchase");
        btnPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseActionPerformed(evt);
            }
        });
        jPanel3.add(btnPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 150, 40));

        btnStock.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnStock.setText("Stock");
        btnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockActionPerformed(evt);
            }
        });
        jPanel3.add(btnStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 150, 40));

        btnCustomarDateils.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnCustomarDateils.setText("Customar ");
        btnCustomarDateils.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomarDateilsActionPerformed(evt);
            }
        });
        jPanel3.add(btnCustomarDateils, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 150, 40));

        btnSupplierDateils.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnSupplierDateils.setText("Vendor");
        btnSupplierDateils.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierDateilsActionPerformed(evt);
            }
        });
        jPanel3.add(btnSupplierDateils, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 150, 40));

        btnEmployeeList.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnEmployeeList.setText("Employee");
        btnEmployeeList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeListActionPerformed(evt);
            }
        });
        jPanel3.add(btnEmployeeList, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 150, 40));

        btnReport.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnReport.setText("Report");
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        jPanel3.add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 150, 40));

        btnReturn1.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnReturn1.setText("Return");
        btnReturn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturn1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnReturn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 150, 40));

        btnLogOut.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        btnLogOut.setText("Log Out");
        btnLogOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogOutMouseClicked(evt);
            }
        });
        jPanel3.add(btnLogOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 150, 40));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 150, 590));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        home.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Rockwell", 3, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Home");
        jPanel14.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 80));

        home.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 75));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jLabel50.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 18)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("Todays Sales");

        lblTodaysSale.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        lblTodaysSale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodaysSale.setText("0.00");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTodaysSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTodaysSale, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        jLabel66.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 18)); // NOI18N
        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel66.setText("Total Sales");

        lblTotalSele.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        lblTotalSele.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalSele.setText("0.00");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                    .addComponent(lblTotalSele, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalSele, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jLabel51.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 18)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("Todays Purchase");

        lblTodaysPurchase.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        lblTodaysPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodaysPurchase.setText("0.00");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                    .addComponent(lblTodaysPurchase, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTodaysPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));

        jLabel65.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 18)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("Todays Profit");

        lblTodaysProfit.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        lblTodaysProfit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodaysProfit.setText("0.00");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 9, Short.MAX_VALUE))
                    .addComponent(lblTodaysProfit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTodaysProfit, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));

        jLabel67.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 18)); // NOI18N
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel67.setText("Total Purchase");

        lblTotalPurchase.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        lblTotalPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalPurchase.setText("0.00");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTotalPurchase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        jLabel68.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 18)); // NOI18N
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("Total Profit");

        lblTotalProfit.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        lblTotalProfit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalProfit.setText("0.00");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addGap(14, 14, 14))
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addComponent(lblTotalProfit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalProfit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(78, 78, 78)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        home.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 1050, 530));

        menu.addTab("tab1", home);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Rockwell", 3, 48)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("sales");
        jPanel16.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 420, 60));
        jPanel16.add(txtSelesDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 30, 190, 40));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Date");
        jPanel16.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 30, 110, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("ID");
        jPanel16.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 50, -1));

        txtSelesID.setEditable(false);
        jPanel16.add(txtSelesID, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 120, 30));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Discount");
        jPanel17.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 330, 130, -1));

        txtSelesDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSelesDiscountFocusLost(evt);
            }
        });
        jPanel17.add(txtSelesDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 330, 180, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("Product name");
        jPanel17.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 160, 30));
        jPanel17.add(txtSelesActualPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 370, 180, 30));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("Receive Amount");
        jPanel17.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 410, 140, 30));

        txtSelesReceiveAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSelesReceiveAmountFocusLost(evt);
            }
        });
        txtSelesReceiveAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSelesReceiveAmountActionPerformed(evt);
            }
        });
        jPanel17.add(txtSelesReceiveAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 410, 180, 30));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("Unit price");
        jPanel17.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 190, 30));

        txtSelesUnitPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSelesUnitPriceMouseClicked(evt);
            }
        });
        jPanel17.add(txtSelesUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 190, 30));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setText("Quantity");
        jPanel17.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 190, 30));

        txtSelesQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSelesQuantityFocusLost(evt);
            }
        });
        jPanel17.add(txtSelesQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 190, 30));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setText("Return Amount");
        jPanel17.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 460, 140, 30));
        jPanel17.add(txtSelesReturnAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 460, 180, 30));

        tblCardTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tblCardTable);

        jPanel17.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 790, 190));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setText("Total price");
        jPanel17.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 160, 30));

        txtSelesTotalPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSelesTotalPriceFocusLost(evt);
            }
        });
        jPanel17.add(txtSelesTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 190, 30));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel21.setText("Saler Name");
        jPanel17.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, 100, 30));

        btnSalesPrint.setBackground(new java.awt.Color(204, 204, 204));
        btnSalesPrint.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSalesPrint.setText("Print");
        btnSalesPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesPrintMouseClicked(evt);
            }
        });
        jPanel17.add(btnSalesPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 470, 150, 30));

        btnSalesReset.setBackground(new java.awt.Color(204, 204, 204));
        btnSalesReset.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSalesReset.setText("Reset");
        btnSalesReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesResetMouseClicked(evt);
            }
        });
        btnSalesReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesResetActionPerformed(evt);
            }
        });
        jPanel17.add(btnSalesReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 150, 30));

        btnSelesAddToCard.setBackground(new java.awt.Color(204, 204, 204));
        btnSelesAddToCard.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSelesAddToCard.setText("Add To Card");
        btnSelesAddToCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSelesAddToCardMouseClicked(evt);
            }
        });
        jPanel17.add(btnSelesAddToCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 150, 30));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setText("Actual Price");
        jPanel17.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 370, 120, 30));

        txtSelesProductName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "mouse", "keyboard", "monitor", "i-phone", "samgsung", "lenovo", " " }));
        jPanel17.add(txtSelesProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 190, 40));

        btnSalesSaved.setBackground(new java.awt.Color(204, 204, 204));
        btnSalesSaved.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSalesSaved.setText("Saved");
        btnSalesSaved.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesSavedMouseClicked(evt);
            }
        });
        jPanel17.add(btnSalesSaved, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 400, 150, 30));

        comboSelesCustomarName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel17.add(comboSelesCustomarName, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 210, 40));

        txtSalesSelerName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel17.add(txtSalesSelerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 20, 180, 40));

        jLabel74.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel74.setText("Grand Total");
        jPanel17.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 280, 130, -1));

        txtSelesGrandTotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSelesGrandTotalFocusLost(evt);
            }
        });
        jPanel17.add(txtSelesGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 280, 180, 30));

        jLabel75.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel75.setText("Customar Name");
        jPanel17.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 150, 40));

        javax.swing.GroupLayout salesLayout = new javax.swing.GroupLayout(sales);
        sales.setLayout(salesLayout);
        salesLayout.setHorizontalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        salesLayout.setVerticalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesLayout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menu.addTab("tab2", sales);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Rockwell", 1, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Purchase Product");
        jPanel18.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 84));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jLabel24.setText("purchase ID");
        jPanel19.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 140, 30));

        jLabel25.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jLabel25.setText("Product Name");
        jPanel19.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 140, 30));

        jLabel26.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jLabel26.setText("Unit Price");
        jPanel19.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 140, 30));

        jLabel27.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jLabel27.setText("Quantity");
        jPanel19.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 140, 40));

        jLabel28.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jLabel28.setText("Total Price");
        jPanel19.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 140, 30));

        jLabel29.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jLabel29.setText("Supplier");
        jPanel19.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 140, 30));

        btnPurchaseSave.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnPurchaseSave.setText("Save");
        btnPurchaseSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseSaveMouseClicked(evt);
            }
        });
        jPanel19.add(btnPurchaseSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 140, 40));

        btnPurchaseDelete.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnPurchaseDelete.setText("Delete");
        btnPurchaseDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseDeleteMouseClicked(evt);
            }
        });
        jPanel19.add(btnPurchaseDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 430, 130, 30));

        tblPurchaseTable.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPurchaseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPurchaseTableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblPurchaseTable);

        jPanel19.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, 560, 230));

        btnPurchaseUpdate.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnPurchaseUpdate.setText("Update");
        btnPurchaseUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseUpdateMouseClicked(evt);
            }
        });
        jPanel19.add(btnPurchaseUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 430, 140, 30));

        btnPurchaseReset.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnPurchaseReset.setText("Reset");
        btnPurchaseReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseResetMouseClicked(evt);
            }
        });
        jPanel19.add(btnPurchaseReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 430, 130, 40));

        txtPurchaseID.setEditable(false);
        jPanel19.add(txtPurchaseID, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 290, 40));

        txtPurchaseUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseUnitPriceFocusLost(evt);
            }
        });
        jPanel19.add(txtPurchaseUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 290, 40));

        txtPurchaseQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseQuantityFocusLost(evt);
            }
        });
        jPanel19.add(txtPurchaseQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 290, 40));
        jPanel19.add(txtPurchaseDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 10, 230, 30));

        jLabel49.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        jLabel49.setText("Purchase date");
        jPanel19.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 140, 30));
        jPanel19.add(txtPurchaseTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 290, 40));

        ComboSupplierName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----Supplier---", "Nur Telicome", "Bornali Telicome", "Hasan Electronics", "Fast Line ltd.", " " }));
        jPanel19.add(ComboSupplierName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 290, 40));
        jPanel19.add(txtPurchaseProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 290, 40));

        jLabel63.setBackground(new java.awt.Color(255, 255, 255));
        jLabel63.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 51, 51));
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setText("Product List");
        jPanel19.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 160, 30));

        javax.swing.GroupLayout purchaseLayout = new javax.swing.GroupLayout(purchase);
        purchase.setLayout(purchaseLayout);
        purchaseLayout.setHorizontalGroup(
            purchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        purchaseLayout.setVerticalGroup(
            purchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchaseLayout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        menu.addTab("tab3", purchase);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Rockwell", 3, 48)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Stock");
        jPanel20.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 6, 1040, 70));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblStockTable.setModel(new javax.swing.table.DefaultTableModel(
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
        tblStockTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStockTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblStockTable);

        jPanel21.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(512, 111, 522, 240));

        jLabel6.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel6.setText("Quantity");
        jPanel21.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 130, 30));

        txtStockQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStockQuantityFocusLost(evt);
            }
        });
        jPanel21.add(txtStockQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 300, 30));

        jLabel7.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel7.setText("Product ID");
        jPanel21.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 120, 30));

        jLabel30.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel30.setText("Product Name");
        jPanel21.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 120, 30));

        jLabel31.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel31.setText("Unit Price");
        jPanel21.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 130, 30));

        btnStockDelete.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnStockDelete.setText("Delete");
        btnStockDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStockDeleteMouseClicked(evt);
            }
        });
        jPanel21.add(btnStockDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 440, 140, 40));

        btnStockUpdate.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnStockUpdate.setText("Update");
        btnStockUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStockUpdateMouseClicked(evt);
            }
        });
        jPanel21.add(btnStockUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 140, 40));

        btnStockReset.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        btnStockReset.setText("Reset");
        btnStockReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStockResetMouseClicked(evt);
            }
        });
        jPanel21.add(btnStockReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 440, 140, 40));
        jPanel21.add(txtStockID, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 300, 30));
        jPanel21.add(txtStockName, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 300, 30));

        txtStockUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStockUnitPriceFocusLost(evt);
            }
        });
        jPanel21.add(txtStockUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 200, 300, 30));

        jLabel52.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel52.setText("Total Amount");
        jPanel21.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 130, 30));

        jLabel60.setBackground(new java.awt.Color(255, 204, 204));
        jLabel60.setFont(new java.awt.Font("SimSun", 0, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 51, 51));
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("Stock List");
        jPanel21.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 60, 240, 30));

        txtStockTotalAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStockTotalAmountFocusLost(evt);
            }
        });
        jPanel21.add(txtStockTotalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, 300, 30));

        javax.swing.GroupLayout stockLayout = new javax.swing.GroupLayout(stock);
        stock.setLayout(stockLayout);
        stockLayout.setHorizontalGroup(
            stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        stockLayout.setVerticalGroup(
            stockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stockLayout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab4", stock);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Rockwell Condensed", 3, 36)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Customar Dateils");
        jPanel22.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 88));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblCustomarTable.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCustomarTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustomarTableMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblCustomarTable);

        jPanel5.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 1010, 220));

        jLabel32.setText("Customar ID");
        jPanel5.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 120, 39));

        txtCustomarID.setEditable(false);
        jPanel5.add(txtCustomarID, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 230, 40));

        jLabel33.setText("Customar Name");
        jPanel5.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 90, 120, 39));
        jPanel5.add(txtCustomarName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 230, 40));

        jLabel34.setText("Mobile Number");
        jPanel5.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 140, 130, 39));
        jPanel5.add(txtCustomarMobileNmber, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 230, 40));

        jLabel35.setText("Address");
        jPanel5.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 39, 136, 40));

        btnCustomarSave.setText("Save");
        btnCustomarSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomarSaveMouseClicked(evt);
            }
        });
        jPanel5.add(btnCustomarSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 450, 120, 30));

        btnCustomardelete.setText("Delete");
        btnCustomardelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomardeleteMouseClicked(evt);
            }
        });
        jPanel5.add(btnCustomardelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 450, 140, 30));

        btnCustomarUpdate.setText("Update");
        btnCustomarUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomarUpdateMouseClicked(evt);
            }
        });
        jPanel5.add(btnCustomarUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 450, 120, 30));

        btnCustomarReset.setText("Reset");
        btnCustomarReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomarResetMouseClicked(evt);
            }
        });
        jPanel5.add(btnCustomarReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 450, 140, 30));

        txtCustomarAddress.setColumns(20);
        txtCustomarAddress.setRows(5);
        jScrollPane9.setViewportView(txtCustomarAddress);

        jPanel5.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, 340, 100));

        javax.swing.GroupLayout customarLayout = new javax.swing.GroupLayout(customar);
        customar.setLayout(customarLayout);
        customarLayout.setHorizontalGroup(
            customarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        customarLayout.setVerticalGroup(
            customarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customarLayout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab5", customar);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Rockwell", 3, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("vendor Dateils");
        jPanel23.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1040, 80));

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblSupplierDeteils.setModel(new javax.swing.table.DefaultTableModel(
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
        tblSupplierDeteils.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierDeteilsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSupplierDeteils);

        jPanel24.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 1010, 210));

        jLabel36.setText("Supplier ID");
        jPanel24.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 30));

        txtSupplierID.setEditable(false);
        txtSupplierID.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.add(txtSupplierID, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 280, 40));

        jLabel37.setText("Supplier ID");
        jPanel24.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 30));

        jLabel38.setText("Product  Name");
        jPanel24.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, 130, 30));
        jPanel24.add(txtSupplierName, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 280, 40));

        jLabel39.setText("Phone Number");
        jPanel24.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 150, 30));
        jPanel24.add(txtSupplierProductQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 160, 280, 40));

        jLabel40.setText("Shop Address");
        jPanel24.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, 150, 30));

        txtSupplierAddress.setColumns(20);
        txtSupplierAddress.setRows(5);
        jScrollPane8.setViewportView(txtSupplierAddress);

        jPanel24.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, 280, 70));

        ComboSupplierProductName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel24.add(ComboSupplierProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 110, 280, 40));

        jLabel41.setText("Vendor Name");
        jPanel24.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, 30));

        jLabel42.setText("Product  Quantity");
        jPanel24.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 160, 140, 30));
        jPanel24.add(txtSupplierphoneNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 280, 40));

        btnSupplierSave.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSupplierSave.setText("Save");
        btnSupplierSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierSaveMouseClicked(evt);
            }
        });
        jPanel24.add(btnSupplierSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 110, -1));

        btnSupplierUpdate.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSupplierUpdate.setText("Update");
        btnSupplierUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierUpdateMouseClicked(evt);
            }
        });
        jPanel24.add(btnSupplierUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 460, 120, -1));

        btnSupplierDelete.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSupplierDelete.setText("Delete");
        btnSupplierDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierDeleteMouseClicked(evt);
            }
        });
        jPanel24.add(btnSupplierDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 460, 110, -1));

        btnSupplierReset.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnSupplierReset.setText("Reset");
        btnSupplierReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierResetMouseClicked(evt);
            }
        });
        jPanel24.add(btnSupplierReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 460, 120, -1));

        javax.swing.GroupLayout vendorLayout = new javax.swing.GroupLayout(vendor);
        vendor.setLayout(vendorLayout);
        vendorLayout.setHorizontalGroup(
            vendorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        vendorLayout.setVerticalGroup(
            vendorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vendorLayout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        menu.addTab("tab6", vendor);

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Sylfaen", 3, 36)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Employee List");
        jPanel25.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 86));

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblEmployeeList.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEmployeeList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeeListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblEmployeeList);

        jPanel26.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 600, 270));

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel43.setText("Joining Date");
        jPanel26.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, 120, 30));

        txtEmployeeID.setEditable(false);
        jPanel26.add(txtEmployeeID, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 220, 40));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel44.setText("Name");
        jPanel26.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 120, 30));
        jPanel26.add(txtEmployeeName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 220, 40));

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel45.setText("Phone Nmber");
        jPanel26.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 120, 30));
        jPanel26.add(txtEmployeePhoneNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 220, 40));

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel46.setText("Address");
        jPanel26.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 120, 30));

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel47.setText("Salary");
        jPanel26.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 120, 30));
        jPanel26.add(txtEmployeeSalary, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, 220, 40));
        jPanel26.add(dateEmployeeJoin, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 40, 260, 40));

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel48.setText("Employee ID");
        jPanel26.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 120, 30));

        btnEmployeeSave.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEmployeeSave.setText("Save");
        btnEmployeeSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEmployeeSaveMouseClicked(evt);
            }
        });
        jPanel26.add(btnEmployeeSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 110, -1));

        btnEmployeeUpdate.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEmployeeUpdate.setText("Update");
        btnEmployeeUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEmployeeUpdateMouseClicked(evt);
            }
        });
        jPanel26.add(btnEmployeeUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 450, 110, -1));

        btnEmployeeReset.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEmployeeReset.setText("Reset");
        btnEmployeeReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEmployeeResetMouseClicked(evt);
            }
        });
        jPanel26.add(btnEmployeeReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 450, 110, -1));

        btnEmployeeDelete.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEmployeeDelete.setText("Delete");
        btnEmployeeDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEmployeeDeleteMouseClicked(evt);
            }
        });
        jPanel26.add(btnEmployeeDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 450, 120, -1));

        txtEmployeeAddress.setColumns(20);
        txtEmployeeAddress.setRows(5);
        jScrollPane11.setViewportView(txtEmployeeAddress);

        jPanel26.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 220, 80));

        jLabel64.setFont(new java.awt.Font("Poor Richard", 0, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 51, 51));
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("Employee List");
        jPanel26.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, 300, 30));

        javax.swing.GroupLayout employeeLayout = new javax.swing.GroupLayout(employee);
        employee.setLayout(employeeLayout);
        employeeLayout.setHorizontalGroup(
            employeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        employeeLayout.setVerticalGroup(
            employeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeLayout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab7", employee);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Rockwell", 3, 36)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Return Product");
        jPanel27.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 580, 90));

        jLabel54.setFont(new java.awt.Font("Rockwell Condensed", 3, 14)); // NOI18N
        jLabel54.setText("Invoice Number");
        jPanel27.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 110, 20));
        jPanel27.add(txtReturnProductID, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 130, 20));

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblShowSelesReturn.setModel(new javax.swing.table.DefaultTableModel(
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
        tblShowSelesReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblShowSelesReturnMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblShowSelesReturn);

        jPanel28.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 1000, 210));

        jLabel53.setFont(new java.awt.Font("Rockwell Condensed", 3, 14)); // NOI18N
        jLabel53.setText("Customar Name");
        jPanel28.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 130, 30));

        jLabel55.setFont(new java.awt.Font("Rockwell Condensed", 3, 14)); // NOI18N
        jLabel55.setText("Sales Amount ");
        jPanel28.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 150, 170, 30));
        jPanel28.add(txtReturnSelesAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 150, 220, 30));

        jLabel57.setFont(new java.awt.Font("Rockwell Condensed", 3, 14)); // NOI18N
        jLabel57.setText("Product Quantity");
        jPanel28.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 90, 160, 30));
        jPanel28.add(txtReturnProductQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 90, 220, 30));

        jLabel58.setFont(new java.awt.Font("Rockwell Condensed", 3, 14)); // NOI18N
        jLabel58.setText("Product Name");
        jPanel28.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 140, 30));
        jPanel28.add(txtReturnCustomarName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 290, 30));
        jPanel28.add(txtReturnProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 290, 30));

        btnReturnUpdate.setText("Update");
        btnReturnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReturnUpdateMouseClicked(evt);
            }
        });
        jPanel28.add(btnReturnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 440, 130, 40));

        btnReturnReset.setText("Reset");
        btnReturnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReturnResetMouseClicked(evt);
            }
        });
        jPanel28.add(btnReturnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 440, 120, 40));

        jLabel61.setFont(new java.awt.Font("Rockwell Condensed", 3, 14)); // NOI18N
        jLabel61.setText("Product Unit Price");
        jPanel28.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 150, 30));
        jPanel28.add(txtReturnProductUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 30, 220, 30));

        javax.swing.GroupLayout ReturnLayout = new javax.swing.GroupLayout(Return);
        Return.setLayout(ReturnLayout);
        ReturnLayout.setHorizontalGroup(
            ReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ReturnLayout.setVerticalGroup(
            ReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReturnLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab8", Return);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel56.setFont(new java.awt.Font("Rockwell", 3, 48)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("Report");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel56, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));

        buttonGroup2.add(radioSelesReport);
        radioSelesReport.setFont(new java.awt.Font("Rockwell Condensed", 3, 18)); // NOI18N
        radioSelesReport.setText("Seles Report");
        radioSelesReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioSelesReportMouseClicked(evt);
            }
        });

        buttonGroup2.add(radiopurchaseReport);
        radiopurchaseReport.setFont(new java.awt.Font("Rockwell Condensed", 3, 18)); // NOI18N
        radiopurchaseReport.setText("Purchase Report");
        radiopurchaseReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radiopurchaseReportMouseClicked(evt);
            }
        });

        buttonGroup2.add(radioStockReport);
        radioStockReport.setFont(new java.awt.Font("Rockwell Condensed", 3, 18)); // NOI18N
        radioStockReport.setText("Stock Report");
        radioStockReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioStockReportMouseClicked(evt);
            }
        });

        tblReportView.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane10.setViewportView(tblReportView);

        jLabel71.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel71.setText("Total Quantity");

        jLabel72.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel72.setText("Total Amount");

        TotalReportQuantity.setText("0.00");

        TotalReportAmount.setText("0.00");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TotalReportQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TotalReportAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144))
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addComponent(radioSelesReport, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                        .addComponent(radiopurchaseReport, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(179, 179, 179)
                        .addComponent(radioStockReport, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(68, 68, 68))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioStockReport, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radiopurchaseReport, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioSelesReport, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TotalReportQuantity))
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TotalReportAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ReportLayout = new javax.swing.GroupLayout(Report);
        Report.setLayout(ReportLayout);
        ReportLayout.setHorizontalGroup(
            ReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        ReportLayout.setVerticalGroup(
            ReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        menu.addTab("tab9", Report);

        pdfShow.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pdfShow.add(txtUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 150, -1));
        pdfShow.add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 150, -1));

        jLabel4.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel4.setText("quantity");
        pdfShow.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 100, -1));

        jLabel59.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel59.setText("Unit Price");
        pdfShow.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 100, 20));

        jLabel69.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel69.setText("Product Name");
        pdfShow.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 100, -1));
        pdfShow.add(txtProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 150, -1));
        pdfShow.add(txtInvoiceNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 90, -1));

        jLabel70.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel70.setText("Invoice No.");
        pdfShow.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 80, 20));

        jLabel73.setFont(new java.awt.Font("Rockwell Extra Bold", 3, 24)); // NOI18N
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText(" Gadget  Galaxy");
        pdfShow.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 230, 30));

        jLabel76.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 14)); // NOI18N
        jLabel76.setText("mirpur-10, dhaka 1200");
        pdfShow.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 200, 20));

        jLabel77.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 12)); // NOI18N
        jLabel77.setText("Tonmoy");
        pdfShow.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 480, 70, 30));

        jLabel78.setFont(new java.awt.Font("Vladimir Script", 0, 24)); // NOI18N
        jLabel78.setText("Tonmoy");
        pdfShow.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 440, 80, 30));
        pdfShow.add(txtCustomarName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, 100, -1));

        jLabel79.setText("Name");
        pdfShow.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 50, -1));

        jLabel81.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel81.setText("Discunt");
        pdfShow.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 100, -1));

        txtProductDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductDiscountActionPerformed(evt);
            }
        });
        pdfShow.add(txtProductDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 340, 150, 20));

        jLabel82.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel82.setText("Total Amunt");
        pdfShow.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 100, -1));

        txtProductTtalAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductTtalAmountActionPerformed(evt);
            }
        });
        pdfShow.add(txtProductTtalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 380, 150, -1));

        jButton1.setFont(new java.awt.Font("Rockwell", 1, 14)); // NOI18N
        jButton1.setText("Print");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        brnprintview.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        brnprintview.setText("view");
        brnprintview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                brnprintviewMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout printPanaleLayout = new javax.swing.GroupLayout(printPanale);
        printPanale.setLayout(printPanaleLayout);
        printPanaleLayout.setHorizontalGroup(
            printPanaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printPanaleLayout.createSequentialGroup()
                .addGap(308, 308, 308)
                .addComponent(jButton1)
                .addGap(95, 95, 95)
                .addComponent(brnprintview)
                .addContainerGap(497, Short.MAX_VALUE))
            .addGroup(printPanaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printPanaleLayout.createSequentialGroup()
                    .addContainerGap(181, Short.MAX_VALUE)
                    .addComponent(pdfShow, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(257, Short.MAX_VALUE)))
        );
        printPanaleLayout.setVerticalGroup(
            printPanaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printPanaleLayout.createSequentialGroup()
                .addContainerGap(556, Short.MAX_VALUE)
                .addGroup(printPanaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(brnprintview))
                .addGap(26, 26, 26))
            .addGroup(printPanaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printPanaleLayout.createSequentialGroup()
                    .addContainerGap(36, Short.MAX_VALUE)
                    .addComponent(pdfShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(57, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout printPanelLayout = new javax.swing.GroupLayout(printPanel);
        printPanel.setLayout(printPanelLayout);
        printPanelLayout.setHorizontalGroup(
            printPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(printPanale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        printPanelLayout.setVerticalGroup(
            printPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printPanelLayout.createSequentialGroup()
                .addComponent(printPanale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        menu.addTab("tab10", printPanel);

        jPanel4.add(menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -50, 1040, 640));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 1050, 590));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(0);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(1);
    }//GEN-LAST:event_btnSalesActionPerformed

    private void btnPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(2);
    }//GEN-LAST:event_btnPurchaseActionPerformed

    private void btnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(3);
    }//GEN-LAST:event_btnStockActionPerformed

    private void btnCustomarDateilsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomarDateilsActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(4);
    }//GEN-LAST:event_btnCustomarDateilsActionPerformed

    private void btnSupplierDateilsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierDateilsActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(5);
    }//GEN-LAST:event_btnSupplierDateilsActionPerformed

    private void btnEmployeeListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeListActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(6);
    }//GEN-LAST:event_btnEmployeeListActionPerformed

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(8);
    }//GEN-LAST:event_btnReportActionPerformed


    private void btnReturn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturn1ActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(7);
    }//GEN-LAST:event_btnReturn1ActionPerformed


    
    
    
   
    
    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        // TODO add your handling code here:
        homeTodaysSales();
        homeTodaysPurchase();
        totalSelesProfit();
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnLogOutMouseClicked

    private void radioStockReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioStockReportMouseClicked
        // TODO add your handling code here:

        getAllStockData();
    }//GEN-LAST:event_radioStockReportMouseClicked

    private void radiopurchaseReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radiopurchaseReportMouseClicked
        // TODO add your handling code here:
        getAllPurchaseData();
    }//GEN-LAST:event_radiopurchaseReportMouseClicked

    private void radioSelesReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioSelesReportMouseClicked
        // TODO add your handling code here:
        getAllselsToReturn();
    }//GEN-LAST:event_radioSelesReportMouseClicked

    private void btnReturnResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReturnResetMouseClicked
        // TODO add your handling code here:
        ReturnReset();
    }//GEN-LAST:event_btnReturnResetMouseClicked

    private void btnReturnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReturnUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update salestable set name=?,customarName=?,unit=?,quantity=?,grandTotal=? where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);
            //ps.setString(1, txtStockID.getText().trim());
            ps.setString(1, txtReturnProductName.getText().trim());
            ps.setString(2, txtReturnCustomarName.getText().trim());

            ps.setFloat(3, Float.parseFloat(txtReturnProductUnitPrice.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtReturnProductQuantity.getText().trim()));
            ps.setFloat(5, Float.parseFloat(txtReturnSelesAmount.getText().trim()));

            ps.setInt(6, Integer.parseInt(txtReturnProductID.getText()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Return Updated");
            getAllSelsData();
            ReturnReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Return Not Update");
        }

    }//GEN-LAST:event_btnReturnUpdateMouseClicked

    private void tblShowSelesReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShowSelesReturnMouseClicked
        // TODO add your handling code here:
        int row = tblShowSelesReturn.getSelectedRow();
        String id = tblShowSelesReturn.getModel().getValueAt(row, 0).toString();
        String customarName = tblShowSelesReturn.getModel().getValueAt(row, 1).toString();
        String name = tblShowSelesReturn.getModel().getValueAt(row, 2).toString();
        String unitprice = tblShowSelesReturn.getModel().getValueAt(row, 3).toString();
        String quantity = tblShowSelesReturn.getModel().getValueAt(row, 4).toString();
        String totalamount = tblShowSelesReturn.getModel().getValueAt(row, 5).toString();

        txtReturnProductID.setText(id);
        txtReturnCustomarName.setText(customarName);
        txtReturnProductName.setText(name);
        txtReturnProductUnitPrice.setText(unitprice);
        txtReturnProductQuantity.setText(quantity);
        txtReturnSelesAmount.setText(totalamount);

    }//GEN-LAST:event_tblShowSelesReturnMouseClicked

    private void btnEmployeeDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmployeeDeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete from employeetable where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(txtEmployeeID.getText()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Delete");
            getAllEmployeeData();
            employeeReset();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Delete");
        }
    }//GEN-LAST:event_btnEmployeeDeleteMouseClicked

    private void btnEmployeeResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmployeeResetMouseClicked
        // TODO add your handling code here:
        employeeReset();
    }//GEN-LAST:event_btnEmployeeResetMouseClicked

    private void btnEmployeeUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmployeeUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update employeetable set name=?,phonenumber=?, address=?,salary=?,date=? where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);
            //ps.setString(1, txtStockID.getText().trim());

            ps.setString(1, txtEmployeeName.getText().trim());
            ps.setString(2, txtEmployeePhoneNumber.getText().trim());
            ps.setString(3, txtEmployeeAddress.getText().trim());
            ps.setFloat(4, Float.parseFloat(txtEmployeeSalary.getText().trim()));
            ps.setDate(5, convertUtilDateToSqlDate(dateEmployeeJoin.getDate()));

            ps.setInt(6, Integer.parseInt(txtEmployeeID.getText()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Updated");
            getAllEmployeeData();
            employeeReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Update");
        }
    }//GEN-LAST:event_btnEmployeeUpdateMouseClicked

    private void btnEmployeeSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmployeeSaveMouseClicked
        // TODO add your handling code here:
        sql = "insert into employeetable(name,phonenumber,address,salary,date) values(?,?,?,?,?)";

        try {
            ps = connection.getcon().prepareStatement(sql);
            //ps.setInt(1, 1);
            ps.setString(1, txtEmployeeName.getText().toString());
            ps.setString(2, txtEmployeePhoneNumber.getText().toString());
            ps.setString(3, txtEmployeeAddress.getText().toString());
            ps.setString(4, txtEmployeeSalary.getText().toString());
            ps.setDate(5, convertUtilDateToSqlDate(dateEmployeeJoin.getDate()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Saved");

            getAllEmployeeData();
            getAllEmployeeNameToSeles();
            employeeReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Save");
        }
    }//GEN-LAST:event_btnEmployeeSaveMouseClicked

    private void tblEmployeeListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeListMouseClicked
        // TODO add your handling code here:
        int row = tblEmployeeList.getSelectedRow();
        String id = tblEmployeeList.getModel().getValueAt(row, 0).toString();
        String name = tblEmployeeList.getModel().getValueAt(row, 1).toString();
        String phonenumber = tblEmployeeList.getModel().getValueAt(row, 2).toString();
        String address = tblEmployeeList.getModel().getValueAt(row, 3).toString();
        String salary = tblEmployeeList.getModel().getValueAt(row, 4).toString();
        String date = tblEmployeeList.getModel().getValueAt(row, 5).toString();

        txtEmployeeID.setText(id);
        txtEmployeeName.setText(name);
        txtEmployeePhoneNumber.setText(phonenumber);
        txtEmployeeAddress.setText(address);
        txtEmployeeSalary.setText(salary);
        dateEmployeeJoin.setDate(convertStringToDate(date));
    }//GEN-LAST:event_tblEmployeeListMouseClicked

    private void btnSupplierResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierResetMouseClicked
        // TODO add your handling code here:
        supplierReset();
    }//GEN-LAST:event_btnSupplierResetMouseClicked

    private void btnSupplierDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierDeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete from suppliertable where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(txtSupplierID.getText()));
            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Delete");
            getAllSelsData();
            supplierReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Delete");
        }
    }//GEN-LAST:event_btnSupplierDeleteMouseClicked

    private void btnSupplierUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update suppliertable set name=?,number=?, address=?, productname=?, quantity=? where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);
            ;
            ps.setString(1, txtSupplierName.getText().trim());
            ps.setString(2, txtSupplierphoneNumber.getText().trim());
            ps.setString(3, txtSupplierAddress.getText().trim());
            ps.setString(4, ComboSupplierProductName.getSelectedItem().toString());
            ps.setFloat(5, Float.parseFloat(txtSupplierProductQuantity.getText().trim()));
            ps.setString(6, txtSupplierID.getText().trim());

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Updated");
            getAllsupplierData();
            supplierReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Update");
        }
    }//GEN-LAST:event_btnSupplierUpdateMouseClicked

    private void btnSupplierSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierSaveMouseClicked
        // TODO add your handling code here:
        sql = "insert into suppliertable(name,number,address,productname,quantity) values(?,?,?,?,?)";

        try {
            ps = connection.getcon().prepareStatement(sql);

            ps.setString(1, txtSupplierName.getText().toString());
            ps.setString(2, txtSupplierphoneNumber.getText().trim());
            ps.setString(3, txtSupplierAddress.getText().trim());
            ps.setString(4, ComboSupplierProductName.getSelectedItem().toString());
            ps.setFloat(5, Float.parseFloat(txtSupplierProductQuantity.getText().trim()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Saved");
            getAllsupplierData();
            getAllsupplierNameToPurches();
            supplierReset();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Save");
        }

    }//GEN-LAST:event_btnSupplierSaveMouseClicked

    private void tblSupplierDeteilsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierDeteilsMouseClicked
        // TODO add your handling code here:
        int row = tblSupplierDeteils.getSelectedRow();
        String id = tblSupplierDeteils.getModel().getValueAt(row, 0).toString();
        String name = tblSupplierDeteils.getModel().getValueAt(row, 1).toString();
        String number = tblSupplierDeteils.getModel().getValueAt(row, 2).toString();
        String address = tblSupplierDeteils.getModel().getValueAt(row, 3).toString();
        String productname = tblSupplierDeteils.getModel().getValueAt(row, 4).toString();
        String quantity = tblSupplierDeteils.getModel().getValueAt(row, 5).toString();

        txtSupplierID.setText(id);
        txtSupplierName.setText(name);
        txtSupplierphoneNumber.setText(number);
        txtSupplierAddress.setText(address);
        ComboSupplierProductName.setSelectedItem(productname);
        txtSupplierProductQuantity.setText(quantity);

    }//GEN-LAST:event_tblSupplierDeteilsMouseClicked

    private void btnCustomarResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomarResetMouseClicked
        // TODO add your handling code here:
        customarReset();
    }//GEN-LAST:event_btnCustomarResetMouseClicked

    private void btnCustomarUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomarUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update customar set customarname=?,phone=?, address=? where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);
            //ps.setString(1, txtStockID.getText().trim());
            ps.setString(1, txtCustomarName.getText().trim());
            ps.setString(2, txtCustomarMobileNmber.getText().trim());
            ps.setString(3, txtCustomarAddress.getText().trim());

            ps.setString(4, txtCustomarID.getText());

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Updated");
            getAllCustomarData();
            customarReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Update");
        }
    }//GEN-LAST:event_btnCustomarUpdateMouseClicked

    private void btnCustomardeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomardeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete from customar where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(txtCustomarID.getText()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Delete");
            getAllCustomarData();
            customarReset();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Delete");
        }
    }//GEN-LAST:event_btnCustomardeleteMouseClicked

    private void btnCustomarSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomarSaveMouseClicked
        // TODO add your handling code here:
        sql = "insert into customar(customarname,phone,address) values(?,?,?)";

        try {
            ps = connection.getcon().prepareStatement(sql);
            //ps.setInt(1, 1);
            ps.setString(1, txtCustomarName.getText().toString());
            ps.setString(2, txtCustomarMobileNmber.getText().toString());
            ps.setString(3, txtCustomarAddress.getText().toString());

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Saved");

            getAllCustomarData();
            getCustomarNameToSell();
            customarReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Save");
        }
    }//GEN-LAST:event_btnCustomarSaveMouseClicked

    private void tblCustomarTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustomarTableMouseClicked
        // TODO add your handling code here:
        int row = tblCustomarTable.getSelectedRow();
        String id = tblCustomarTable.getModel().getValueAt(row, 0).toString();
        String customarname = tblCustomarTable.getModel().getValueAt(row, 1).toString();
        String phone = tblCustomarTable.getModel().getValueAt(row, 2).toString();
        String address = tblCustomarTable.getModel().getValueAt(row, 3).toString();

        txtCustomarID.setText(id);
        txtCustomarName.setText(customarname);
        txtCustomarMobileNmber.setText(phone);
        txtCustomarAddress.setText(address);
    }//GEN-LAST:event_tblCustomarTableMouseClicked

    private void txtStockTotalAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockTotalAmountFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockTotalAmountFocusLost

    private void txtStockUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockUnitPriceFocusLost
        // TODO add your handling code here:
        if (txtStockUnitPrice.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(rootPane, "Quantity and Unit Price Can not be Empty");
            txtStockUnitPrice.setText(0 + "");
            txtStockUnitPrice.requestFocus();
        }
    }//GEN-LAST:event_txtStockUnitPriceFocusLost

    private void btnStockResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStockResetMouseClicked
        // TODO add your handling code here:
        stockReset();
    }//GEN-LAST:event_btnStockResetMouseClicked

    private void btnStockUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStockUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update stocktable set name=?,unitPrice=?, quantity=?, totalamount=? where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);
            //ps.setString(1, txtStockID.getText().trim());
            ps.setString(1, txtStockName.getText().trim());
            ps.setFloat(2, Float.parseFloat(txtStockUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtStockQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtStockTotalAmount.getText().trim()));
            ps.setString(5, txtStockID.getText());

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Updated");
            getAllStockData();
            stockReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Update");
        }
    }//GEN-LAST:event_btnStockUpdateMouseClicked

    private void btnStockDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStockDeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete from stocktable where id=?";

        try {
            ps = connection.getcon().prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(txtStockID.getText()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Delete");
            getAllStockData();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Delete");
        }
    }//GEN-LAST:event_btnStockDeleteMouseClicked

    private void txtStockQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockQuantityFocusLost
        // TODO add your handling code here:
        float totalPrice = getStockTotalPrice();

        txtStockTotalAmount.setText(totalPrice + "");
    }//GEN-LAST:event_txtStockQuantityFocusLost

    private void tblStockTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockTableMouseClicked
        // TODO add your handling code here:
        int row = tblStockTable.getSelectedRow();
        String id = tblStockTable.getModel().getValueAt(row, 0).toString();
        String name = tblStockTable.getModel().getValueAt(row, 1).toString();
        String unitprice = tblStockTable.getModel().getValueAt(row, 2).toString();
        String quantity = tblStockTable.getModel().getValueAt(row, 3).toString();
        String totalamount = tblStockTable.getModel().getValueAt(row, 4).toString();

        txtStockID.setText(id);
        txtStockName.setText(name);
        txtStockUnitPrice.setText(unitprice);
        txtStockQuantity.setText(quantity);
        txtStockTotalAmount.setText(totalamount);

    }//GEN-LAST:event_tblStockTableMouseClicked

    private void txtPurchaseQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseQuantityFocusLost
        // TODO add your handling code here:
        float totalPrice = getPurchaseTotalPrice();

        txtPurchaseTotalPrice.setText(totalPrice + "");
    }//GEN-LAST:event_txtPurchaseQuantityFocusLost

    private void txtPurchaseUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseUnitPriceFocusLost
        // TODO add your handling code here:
        if (txtPurchaseUnitPrice.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(rootPane, "Quantity and Unit Price Can not be Empty");
            txtPurchaseUnitPrice.setText(0 + "");
            txtPurchaseUnitPrice.requestFocus();
        }
    }//GEN-LAST:event_txtPurchaseUnitPriceFocusLost

    private void btnPurchaseResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseResetMouseClicked
        // TODO add your handling code here:
        purchaseReset();

    }//GEN-LAST:event_btnPurchaseResetMouseClicked

    private void btnPurchaseUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update purchasetable set productname=?,unitPrice=?, quantity=?, totalprice=?, purchasedate=?, supplier=? where purchaseid=?";

        try {
            ps = connection.getcon().prepareStatement(sql);
            ps.setString(1, txtPurchaseID.getText().trim());
            ps.setFloat(2, Float.parseFloat(txtPurchaseUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtPurchaseTotalPrice.getText().trim()));
            ps.setDate(5, convertUtilDateToSqlDate(txtPurchaseDate.getDate()));
            ps.setString(6, ComboSupplierName.getSelectedItem().toString());
            ps.setInt(7, Integer.parseInt(txtPurchaseID.getText()));

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Updated");
            getAllSelsData();
            purchaseReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Update");
        }
    }//GEN-LAST:event_btnPurchaseUpdateMouseClicked

    private void tblPurchaseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPurchaseTableMouseClicked
        // TODO add your handling code here:

        int row = tblPurchaseTable.getSelectedRow();
        String id = tblPurchaseTable.getModel().getValueAt(row, 0).toString();
        String name = tblPurchaseTable.getModel().getValueAt(row, 1).toString();
        String unitPrice = tblPurchaseTable.getModel().getValueAt(row, 2).toString();
        String quantity = tblPurchaseTable.getModel().getValueAt(row, 3).toString();
        String totalPrice = tblPurchaseTable.getModel().getValueAt(row, 4).toString();
        String purchasedate = tblPurchaseTable.getModel().getValueAt(row, 5).toString();
        String supplier = tblPurchaseTable.getModel().getValueAt(row, 6).toString();

        txtPurchaseID.setText(id);
        txtPurchaseProductName.setText(name);
        txtPurchaseUnitPrice.setText(unitPrice);
        txtPurchaseQuantity.setText(quantity);
        txtPurchaseTotalPrice.setText(totalPrice);
        txtPurchaseDate.setDate(convertStringToDate(purchasedate));
        ComboSupplierName.setSelectedItem(supplier);
    }//GEN-LAST:event_tblPurchaseTableMouseClicked

    private void btnPurchaseDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseDeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete from purchasetable where purchaseid=?";

        try {
            ps = connection.getcon().prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(txtPurchaseID.getText()));
            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Delete");
            getAllSelsData();
            purchaseReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Delete");
        }
    }//GEN-LAST:event_btnPurchaseDeleteMouseClicked

    private void btnPurchaseSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseSaveMouseClicked
        // TODO add your handling code here:
        sql = "insert into purchasetable(productname,unitPrice,quantity,totalprice,purchasedate,supplier) values(?,?,?,?,?,?)";

        try {
            ps = connection.getcon().prepareStatement(sql);
            //ps.setInt(1, 1);
            ps.setString(1, txtPurchaseProductName.getText().toString());
            ps.setFloat(2, Float.parseFloat(txtPurchaseUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtPurchaseQuantity.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtPurchaseTotalPrice.getText().trim()));
            ps.setDate(5, convertUtilDateToSqlDate(txtPurchaseDate.getDate()));
            ps.setString(6, ComboSupplierName.getSelectedItem().toString());

            ps.executeUpdate();
            ps.close();
            connection.getcon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Saved");

            getAllSelsData();
            getPurchaseDataToStock();
            getAllStockData();
            getAllProductName();

            purchaseReset();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, "Data Not Save");
        }
    }//GEN-LAST:event_btnPurchaseSaveMouseClicked

    private void txtSelesGrandTotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSelesGrandTotalFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSelesGrandTotalFocusLost

    private void btnSalesSavedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesSavedMouseClicked
        // TODO add your handling code here:

        DefaultTableModel model = (DefaultTableModel) tblCardTable.getModel();
        int rowcount = model.getRowCount();
        for (int i = 0; i < rowcount; i++) {
            String name = tblCardTable.getModel().getValueAt(i, 0).toString();
            float unitP = Float.parseFloat(tblCardTable.getModel().getValueAt(i, 1).toString());
            float qty = Float.parseFloat(tblCardTable.getModel().getValueAt(i, 2).toString());
            float grandTotal = Float.parseFloat(tblCardTable.getModel().getValueAt(i, 3).toString());

            float dis = Float.parseFloat(txtSelesDiscount.getText().trim());
            float actualPrice = Float.parseFloat(txtSelesActualPrice.getText().trim());
            float cashReceive = Float.parseFloat(txtSelesReceiveAmount.getText().trim());
            float cashReturn = Float.parseFloat(txtSelesReturnAmount.getText().trim());
            String cusN = comboSelesCustomarName.getSelectedItem().toString();
            String saller = txtSalesSelerName.getSelectedItem().toString();
            Date saledate = convertUtilDateToSqlDate(txtSelesDate.getDate());

            sql = "insert into salestable(name,unit,quantity,grandTotal,discount,actualPrice,cashReceive,cashReturn,customarName,selerid,date) values(?,?,?,?,?,?,?,?,?,?,?)";

            try {
                ps = connection.getcon().prepareStatement(sql);
                ps.setString(1, name);
                ps.setFloat(2, unitP);
                ps.setFloat(3, qty);
                ps.setFloat(4, grandTotal);
                ps.setFloat(5, dis);
                ps.setFloat(6, actualPrice);
                ps.setFloat(7, cashReceive);
                ps.setFloat(8, cashReturn);
                ps.setString(9, cusN);
                ps.setString(10, saller);
                ps.setDate(11, saledate);

                ps.executeUpdate();

                ps.close();
                connection.getcon().close();

                getAllProductName();
                setQunantitySales(name, qty);

                // salesTbl();
                JOptionPane.showMessageDialog(rootPane, "Product Sale");
            } catch (SQLException ex) {
                Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        getAllStockData();
        model.setRowCount(0);

        }
        float sumTolalPrices = 0.0f;

        public void getSalesGroundTotal() {
            //sumTolalPrices=Float.parseFloat(txtSelesTotalPrice.getText().trim());
            sumTolalPrices = sumTolalPrices + Float.parseFloat(txtSelesTotalPrice.getText().trim());
            txtSelesGrandTotal.setText(String.valueOf(sumTolalPrices));
    }//GEN-LAST:event_btnSalesSavedMouseClicked

    private void btnSelesAddToCardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelesAddToCardMouseClicked
        // TODO add your handling code here:

        String pName = txtSelesProductName.getModel().getSelectedItem().toString();
        float unitPrice = Float.parseFloat(txtSelesUnitPrice.getText());
        float qty = Float.parseFloat(txtSelesQuantity.getText().trim());
        float totalprice = Float.parseFloat(txtSelesTotalPrice.getText());

        List<Object> productList = new ArrayList<>();
        productList.add(new Object[]{pName, unitPrice, qty, totalprice});
        DefaultTableModel dtm = (DefaultTableModel) tblCardTable.getModel();
        for (Object i : productList) {
            dtm.addRow((Object[]) i);

            getSalesGroundTotal();
        }
        selesReset();
    }//GEN-LAST:event_btnSelesAddToCardMouseClicked

    private void btnSalesResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesResetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalesResetActionPerformed

    private void btnSalesResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesResetMouseClicked
        // TODO add your handling code here:

        selesReset();
        txtSelesGrandTotal.setText("");

    }//GEN-LAST:event_btnSalesResetMouseClicked

    private void btnSalesPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesPrintMouseClicked
        menu.setSelectedIndex(9);       
       
//        txtInvoiceNumber.setText(txtSelesID.getText().trim());
//         txtProductName.setText(txtSelesUnitPrice.getText().trim());
//         txtppbadno.setText(txtRoomNo.getText().trim());
//         txtppdocname.setText(txtbillDocname.getText().trim());
//         txtppneetbill.setText(txtneetbill.getText().trim());
//         txtppage.setText(txtbillAge.getText().trim());
//         txtppdischargdate.setText(utiltoString(dateDischargebill.getDate()));
         


    }//GEN-LAST:event_btnSalesPrintMouseClicked

    private void txtSelesTotalPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSelesTotalPriceFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_txtSelesTotalPriceFocusLost

    private void txtSelesQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSelesQuantityFocusLost
        // TODO add your handling code here:
        float totalPrice = getSalesTotalPrice();

        txtSelesTotalPrice.setText(totalPrice + "");
    }//GEN-LAST:event_txtSelesQuantityFocusLost

    private void txtSelesUnitPriceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSelesUnitPriceMouseClicked
        // TODO add your handling code here:
        sql = "SELECT (unitprice + unitprice/10) AS unitprice FROM stocktable WHERE name = ?";

        try {
            ps = connection.getcon().prepareStatement(sql);
            ps.setString(1, txtSelesProductName.getSelectedItem().toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                String unitprice = rs.getString("unitprice");
                txtSelesUnitPrice.setText(unitprice);
            }
            ps.close();
            rs.close();
            connection.getcon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtSelesUnitPriceMouseClicked

    private void txtSelesReceiveAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSelesReceiveAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSelesReceiveAmountActionPerformed

    private void txtSelesReceiveAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSelesReceiveAmountFocusLost
        // TODO add your handling code here:
        float actualPrice = getActualPrice();
        float cashReceived = Float.parseFloat(txtSelesReceiveAmount.getText().trim());
        float returnAmount = actualPrice - cashReceived;
        txtSelesReturnAmount.setText(returnAmount + "");
    }//GEN-LAST:event_txtSelesReceiveAmountFocusLost

    private void txtSelesDiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSelesDiscountFocusLost
        // TODO add your handling code here:
        float actualPrice = getActualPrice();
        txtSelesActualPrice.setText(actualPrice + "");
    }//GEN-LAST:event_txtSelesDiscountFocusLost

    private void txtProductDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductDiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductDiscountActionPerformed

    private void txtProductTtalAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductTtalAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductTtalAmountActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
         PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("print Data");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                pf.setOrientation(PageFormat.LANDSCAPE);
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(0.47, 0.47);
                printPanale.print(g2);
                return Printable.PAGE_EXISTS;
            }

        });

        boolean ok = job.printDialog();
        if (ok) {

            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void brnprintviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_brnprintviewMouseClicked
       String salesid = txtInvoiceNumber.getText();
       sql="SELECT name, unit, quantity, discount, actualPrice, customarName FROM shopmanagement.salestable where id = ?";
        try {
            ps=connection.getcon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(salesid));
            rs=ps.executeQuery();
            while (rs.next()) {                
              String proName = rs.getString("name");
              String cusName = rs.getString("customarName");
              float unit = rs.getFloat("unit");
              float qty = rs.getFloat("quantity");
              float dis = rs.getFloat("discount");
              float actp = rs.getFloat("actualPrice");
              
              txtProductName.setText(proName);
              txtCustomarName1.setText(cusName);
              txtUnitPrice.setText(unit+"");
              txtQuantity.setText(qty+"");
              txtProductDiscount.setText(dis+"");
              txtProductTtalAmount.setText(actp+"" );
              
            }
            rs.close();
            ps.close();
            connection.getcon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
               
    }//GEN-LAST:event_brnprintviewMouseClicked

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
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
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashBoard().setVisible(true);
            }
        });
    }

    //end codding 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboSupplierName;
    private javax.swing.JComboBox<String> ComboSupplierProductName;
    private javax.swing.JPanel Report;
    private javax.swing.JPanel Return;
    private javax.swing.JLabel TotalReportAmount;
    private javax.swing.JLabel TotalReportQuantity;
    private javax.swing.JButton brnprintview;
    private javax.swing.JButton btnCustomarDateils;
    private javax.swing.JButton btnCustomarReset;
    private javax.swing.JButton btnCustomarSave;
    private javax.swing.JButton btnCustomarUpdate;
    private javax.swing.JButton btnCustomardelete;
    private javax.swing.JButton btnEmployeeDelete;
    private javax.swing.JButton btnEmployeeList;
    private javax.swing.JButton btnEmployeeReset;
    private javax.swing.JButton btnEmployeeSave;
    private javax.swing.JButton btnEmployeeUpdate;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnPurchase;
    private javax.swing.JButton btnPurchaseDelete;
    private javax.swing.JButton btnPurchaseReset;
    private javax.swing.JButton btnPurchaseSave;
    private javax.swing.JButton btnPurchaseUpdate;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnReturn1;
    private javax.swing.JButton btnReturnReset;
    private javax.swing.JButton btnReturnUpdate;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnSalesPrint;
    private javax.swing.JButton btnSalesReset;
    private javax.swing.JButton btnSalesSaved;
    private javax.swing.JButton btnSelesAddToCard;
    private javax.swing.JButton btnStock;
    private javax.swing.JButton btnStockDelete;
    private javax.swing.JButton btnStockReset;
    private javax.swing.JButton btnStockUpdate;
    private javax.swing.JButton btnSupplierDateils;
    private javax.swing.JButton btnSupplierDelete;
    private javax.swing.JButton btnSupplierReset;
    private javax.swing.JButton btnSupplierSave;
    private javax.swing.JButton btnSupplierUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> comboSelesCustomarName;
    private javax.swing.JPanel customar;
    private javax.swing.JLabel date;
    private com.toedter.calendar.JDateChooser dateEmployeeJoin;
    private javax.swing.JLabel dateShow;
    private javax.swing.JPanel employee;
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblTodaysProfit;
    private javax.swing.JLabel lblTodaysPurchase;
    private javax.swing.JLabel lblTodaysSale;
    private javax.swing.JLabel lblTotalProfit;
    private javax.swing.JLabel lblTotalPurchase;
    private javax.swing.JLabel lblTotalSele;
    private javax.swing.JTabbedPane menu;
    private javax.swing.JPanel pdfShow;
    private javax.swing.JPanel printPanale;
    private javax.swing.JPanel printPanel;
    private javax.swing.JPanel purchase;
    private javax.swing.JRadioButton radioSelesReport;
    private javax.swing.JRadioButton radioStockReport;
    private javax.swing.JRadioButton radiopurchaseReport;
    private javax.swing.JPanel sales;
    private javax.swing.JPanel stock;
    private javax.swing.JTable tblCardTable;
    private javax.swing.JTable tblCustomarTable;
    private javax.swing.JTable tblEmployeeList;
    private javax.swing.JTable tblPurchaseTable;
    private javax.swing.JTable tblReportView;
    private javax.swing.JTable tblShowSelesReturn;
    private javax.swing.JTable tblStockTable;
    private javax.swing.JTable tblSupplierDeteils;
    private javax.swing.JLabel timeShow;
    private javax.swing.JTextArea txtCustomarAddress;
    private javax.swing.JTextField txtCustomarID;
    private javax.swing.JTextField txtCustomarMobileNmber;
    private javax.swing.JTextField txtCustomarName;
    private javax.swing.JTextField txtCustomarName1;
    private javax.swing.JTextArea txtEmployeeAddress;
    private javax.swing.JTextField txtEmployeeID;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtEmployeePhoneNumber;
    private javax.swing.JTextField txtEmployeeSalary;
    private javax.swing.JTextField txtInvoiceNumber;
    private javax.swing.JTextField txtProductDiscount;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductTtalAmount;
    private com.toedter.calendar.JDateChooser txtPurchaseDate;
    private javax.swing.JTextField txtPurchaseID;
    private javax.swing.JTextField txtPurchaseProductName;
    private javax.swing.JTextField txtPurchaseQuantity;
    private javax.swing.JTextField txtPurchaseTotalPrice;
    private javax.swing.JTextField txtPurchaseUnitPrice;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtReturnCustomarName;
    private javax.swing.JTextField txtReturnProductID;
    private javax.swing.JTextField txtReturnProductName;
    private javax.swing.JTextField txtReturnProductQuantity;
    private javax.swing.JTextField txtReturnProductUnitPrice;
    private javax.swing.JTextField txtReturnSelesAmount;
    private javax.swing.JComboBox<String> txtSalesSelerName;
    private javax.swing.JTextField txtSelesActualPrice;
    private com.toedter.calendar.JDateChooser txtSelesDate;
    private javax.swing.JTextField txtSelesDiscount;
    private javax.swing.JTextField txtSelesGrandTotal;
    private javax.swing.JTextField txtSelesID;
    private javax.swing.JComboBox<String> txtSelesProductName;
    private javax.swing.JTextField txtSelesQuantity;
    private javax.swing.JTextField txtSelesReceiveAmount;
    private javax.swing.JTextField txtSelesReturnAmount;
    private javax.swing.JTextField txtSelesTotalPrice;
    private javax.swing.JTextField txtSelesUnitPrice;
    private javax.swing.JTextField txtStockID;
    private javax.swing.JTextField txtStockName;
    private javax.swing.JTextField txtStockQuantity;
    private javax.swing.JTextField txtStockTotalAmount;
    private javax.swing.JTextField txtStockUnitPrice;
    private javax.swing.JTextArea txtSupplierAddress;
    private javax.swing.JTextField txtSupplierID;
    private javax.swing.JTextField txtSupplierName;
    private javax.swing.JTextField txtSupplierProductQuantity;
    private javax.swing.JTextField txtSupplierphoneNumber;
    private javax.swing.JTextField txtUnitPrice;
    private javax.swing.JPanel vendor;
    // End of variables declaration//GEN-END:variables

}
