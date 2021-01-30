package com.example.smartstethoscope;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartstethoscope.Model.Deneme;

import java.util.ArrayList;

public class DenemeAdapter extends BaseAdapter {
    private Context context;
    private  int layout;
    private ArrayList<Deneme> denemeList;
    public DenemeAdapter(Context context, int layout, ArrayList<Deneme> ArrayDenemeList) {
        this.context = context;
        this.layout = layout;
        this.denemeList = ArrayDenemeList;
    }

    @Override
    public int getCount() {
        return denemeList.size();
    }

    @Override
    public Object getItem(int position) {
        return denemeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;

    }
    private class ViewHolder{
        ImageView imageView;
        TextView txtLabel;
        TextView txtID;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        DenemeAdapter.ViewHolder holder = new DenemeAdapter.ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtLabel = (TextView) row.findViewById(R.id.txtLabelName);
            holder.txtID = (TextView) row.findViewById(R.id.txtItemID);
            holder.imageView = (ImageView) row.findViewById(R.id.imgRecord);
            row.setTag(holder);
        }
        else {
            holder = (DenemeAdapter.ViewHolder) row.getTag();
        }
        Deneme deneme = denemeList.get(position);
        holder.txtLabel.setText(deneme.getDiagnose());
        byte[] recordImage = deneme.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.imageView.setImageBitmap(bitmap);
        holder.txtID.setText(deneme.getID() + "");
        return row;

    }
}
