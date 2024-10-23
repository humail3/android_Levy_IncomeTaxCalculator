package com.novahumail.levy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SlabsActivity extends AppCompatActivity {
    ImageView menuIV_slabs;
    String maleFemaleSTR_slabs = "", residentialStatusSTR_slabs = "";
    Button calculateBTN_slabs;
    EditText netTaxableIncomeET_slabs;
    double netTaxableIncomeDBL_slabs = 0.0, payableTaxDBL_slabs = 0.0, totalPayableTaxDBL_slabs = 0.0, surChargeDBL_slabs = 0.0,
            healthEducationCessDBL_slabs = 0.0;
    private boolean isMenuOpen = false; // Variable to track the menu state
    private PopupWindow popupWindow; // Declare PopupWindow as a class member

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slabs);


        menuIV_slabs = findViewById(R.id.IV_menu_slabs);
        calculateBTN_slabs = findViewById(R.id.BTN_calculate_slabs);
        netTaxableIncomeET_slabs = findViewById(R.id.ET_netTaxableIncome_slabs);


        menuIV_slabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMenuOpen) {
                    showCustomMenu(v); // Open the menu
                } else {
                    closeCustomMenu(); // Close the menu
                }
            }
        });


        Spinner maleFemaleSP_slabs = findViewById(R.id.SP_maleFemale_slabs);
        List<String> maleFemale_items = Arrays.asList("Select", "Male", "Female", "Senior Citizen", "Super Senior Citizen");
        CustomSpinnerAdapter maleFemale_adapter = new CustomSpinnerAdapter(this, maleFemale_items);
        maleFemaleSP_slabs.setAdapter(maleFemale_adapter);


        Spinner residentialSP_slabs = findViewById(R.id.SP_residential_slabs);
        List<String> residential_items = Arrays.asList("Select", "Resident", "Non Resident");
        CustomSpinnerAdapter residential_adapter = new CustomSpinnerAdapter(this, residential_items);
        residentialSP_slabs.setAdapter(residential_adapter);


        maleFemaleSP_slabs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = maleFemale_items.get(position); // Get the selected item from the list
                // Now you can perform actions based on the selected item
                if (selectedItem.equals("Select")) {
                    // Handle "Select" item selection
                    maleFemaleSTR_slabs = "Select";
                } else if (selectedItem.equals("Male")) {
                    // Handle "Male" item selection
                    maleFemaleSTR_slabs = "Male";
                } else if (selectedItem.equals("Female")) {
                    // Handle "Female" item selection
                    maleFemaleSTR_slabs = "Female";
                } else if (selectedItem.equals("Senior Citizen")) {
                    // Handle "Senior Citizen" item selection
                    maleFemaleSTR_slabs = "Senior Citizen";
                } else if (selectedItem.equals("Super Senior Citizen")) {
                    // Handle "Senior Citizen" item selection
                    maleFemaleSTR_slabs = "Super Senior Citizen";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected (if necessary)
            }
        });


        residentialSP_slabs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = residential_items.get(position); // Get the selected item from the list
                // Now you can perform actions based on the selected item
                if (selectedItem.equals("Select")) {
                    // Handle "Select" item selection
                    residentialStatusSTR_slabs = "Select";
                } else if (selectedItem.equals("Resident")) {
                    // Handle "Resident" item selection
                    residentialStatusSTR_slabs = "Resident";
                } else if (selectedItem.equals("Non Resident")) {
                    // Handle "Non Resident" item selection
                    residentialStatusSTR_slabs = "Non Resident";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected (if necessary)
            }
        });


        calculateBTN_slabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // making the variables empty for again using
                netTaxableIncomeDBL_slabs = 0.0;
                payableTaxDBL_slabs = 0.0;
                totalPayableTaxDBL_slabs = 0.0;
                surChargeDBL_slabs = 0.0;
                healthEducationCessDBL_slabs = 0.0;

                if (maleFemaleSTR_slabs.equals("Select") || residentialStatusSTR_slabs.equals("Select")) {
                    Toast.makeText(SlabsActivity.this, "Kindly Select Options", Toast.LENGTH_SHORT).show();
                } else if (netTaxableIncomeET_slabs.getText().toString().trim().equals("")) {
                    Toast.makeText(SlabsActivity.this, "Kindly Put Income", Toast.LENGTH_SHORT).show();
                } else if (residentialStatusSTR_slabs.equals("Resident") && maleFemaleSTR_slabs.equals("Male") ||
                        maleFemaleSTR_slabs.equals("Female")) {
                    netTaxableIncomeDBL_slabs = Double.parseDouble(netTaxableIncomeET_slabs.getText().toString().trim());
                    maleFemale_resident();
//                    send total Tax Value to youTax Screen
                    sendTaxValue();
                } else if (maleFemaleSTR_slabs.equals("Senior Citizen") && residentialStatusSTR_slabs.equals("Resident")) {
                    netTaxableIncomeDBL_slabs = Double.parseDouble(netTaxableIncomeET_slabs.getText().toString().trim());
                    seniorCitizen_resident();
//                    send total Tax Value to youTax Screen
                    sendTaxValue();
                } else if (maleFemaleSTR_slabs.equals("Super Senior Citizen") && residentialStatusSTR_slabs.equals("Resident")) {
                    netTaxableIncomeDBL_slabs = Double.parseDouble(netTaxableIncomeET_slabs.getText().toString().trim());
                    superSeniorCitizen_resident();
//                    send total Tax Value to youTax Screen
                    sendTaxValue();
                } else if (residentialStatusSTR_slabs.equals("Non Resident")) {
                    netTaxableIncomeDBL_slabs = Double.parseDouble(netTaxableIncomeET_slabs.getText().toString().trim());
                    mfss_nonResident();
//                    send  total Tax Value to youTax Screen
                    sendTaxValue();
                }
            }
        });


    }


    // Method to close the custom menu
    private void closeCustomMenu() {
        // Dismiss the PopupWindow if it's open
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        // Update the menu state
        isMenuOpen = false;
    }

    // Method to show the custom menu
    private void showCustomMenu(View view) {
        // Create a PopupWindow with a custom layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customMenuView = inflater.inflate(R.layout.menu_item_layout, null);

        // Initialize buttons in the custom menu layout
        Button logoutButton = customMenuView.findViewById(R.id.BTN_logout_menuItemLayout);
        Button recordsButton = customMenuView.findViewById(R.id.BTN_records_menuItemLayout);

        // Set click listeners for buttons
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout action
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SlabsActivity.this, LoginActivity.class));
                finish(); // Close MainActivity after logout
                closeCustomMenu(); // Close the menu after logout
            }
        });

        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RecordsActivity
                startActivity(new Intent(SlabsActivity.this, RecordsActivity.class));
                closeCustomMenu(); // Close the menu after navigating to RecordsActivity
            }
        });

        // Create a PopupWindow to display the custom menu
        popupWindow = new PopupWindow(customMenuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Show the PopupWindow
        popupWindow.showAsDropDown(view, 0, 0);

        // Update the menu state
        isMenuOpen = true;
    }


    private void maleFemale_resident() {

        if (netTaxableIncomeDBL_slabs <= 300000) {
            Toast.makeText(SlabsActivity.this, "Your Payable Tax is NIL", Toast.LENGTH_SHORT).show();
        } else if (netTaxableIncomeDBL_slabs > 300000 && netTaxableIncomeDBL_slabs <= 600000) {
            payableTaxDBL_slabs = ((netTaxableIncomeDBL_slabs * 5) / 100);
        } else if (netTaxableIncomeDBL_slabs > 600000 && netTaxableIncomeDBL_slabs <= 900000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 10) / 100) + 15000);
        } else if (netTaxableIncomeDBL_slabs > 900000 && netTaxableIncomeDBL_slabs <= 1200000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 15) / 100) + 45000);
        } else if (netTaxableIncomeDBL_slabs > 1200000 && netTaxableIncomeDBL_slabs <= 1500000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 20) / 100) + 90000);
        } else if (netTaxableIncomeDBL_slabs > 1500000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 30) / 100) + 150000);

//            adding surcharge
            if (netTaxableIncomeDBL_slabs > 5000000 && netTaxableIncomeDBL_slabs <= 10000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 10) / 100);
            } else if (netTaxableIncomeDBL_slabs > 10000000 && netTaxableIncomeDBL_slabs <= 20000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 15) / 100);
            } else if (netTaxableIncomeDBL_slabs > 20000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 25) / 100);
            }
        }

//        calculating health and educational cess
        if (netTaxableIncomeDBL_slabs <= 300000) {
            healthEducationCessDBL_slabs = 0.0;
        } else {
            healthEducationCessDBL_slabs = ((payableTaxDBL_slabs * 4) / 100);
        }

//        health and education cess is 4% of total income tax +surcharge(if applicable)
        totalPayableTaxDBL_slabs = payableTaxDBL_slabs+healthEducationCessDBL_slabs + surChargeDBL_slabs;

    }


    private void seniorCitizen_resident() {

        if (netTaxableIncomeDBL_slabs <= 500000) {
            Toast.makeText(SlabsActivity.this, "Your Payable Tax is NIL", Toast.LENGTH_SHORT).show();
        } else if (netTaxableIncomeDBL_slabs > 500000 && netTaxableIncomeDBL_slabs <= 900000) {
            payableTaxDBL_slabs = ((netTaxableIncomeDBL_slabs * 10) / 100);
        } else if (netTaxableIncomeDBL_slabs > 900000 && netTaxableIncomeDBL_slabs <= 1200000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 15) / 100) + 45000);
        } else if (netTaxableIncomeDBL_slabs > 1200000 && netTaxableIncomeDBL_slabs <= 1500000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 20) / 100) + 90000);
        } else if (netTaxableIncomeDBL_slabs > 1500000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 30) / 100) + 150000);
//            adding surcharge
            if (netTaxableIncomeDBL_slabs > 5000000 && netTaxableIncomeDBL_slabs <= 10000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 10) / 100);
            } else if (netTaxableIncomeDBL_slabs > 10000000 && netTaxableIncomeDBL_slabs <= 20000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 15) / 100);
            } else if (netTaxableIncomeDBL_slabs > 20000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 25) / 100);
            }
        }

        //        calculating health and educational cess
        if (netTaxableIncomeDBL_slabs <= 500000) {
            healthEducationCessDBL_slabs = 0.0;
        } else {
            healthEducationCessDBL_slabs = ((payableTaxDBL_slabs * 4) / 100);
        }

//        health and education cess is 4% of total income tax +surcharge(if applicable)
        totalPayableTaxDBL_slabs = payableTaxDBL_slabs+ healthEducationCessDBL_slabs + surChargeDBL_slabs;

    }


    private void superSeniorCitizen_resident() {

        if (netTaxableIncomeDBL_slabs <= 600000) {
            Toast.makeText(SlabsActivity.this, "Your Payable Tax is NIL", Toast.LENGTH_SHORT).show();
        } else if (netTaxableIncomeDBL_slabs > 600000 && netTaxableIncomeDBL_slabs <= 900000) {
            payableTaxDBL_slabs = ((netTaxableIncomeDBL_slabs * 10) / 100);
        } else if (netTaxableIncomeDBL_slabs > 900000 && netTaxableIncomeDBL_slabs <= 1200000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 15) / 100) + 45000);
        } else if (netTaxableIncomeDBL_slabs > 1200000 && netTaxableIncomeDBL_slabs <= 1500000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 20) / 100) + 90000);
        } else if (netTaxableIncomeDBL_slabs > 1500000) {
            payableTaxDBL_slabs = (((netTaxableIncomeDBL_slabs * 30) / 100) + 150000);
//            adding surcharge
            if (netTaxableIncomeDBL_slabs > 5000000 && netTaxableIncomeDBL_slabs <= 10000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 10) / 100);
            } else if (netTaxableIncomeDBL_slabs > 10000000 && netTaxableIncomeDBL_slabs <= 20000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 15) / 100);
            } else if (netTaxableIncomeDBL_slabs > 20000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 25) / 100);
            }
        }

        //        calculating health and educational cess
        if (netTaxableIncomeDBL_slabs <= 600000) {
            healthEducationCessDBL_slabs = 0.0;
        } else {
            healthEducationCessDBL_slabs = ((payableTaxDBL_slabs * 4) / 100);
        }

//        health and education cess is 4% of total income tax +surcharge(if applicable)
        totalPayableTaxDBL_slabs =  payableTaxDBL_slabs+healthEducationCessDBL_slabs + surChargeDBL_slabs;

    }


    private void mfss_nonResident() {

        if (netTaxableIncomeDBL_slabs <= 250000) {
            Toast.makeText(SlabsActivity.this, "Your Payable Tax is NIL", Toast.LENGTH_SHORT).show();
        } else if (netTaxableIncomeDBL_slabs > 250000 && netTaxableIncomeDBL_slabs <= 500000) {
            payableTaxDBL_slabs = ((netTaxableIncomeDBL_slabs * 5) / 100);
        } else if (netTaxableIncomeDBL_slabs > 500000 && netTaxableIncomeDBL_slabs <= 1000000) {
            payableTaxDBL_slabs = ((netTaxableIncomeDBL_slabs * 20) / 100);
        } else if (netTaxableIncomeDBL_slabs > 1000000) {
            payableTaxDBL_slabs = ((netTaxableIncomeDBL_slabs * 30) / 100);
//            adding surcharge
            if (netTaxableIncomeDBL_slabs > 5000000 && netTaxableIncomeDBL_slabs <= 10000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 10) / 100);
            } else if (netTaxableIncomeDBL_slabs > 10000000 && netTaxableIncomeDBL_slabs <= 20000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 15) / 100);
            } else if (netTaxableIncomeDBL_slabs > 20000000) {
                surChargeDBL_slabs = ((payableTaxDBL_slabs * 25) / 100);
            }
        }

        //        calculating health and educational cess
        if (netTaxableIncomeDBL_slabs <= 250000) {
            healthEducationCessDBL_slabs = 0.0;
        } else {
            healthEducationCessDBL_slabs = ((payableTaxDBL_slabs * 4) / 100);
        }

//        health and education cess is 4% of total income tax +surcharge(if applicable)
        totalPayableTaxDBL_slabs = payableTaxDBL_slabs+ healthEducationCessDBL_slabs + surChargeDBL_slabs;

    }


    private void sendTaxValue() {
        Intent intent = new Intent(SlabsActivity.this, YourTaxActivity.class);
        intent.putExtra("income", netTaxableIncomeDBL_slabs);
        intent.putExtra("payableTax", payableTaxDBL_slabs);
        intent.putExtra("healthEducationCess", healthEducationCessDBL_slabs);
        intent.putExtra("surcharge", surChargeDBL_slabs);
        intent.putExtra("totalPayableTax", totalPayableTaxDBL_slabs);
        startActivity(intent);
    }


}