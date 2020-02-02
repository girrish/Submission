package com.imed.submission;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imed.submission.modal.UploadAssignmentModal;
import com.imed.submission.modal.UserModel;

import java.text.DateFormat;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {

    private String uId;
    private EditText firstNameSignUp;
    private EditText lastNameSignUp;
    private EditText emailSignUp;
    private EditText mobilNumberSignUp;
    private EditText passwordSignUp;
    private EditText courseName;
    private EditText courseYear;
    private Button btnCreateUser;
    private Button btnGoToSignIn;

    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;

    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        firstNameSignUp = findViewById(R.id.first_name_sign_up);
        lastNameSignUp = findViewById(R.id.last_name_sign_up);
        emailSignUp = findViewById(R.id.email_sign_up);
        mobilNumberSignUp = findViewById(R.id.mobile_number_sign_up);
        passwordSignUp = findViewById(R.id.password_sign_up);
        courseName = findViewById(R.id.course_name_sign_up);
        courseYear = findViewById(R.id.course_year_sign_up);

        btnCreateUser = findViewById(R.id.btn_create_user);
        btnGoToSignIn = findViewById(R.id.btn_go_to_sign_in);




        btnCreateUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //String strFirstNameSignUp = firstNameSignUp.getText().toString().trim();
                //String strLastNameSignUp = lastNameSignUp.getText().toString().trim();
                String strEmailSignUp = emailSignUp.getText().toString().trim();
                //String strMobileSignUp = mobilNumberSignUp.getText().toString().trim();
                String strPasswordSignUp = passwordSignUp.getText().toString().trim();

                /*if(TextUtils.isEmpty(strFirstNameSignUp) || strFirstNameSignUp == null){
                    firstNameSignUp.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(strLastNameSignUp) || strLastNameSignUp == null){
                    lastNameSignUp.setError("Required");
                    return;
                }*/
                if(TextUtils.isEmpty(strEmailSignUp) || strEmailSignUp == null){
                    emailSignUp.setError("Required");
                    return;
                }
                /*if (TextUtils.isEmpty(strMobileSignUp) || strMobileSignUp == null){
                    mobilNumberSignUp.setError("Required");
                    return;
                }*/
                if (TextUtils.isEmpty(strPasswordSignUp) || strPasswordSignUp == null){
                    passwordSignUp.setError("Required");
                    return;
                }

                mDialog.setMessage("Processing..");
                mDialog.show();
                mAuth.createUserWithEmailAndPassword(strEmailSignUp,strPasswordSignUp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mDialog.show();

                            uId = task.getResult().getUser().getUid();
                            System.out.println(uId);

                            mDataBase = FirebaseDatabase.getInstance().getReference().child(uId).child("user_details");
                            String currentDate = DateFormat.getDateInstance().format(new Date());

                            //FirebaseUser mUser = mAuth.getCurrentUser();
                            //uId = mUser.getUid();

                            UserModel userModel = new UserModel(firstNameSignUp.toString(),lastNameSignUp.toString(),courseName.toString(),courseYear.toString(),false,true,false,uId,mobilNumberSignUp.toString(),null,emailSignUp.toString(),currentDate,uId,null,null
                            );

                            mDataBase.setValue(userModel);


                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            Toast.makeText(getApplicationContext(),"User Created Successfully. Please Login.",Toast.LENGTH_LONG).show();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Error Occurred, User not created.",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        btnGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }


}
