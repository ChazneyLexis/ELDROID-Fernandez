package com.example.eldroid_fernandez;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMovie extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Button saveButton;
    EditText movietitle, movie_year, movie_runtime, movie_lang, movie_rdate, movie_country;
    final Calendar myCalendar = Calendar.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    ImageView image;
    Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        movietitle = findViewById(R.id.movietitle);
        movie_rdate = (EditText) findViewById(R.id.movie_rdate);
        movie_year = findViewById(R.id.movie_year);
        movie_runtime = findViewById(R.id.movie_runtime);
        movie_lang = findViewById(R.id.movie_lang);
        movie_country = findViewById(R.id.movie_country);
        image = findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        movie_rdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMovie.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movTitle = movietitle.getText().toString();
                Timestamp movRDate = new Timestamp(myCalendar.getTime());
                String movYear = movie_year.getText().toString();
                String movRunT = movie_runtime.getText().toString();
                String movLang = movie_lang.getText().toString();
                String movCount = movie_country.getText().toString();

                addData(movTitle, movRDate, movYear, movRunT, movLang, movCount);
            }
        });
    }

    private void addData(String Title, Timestamp ReleaseDate, String Year, String RunTime, String Language, String Country) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Adding game, please wait...");
        dialog.setCancelable(false);
        dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyhhmm", Locale.US);
        String id = mUser.getUid() + dateFormat.format(new Date());
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();
        StorageReference storageRef = storage.getReference().child("image/"+mUser.getUid()+"/"+dateFormat.format(new Date()));
        UploadTask uploadTask = storageRef.putFile(uriImage, metadata);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Movie movie = new Movie(id, Title, ReleaseDate, Year, RunTime, Language, Country,downloadUri.toString());
                    db.collection("Games").document(id).set(movie).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddMovie.this, "Game successfully added!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),ViewAll.class));
                            }
                            else {
                                dialog.dismiss();
                                AlertDialog.Builder alert = new AlertDialog.Builder(AddMovie.this);
                                alert.setCancelable(false);
                                alert.setTitle("Error!");
                                alert.setMessage(task.getException().getLocalizedMessage());
                                alert.setPositiveButton("Okay", null);
                                alert.show();
                            }
                        }
                    });
                } else {
                    // Handle failures
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddMovie.this);
                    alert.setCancelable(false);
                    alert.setTitle("Error Uploading image!");
                    alert.setMessage(task.getException().getLocalizedMessage());
                    alert.setPositiveButton("Okay", null);
                    alert.show();
                    dialog.dismiss();
                    // ...
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                uriImage = selectedImageUri;
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    image.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void updateLabel() {
    }
}