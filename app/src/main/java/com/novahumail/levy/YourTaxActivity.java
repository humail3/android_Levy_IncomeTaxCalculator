package com.novahumail.levy;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YourTaxActivity extends AppCompatActivity {
    TextView incomeTV_yourTax,payableTaxTV_yourTax,healthEducationCessTV_yourTax,surchargeTV_yourTax,totalPayableTaxTV_yourTax, dateTV_yourTax;
    Button saveBTN_yourTax;
    double totalPayableTaxDBL_yourTax = 0.0,payableTaxDBL_yourTax=0.0,healthEducationCessDBL_yourTax=0.0,surchargeDBL_yourTax=0.0,
            incomeDBL_yourTax=0.0;
    int totalPayableTaxINT_yourTax = 0,payableTaxINT_yourTax=0,healthEducationCessINT_yourTax=0,surchargeINT_yourTax=0,incomeINT_yourTax=0;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_tax);


        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        incomeTV_yourTax = findViewById(R.id.TV_income_yourTax);
        payableTaxTV_yourTax = findViewById(R.id.TV_payableTax_yourTax);
        healthEducationCessTV_yourTax = findViewById(R.id.TV_healthEducationCess_yourTax);
        surchargeTV_yourTax = findViewById(R.id.TV_surcharge_yourTax);
        totalPayableTaxTV_yourTax = findViewById(R.id.TV_totalPayableTax_yourTax);
        dateTV_yourTax = findViewById(R.id.TV_date_yourTax);
        saveBTN_yourTax = findViewById(R.id.BTN_save_yourTax);


//         Retrieve the value from the Intent extras from screen Slabs
        incomeDBL_yourTax = getIntent().getDoubleExtra("income", 0.0); // 0.0 is the default value if there is no value comes
        payableTaxDBL_yourTax = getIntent().getDoubleExtra("payableTax", 0.0); // 0.0 is the default value if there is no value comes
        healthEducationCessDBL_yourTax = getIntent().getDoubleExtra("healthEducationCess", 0.0); // 0.0 is the default value if there is no value comes
        surchargeDBL_yourTax = getIntent().getDoubleExtra("surcharge", 0.0); // 0.0 is the default value if there is no value comes
        totalPayableTaxDBL_yourTax = getIntent().getDoubleExtra("totalPayableTax", 0.0); // 0.0 is the default value if there is no value comes


//        convert double variables into int
        incomeINT_yourTax = (int) incomeDBL_yourTax;
        payableTaxINT_yourTax = (int) payableTaxDBL_yourTax;
        healthEducationCessINT_yourTax = (int) healthEducationCessDBL_yourTax;
        surchargeINT_yourTax = (int) surchargeDBL_yourTax;
        totalPayableTaxINT_yourTax = (int) totalPayableTaxDBL_yourTax;


//        showing the value to the screen
        incomeTV_yourTax.setText(String.valueOf("Income: "+incomeINT_yourTax));
        payableTaxTV_yourTax.setText(String.valueOf("Tax Payable: "+payableTaxINT_yourTax));
        healthEducationCessTV_yourTax.setText(String.valueOf("Health and Education Cess: "+healthEducationCessINT_yourTax));
        surchargeTV_yourTax.setText(String.valueOf("Surcharge: "+surchargeINT_yourTax));
        totalPayableTaxTV_yourTax.setText(String.valueOf("Total Payable Tax: "+totalPayableTaxINT_yourTax));


        // Get today's date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String todayDateSTR = dateFormat.format(calendar.getTime());
        // Set today's date to the TextView
        dateTV_yourTax.setText("Date: "+todayDateSTR);


//        save the values to the firestore database with uid
        saveBTN_yourTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        Create a map with field names and values
                Map<String, Object> data = new HashMap<>();
                data.put("totalPayableTax", totalPayableTaxINT_yourTax);
                data.put("todayDate", todayDateSTR);

//        Add the data to Firestore
                db.collection("users")  // Replace "your_collection_name" with your actual collection name
                        .document(firebaseAuth.getCurrentUser().getUid())        // Replace "your_document_id" with your actual document ID
                        .collection("taxData")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Document added successfully
                                Toast.makeText(YourTaxActivity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(YourTaxActivity.this, RecordsActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error occurred while adding the document
                                Toast.makeText(YourTaxActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }
}