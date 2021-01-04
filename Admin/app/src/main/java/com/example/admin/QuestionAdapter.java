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

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

import static com.example.admin.CategoryActivity.catList;
import static com.example.admin.CategoryActivity.selectedCatIndex;
import static com.example.admin.SetsActivity.selectedSetIndex;
import static com.example.admin.SetsActivity.setIDs;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>
{
    private List<com.example.admin.QuestionModel> questionList;

    public QuestionAdapter(List<com.example.admin.QuestionModel> questionList)
    {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder viewHolder, int position)
    {
        viewHolder.setData(position,this);
    }

    @Override
    public int getItemCount()
    {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private ImageView deleteBtn;
        private Dialog loadingDialog;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.catName);
            deleteBtn = itemView.findViewById(R.id.catDeleteBtn);

            loadingDialog = new Dialog(itemView.getContext());
            loadingDialog.setContentView(R.layout.loading_progressbar);
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        private void setData(final int position,QuestionAdapter questionAdapter)
        {
            title.setText("QUESTION "+ String.valueOf(position + 1));

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(itemView.getContext(), com.example.admin.QuestionDetailActivity.class);
                    intent.putExtra("ACTION","EDIT");
                    intent.putExtra("Q_ID",position);
                    itemView.getContext().startActivity(intent);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Question")
                            .setMessage("Do you want to delete this question?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    deleteQuestion(position,itemView.getContext(),questionAdapter);
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

        private void deleteQuestion(final int position, Context context,QuestionAdapter questionAdapter)
        {
            loadingDialog.show();

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                    .collection(setIDs.get(selectedSetIndex)).document(questionList.get(position).getQuestionID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Map<String,Object> questionDoc = new ArrayMap<>();
                            int index = 1;

                            for(int i=0;i<questionList.size();i++)
                            {
                                if(i != position)
                                {
                                    questionDoc.put("Q"+String.valueOf(index) + "_ID",questionList.get(i).getQuestionID());
                                    index++;
                                }
                            }

                            questionDoc.put("COUNT",String.valueOf(index-1));

                            firestore.collection("QUIZ").document(catList.get(selectedCatIndex).getId())
                                    .collection(setIDs.get(selectedSetIndex)).document("QUESTION_LIST")
                                    .set(questionDoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Toast.makeText(context,"Question deleted successfully",Toast.LENGTH_SHORT).show();

                                            questionList.remove(position);
                                            questionAdapter.notifyDataSetChanged();
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
    }
}
