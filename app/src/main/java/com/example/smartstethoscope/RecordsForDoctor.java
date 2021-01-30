package com.example.smartstethoscope;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartstethoscope.Model.DatabaseAccess;
import com.example.smartstethoscope.Model.Records;

import java.util.ArrayList;

import io.paperdb.Paper;

public class RecordsForDoctor extends AppCompatActivity {
    GridView gridView;
    RecordListAdapter adapter;
    DatabaseAccess dbAccess;
    ArrayList<Records> list;
    ImageView imageViewRecord;
    private Button LogoutBtn;
    ImageView imageViewItem;
    TextView txtLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_for_doctor);

        dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.open();
        LogoutBtn = findViewById(R.id.logout_button_home);


        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();

        adapter = new RecordListAdapter(this, R.layout.record_items, list);
        gridView.setAdapter(adapter);

        Cursor cursor = dbAccess.getData("SELECT * FROM Records");
        list.clear();
        while (cursor.moveToNext()){
            String fname = cursor.getString(0);
            String label = cursor.getString(1);
            String ImageName = cursor.getString(2);
            int id = cursor.getInt(3);
            byte[] image = cursor.getBlob(4);

            list.add(new com.example.smartstethoscope.Model.Records(fname,label,ImageName,id,image));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtID = (TextView) view.findViewById(R.id.txtItemID);
                String  strID = txtID.getText().toString();
                Intent intent = new Intent(RecordsForDoctor.this,ItemDetailUser.class);
                intent.putExtra("RegID",strID);
                startActivity(intent);

            }
        });
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(RecordsForDoctor.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

}