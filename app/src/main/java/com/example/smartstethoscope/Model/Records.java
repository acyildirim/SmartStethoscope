package com.example.smartstethoscope.Model;

public class Records {

    private String fname,label,ImageName;
    private int ID;
    private byte[] Image;

    public Records(String fname, String label, String ImageName, int ID, byte[] Image){
        this.fname = fname;
        this.label = label;
        this.ImageName = ImageName;
        this.ID = ID;
        this.Image = Image;

    }

    public Records() {

    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
}
