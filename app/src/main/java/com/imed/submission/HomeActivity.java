package com.imed.submission;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imed.submission.modal.UploadAssignmentModal;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class HomeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    private String courseNameGlobal;
    private String courseYearGlobal;
    private String subjectGlobal;
    private String topicGlobal;
    private String postKeyGlobal;

    private Toolbar toolbarHome;
    private Button uploadAssignmentBtn;
    private String uId;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView textView;

    private Uri filePath;

    //firebase data base

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private StorageReference mStorage;

    private FirebaseRecyclerAdapter assignmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbarHome = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbarHome);
        getSupportActionBar().setTitle("Class Room");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        uId = mUser.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference().child(uId).child("all_user_notes");

        mStorage = FirebaseStorage.getInstance().getReference();

        //Recycler View
        recyclerView=findViewById(R.id.recycler_view_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        uploadAssignmentBtn = findViewById(R.id.btn_go_to_upload_assignment);

        uploadAssignmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAssignment();
            }
        });

        Query query = mDataBase;

        System.err.println("uId-->" + uId);
        System.err.println("query-->" + query.toString());

        FirebaseRecyclerOptions<UploadAssignmentModal> options = new FirebaseRecyclerOptions.Builder<UploadAssignmentModal>()
                .setQuery(query,UploadAssignmentModal.class).build();

        assignmentAdapter = new FirebaseRecyclerAdapter<UploadAssignmentModal, AssignmentViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull AssignmentViewHolder holder, final int position, @NonNull final UploadAssignmentModal model) {

                //String dummyName="fundamentals of computer science";
                //String dummyMeta = "Java / Concepts / 2018-2019";
                holder.setCourseName(model.getTopic());
                String combinedMeta = model.getSubject() + " / " + model.getCourseName() + " / " + model.getCourseYear();
                holder.setCourseMeta(combinedMeta);

                System.out.println("holder.toString()-->" + holder.toString());

                holder.myView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        postKeyGlobal= getRef(position).getKey();
                        courseNameGlobal=model.getCourseName();
                        courseYearGlobal=model.getCourseYear();
                        subjectGlobal=model.getSubject();
                        topicGlobal=model.getTopic();

                        updateAssignment();
                        return false;
                    }
                });

                holder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*postKeyGlobal= getRef(position).getKey();
                        courseNameGlobal=model.getCourseName();
                        courseYearGlobal=model.getCourseYear();
                        subjectGlobal=model.getSubject();
                        topicGlobal=model.getTopic();

                        updateAssignment();*/
                        //download();
                    }
                });
            }

            @NonNull
            @Override
            public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view1 = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.assignment_list, viewGroup, false);

                System.out.println("holder.toString()--> Other************");

                return new AssignmentViewHolder(view1);
            }

        };

        System.err.println("assignmentAdapter ------------>" + assignmentAdapter.getItemCount());

        assignmentAdapter.startListening();
        recyclerView.setAdapter(assignmentAdapter);

        MainActivity.hand.sendEmptyMessage(0);
    }

    /*public void download(){

        mStorage = FirebaseStorage.getInstance().getReference();
        mStorage.child("assignments_docs/Topic1558189683930.jpg");

        mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {

                try {
                    String url = uri.toString();
                    System.out.println(url);
//                    downloadFile(HomeActivity.this, "Demo", ".pdf", DIRECTORY_DOWNLOADS,url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void downloadFile(Context context, String fileName, String fileExtension, String directory, String url) throws IOException {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,directory,fileName + fileExtension);

        downloadManager.enqueue(request);
    }*/


    @Override
    protected void onStart() {
        super.onStart();

        if (assignmentAdapter != null){
            assignmentAdapter.startListening();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (assignmentAdapter != null){
            assignmentAdapter.stopListening();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (assignmentAdapter != null){
            assignmentAdapter.startListening();
        }
    }

    public static class AssignmentViewHolder extends RecyclerView.ViewHolder{

        View myView;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            myView=itemView;
        }

        public void setCourseName(String courseName){
            TextView courseNameView =  myView.findViewById(R.id.list_course_name);
            courseNameView.setText(courseName);
        }

        public void setCourseMeta(String courseMeta){
            TextView courseMetaView = myView.findViewById(R.id.list_course_meta);
            courseMetaView.setText(courseMeta);
        }

    }

    public void uploadAssignment(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.upload_assignment,null);

        myDialog.setView(myView);

        final AlertDialog alertDialog = myDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        final EditText courseNameUpload = myView.findViewById(R.id.course_name_upload);
        final EditText courseYearUpload = myView.findViewById(R.id.course_year_upload);
        final EditText subjectUpload = myView.findViewById(R.id.subject_upload);
        final EditText topicUpload = myView.findViewById(R.id.topic_upload);
        imageView = myView.findViewById(R.id.view_chosen_file);
        textView = myView.findViewById(R.id.path_file_chosen);

        Button btnChoseFileToUpload = myView.findViewById(R.id.btn_chose_file_to_upload_assignment);

        Button btnCancelUpload = myView.findViewById(R.id.btn_cancel_upload_assignment);
        Button btnUploadAssignment = myView.findViewById(R.id.btn_upload_assignment);

        btnChoseFileToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnUploadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCourseNameUpload = courseNameUpload.getText().toString().trim();
                String strCourseYearUpload = courseYearUpload.getText().toString().trim();
                String strSubjectUpload = subjectUpload.getText().toString().trim();
                String strTopicUpload = topicUpload.getText().toString().trim();

                if(TextUtils.isEmpty(strCourseNameUpload) || strCourseNameUpload == null){
                    courseNameUpload.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(strCourseYearUpload) || strCourseYearUpload == null){
                    courseYearUpload.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(strSubjectUpload) || strSubjectUpload == null){
                    subjectUpload.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(strTopicUpload) || strTopicUpload == null){
                    topicUpload.setError("Required");
                    return;
                }

                String id = mDataBase.push().getKey();
                String currentDate = DateFormat.getDateInstance().format(new Date());

                UploadAssignmentModal assignmentModal = new UploadAssignmentModal(id,
                        strCourseNameUpload,  strCourseYearUpload, strSubjectUpload,
                        strTopicUpload, currentDate, uId
                );
                mDataBase.child(id).setValue(assignmentModal);
                Toast.makeText(getApplicationContext(),"Notes Uploaded.",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();

                uploadFileToStorage();
            }
        });

        btnCancelUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select and Image"), PICK_IMAGE_REQUEST);
    }

    private void uploadFileToStorage(){

        if (filePath != null){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            Date date = new Date();

            //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            StorageReference riversRef = mStorage.child("assignments_docs/Topic"+ date.getTime()+".jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //Uri downloadUrl = taskSnapshot. getDownloadUrl();
                            System.out.println("Inside Onsuccess Method");
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(),"File Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            System.out.println("Inside onFaiure Method");
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(),exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage((int) progress + " % Uploading..");
                    System.out.println("Inside OnProgressListener Method");
                }
            })
            ;
        } else {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.println(1,"Error",data.getData().toString());
        Log.println(1,"Error",data.getData().toString());
        Log.println(1,"Error",data.getData().toString());
        System.out.println("out side");
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath=data.getData();
            textView.setText(filePath.toString());


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

/*
            LayoutInflater inflater = LayoutInflater.from(this);
            View myView = inflater.inflate(R.layout.upload_assignment,null);
            Log.println(1,"Error",data.getData().toString());
            TextView selectedFilePath = myView.findViewById(R.id.path_file_chosen);
            System.out.println(selectedFilePath);
            System.out.println(filePath.toString());
            selectedFilePath.setText(filePath.toString());*/
        }

    }

    public void updateAssignment(){

        System.out.println("Debubing1");

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myViewUpdate =  inflater.inflate(R.layout.update_assignment,null);

        System.out.println("Debubing2");

        myDialog.setView(myViewUpdate);

        final AlertDialog dialog = myDialog.create();
        //dialog.setCancelable(false);
        dialog.show();

        System.out.println("Debubing3");

        final EditText courseNameUpdate = myViewUpdate.findViewById(R.id.course_name_update);
        final EditText courseYearUpdate = myViewUpdate.findViewById(R.id.course_year_update);
        final EditText subjectUpdate = myViewUpdate.findViewById(R.id.subject_update);
        final EditText topicUpdate = myViewUpdate.findViewById(R.id.topic_update);

        courseNameUpdate.setText(courseNameGlobal);
        courseNameUpdate.setSelection(courseNameGlobal.length());

        courseYearUpdate.setText(courseYearGlobal);
        courseYearUpdate.setSelection(courseYearGlobal.length());

        subjectUpdate.setText(subjectGlobal);
        subjectUpdate.setSelection(subjectGlobal.length());

        topicUpdate.setText(topicGlobal);
        topicUpdate.setSelection(topicGlobal.length());

        Button btnUpdateAssignment = myViewUpdate.findViewById(R.id.btn_update_assignment);
        Button btnDeleteAssignment = myViewUpdate.findViewById(R.id.btn_delete_assignment);

        System.out.println("Debubing4");

        btnUpdateAssignment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("Inside Update button onClickListener..");
                courseNameGlobal=courseNameUpdate.getText().toString().trim();
                courseYearGlobal=courseYearUpdate.getText().toString().trim();
                subjectGlobal=subjectUpdate.getText().toString().trim();
                topicGlobal=topicUpdate.getText().toString().trim();

                //String id = mDataBase.push().getKey();
                String currentDate = DateFormat.getDateInstance().format(new Date());

                UploadAssignmentModal updateData = new UploadAssignmentModal(postKeyGlobal,
                        courseNameGlobal, courseYearGlobal, subjectGlobal, topicGlobal,
                        currentDate,uId);
                System.out.println("postKeyGlobal---->" + postKeyGlobal);

                mDataBase.child(postKeyGlobal).setValue(updateData);

                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Assignment Updated Successfully.",Toast.LENGTH_LONG).show();

                return true;
            }
        });

        /*btnUpdateAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Inside Update button onClickListener..");
                courseNameGlobal=courseNameUpdate.getText().toString().trim();
                courseYearGlobal=courseYearUpdate.getText().toString().trim();
                subjectGlobal=subjectUpdate.getText().toString().trim();
                topicGlobal=topicUpdate.getText().toString().trim();

                //String id = mDataBase.push().getKey();
                String currentDate = DateFormat.getDateInstance().format(new Date());

                UploadAssignmentModal updateData = new UploadAssignmentModal(postKeyGlobal,
                        courseNameGlobal, courseYearGlobal, subjectGlobal, topicGlobal,
                        currentDate,uId);
                System.out.println("postKeyGlobal---->" + postKeyGlobal);

                mDataBase.child(postKeyGlobal).setValue(updateData);

                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Assignment Updated Successfully.",Toast.LENGTH_LONG).show();

            }
        });*/

        btnDeleteAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBase.child(postKeyGlobal).removeValue();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Assignment Deleted Successfully",Toast.LENGTH_LONG).show();

            }
        });


        System.out.println("Debubing6");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
