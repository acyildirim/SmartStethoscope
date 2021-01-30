package com.example.smartstethoscope.Model;

import com.mysql.jdbc.Blob;

public class Deneme {
    private String Diagnose,fname,ImageName;
    private int ID;
    private byte[] Image, Audio;

    public Deneme(int ID, String Diagnose, String fname, String ImageName, byte[] Image, byte[] Audio){
        this.Diagnose = Diagnose;
        this.Audio = Audio;
        this.fname = fname;
        this.ImageName = ImageName;
        this.ID = ID;
        this.Image = Image;

    }

    public Deneme() {

    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getDiagnose() {
        return Diagnose;
    }

    public void setDiagnose(String Diagnose) {
        this.Diagnose = Diagnose;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
    public byte[] getAudio() {
        return Audio;
    }

    public void setAudio(byte[] Audio) {
        Audio = Audio;
    }
}
