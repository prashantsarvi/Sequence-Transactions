package com.company;

import java.sql.*;

class Transaction {
    String localUrl = "jdbc:mysql://localhost:3306/userinfo";
    String remoteUrl = "jdbc:mysql://35.224.24.44:3306/purchaseInfo";
    String localUser = "root";
    String localPassword = "007Helo@";
    String remoteUser = "root";
    String remotePassword = "007Helo@";

    Connection localDB = null; // local connection
    Connection remoteDB = null; // remote connection

    public void connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.localDB = DriverManager.getConnection(localUrl, localUser, localPassword);
            this.remoteDB = DriverManager.getConnection(remoteUrl, remoteUser, remotePassword);
            this.localDB.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            this.localDB.close();
            this.remoteDB.close();
        }
    }
    public void sequence1() throws SQLException {
        try {
            if (this.localDB != null) {
                this.localDB.setAutoCommit(false);
                String query = "select * from olist_customers_dataset where customer_zip_code_prefix = 01151";
                Statement stmnt = this.localDB.createStatement();
                stmnt.executeQuery(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.localDB.close();
            this.remoteDB.close();
        }
    }
    public void sequence2(String cityName) throws SQLException {
        try {
            String query = "UPDATE olist_customers_dataset SET customer_city = '"+cityName+"' where customer_zip_code_prefix = 01151";
            Statement stmnt = this.localDB.createStatement();
            stmnt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            this.localDB.close();
            this.remoteDB.close();
        }
    }
    public void commit() throws SQLException {
        try {
            this.localDB.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.localDB.close();
            this.remoteDB.close();
        }
    }


}

class TransactionsData5408 {

    public static void main(String[] args) throws SQLException {
        Transaction T1 = new Transaction();
        Transaction T2 = new Transaction();

        T1.connect();
        T2.connect();

        // sequence 1
        T1.sequence1();
        T2.sequence1();

        // sequence2
        T1.sequence2("T1 CITY");
        T2.sequence2("T2 CITY");

        // sequence3
        T2.commit();

        // sequence4
        T1.commit();
    }
}
