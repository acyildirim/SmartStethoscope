package com.example.smartstethoscope;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartstethoscope.Model.Records;

import java.util.ArrayList;

public class RecordListAdapter extends BaseAdapter {
    private Context context;
    private  int layout;
    private ArrayList<Records> recordsList;

    public RecordListAdapter(Context context, int layout, ArrayList<Records> ArrayrecordsList) {
        this.context = context;
        this.layout = layout;
        this.recordsList = ArrayrecordsList;
    }


    @Override
    public int getCount() {
        return recordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView  txtLabel;
        TextView txtID;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtLabel = (TextView) row.findViewById(R.id.txtLabelName);
            holder.txtID = (TextView) row.findViewById(R.id.txtItemID);
            holder.imageView = (ImageView) row.findViewById(R.id.imgRecord);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Records record = recordsList.get(position);
        holder.txtLabel.setText(record.getLabel());
        byte[] recordImage = record.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.imageView.setImageBitmap(bitmap);
        holder.txtID.setText(record.getID() + "");
        return row;
    }
}
