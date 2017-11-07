package com.mobilelab.artyomska.planecatalog;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobilelab.artyomska.planecatalog.controller.LoginController;
import com.mobilelab.artyomska.planecatalog.controller.MainController;

public class InsertActivity extends AppCompatActivity {

    private Button insertButton;
    private Button clearButton;
    private EditText textName;
    private EditText textEngine;
    private EditText textProducer;
    private EditText textCountry;
    private EditText textYear;
    private EditText textWiki;
    private MainController controller;
    private MainActivityTab1 frag1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        textName = findViewById(R.id.textName);
        textEngine = findViewById(R.id.textEngine);
        textProducer = findViewById(R.id.textProd);
        textCountry = findViewById(R.id.textCountry);
        textYear = findViewById(R.id.textYear);
        textWiki = findViewById(R.id.textProd);

        insertButton = findViewById(R.id.buttonInsertPlane);
        clearButton = findViewById(R.id.buttonClearPlane);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                insertion(textName.getText().toString(), textEngine.getText().toString(),textProducer.getText().toString(),
                        textCountry.getText().toString(),textYear.getText().toString(),textWiki.getText().toString());
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });

        controller = new MainController(getApplicationContext());
    }

    private void insertion(String name, String engine, String producer, String country, String year, String wiki)
    {
        if (name.equals("") || engine.equals("") || producer.equals("") || country.equals("") || year.equals(""))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(InsertActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please complete all the fields(wiki is optional)");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else
        {
            try
            {
                double d = Double.parseDouble(year);
            }
            catch(NumberFormatException nfe)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(InsertActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Year must be a number");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            boolean isInserted = controller.addNewPlane(name,engine,producer,country,Integer.parseInt(year),wiki);
            if (!isInserted)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(InsertActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Plane couldn't be inserted. Username already exists.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(InsertActivity.this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("A new plane was inserted");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent();
                                intent.putExtra("id","value");
                                setResult(RESULT_OK, intent);
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();
            }
        }
    }
}
