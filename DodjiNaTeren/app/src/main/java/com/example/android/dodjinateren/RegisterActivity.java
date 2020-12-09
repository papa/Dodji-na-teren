package com.example.android.dodjinateren;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    static String usern;
    private TextView textViewSignin;
    private EditText editTextUsername;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mojabaza;
    ImageView imageView;
    boolean sifra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        firebaseAuth = FirebaseAuth.getInstance();
       imageView=(ImageView)findViewById(R.id.pogledaj);
       sifra=true;
        //if  getCurrentUser does not returns null
        if (firebaseAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            Intent PokreniActivity=(new Intent(RegisterActivity.this, HomeActivity.class));
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(PokreniActivity);
        }
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        editTextUsername=(EditText)findViewById(R.id.editTextUsername);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        mojabaza= FirebaseDatabase.getInstance().getReference("Korisnici");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sifra)
                {
                    sifra=false;
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    sifra=true;
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

    }
    private void registerUser(){

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password  = editTextPassword.getText().toString().trim();
        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Molimo Vas unesite email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Molimo Vas unesite sifru",Toast.LENGTH_LONG).show();
            return;
        }
        if(editTextUsername.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Molimo Vas unesite username",Toast.LENGTH_LONG).show();
            return;
        }
        if(editTextPassword.getText().length()<6){
            Toast.makeText(this,"Sifra mora da ima najmanje 6 karaktera!",Toast.LENGTH_LONG).show();
            editTextPassword.setText("");
            return;
        }

        progressDialog.setMessage("Registrovanje u toku,molimo sacekajte...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            String id;
                            try {
                                id = firebaseAuth.getCurrentUser().getUid();
                            }
                            catch (NullPointerException e)
                            {
                                e.printStackTrace();
                                id=mojabaza.push().getKey();
                            }
                            Korisnik k=new Korisnik(id,email,editTextUsername.getText().toString(),"nije","","");
                            usern=k.getUsername();
                            mojabaza.child(id).setValue(k);
                            Intent PokreniActivity=(new Intent(RegisterActivity.this, HomeActivity.class));
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(PokreniActivity);

                        }else{
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Greska prilikom registrovanja",Toast.LENGTH_LONG).show();
                            editTextPassword.setText("");
                            editTextEmail.setText("");
                            editTextUsername.setText("");
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }
        if(view == textViewSignin){
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        }

    }
}
