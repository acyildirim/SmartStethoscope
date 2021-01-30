package com.example.smartstethoscope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import com.example.smartstethoscope.Model.Users;

import java.sql.PreparedStatement;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME="register.db";
    public static final String TABLE_NAME = "Users";
    public static final String TABLE_NAME_Admin = "Admin";
    public static final String TABLE_NAME_RECORD = "RECORD";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "name";
    public static final String COL_3 = "password";
    public static final String COL_4 = "phone";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Users(ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(30),  password VARCHAR(100), phone VARCHAR(40))");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Admin(ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(30),  password VARCHAR(100), phone VARCHAR(40))");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS RECORD(ID INTEGER PRIMARY KEY AUTOINCREMENT, GraphName VARCHAR(30), image BLOB)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_Admin);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_RECORD);

        onCreate(sqLiteDatabase);

    }

    public void insertİmage (String name, byte[] image){
        SQLiteDatabase db =this.getWritableDatabase();
        String sql = "INSERT INTO RECORD VALUES(NULL, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindBlob(2,image);
        statement.executeInsert();
    }
    public Cursor getImageData(String sql){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql,null);
    }

    public long addUser(String name, String password, String phone){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        long res = db.insert("Users",null,contentValues);
        db.close();
        return res;
    }

    public long addAdmin(String name, String password, String phone){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        long res = db.insert("Admin",null,contentValues);
        db.close();
        return res;
    }

    public boolean CheckPhoneForAdmin(String phone){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from Admin where phone =?",new String[]{phone});
        return cursor.getCount() <= 0;
    }
    public boolean CheckPhoneAndPasswordForAdmin(String phone, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from Admin where phone =? and password =?",new String[]{phone,password});
        return cursor.getCount() > 0;
    }

    public boolean CheckPhone(String phone){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from Users where phone =?",new String[]{phone});
        return cursor.getCount() <= 0;
    }
    public boolean CheckPhoneAndPassword(String phone, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from Users where phone =? and password =?",new String[]{phone,password});
        return cursor.getCount() > 0;
    }

}

