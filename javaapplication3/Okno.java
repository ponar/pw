/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import model.Mmser;
import model.Upwmagwg;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Query;
import org.hibernate.Session;
import utils.HibernateUtil;

/**
 *
 * @author admin
 */
public class Okno extends javax.swing.JFrame {

    private String nr_pw = null;
    private String zl = null;

    //parametry polaczenia

    public Connection polaczenie() throws ClassNotFoundException {
        Connection con = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.10.16:1433/MAX102PLpo00", "maxmast", "max859");
        } catch (SQLException e1) {
        }
        return con;
    }

    public int zapytanie() {
        int parsedInt = 0;
        int nowy = 0;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //Session session = HibernateUtil.openSession();
        session.beginTransaction();
        Query query = session.createQuery("select nd.mmser_serlast from Mmser nd where nd.mmser_sergroup like :numer ");
        String nr = "_PW";
        List<Mmser> wynik = query.setString("numer", "%" + nr + "%").list();
        for (Iterator it = wynik.iterator(); it.hasNext();) {
            String next = (String) it.next();
            String po_zmianie = next.substring(2, next.length());
            parsedInt = Integer.parseInt(po_zmianie.trim());
            nowy = parsedInt + 1;
            System.out.println(nowy);

        }
        // 
        Object object = session.load(Mmser.class, "_PW");
        Mmser n1 = (Mmser) object;
        n1.setMmser_serlast("PW" + Integer.toString(nowy));
        //System.out.println("Nowy numer: " + "PW" + Integer.toString(nowy));
        String nowy_pw = "PW" + Integer.toString(nowy).trim();
        session.update(n1);
        //
        nr_pw = nowy_pw;
        session.getTransaction().commit();
        //session.close();
        return nowy;
    }

    private void zapis_upwmagwg(String zlecenie, String user, String nr_pw, String now) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String data = formatter.format(new Date());

        Date dzisiaj = null;
        try {
            dzisiaj = formatter.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Upwmagwg upwmagwg = new Upwmagwg(zlecenie, user, nr_pw, dzisiaj);
        Upwmagwg upwmagwg = new Upwmagwg();
        //
        System.out.println("Dane do zapisu data: " + data + " Nr pw: " + nr_pw + " user: " + user + " zlecenie: " + zlecenie);
        upwmagwg.setUpwmagwg_data(dzisiaj);
        upwmagwg.setUpwmagwg_nrpw(nr_pw);
        upwmagwg.setUpwmagwg_user(user);
        upwmagwg.setUpwmagwg_zlecenie(zlecenie);
        //
        session.save(upwmagwg);
        session.getTransaction().commit();

    }

    private void pokaz() throws JRException, ClassNotFoundException, SQLException {
        System.out.println("Podglad raportu o numerze: " + jTextField1.getText());
        //generowanie raportu
        //String fileName = "C:\report1.jasper";
        HashMap hm = new HashMap();//ZGP0056144
        zl = jTextField1.getText().trim();//ZGP0056144
        hm.put("zlecenie", zl);
        Connection con1 = polaczenie();
        //zapis do bazy Upwmagwg
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String data_format = formatter.format(now);
         //?
        //String zl = jTextField1.getText().trim();
        System.out.println("Nowy Numer1: " + nr_pw);
        String user = "Józef Hojdys";
        zapis_upwmagwg(zl, user, nr_pw, data_format);
        //
        JasperReport jasperDesign = JasperCompileManager.compileReport(raport);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, hm, con1);
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);
        con1.close();
        
    }

    /**
     * Creates new form Okno
     */
    public Okno() {
        initComponents();
        //int numerator = zapytanie();
        //jTextField2.setText("PW" + numerator);
        jTextField1.getDocument().addDocumentListener(new MyDocumentListener());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Drukowanie PW z MAX'a");

        jLabel1.setText("Wpisz zlecenie:");

        jButton1.setText("Pokaż");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Aktualny nr PW:");

        jButton2.setText("Zamknij");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Program do drukowania PW");

        jButton3.setText("czysc pole zlecenie");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jButton3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton1)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(66, 66, 66))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            //String pobrano = jTextField1.getText();
            //System.out.println("Pobrano wartosc: " + pobrano);
            pokaz();
        } catch (Exception e) {
            System.out.println("Bład");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // czysci pole tekstowe jtextField1
        jTextField1.setText("");
    }//GEN-LAST:event_jButton3ActionPerformed
    //test 

    class MyDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            updateLog(e, "inserted into");
        }

        public void removeUpdate(DocumentEvent e) {
            updateLog(e, "removed from");
        }

        public void changedUpdate(DocumentEvent e) {
            //Plain text components do not fire these events
        }

        public void updateLog(DocumentEvent e, String action) {
            Document doc = (Document) e.getDocument();
            int changeLength = e.getLength();
            //String zle = "Z";
            //String pk = "P";
            //
            if(Pattern.compile("[A-Z]").matcher(jTextField1.getText()).find()){
                System.out.println("PASUJE");
                jTextField2.setText("PW" + zapytanie());
            }
            /*
            if (jTextField1.getText().equals(zle) || jTextField1.getText().equals(pk)) {
                System.out.println("zlecenie wpisane");
                jTextField2.setText("PW" + zapytanie());
            }
            */

        }
    }

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
            java.util.logging.Logger.getLogger(Okno.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Okno.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Okno.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Okno.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Okno().setVisible(true);
            }
        });
    }
    private String raport = "C:\\raporty\\report1.jrxml"; //moj raport
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
