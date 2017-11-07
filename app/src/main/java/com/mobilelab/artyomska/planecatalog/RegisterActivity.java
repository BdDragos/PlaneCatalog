package com.mobilelab.artyomska.planecatalog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilelab.artyomska.planecatalog.controller.LoginController;
import com.mobilelab.artyomska.planecatalog.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText userText;
    private EditText passText;
    private EditText repeatPassText;
    private Button registerBut;
    private Button clearBut;
    private LoginController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userText = (EditText)findViewById(R.id.userText);
        passText = (EditText)findViewById(R.id.passText);
        repeatPassText = (EditText)findViewById(R.id.repeatPassText);
        registerBut = (Button)findViewById(R.id.registerBut);
        clearBut = (Button)findViewById(R.id.clearBut);

        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                register(userText.getText().toString(), passText.getText().toString(),repeatPassText.getText().toString());
            }
        });
        clearBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                userText.setText("");
                passText.setText("");
                repeatPassText.setText("");

            }
        });

        controller = new LoginController(getApplicationContext());
    }

    private void register(String userName, String userPassword, String repeatUserPassword)
    {
        if (userName == null || userPassword == null || userName.length() < 4 || userPassword.length() < 4)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Username or password are empty/length is less than 4");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if (userPassword.compareTo(repeatUserPassword) != 0)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Passwords aren't the same.Please retype them");
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
            User tryUser = new User(userName, userPassword);
            if (controller.isRegistered(tryUser))
            {
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("User was registered");
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
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                alertDialog.setTitle("Can't register");
                alertDialog.setMessage("User already exists");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }
}
