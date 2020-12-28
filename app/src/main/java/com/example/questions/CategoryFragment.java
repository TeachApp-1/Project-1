package com.example.questions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    public CategoryFragment(){

    }

    private GridView catView;
    private List<CategoryModel> catList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        catView = view.findViewById(R.id.cat_grid);
        loadCategories();
        CategoryAdapter adapter = new CategoryAdapter(catList);
        catView.setAdapter(adapter);
        return view;
    }

    private void loadCategories()
    {
        catList.clear();
        catList.add(new CategoryModel("1", "ADD", 12));
        catList.add(new CategoryModel("2", "SUB", 7));
        catList.add(new CategoryModel("3", "MUL", 16));
        catList.add(new CategoryModel("4", "DIV", 9));
        catList.add(new CategoryModel("5", "COMBINED", 1));
    }
}
