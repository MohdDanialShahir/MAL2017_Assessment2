package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, phone, email;
    Spinner roleSpinner;
    Button btnRegister;
    TextView tvLogin;
    DBHelper DB;

    // Password Validation Pattern
    // At least 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special char
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         // at least 1 digit
                    "(?=.*[a-z])" +         // at least 1 lower case letter
                    "(?=.*[A-Z])" +         // at least 1 upper case letter
                    "(?=.*[@#$%^&+=!])" +   // at least 1 special character
                    "(?=\\S+$)" +           // no white spaces
                    ".{8,}" +               // at least 8 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.etRegUsername);
        password = findViewById(R.id.etRegPassword);
        phone = findViewById(R.id.etRegPhone);
        email = findViewById(R.id.etRegEmail);
        roleSpinner = findViewById(R.id.spinnerRole);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvBackToLogin);
        DB = new DBHelper(this);

        String[] roles = {"Guest", "Staff"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        roleSpinner.setAdapter(adapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String ph = phone.getText().toString();
                String em = email.getText().toString();
                String selectedRole = roleSpinner.getSelectedItem().toString();

                int roleInt = selectedRole.equals("Staff") ? 1 : 0;

                if(user.equals("") || pass.equals("") || ph.equals("") || em.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(pass)) {
                    // Password Validation Failed
                    Toast.makeText(RegisterActivity.this, "Password too weak!\nRequires 8+ chars, 1 Upper, 1 Lower, 1 Digit, 1 Special (@#$%^&+=!)", Toast.LENGTH_LONG).show();
                } else {
                    Boolean checkuser = DB.checkUsername(user);
                    if(!checkuser) {
                        Boolean insert = DB.insertUser(user, pass, roleInt, ph, em);
                        if(insert) {
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
