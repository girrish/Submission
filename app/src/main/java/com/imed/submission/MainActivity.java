package com.imed.submission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private boolean isSignIn = false;
    private EditText emailSignIn ;
    private EditText passwordSignIn ;

    private Button btnSignIn;
    private Button btnGoToSignUp;

    private FirebaseAuth mAuthSignIn;
    private ProgressDialog mDialogSignIn;

    public static Handler hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthSignIn = FirebaseAuth.getInstance();
        mDialogSignIn = new ProgressDialog(this);

        if(mAuthSignIn.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        emailSignIn  = findViewById(R.id.email_sign_in);
        passwordSignIn  = findViewById(R.id.password_sign_in);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnGoToSignUp = findViewById(R.id.btn_go_to_sign_up);

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //mDialogSignIn.setMessage("Processing..");
                //mDialogSignIn.show();
                boolean isValid = true;

                String strEmailSignIn = emailSignIn.getText().toString().trim();
                String strPasswordSignIn  = passwordSignIn.getText().toString().trim();

                if(TextUtils.isEmpty(strEmailSignIn ) || strEmailSignIn  == null){
                    emailSignIn .setError("Required");
                    //return;
                    isValid = false;
                }
                if (TextUtils.isEmpty(strPasswordSignIn ) || strPasswordSignIn  == null){
                    passwordSignIn .setError("Required");
                    //return;
                    isValid = false;
                }

                if (isValid){
                    mDialogSignIn.setMessage("Processing..");
                    mDialogSignIn.show();
                    mAuthSignIn.signInWithEmailAndPassword(strEmailSignIn,strPasswordSignIn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                isSignIn = true;
                                mDialogSignIn.dismiss();

                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                finish();
                                Toast.makeText(getApplicationContext(),"SignIn Success.",Toast.LENGTH_LONG).show();
                            } else {
                                mDialogSignIn.dismiss();
                                Toast.makeText(getApplicationContext(),"Invalid Credentials.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        //for sign up page
        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });

        hand = new Handler ()
        {
            public void handleMessage(Message message)
            {
                super.handleMessage(message);
                switch(message.what)
                {
                    case 0:
                        finish();
                        break;
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(isSignIn){

        }

    }
}
