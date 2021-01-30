package com.example.smartstethoscope.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;


public class DatabaseAccess {
    private static final String COL_1 = "fname";
    private static final String COL_2 = "label";
    private static final String COL_3 = "ImageName";
    private static final String COL_4 = "ID";
    private static final String COL_5 = "Image";
    private static final String TABLE = "Records";



    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);

    }

    public static DatabaseAccess getInstance(Context context){
        if (instance== null){

            instance = new DatabaseAccess(context);
        }
        return instance;
    }
    public void open(){

        this.db = openHelper.getWritableDatabase();
    }
    public void close(){
        if(db != null){
            this.db.close();
        }
    }
    public long addAdmin(String name, String password, String phone){
        this.db =openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        long res = db.insert("Admin",null,contentValues);
        db.close();
        return res;
    }
    public long addUser(String name, String password, String phone){
        this.db = openHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        long res = db.insert("Users",null,contentValues);
        db.close();
        return res;
    }
    public boolean CheckPhone(String phone){
        c = db.rawQuery("Select * from Users where phone =?",new String[]{phone});
        return c.getCount() <= 0;

    }
    public boolean CheckPhoneAndPassword(String phone, String password){
        c = db.rawQuery("Select * from Users where phone =? and password =?",new String[]{phone,password});
        return c.getCount() > 0;
    }

    public boolean CheckPhoneForAdmin(String phone){
        c = db.rawQuery("Select * from Admin where phone =?",new String[]{phone});
        return c.getCount() <= 0;
    }
    public boolean CheckPhoneAndPasswordForAdmin(String phone, String password){
        c = db.rawQuery("Select * from Admin where phone =? and password =?",new String[]{phone,password});
        return c.getCount() > 0;
    }
    public long insertData(byte[] image){
        this.db = openHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("Image", image);
        long res = db.insert("Resim",null,contentValues);
        db.close();
        return res;
    }

    public Cursor getData(String sql){
        this.db = openHelper.getReadableDatabase();
        return db.rawQuery(sql, null);
    }
    public void queryData(String sql){
        this.db = openHelper.getWritableDatabase();
        db.execSQL(sql);
    }
    //String fname, String label, String ImageName, byte[] Image, int ID
    public void updateData(String label, byte[] Image, int ID) {
        this.db = openHelper.getWritableDatabase();

        String sql = "UPDATE Records SET label = ?, Image = ? WHERE ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, label);
        statement.bindBlob(2, Image);
        statement.bindDouble(3, (double)ID);


        statement.execute();
        db.close();
    }
    public void updateLabelData(String label, int ID) {
        this.db = openHelper.getWritableDatabase();

        String sql = "UPDATE Records SET label = ? WHERE ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, label);
        statement.bindDouble(2, (double)ID);


        statement.execute();
        db.close();
    }
    public void updateDataLabelEmpty(byte[] Image, int ID) {
        this.db = openHelper.getWritableDatabase();

        String sql = "UPDATE Records SET  Image = ? WHERE ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindBlob(1, Image);
        statement.bindDouble(2, (double)ID);


        statement.execute();
        db.close();
    }
    public void updateDataByteEmpty(String label , int ID) {
        this.db = openHelper.getWritableDatabase();

        String sql = "UPDATE Records SET  label = ? WHERE ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, label);
        statement.bindDouble(2, (double)ID);


        statement.execute();
        db.close();
    }

    public  void deleteData(int ID) {
        this.db = openHelper.getWritableDatabase();

        String sql = "DELETE FROM Records WHERE ID = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)ID);

        statement.execute();
        db.close();
    }

}
