package com.example.smartstethoscope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstethoscope.Model.DatabaseAccess;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import io.paperdb.Paper;


public class Records extends AppCompatActivity {
    GridView gridView;
    RecordListAdapter adapter;
    DatabaseAccess dbAccess;
    ArrayList<com.example.smartstethoscope.Model.Records> list;
    final int IMAGE_REQUEST= 71;
    Uri imageLocationPath;
    private Button LogoutBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
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
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(Records.this, MainActivity.class);
                startActivity(intent);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Update","Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(Records.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0){
                            Cursor c = dbAccess.getData("SELECT ID FROM Records");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));

                            }
                            showDialogUpdate(Records.this,arrID.get(position));
                        }
                        else {
                            // delete
                            Cursor c = dbAccess.getData("SELECT ID FROM Records");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }
    ImageView imageViewRecord;
    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_record_activity);
        dialog.setTitle("Update");
        imageViewRecord = (ImageView) dialog.findViewById(R.id.imageViewRecord);
        final EditText edtName = (EditText) dialog.findViewById(R.id.edtLabel);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        Cursor c = dbAccess.getData("Select label from Records WHERE ID = '" + position +"'");
        ArrayList<String> arrLbl = new ArrayList<String>();
        while (c.moveToNext()){
            arrLbl.add(c.getString(0));
        }
        edtName.setText(arrLbl.get(0));
        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbAccess.updateData(
                            edtName.getText().toString().trim(),imageViewToByte(imageViewRecord),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                }

                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateFoodList();
            }
        });
    }
    private void showDialogDelete(final int idRecord){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(Records.this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dbAccess.deleteData(idRecord);
                    Toast.makeText(getApplicationContext(), "Delete successfully!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateFoodList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }
    private void updateFoodList(){
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
    }
    private byte[] imageViewToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    public void selectImage (View view){
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent,IMAGE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null){
                imageLocationPath = data.getData();
                Bitmap objectBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageLocationPath);
                imageViewRecord.setImageBitmap(objectBitMap);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
