package com.example.teachapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    public CategoryFragment() {}

    private GridView catView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);

        catView = view.findViewById(R.id.cat_Grid);



        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catList);
        catView.setAdapter(adapter);

        return view;
    }

}
    /*



    private void loadCategories()
    {
        catList.clear();

        catList.add(new CategoryModel("1","חיבור",10));
        catList.add(new CategoryModel("2","חיסור",15));
        catList.add(new CategoryModel("3","חילוק",8));
        catList.add(new CategoryModel("4","כפל",3));
        catList.add(new CategoryModel("5","אחוזים",7));
        catList.add(new CategoryModel("6","שברים",12));
        catList.add(new CategoryModel("1","חיבור",18));
    }
    **/
