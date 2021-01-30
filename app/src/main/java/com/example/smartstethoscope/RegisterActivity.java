package com.example.smartstethoscope;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartstethoscope.Database.ConnectionClass;
import com.example.smartstethoscope.Model.Users;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper db;
    private Button CreateAccountButton;
    private EditText InputName, InputPhone, InputPassword;
    private ProgressDialog loadingBar;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        connectionClass = new ConnectionClass();
        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.username_input);
        InputPhone = findViewById(R.id.register_phone_number_input);
        InputPassword = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);
        db = new DatabaseHelper(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccountNew();
            }
        });

    }

    private void CreateAccountNew() {
        String name = InputName.getText().toString();
        String doctorNo = InputPhone.getText().toString();
        String Password = InputPassword.getText().toString();
        boolean isSuccess = false;
        Users user = new Users();
        user.setName(name);
        user.setPassword(Password);
        user.setPhone(doctorNo);
        if (TextUtils.isEmpty(user.getName())) {

            Toast.makeText(this, "Please Write Your Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(user.getPhone())) {

            Toast.makeText(this, "Please Write Your Phone Number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(user.getPassword())) {

            Toast.makeText(this, "Please Write Your Password...", Toast.LENGTH_SHORT).show();
        } else {
            Boolean CheckDoctorNo = CheckDoctorNo(user.getPhone());
            Boolean CheckAdminDoctorNo = CheckDoctorNoForAdmin(user.getPhone());

            if (CheckDoctorNo == true && CheckAdminDoctorNo == true) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                    } else {

                        String query = "insert into Users (name, password, doctorNo) values('" + user.getName() + "','" + user.getPassword() + "','" + user.getPhone() + "')";

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        loadingBar.setTitle("Create Account");
                        loadingBar.setMessage("Please wait, while we are checking the credentials");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        isSuccess = true;
                        InputPhone.getText().clear();
                        InputPassword.getText().clear();
                        InputName.getText().clear();
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Something Wrong");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                }
            }
            else {
                loadingBar.dismiss();
                Toast.makeText(RegisterActivity.this, "Phone Already Exist...", Toast.LENGTH_SHORT).show();
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
    /*private void CreateAccount() {
        DatabaseAccess dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.open();
        String name = InputName.getText().toString();
        String Phone = InputPhone.getText().toString();
        String Password = InputPassword.getText().toString();
        //Admin admin = new Admin();
        //admin.setName(name);
        //admin.setPassword(Password);
        //admin.setPhone(Phone);
        Users user = new Users();
        user.setName(name);
        user.setPassword(Password);
        user.setPhone(Phone);

        if (TextUtils.isEmpty(user.getName())) {

            Toast.makeText(this, "Please Write Your Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(user.getPhone())) {

            Toast.makeText(this, "Please Write Your Phone Number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(user.getPassword())) {

            Toast.makeText(this, "Please Write Your Password...", Toast.LENGTH_SHORT).show();
        } else {

            Boolean CheckPhone = dbAccess.CheckPhone(user.getPhone());
            Boolean CheckPhoneForAdmin = dbAccess.CheckPhoneForAdmin(user.getPhone());
            if (CheckPhone == true && CheckPhoneForAdmin == true) {
                long val = dbAccess.addUser(user.getName(), user.getPassword(), user.getPhone());
                if (val > 0) {

                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please wait, while we are checking the credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    InputPhone.getText().clear();
                    InputPassword.getText().clear();
                    InputName.getText().clear();
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Network Error:Please Try Again", Toast.LENGTH_SHORT).show();
                }

            } else {
                loadingBar.dismiss();
                Toast.makeText(RegisterActivity.this, "Phone Already Exist...", Toast.LENGTH_SHORT).show();
            }
        }

    }*/
}
