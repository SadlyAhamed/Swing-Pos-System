/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DatabageConnection;

/**
 *
 * @author UseR
 */
public class PdfFormate extends javax.swing.JFrame {
    
 

    /**
     * Creates new form PdfFormate
     */
    public PdfFormate() {
        initComponents();
    }
    
    PreparedStatement ps;
    ResultSet rs;
    String sql="";
    DatabageConnection con=new DatabageConnection();
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pdfShow = new javax.swing.JPanel();
        txtUnitPrice = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        txtLineTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        txtInvoiceNumber = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCustomarName = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCustomarAddress = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtProductDiscount = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtProductTtalAmount = new javax.swing.JTextField();
        btnPrint = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pdfShow.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pdfShow.add(txtUnitPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 150, -1));
        pdfShow.add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 150, -1));

        txtLineTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLineTotalActionPerformed(evt);
            }
        });
        pdfShow.add(txtLineTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 340, 150, -1));

        jLabel5.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel5.setText("Line Total");
        pdfShow.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 100, -1));

        jLabel4.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel4.setText("quantity");
        pdfShow.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 100, -1));

        jLabel3.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel3.setText("Unit Price");
        pdfShow.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 100, 20));

        jLabel2.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel2.setText("Product Name");
        pdfShow.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 100, -1));
        pdfShow.add(txtProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, 150, -1));
        pdfShow.add(txtInvoiceNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 90, -1));

        jLabel1.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel1.setText("Invoice No.");
        pdfShow.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 80, 20));

        jLabel6.setFont(new java.awt.Font("Rockwell Extra Bold", 3, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(" Gadget  Galaxy");
        pdfShow.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 230, 30));

        jLabel7.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 14)); // NOI18N
        jLabel7.setText("mirpur-10, dhaka 1200");
        pdfShow.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 200, 20));

        jLabel8.setFont(new java.awt.Font("Rockwell Extra Bold", 0, 12)); // NOI18N
        jLabel8.setText("Tonmoy");
        pdfShow.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 480, 70, 30));

        jLabel9.setFont(new java.awt.Font("Vladimir Script", 0, 24)); // NOI18N
        jLabel9.setText("Tonmoy");
        pdfShow.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 440, 80, 30));
        pdfShow.add(txtCustomarName, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, 100, -1));

        jLabel10.setText("Name");
        pdfShow.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 50, -1));

        jLabel11.setText("Address");
        pdfShow.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 170, 60, -1));
        pdfShow.add(txtCustomarAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 170, 100, -1));

        jLabel12.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel12.setText("Discunt");
        pdfShow.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 100, -1));

        txtProductDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductDiscountActionPerformed(evt);
            }
        });
        pdfShow.add(txtProductDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 370, 150, 20));

        jLabel13.setFont(new java.awt.Font("Rockwell Condensed", 1, 14)); // NOI18N
        jLabel13.setText("Total Amunt");
        pdfShow.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 100, -1));

        txtProductTtalAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductTtalAmountActionPerformed(evt);
            }
        });
        pdfShow.add(txtProductTtalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 400, 150, -1));

        jPanel1.add(pdfShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 520, 530));

        btnPrint.setText("print");
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrintMouseClicked(evt);
            }
        });
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jPanel1.add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 590, -1, -1));

        jButton1.setText("show");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 590, 80, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 930, 690));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
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
                g2.scale(0.60, 0.60);
                pdfShow.print(g2);
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
        this.dispose();

    }//GEN-LAST:event_btnPrintActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        sql = "select * from salestable";

        try {
            ps = con.getcon().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String unit = rs.getString("unit");
                String quantity = rs.getString("quantity");
                String totalAmount = rs.getString("totalPrice");
                
                txtInvoiceNumber.setText(id);
                txtProductName.setText(name);
                txtUnitPrice.setText(unit);
                txtQuantity.setText(quantity);
                txtLineTotal.setText(totalAmount);

               

            }
            ps.close();
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtLineTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLineTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLineTotalActionPerformed

    private void txtProductDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductDiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductDiscountActionPerformed

    private void txtProductTtalAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductTtalAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductTtalAmountActionPerformed

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        // TODO add your handling code here:
      
    }//GEN-LAST:event_btnPrintMouseClicked

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
            java.util.logging.Logger.getLogger(PdfFormate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PdfFormate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PdfFormate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PdfFormate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PdfFormate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pdfShow;
    private javax.swing.JTextField txtCustomarAddress;
    private javax.swing.JTextField txtCustomarName;
    private javax.swing.JTextField txtInvoiceNumber;
    private javax.swing.JTextField txtLineTotal;
    private javax.swing.JTextField txtProductDiscount;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductTtalAmount;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtUnitPrice;
    // End of variables declaration//GEN-END:variables
}
