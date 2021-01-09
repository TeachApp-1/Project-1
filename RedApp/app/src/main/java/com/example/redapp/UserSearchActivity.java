package com.example.redapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class UserSearchActivity extends AppCompatActivity
{
   private FirebaseFirestore firestore;
   RecyclerView userRecView;
   ArrayList<UserModel> userModelArrayList = new ArrayList<>();
   UserAdapter userAdapter;
   private EditText userSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("רשימת משתמשים");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //userSearchBar = findViewById(R.id.userSearchBar);
        userRecView = findViewById(R.id.adminSearchRecView);
        firestore = FirebaseFirestore.getInstance();

        /*
        userSearchBar.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(userModelArrayList.size() > 0)
                    userModelArrayList.clear();

                firestore.collection("USERS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {

                        for(QueryDocumentSnapshot doc: task.getResult())
                        {
                            if(doc.getString("NAME").toLowerCase().equals(s.toString().toLowerCase()))
                            {
                                UserModel userModel = new UserModel(doc.getString("NAME"), doc.getString("EMAIL_ID"), doc.getString("SCHOOL"), doc.getString("CLASSROOM"));
                                userModelArrayList.add(userModel);
                            }
                        }
                        userAdapter = new UserAdapter(userModelArrayList);
                        userRecView.setAdapter(userAdapter);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(UserSearchActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

         */

        setUpRecyclerView();
        setUpFirebase();
        loadDataFirebase();
    }

    private void loadDataFirebase()
    {
        if(userModelArrayList.size() > 0)
            userModelArrayList.clear();

        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                for(QueryDocumentSnapshot doc: task.getResult())
                {
                    UserModel userModel = new UserModel(doc.getString("Name"),doc.getString("Email"),doc.getString("School"),doc.getString("Classroom"));
                    userModelArrayList.add(userModel);
                }
                userAdapter = new UserAdapter(UserSearchActivity.this,userModelArrayList);
                userRecView.setAdapter(userAdapter);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(UserSearchActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpFirebase()
    {
        firestore = FirebaseFirestore.getInstance();
    }

    private void setUpRecyclerView()
    {
        userRecView = findViewById(R.id.adminSearchRecView);
        userRecView.setHasFixedSize(true);
        userRecView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            UserSearchActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}