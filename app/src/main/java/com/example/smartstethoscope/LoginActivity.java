package com.example.smartstethoscope;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smartstethoscope.RegisterActivity.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstethoscope.Database.ConnectionClass;
import com.example.smartstethoscope.Model.Admin;
import com.example.smartstethoscope.Model.DatabaseAccess;
import com.example.smartstethoscope.Model.Users;
import com.example.smartstethoscope.Prevalent.AdminPrevalent;
import com.example.smartstethoscope.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper db;
    private Button LoginButton;
    private EditText Phone;
    private EditText Password;
    private ProgressDialog loadingBar;
    private CheckBox checkBoxRememberMe;
    private TextView AdminLink, NotAdminLink;
    DatabaseAccess dbAccess;
    ConnectionClass connectionClass;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton = findViewById(R.id.login_btn);
        Phone = findViewById(R.id.login_phone_number_input);
        Password = findViewById(R.id.login_password_input);
        checkBoxRememberMe = findViewById(R.id.remember_me_check);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink= findViewById(R.id.not_admin_panel_link);
        connectionClass = new ConnectionClass();
        Paper.init(this);
        loadingBar = new ProgressDialog(this);
        db = new DatabaseHelper(this);
        context = this;
        dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.open();
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);

            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AdminLink.getVisibility()== View.INVISIBLE && NotAdminLink.getVisibility() == View.VISIBLE)
                {

                    LoginAdmin();


                }
                else if(AdminLink.getVisibility()== View.VISIBLE && NotAdminLink.getVisibility() == View.INVISIBLE){
                    LoginUser();

                }

            }
        });


    }
    private void LoginUser(){
        String doctorNo = Phone.getText().toString().trim();
        String password = Password.getText().toString().trim();
        Users user = new Users();
        user.setPhone(doctorNo);
        user.setPassword(password);

        if(checkBoxRememberMe.isChecked()){

            Paper.book().write(Prevalent.UserPhoneKey,user.getPhone());
            Paper.book().write(Prevalent.UserPassword,user.getPassword());
        }

        if(TextUtils.isEmpty(user.getPassword())){

            Toast.makeText(this, "Please Write Your Password...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(user.getPhone())){

            Toast.makeText(this, "Please Write Your Phone Number...", Toast.LENGTH_SHORT).show();
        }
        else{
            Boolean CheckPhoneAndPasswordUser = CheckPhoneAndPasswordUser(user.getPhone(),user.getPassword());
            Boolean CheckPhone = CheckDoctorNo(user.getPhone());
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            if(CheckPhone == true){

                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Account does not exists", Toast.LENGTH_SHORT).show();
            }
            else {
                if(CheckPhoneAndPasswordUser == true){
                    Toast.makeText(LoginActivity.this, "Successfully Logged In...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this,DenemeMysql.class);
                    startActivity(intent);
                    String empty = "";
                    Phone.getText().clear();
                    Password.getText().clear();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        }
    }
    private void LoginAdmin(){
        String phone = Phone.getText().toString().trim();
        String password = Password.getText().toString().trim();
        Admin admin = new Admin();
        admin.setPhone(phone);
        admin.setPassword(password);

        if(checkBoxRememberMe.isChecked()){

            Paper.book().write(AdminPrevalent.AdminPhoneKey,admin.getPhone());
            Paper.book().write(AdminPrevalent.AdminPassword,admin.getPassword());
        }

        if(TextUtils.isEmpty(admin.getPassword())){

            Toast.makeText(this, "Please Write Your Password...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(admin.getPhone())){

            Toast.makeText(this, "Please Write Your Phone Number...", Toast.LENGTH_SHORT).show();
        }
        else{
            Boolean CheckPhonePassForAdmin = CheckPhoneAndPasswordAdmin(admin.getPhone(),admin.getPassword());
            Boolean CheckPhoneForAdmin = CheckDoctorNoForAdmin(admin.getPhone());
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            if(CheckPhoneForAdmin == true){

                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Account does not exists", Toast.LENGTH_SHORT).show();
            }
            else {
                if(CheckPhonePassForAdmin == true){
                    Toast.makeText(LoginActivity.this, "Welcome Admin,Successfully Logged In...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this,Records.class);
                    startActivity(intent);
                    String empty = "";
                    Phone.getText().clear();
                    Password.getText().clear();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
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
