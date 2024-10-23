package com.novahumail.levy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecordsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaxDataAdapter adapter;
    private List<TaxDataModel> taxDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);


        // Retrieve tax data from Firestore
        retrieveTaxDataFromFirestore();


        taxDataList = new ArrayList<>();


//        Initialize RecyclerView and set adapter
        recyclerView = findViewById(R.id.RV_taxData_records);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaxDataAdapter(taxDataList);
        recyclerView.setAdapter(adapter);


    }


    private void retrieveTaxDataFromFirestore() {
        // Perform Firestore query to fetch tax data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("taxData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Clear existing data
                            taxDataList.clear();

                            // Iterate through Firestore documents
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract data from Firestore document
                                int totalPayableTax = document.getLong("totalPayableTax").intValue();
                                String todayDate = document.getString("todayDate");

                                // Create TaxDataModel object and add to list
                                TaxDataModel taxData = new TaxDataModel(totalPayableTax, todayDate);
                                taxDataList.add(taxData);
                            }

                            // Notify adapter of data change
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}