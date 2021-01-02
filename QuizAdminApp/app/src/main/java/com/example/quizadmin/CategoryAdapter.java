package com.example.quizadmin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
{
    private List<CategoryModel> catList;

    public CategoryAdapter(List<CategoryModel> catList)
    {
        this.catList = catList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder viewHolder, int position)
    {
        String title = catList.get(position).getName();
        viewHolder.setData(title,position,this);
    }

    @Override
    public int getItemCount()
    {
        return catList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView catName;
        private ImageView deleteBtn;
        private Dialog loadingDialog,editDialog;
        private EditText editCatNameDialog;
        private Button editCatBtnDialog;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            catName = itemView.findViewById(R.id.catName);
            deleteBtn = itemView.findViewById(R.id.catDeleteBtn);

            loadingDialog = new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.loading_progressbar);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            editDialog = new Dialog(itemView.getContext());
            editDialog.setContentView(R.layout.edit_category_dialog);
            editDialog.setCancelable(true);
            editDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            editCatNameDialog = editDialog.findViewById(R.id.editCatNameDialog);
            editCatBtnDialog = editDialog.findViewById(R.id.editCatDialogBtn);
        }
        private void setData(String title,final int pos,final CategoryAdapter adapter)
        {
            catName.setText(title); //setting category name to current title

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CategoryActivity.selectedCatIndex = pos;
                    Intent intent = new Intent(itemView.getContext(),SetsActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    editCatNameDialog.setText(catList.get(pos).getName());
                    editDialog.show();
                    return false;
                }
            });

            editCatBtnDialog.setOnClickListener(new View.OnClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v)
                {
                    if(editCatNameDialog.getText().toString().isEmpty())
                    {
                        editCatNameDialog.setError("Enter Category Name");
                        return;
                    }
                    editCatBtnDialog.setVisibility(View.VISIBLE);
                    updateCategory(editCatNameDialog.getText().toString(),pos,itemView.getContext(),adapter);
                    editCatBtnDialog.setVisibility(View.INVISIBLE);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener()
            {
                @SuppressLint("ResourceType")
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Category")
                            .setMessage("Do you want to delete this category?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    deleteCategory(pos,itemView.getContext(),adapter);
                                }
                            }).setNegativeButton("Cancel",null)
                            .setIcon(android.R.drawable.ic_dialog_alert).show();

                    alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.RED);
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.RED);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,50,0);
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setLayoutParams(params);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void deleteCategory(final int id,final Context context,CategoryAdapter adapter)
        {
            loadingDialog.show();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            Map<String,Object> catDoc = new ArrayMap<>();
            int index = 1;

            for(int i = 0; i < catList.size(); i++)
            {
                if(i != id)
                {
                    catDoc.put("CAT" + String.valueOf(index) + "_ID",catList.get(i).getId());
                    catDoc.put("CAT" + String.valueOf(index) + "_NAME",catList.get(i).getName());
                    index++;
                }
            }

            catDoc.put("COUNT",index-1);

            firestore.collection("QUIZ").document("CATEGORIES").set(catDoc)
                    .addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(context,"Category has been deleted",Toast.LENGTH_SHORT).show();
                            CategoryActivity.catList.remove(id);
                            adapter.notifyDataSetChanged();
                            loadingDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void updateCategory(final String newCatName,final int pos,final Context context,CategoryAdapter adapter)
        {
            editDialog.dismiss();
            loadingDialog.show();

            Map<String,Object> catData = new ArrayMap<>();
            catData.put("NAME",newCatName);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("QUIZ").document(catList.get(pos).getId())
                    .update(catData).addOnSuccessListener(new OnSuccessListener<Void>()
            {
                @Override
                public void onSuccess(Void aVoid)
                {
                    Map<String,Object> catDoc = new ArrayMap<>();
                    catDoc.put("CAT" + String.valueOf(pos+1) + "_NAME",newCatName);

                    firestore.collection("QUIZ").document("CATEGORIES")
                            .update(catDoc).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(context,"Category Name Changed Successfully",Toast.LENGTH_LONG).show();
                            CategoryActivity.catList.get(pos).setName(newCatName);
                            adapter.notifyDataSetChanged();
                            loadingDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        }
    }
}
