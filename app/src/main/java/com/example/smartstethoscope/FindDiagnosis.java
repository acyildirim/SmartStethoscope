package com.example.smartstethoscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstethoscope.Database.ConnectionClass;
import com.example.smartstethoscope.Model.DatabaseAccess;
import com.mysql.jdbc.PreparedStatement;

import org.w3c.dom.Text;

import java.sql.Connection;

public class FindDiagnosis extends AppCompatActivity {
    TextView txt,txtGetLabel;
    Button btn1,btn2,btn3,btn4,btnUpdate;
    DatabaseAccess dbAccess;
    ConnectionClass connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_diagnosis);

        final String Id = getIntent().getStringExtra("Reg2ID");
        final int IdRecord = Integer.parseInt(Id);

        dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.open();
        connectionClass = new ConnectionClass();
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btnUpdate = findViewById(R.id.btnUpdate);
        txtGetLabel = findViewById(R.id.txtLabel);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String buttonText = btn1.getText().toString();
                    Uptade(IdRecord,buttonText);
                    Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindDiagnosis.this,DenemeMysql.class);
                    startActivity(intent);
                }

                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String buttonText = btn2.getText().toString();
                    Uptade(IdRecord,buttonText);
                    Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindDiagnosis.this,DenemeMysql.class);
                    startActivity(intent);

                }

                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String buttonText = btn3.getText().toString();
                    Uptade(IdRecord,buttonText);
                    Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindDiagnosis.this,DenemeMysql.class);
                    startActivity(intent);

                }

                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String buttonText = btn4.getText().toString();
                    Uptade(IdRecord,buttonText);
                    Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindDiagnosis.this,DenemeMysql.class);
                    startActivity(intent);

                }

                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String buttonText = txtGetLabel.getText().toString();
                    Uptade(IdRecord,buttonText);
                    Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindDiagnosis.this,DenemeMysql.class);
                    startActivity(intent);
                }

                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
            }
        });
    }
    private void Uptade(int Id, String index){
        String sqlUpdate = "UPDATE Deneme "
                + "SET Diagnose = ? "
                + "WHERE id = ?";
        try {
            Connection con = connectionClass.CONN();
            if (con == null){
                System.out.println("Cannot connect to database");
            }
            else {
                PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sqlUpdate);
                // prepare data for update
                pstmt.setString(1, index);
                pstmt.setInt(2, Id);
                int rowAffected = pstmt.executeUpdate();
                System.out.println(String.format("Row affected %d", rowAffected));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
