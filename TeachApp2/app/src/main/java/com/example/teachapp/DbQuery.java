package com.example.teachapp;

import android.os.Build;
import android.util.ArrayMap;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DbQuery {

    public static FirebaseFirestore g_firestore;

    public static String g_selected_lev;

    public static List<CategoryModel> g_catList = new ArrayList<>();
    public static int g_selected_cat_index = 0;

    public static List<TestModel> g_testList = new ArrayList<>();
    public static int g_selected_test_index = 0;
    public static List<QuestionModel> g_quesList = new ArrayList<>();

    public static ProfileModel myProfile = new ProfileModel("NA", null, "AB", "A3");

    public static void createUserDate(String email, String name, String school, String grade, final MyCompleteListener completeListener) {
        Map<String, Object> userDate = new ArrayMap<>();

        userDate.put("EMAIL_ID", email);
        userDate.put("NAME", name);
        userDate.put("SCHOOL", school);
        userDate.put("CLASSROOM", grade);
        userDate.put("TOTAL_SCORE", 0);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, userDate);

        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(aVoid -> {
                    completeListener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    completeListener.onFailure();
                });
    }

    public static void getUserData(final MyCompleteListener completeListener) {
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    myProfile.setName(documentSnapshot.getString("NAME"));
                    myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                    myProfile.setSchool(documentSnapshot.getString("SCHOOL"));
                    myProfile.setGrade(documentSnapshot.getString("CLASSROOM"));


                    completeListener.onSuccess();
                })
                .addOnFailureListener(e -> completeListener.onFailure());
    }


    public static void loadQuestions(final MyCompleteListener completeListener) {
        g_quesList.clear();

        switch (g_selected_lev) {
            case "QUIZ_1-10":

                g_firestore.collection("Questions_1-10")
                        .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDocID())
                        .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestID())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                g_quesList.add(new QuestionModel(
                                        doc.getString("Question"),
                                        doc.getString("A"),
                                        doc.getString("B"),
                                        doc.getString("C"),
                                        doc.getString("D"),
                                        doc.getLong("ANSWER").intValue()
                                ));
                            }

                            completeListener.onSuccess();

                        })
                        .addOnFailureListener(e -> {

                            completeListener.onFailure();

                        });
                break;
            case "QUIZ_10-100":

                g_firestore.collection("Questions_10-100")
                        .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDocID())
                        .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestID())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                g_quesList.add(new QuestionModel(
                                        doc.getString("Question"),
                                        doc.getString("A"),
                                        doc.getString("B"),
                                        doc.getString("C"),
                                        doc.getString("D"),
                                        doc.getLong("ANSWER").intValue()
                                ));
                            }

                            completeListener.onSuccess();

                        })
                        .addOnFailureListener(e -> {

                            completeListener.onFailure();

                        });
                break;
            case "QUIZ_100-1000":

                g_firestore.collection("Questions_100-1000")
                        .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDocID())
                        .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestID())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                g_quesList.add(new QuestionModel(
                                        doc.getString("Question"),
                                        doc.getString("A"),
                                        doc.getString("B"),
                                        doc.getString("C"),
                                        doc.getString("D"),
                                        doc.getLong("ANSWER").intValue()
                                ));
                            }

                            completeListener.onSuccess();

                        })
                        .addOnFailureListener(e -> {

                            completeListener.onFailure();

                        });
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void loadTestData(final MyCompleteListener completeListener) {
        g_testList.clear();

        switch (g_selected_lev) {
            case "QUIZ_1-10":
                g_firestore.collection("QUIZ_1-10").document(g_catList.get(g_selected_cat_index).getDocID())
                        .collection("TESTS_LIST").document("TESTS_INFO")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {

                            int noOfTests = Integer.parseInt(String.valueOf(g_catList.get(g_selected_cat_index).getNoOfTest()));

                            for (int i = 1; i <=noOfTests; i++) {
                                g_testList.add(new TestModel(
                                        documentSnapshot.getString("TEST" + i + "_ID"),
                                        "0",
                                        documentSnapshot.getString("TEST" + i + "_TIME")
                                ));

                            }
                            completeListener.onSuccess();
                        })
                        .addOnFailureListener(e -> completeListener.onFailure());
                break;
            case "QUIZ_10-100":
                g_firestore.collection("QUIZ_10-100").document(g_catList.get(g_selected_cat_index).getDocID())
                        .collection("TESTS_LIST").document("TESTS_INFO")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {

                            int noOfTests = Integer.parseInt(String.valueOf(g_catList.get(g_selected_cat_index).getNoOfTest()));

                            for (int i = 1; i <= noOfTests; i++) {
                                g_testList.add(new TestModel(
                                        documentSnapshot.getString("TEST" + i + "_ID"),
                                        "0",
                                        documentSnapshot.getString("TEST" + i + "_TIME")
                                ));

                            }
                            completeListener.onSuccess();
                        })
                        .addOnFailureListener(e -> completeListener.onFailure());
                break;
            case "QUIZ_100-1000":
                g_firestore.collection("QUIZ_100-1000").document(g_catList.get(g_selected_cat_index).getDocID())
                        .collection("TESTS_LIST").document("TESTS_INFO")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {

                            int noOfTests = Integer.parseInt(String.valueOf(g_catList.get(g_selected_cat_index).getNoOfTest()));

                            for (int i = 1; i <= noOfTests; i++) {
                                g_testList.add(new TestModel(
                                        documentSnapshot.getString("TEST" + i + "_ID"),
                                        "0",
                                        documentSnapshot.getString("TEST" + i + "_TIME")
                                ));

                            }
                            completeListener.onSuccess();
                        })
                        .addOnFailureListener(e -> completeListener.onFailure());
                break;
        }
}

    public static void loadData(final MyCompleteListener completeListener)
    {
        switch (g_selected_lev) {
            case "QUIZ_1-10":
                g_catList.clear();

                g_firestore.collection("QUIZ_1-10").get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                docList.put(doc.getId(), doc);
                            }

                            QueryDocumentSnapshot catListDooc = docList.get("Categories");

                            long catCount = catListDooc.getLong("COUNT");

                            for (int i = 1; i <= catCount; i++) {
                                String catID = catListDooc.getString("CAT" + String.valueOf(i) + "_ID");

                                QueryDocumentSnapshot catDoc = docList.get(catID);


                                int noOfTest = Integer.parseInt(catDoc.getString("NO_OF_TESTS"));

                                String catName = catDoc.getString("NAME");

                                g_catList.add(new CategoryModel(catID, catName, noOfTest));
                            }
                            getUserData(completeListener);
                            completeListener.onSuccess();

                        })
                        .addOnFailureListener(e -> {

                            completeListener.onFailure();

                        });

                break;
            case "QUIZ_10-100":
                g_catList.clear();

                g_firestore.collection("QUIZ_10-100").get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                docList.put(doc.getId(), doc);
                            }

                            QueryDocumentSnapshot catListDooc = docList.get("Categories");

                            long catCount = catListDooc.getLong("COUNT");

                            for (int i = 1; i <= catCount; i++)
                            {
                                String catID = catListDooc.getString("CAT" + String.valueOf(i) + "_ID");

                                QueryDocumentSnapshot catDoc = docList.get(catID);


                                int noOfTest = Integer.parseInt(catDoc.getString("NO_OF_TESTS"));

                                String catName = catDoc.getString("NAME");

                                g_catList.add(new CategoryModel(catID, catName, noOfTest));
                            }
                            getUserData(completeListener);
                            completeListener.onSuccess();

                        })
                        .addOnFailureListener(e -> {

                            completeListener.onFailure();

                        });

                break;
            case "QUIZ_100-1000":
                g_catList.clear();

                g_firestore.collection("QUIZ_100-1000").get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                docList.put(doc.getId(), doc);
                            }

                            QueryDocumentSnapshot catListDooc = docList.get("Categories");

                            long catCount = catListDooc.getLong("COUNT");

                            for (int i = 1; i <= catCount; i++) {
                                String catID = catListDooc.getString("CAT" + String.valueOf(i) + "_ID");

                                QueryDocumentSnapshot catDoc = docList.get(catID);


                                int noOfTest = Integer.parseInt(catDoc.getString("NO_OF_TESTS"));

                                String catName = catDoc.getString("NAME");

                                g_catList.add(new CategoryModel(catID, catName, noOfTest));
                            }
                            getUserData(completeListener);
                            completeListener.onSuccess();

                        })
                        .addOnFailureListener(e -> {

                            completeListener.onFailure();

                        });

                break;
        }
    }
}

