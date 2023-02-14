package com.rajendra.foodapp.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajendra.foodapp.R;

public class ProgramViewHolder {

    ImageView itemImage;
    TextView programTitle,programDescription;
    ProgramViewHolder(View v)
    {
        itemImage=v.findViewById(R.id.imageView);
        programTitle= v.findViewById(R.id.textView1);
        programDescription=v.findViewById(R.id.textView2);

    }
}
