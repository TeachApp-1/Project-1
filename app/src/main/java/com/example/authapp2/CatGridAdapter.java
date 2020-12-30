package com.example.authapp2;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class CatGridAdapter extends BaseAdapter
{
    private List<String> catList;

    public CatGridAdapter(List<String> catList)
    {
        this.catList = catList;
    }

    @Override
    public int getCount()
    {
        return catList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;

        if(convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_grid_layout,parent,false);
        }
        else
        {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //MainActivity.selected_cat_index = position;
                Intent intent = new Intent(parent.getContext(),SetsActivity.class);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) view.findViewById(R.id.categoryName)).setText(catList.get(position));
        Random random = new Random();
        int color = Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255));
        view.setBackgroundColor(color);

        return view;
    }
}
