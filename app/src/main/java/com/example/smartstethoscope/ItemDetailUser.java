package com.example.smartstethoscope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartstethoscope.Database.ConnectionClass;
import com.example.smartstethoscope.Model.DatabaseAccess;
import com.example.smartstethoscope.Model.Records;
import com.mysql.jdbc.Blob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ItemDetailUser extends AppCompatActivity {
    private TextView txtLabel, txtID, elapsedTimeLabel, remainingTimeLabel;
    private ImageView imageViewRecord;
    DatabaseAccess dbAccess;
    Cursor c;
    Records records;
    private SeekBar positionBar, volumeBar;
    Button btnYes, btnNo, btnPlay;
    private MediaPlayer mMediaPlayer;
    private int totalTime;
    ConnectionClass connectionClass;
    ArrayList<String> arrLbl;
    ArrayList<byte[]> arrImage;
    ArrayList<byte[]> arrAudio;
    ArrayList<String> arrFame;
    private static final int PERMISSION_REQUEST_CODE = 200;
    String Id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail_user);
        connectionClass = new ConnectionClass();
        dbAccess = DatabaseAccess.getInstance(getApplicationContext());
        dbAccess.open();
        btnPlay = findViewById(R.id.playBtn);
        txtLabel = findViewById(R.id.txtLabelNameUser);
        imageViewRecord = findViewById(R.id.imgRecordUser);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);


        Id = getIntent().getStringExtra("RegID");
        txtID = findViewById(R.id.txtIDPass);
        txtID.setText(Id);

        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);

        arrLbl = new ArrayList<String>();
        arrImage = new ArrayList<>();
        arrAudio = new ArrayList<>();
        arrFame = new ArrayList<>();
        int IdRecord = Integer.parseInt(Id);
        try {
            arrLbl = getDiagnose(IdRecord);
            arrImage = getImage(IdRecord);
            arrFame = getFname(IdRecord);
            //readBlob(IdRecord, arrFame.get(0)+".png");
            readAudio(IdRecord, arrFame.get(0) + ".waw");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtLabel.setText(arrLbl.get(0));
        Bitmap bitmap = BitmapFactory.decodeByteArray(arrImage.get(0), 0, arrImage.get(0).length);
        imageViewRecord.setImageBitmap(bitmap);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailUser.this, DenemeMysql.class);
                startActivity(intent);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailUser.this, FindDiagnosis.class);
                intent.putExtra("Reg2ID", Id);
                startActivity(intent);
            }
        });
       /* Cursor music = dbAccess.getData("Select fname from Records WHERE ID = '" + Id + "'");
        ArrayList<String> arrFname = new ArrayList<>();
        while (music.moveToNext()) {
            arrFname.add(music.getString(0));
        }*/
        String root = android.os.Environment.getExternalStorageDirectory().getPath() + "/download/" + arrFame.get(0) + ".waw";
        File file = new File(root);
        Log.d("Main", "voice exists" + file.exists() + " can read " + file.canRead());
        //int resID = getResId(arrFname.get(0), R.raw.class); // or other resource class

        mMediaPlayer = MediaPlayer.create(this, Uri.parse(root));
        mMediaPlayer.setLooping(true);
        mMediaPlayer.seekTo(0);
        mMediaPlayer.setVolume(0.5f, 0.5f);
        totalTime = mMediaPlayer.getDuration();


        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mMediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mMediaPlayer.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mMediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();


    }


    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void playBtnClick(View view) {

        if (!mMediaPlayer.isPlaying()) {
            // Stopping
            mMediaPlayer.start();
            btnPlay.setBackgroundResource(R.drawable.stop);

        } else {
            // Playing
            mMediaPlayer.pause();
            btnPlay.setBackgroundResource(R.drawable.play);
        }

    }

    private ArrayList<String> getDiagnose(int Id) throws SQLException, IOException {
        arrLbl.clear();
        Connection con = connectionClass.CONN();
        if (con == null) {

        } else {
            Statement stmt = con.createStatement();


            String sql = "Select * From Deneme where Id ='" + Id + "'";
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String fileName = "C:\\Users\\yldrm\\Desktop\\Projects\\SmartStethoscope\\app\\src\\main\\res\\raw\\" + resultSet.getString(3) + ".waw";
                int ID = resultSet.getInt(1);
                String Diagnose = resultSet.getString(2);
                String fName = resultSet.getString(3);
                String ImageName = resultSet.getString(4);
                Blob blobImage = (Blob) resultSet.getBlob(5);
                Blob blobAudio = (Blob) resultSet.getBlob(6);
                int len = (int) blobAudio.length();
                byte[] byteImage = blobImage.getBytes(1, (int) blobImage.length());
                byte[] byteAudio = blobAudio.getBytes(1, (int) blobAudio.length());
                arrLbl.add(Diagnose);
            }

        }
        return arrLbl;
    }

    private ArrayList<String> getFname(int Id) throws SQLException, IOException {
        arrFame.clear();
        Connection con = connectionClass.CONN();
        if (con == null) {

        } else {
            Statement stmt = con.createStatement();


            String sql = "Select * From Deneme where Id ='" + Id + "'";
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String fileName = "C:\\Users\\yldrm\\Desktop\\Projects\\SmartStethoscope\\app\\src\\main\\res\\raw\\" + resultSet.getString(3) + ".waw";
                int ID = resultSet.getInt(1);
                String Diagnose = resultSet.getString(2);
                String fName = resultSet.getString(3);
                String ImageName = resultSet.getString(4);
                Blob blobImage = (Blob) resultSet.getBlob(5);
                Blob blobAudio = (Blob) resultSet.getBlob(6);
                int len = (int) blobAudio.length();
                byte[] byteImage = blobImage.getBytes(1, (int) blobImage.length());
                byte[] byteAudio = blobAudio.getBytes(1, (int) blobAudio.length());
                arrFame.add(fName);
            }

        }
        return arrFame;
    }

    private ArrayList<byte[]> getImage(int Id) throws SQLException, IOException {
        arrImage.clear();
        Connection con = connectionClass.CONN();
        if (con == null) {

        } else {
            Statement stmt = con.createStatement();


            String sql = "Select * From Deneme where Id ='" + Id + "'";
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String fileName = "C:\\Users\\yldrm\\Desktop\\Projects\\SmartStethoscope\\app\\src\\main\\res\\raw\\" + resultSet.getString(3) + ".waw";
                int ID = resultSet.getInt(1);
                String Diagnose = resultSet.getString(2);
                String fName = resultSet.getString(3);
                String ImageName = resultSet.getString(4);
                Blob blobImage = (Blob) resultSet.getBlob(5);
                Blob blobAudio = (Blob) resultSet.getBlob(6);
                int len = (int) blobAudio.length();
                byte[] byteImage = blobImage.getBytes(1, (int) blobImage.length());
                byte[] byteAudio = blobAudio.getBytes(1, (int) blobAudio.length());
                arrImage.add(byteImage);
            }

        }
        return arrImage;
    }

    public void readAudio(int Id, String filename) {
        String selectSQL = "SELECT Audio FROM Deneme WHERE id=?";
        ResultSet rs = null;
        Context context = this;
        try (Connection conn = connectionClass.CONN();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
            pstmt.setInt(1, Id);
            rs = pstmt.executeQuery();
            // write binary stream into file
            //File file = new File(filename);
            //File file = new File(context.getFilesDir(), filename);

            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/Download");
            File file = new File(dir, filename);
            FileOutputStream output = new FileOutputStream(file);
            System.out.println("Writing to file " + file.getAbsolutePath());
            while (rs.next()) {
                InputStream input = rs.getBinaryStream("Audio");
                byte[] buffer = new byte[4096];
                while (input.read(buffer) > 0) {
                    output.write(buffer);
                }
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
