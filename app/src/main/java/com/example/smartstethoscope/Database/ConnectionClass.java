package com.example.smartstethoscope.Database;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by ABHI on 9/20/2016.
 */
public class ConnectionClass {
    String classs = "com.mysql.jdbc.Driver";

    String url = "PUT YOUR MYSQL URL";
    String un = "PUT USER NAME";
    String password = "PUT DATABASE PASSWORD";


    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);

            conn = DriverManager.getConnection(url, un, password);


            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
    public boolean addUser(String name, String password, String doctorNo){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        boolean isSuccess=false;
        String z="";
        if(name.trim().equals("")|| password.trim().equals("") ||doctorNo.trim().equals(""))
            z = "Please enter all fields....";
        else
        {
            try {
                Class.forName(classs);

                conn = DriverManager.getConnection(url, un, password);
                if (conn == null) {
                    z = "Please check your internet connection";
                } else {

                    String query="insert into Users (name, password, doctorNo) values('"+name+"','"+password+"','"+doctorNo+"')";

                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(query);

                    z = "Register successfull";
                    isSuccess=true;


                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = "Exceptions"+ex;
            }
        }
        return isSuccess;
    }
}
