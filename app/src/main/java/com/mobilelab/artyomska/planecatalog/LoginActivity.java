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
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.model.User;
import com.mobilelab.artyomska.planecatalog.repository.PlaneRepository;

public class LoginActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private Button loginButton;
    private Button registerButton;
    private TextView attemptsText;
    private int counter = 5;
    private LoginController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.userInput);
        password = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        attemptsText = findViewById(R.id.attemptsText);
        registerButton = findViewById(R.id.registerBut);

        attemptsText.setText("No of attempts remaining: 5");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validate(name.getText().toString(), password.getText().toString());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        /*
        PlaneRepository repo = new PlaneRepository(getApplicationContext());
        repo.insertPlane(new Plane("BF-109","Daimler","Messerschmit","Germany",1939));
        repo.insertPlane(new Plane("Spitfire","Rolls-Royce","Supermarine","UK",1940));
        repo.insertPlane(new Plane("P-51","Packard","North-American","USA",1942));
        repo.insertPlane(new Plane("LA-7","Klimov","Lavochkin","USSR",1943));
        repo.insertPlane(new Plane("A6M2","Mitsubishi","Mitsubishi","Japan",1941));
        */

        controller = new LoginController(getApplicationContext());
    }

    private void validate(String userName, String userPassword)
    {
        User tryUser = new User(userName,userPassword);
        if(controller.isAuthenticated(tryUser))
        {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Wrong username or password");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            counter--;
            attemptsText.setVisibility(View.VISIBLE);
            attemptsText.setText("No of attempts remaining: " + String.valueOf(counter));
            if(counter == 0)
            {
                loginButton.setEnabled(false);

                alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Login button locked. Too many tries");
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

