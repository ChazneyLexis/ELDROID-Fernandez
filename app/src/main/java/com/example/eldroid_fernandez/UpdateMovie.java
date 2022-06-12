package com.example.eldroid_fernandez;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateMovie extends AppCompatActivity {

    EditText movietitle, movie_year, movie_runtime, movie_lang, movie_rdate, movie_country;
    Button updateButton;
    ImageView updaImage;
    Uri uriImage;
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the updaImage from data
                Uri selectedImageUri = data.getData();
                uriImage = selectedImageUri;
                if (null != selectedImageUri) {
                    // update the preview updaImage in the layout
                    updaImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);

        movietitle = findViewById(R.id.movietitle);
        movie_rdate = (EditText) findViewById(R.id.movie_rdate);
        movie_year = findViewById(R.id.movie_year);
        movie_runtime = findViewById(R.id.movie_runtime);
        movie_lang = findViewById(R.id.movie_lang);
        movie_country = findViewById(R.id.movie_country);

        updateButton = findViewById(R.id.updateButton);
        updaImage = findViewById(R.id.updaImage);

        updaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
            }
        });



        if (getIntent().getExtras() != null) {
            Movie movie = (Movie) getIntent().getExtras().getParcelable("Movie");
            String myFormat = "MM/dd/yy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);

            Glide.with(getBaseContext()).load(movie.getImage()).into(updaImage);
            movietitle.setText(movie.getMovTit());
            movie_rdate.setText(dateFormat.format(movie.getMovReleaseD().toDate()));
            movie_year.setText(movie.getMovYear());
            movie_runtime.setText(movie.getMovRunT());
            movie_lang.setText(movie.getMovLanguage());
            movie_country.setText(movie.getMovCount());

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    ProgressDialog dialog = new ProgressDialog(UpdateMovie.this);
                    dialog.setMessage("Updating game, please wait...");
                    dialog.setCancelable(false);
                    dialog.show();

                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("updaImage/jpeg")
                            .build();
                    StorageReference storageRef = storage.getReference().child("updaImage/" + mUser.getUid() + "/" + dateFormat.format(new Date()));
// Upload file and metadata to the path 'images/mountains.jpg'
                    UploadTask uploadTask = storageRef.putFile(uriImage, metadata);

// Listen for state changes, errors, and completion of the upload.
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
                                movietitle.setText(movie.getMovTit());
                                movie_rdate.setText(dateFormat.format(movie.getMovReleaseD().toDate()));
                                movie_year.setText(movie.getMovYear());
                                movie_runtime.setText(movie.getMovRunT());
                                movie_lang.setText(movie.getMovLanguage());
                                movie_country.setText(movie.getMovCount());

                                movie.setMovTit(movietitle.getText().toString());
                                movie.setMovYear(movie_year.getText().toString());
                                movie.setMovRunT(movie_runtime.getText().toString());
                                movie.setMovCount(movie_country.getText().toString());
                                movie.setMovRunT(movie_runtime.getText().toString());
                                movie.setMovLanguage(movie_lang.getText().toString());
                                movie.setImage(downloadUri.toString());

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Movie").document(movie.getMovID()).set(movie).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UpdateMovie.this, "Movie successfully updated!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(UpdateMovie.this, MainActivity.class));
                                        } else {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(UpdateMovie.this);
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
                                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                alert.setCancelable(false);
                                alert.setTitle("Error Uploading movie Image!");
                                alert.setMessage(task.getException().getLocalizedMessage());
                                alert.setPositiveButton("Okay", null);
                                alert.show();
                                dialog.dismiss();
                                // ...
                            }
                        }
                    });


                }
            });


        }
    }
//
//    @GlideModule
//    public class MyAppGlideModule extends AppGlideModule {
//        @Override
//        public void registerComponents(Context context, Registry registry) {
//            // Register FirebaseImageLoader to handle StorageReference
//            registry.append(StorageReference.class, InputStream.class,
//                    new FirebaseImageLoader.Factory());
//        }
//    }
}