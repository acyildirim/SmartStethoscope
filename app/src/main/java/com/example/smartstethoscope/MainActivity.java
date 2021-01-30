package com.example.smartstethoscope;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smartstethoscope.Database.ConnectionClass;
import com.example.smartstethoscope.Model.Admin;
import com.example.smartstethoscope.Model.DatabaseAccess;
import com.example.smartstethoscope.Model.Users;
import com.example.smartstethoscope.Prevalent.AdminPrevalent;
import com.example.smartstethoscope.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    //DatabaseHelper db;
    DatabaseAccess db;
    ConnectionClass connectionClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinNowButton = findViewById(R.id.main_join_now_btn);
        loginButton = findViewById(R.id.main_login_btn);
        //db = new DatabaseHelper(this);
        loadingBar = new ProgressDialog(this);
        db = DatabaseAccess.getInstance(getApplicationContext());
        db.open();
        connectionClass = new ConnectionClass();
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");

                myRef.setValue("Hello, World!");
            }

        });
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPassword = Paper.book().read(Prevalent.UserPassword);
        String AdminPhoneKey = Paper.book().read(AdminPrevalent.AdminPhoneKey);
        String AdminPassword = Paper.book().read(AdminPrevalent.AdminPassword);

        /*if(UserPhoneKey != "" && UserPassword != "" && AdminPhoneKey != "" && AdminPassword != ""){
            if (!TextUtils.isEmpty(UserPhoneKey)&& !TextUtils.isEmpty(UserPassword))
            {
                AllowAccess(UserPhoneKey,UserPassword);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait, while we are checking the credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
            else if (!TextUtils.isEmpty(AdminPhoneKey)&& !TextUtils.isEmpty(AdminPassword))
            {
                AllowAccessForAdmin(AdminPhoneKey,AdminPassword);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait, while we are checking the credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }*/
    }

    private void AllowAccess(final String phone, final String password) {

        Users user = new Users();
        user.setPhone(phone);
        user.setPassword(password);

        Boolean CheckDoctorNo = CheckDoctorNo(user.getPhone());
        Boolean CheckPhoneAndPasswordUser = CheckPhoneAndPasswordUser(user.getPhone(),user.getPassword());

        if (CheckDoctorNo == true) {

            loadingBar.dismiss();
        } else {
            if (CheckPhoneAndPasswordUser == true) {
                Toast.makeText(MainActivity.this, "Already Logged In...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                Intent intent = new Intent(MainActivity.this, RecordsForDoctor.class);
                startActivity(intent);
                String empty = "";
            } else {
                Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void AllowAccessForAdmin(final String phone, final String password) {

        Admin admin = new Admin();
        admin.setPhone(phone);
        admin.setPassword(password);

        Boolean CheckPhoneAndPasswordAdmin = CheckPhoneAndPasswordAdmin(admin.getPhone(),admin.getPassword());
        Boolean CheckDoctorNoForAdmin = CheckDoctorNoForAdmin(admin.getPhone());

        if (CheckDoctorNoForAdmin == true) {

            loadingBar.dismiss();
            Toast.makeText(MainActivity.this, "Account does not exists", Toast.LENGTH_SHORT).show();
        } else {
            if (CheckPhoneAndPasswordAdmin == true) {
                Toast.makeText(MainActivity.this, "Already Logged In...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                Intent intent = new Intent(MainActivity.this, Records.class);
                startActivity(intent);
                String empty = "";
            } else {
                Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private  boolean CheckDoctorNo(String doctorNo){
        boolean isSuccess = false;
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
            } else {
                Statement stmt = con.createStatement();

                String query = "SELECT * from Users WHERE doctorNo = '" + doctorNo + "'";
                ResultSet resultSet = stmt.executeQuery(query);

                if (resultSet.next()) {
                    isSuccess = false;

                } else {
                    isSuccess = true;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        return  isSuccess;
    }
    private boolean CheckDoctorNoForAdmin(String doctorNo){
        boolean isSuccess = false;
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
            } else {
                Statement stmt = con.createStatement();

                String query = "SELECT * from Admin WHERE doctorNo = '" + doctorNo + "'";
                ResultSet resultSet = stmt.executeQuery(query);

                if (resultSet.next()) {
                    isSuccess = false;

                } else {
                    isSuccess = true;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        return  isSuccess;
    }
    public boolean CheckPhoneAndPasswordUser(String doctorNo, String password){
        boolean isSuccess = false;
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
            } else {
                Statement stmt = con.createStatement();
                String query = "SELECT * from Users WHERE doctorNo = '" + doctorNo + "'" + " "+ "and" + " " + "password = '" + password + "'";
                ResultSet resultSet = stmt.executeQuery(query);

                if (resultSet.next()) {
                    isSuccess = true;

                } else {
                    isSuccess = false;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        return  isSuccess;
    }
    public boolean CheckPhoneAndPasswordAdmin(String doctorNo, String password){
        boolean isSuccess = false;
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
            } else {
                Statement stmt = con.createStatement();
                String query = "SELECT * from Admin WHERE doctorNo = '" + doctorNo + "'" + " "+ "and" + " " + "password = '" + password + "'";
                ResultSet resultSet = stmt.executeQuery(query);

                if (resultSet.next()) {
                    isSuccess = true;

                } else {
                    isSuccess = false;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
        return  isSuccess;
    }

}
