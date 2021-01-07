package com.example.admin;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Map;

import static com.example.admin.CategoryActivity.catList;
import static com.example.admin.CategoryActivity.selectedCatIndex;
import static com.example.admin.SetsActivity.selectedSetIndex;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder>
{
    private List<String> setIDs;

    public SetAdapter(List<String> setIDs)
    {
        this.setIDs = setIDs;
    }

    @NonNull
    @Override
    public SetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetAdapter.ViewHolder holder, int position)
    {
        String setID = setIDs.get(position);
        holder.setData(position,setID,this);
    }

    @Override
    public int getItemCount()
    {
        return setIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView setName;
        private ImageView deleteSetBtn;
        private Dialog loadingDialog;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            setName = itemView.findViewById(R.id.catName);
            deleteSetBtn = itemView.findViewById(R.id.catDeleteBtn);

            loadingDialog = new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.loading_progressbar);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private void setData(final int position,final String setID,final SetAdapter setAdapter)
        {
            setName.setText("SET " + String.valueOf(position+1));

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedSetIndex = position;

                    Intent intent = new Intent(itemView.getContext(),QuestionActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

            deleteSetBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Set")
                            .setMessage("Do you want to delete this set?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    deleteSet(position,setID,itemView.getContext(),setAdapter);
                                }
                            }).setNegativeButton("Cancel",null)
                            .setIcon(android.R.drawable.ic_dialog_alert).show();

                    alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.RED);
                    alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.WHITE);
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,50,0);
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setLayoutParams(params);
                }
            });
        }

        private void deleteSet(final int position,String setID,final Context context,final SetAdapter setAdapter)
        {
            loadingDialog.show();

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                    .collection(setID).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                    {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                        {
                            WriteBatch batch = firestore.batch();

                            for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                            {
                                batch.delete(doc.getReference());
                            }

                            batch.commit().addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Map<String,Object> catDoc = new ArrayMap<>();
                                    int index = 1;
                                    for(int i = 0;i < setIDs.size();i++)
                                    {
                                        if(i != position)
                                        {
                                            catDoc.put("SET" + String.valueOf(index) + "_ID",setIDs.get(i));
                                            index++;
                                        }
                                    }
                                    catDoc.put("SETS",index-1);

                                    firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                                            .update(catDoc).addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Toast.makeText(context,"Set Deleted Successfully",Toast.LENGTH_SHORT).show();
                                            com.example.admin.SetsActivity.setIDs.remove(position);
                                            catList.get(selectedCatIndex).setNumOfSets(String.valueOf(com.example.admin.SetsActivity.setIDs.size()));
                                            setAdapter.notifyDataSetChanged();
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
