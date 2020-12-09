package com.example.android.dodjinateren;

import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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

import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private TextView textViewResteujSifru;

    private FirebaseAuth firebaseAuth;
   ImageView imageView;
   boolean sifra;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firebaseAuth = FirebaseAuth.getInstance();
        imageView=(ImageView)findViewById(R.id.pogledajl);
        sifra=true;

        if(firebaseAuth.getCurrentUser() != null){

            finish();

            Intent PokreniActivity=(new Intent(LoginActivity.this, HomeActivity.class));
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(PokreniActivity);
        }


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup  = (TextView) findViewById(R.id.textViewSignUp);
        textViewResteujSifru = (TextView) findViewById(R.id.textViewResetujSifru);
        progressDialog = new ProgressDialog(this);


        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        textViewResteujSifru.setOnClickListener(this);
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

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();



        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Molimo Vas unesite email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Molimo Vas unesite sifru",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Logovanje u toku...");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            Intent PokreniActivity=(new Intent(LoginActivity.this, HomeActivity.class));
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PokreniActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(PokreniActivity);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"Greska prilikom logovanja",Toast.LENGTH_LONG).show();
                            editTextEmail.setText("");
                            editTextPassword.setText("");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();
        }

        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if(view== textViewResteujSifru){

           final String email = editTextEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Unesite Vašu email adresu!", Toast.LENGTH_SHORT).show();
                return;
            }


            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Poslali smo mail za resetovanje sifre na: "+email, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Neuspelo slenje poruke za resetovanje sifr, molimo Vas pokušajte kasnije!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

}
