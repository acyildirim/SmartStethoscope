package com.example.smartstethoscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartstethoscope.Database.ConnectionClass;
import com.example.smartstethoscope.Model.DatabaseAccess;
import com.example.smartstethoscope.Model.Deneme;
import com.example.smartstethoscope.Model.Records;
import com.mysql.jdbc.PreparedStatement;

import com.mysql.jdbc.Blob;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class DenemeMysql extends AppCompatActivity {
    GridView gridView;
    DenemeAdapter adapter;
    ArrayList<Deneme> list;
    private Button LogoutBtn;
    ConnectionClass connectionClass;
    private static final int BUFFER_SIZE = 4096;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deneme_mysql);
        LogoutBtn = findViewById(R.id.logout_button_home);
        connectionClass = new ConnectionClass();
        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<Deneme>();
        adapter = new DenemeAdapter(this, R.layout.record_items, list);
        gridView.setAdapter(adapter);

        try {
            list = getDenemeData();
            adapter.notifyDataSetChanged();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }



        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(DenemeMysql.this, MainActivity.class);
                startActivity(intent);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtID = (TextView) view.findViewById(R.id.txtItemID);
                String  strID = txtID.getText().toString();
                Intent intent = new Intent(DenemeMysql.this,ItemDetailUser.class);
                intent.putExtra("RegID",strID);
                startActivity(intent);
            }
        });

    }

    private ArrayList<Deneme> getDenemeData() throws SQLException, IOException {
        list.clear();
        Connection con = connectionClass.CONN();
        if (con == null) {

        }
        else {
            Statement stmt = con.createStatement();


            String sql = "Select * From Deneme";
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String fileName = "C:\\Users\\yldrm\\Desktop\\Projects\\SmartStethoscope\\app\\src\\main\\res\\raw\\"+resultSet.getString(3)+".waw";
                int ID = resultSet.getInt(1);
                String Diagnose = resultSet.getString(2);
                String fName = resultSet.getString(3);
                String ImageName = resultSet.getString(4);
                Blob blobImage = (Blob) resultSet.getBlob(5);
                Blob blobAudio = (Blob) resultSet.getBlob(6);
                int len = (int) blobAudio.length();
                byte[] byteImage = blobImage.getBytes(1, (int) blobImage.length());
                byte[] byteAudio = blobAudio.getBytes(1, (int) blobAudio.length());
                list.add(new Deneme(ID, Diagnose, fName, ImageName, byteImage, byteAudio));
            }

        }
        return list;
    }
}