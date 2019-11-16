package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.snackbar.Snackbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class TestToolbar extends AppCompatActivity {
    Toolbar testToolbar;
    String dogstr = "Initital Dog Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        testToolbar = findViewById(R.id.testToolbar);
        setSupportActionBar(testToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_dog:
                Toast.makeText(this, dogstr, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_duck:
                View alertMiddle = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
                new AlertDialog.Builder(this)
                        .setView(alertMiddle)
                        .setPositiveButton("Set Message", (dialog, id) -> {
                            EditText alertET = alertMiddle.findViewById(R.id.alertET);
                            dogstr = alertET.getText().toString();
                            Snackbar.make(testToolbar, "Dog Message Changed", Snackbar.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()))
                        .create()
                        .show();
                break;
            case R.id.action_sheep:
                Snackbar.make(testToolbar, "Go Back?", Snackbar.LENGTH_SHORT)
                        .setAction("Going Back", sbBtn -> finish())
                        .show();
                break;
            case R.id.overflowChoice:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
