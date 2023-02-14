package com.rajendra.foodapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rajendra.foodapp.R;


public class ProgramAdapter extends ArrayAdapter<String> {

    Context context;
    int[] images;
    String[] programName;
    String[] programDescription;

    public ProgramAdapter(Context context, String[] programName,int[] images,String[] programDescription) {
        super(context, R.layout.single_item,R.id.textView1,programName);


            this.context= context;
            this.images=images;
            this.programName=programName;
            this.programDescription=programDescription;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View singleItem =convertView;
        ProgramViewHolder holder = null;
        if (singleItem == null){
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.single_item,parent,false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);

        }
        else {
            holder = (ProgramViewHolder) singleItem.getTag();
        }
        holder.itemImage.setImageResource(images[position]);
        holder.programTitle.setText(programName[position]);
        holder.programDescription.setText(programDescription[position]);
        singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"You clicked"+programName[position],Toast.LENGTH_SHORT).show();
            }
        });
        return singleItem;
    }
}
