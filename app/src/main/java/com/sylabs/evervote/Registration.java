package com.sylabs.evervote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText mFName, mLName, mAge, mEmail, mPhone, mPassword;
    private int item;
    private Spinner genderSpinner;
    private Spinner areaSpinner;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        mFName = (EditText) findViewById(R.id.txtRegFname);
        mLName = (EditText) findViewById(R.id.txtRegLname);
        mAge = (EditText) findViewById(R.id.txtRegAge);
        mEmail = (EditText) findViewById(R.id.txtRegEmail);
        mPhone = (EditText) findViewById(R.id.txtRegPhone);
        mPassword = (EditText) findViewById(R.id.txtRegPassword);

        //Spinner code
        genderSpinner = (Spinner)findViewById(R.id.genderSpinner);
        areaSpinner = (Spinner)findViewById(R.id.areaSpinner);

        genderSpinner.setOnItemSelectedListener(this);
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Male");
        categoryList.add("Female");
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categoryList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dateAdapter);

        areaSpinner.setOnItemSelectedListener(this);
        List<String> areaCategoryList = new ArrayList<>();
        areaCategoryList.add("Colombo");
        areaCategoryList.add("Gampaha");
        areaCategoryList.add("Negombo");
        areaCategoryList.add("Kurunegala");
        areaCategoryList.add("Anuradhapura");
        ArrayAdapter<String> areaDateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,areaCategoryList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaDateAdapter);


        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(Registration.this, MainActivity.class);
                    startActivity(intent);
                    //finish(); window leaked error fixed
                    return;
                }
            }
        };

    }

    public void goToLogin(View view) {
        Intent intent = new Intent(Registration.this,Login.class);
        startActivity(intent);
    }

    public void Register(View view) {
        final String FName = mFName.getText().toString();
        final String LName = mLName.getText().toString();
        final String Age = mAge.getText().toString();
        final String Gender = genderSpinner.getSelectedItem().toString();
        final String Area = areaSpinner.getSelectedItem().toString();
        final String Phone = mPhone.getText().toString();
        final String Email = mEmail.getText().toString();
        final String Password = mPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(Registration.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                }else{
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    Map userInfo = new HashMap<>();
                    userInfo.put("FName", FName);
                    userInfo.put("LName", LName);
                    userInfo.put("Age", Age);
                    userInfo.put("Gender", Gender);
                    userInfo.put("Area", Area);
                    userInfo.put("Phone", Phone);
                    userInfo.put("Email", Email);
                    currentUserDb.updateChildren(userInfo);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getSelectedItemPosition();
        item = item+1;
        //Toast.makeText(Registration.this, "Spinner Item" + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}