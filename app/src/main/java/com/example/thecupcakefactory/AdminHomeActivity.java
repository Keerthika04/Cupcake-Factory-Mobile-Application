package com.example.thecupcakefactory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void toCupcake(View view) {
        Intent i = new Intent(this,AdminCupcakeActivity.class);
        startActivity(i);
    }

    public void toOrders(View view) {
        Intent i = new Intent(this,AdminOrdersActivity.class);
        startActivity(i);
    }

    public void toCategory(View view) {
        Intent i = new Intent(this,AdminCategoryActivity.class);
        startActivity(i);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onLogout(View view){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
