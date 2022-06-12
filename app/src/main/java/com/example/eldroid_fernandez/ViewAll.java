package com.example.eldroid_fernandez;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAll extends AppCompatActivity {

    MovieAdapt listAdapter;
    List<Movie> movieList;
    ListView movieListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        movieList = new ArrayList<>();
        listAdapter = new MovieAdapt(movieList, this);
        movieListView = findViewById(R.id.movieListView);
        movieListView.setAdapter(listAdapter);
        getMovie();
    }

    private void getMovie() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Movie")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                movieList.add(document.toObject(Movie.class));
                            }
                        } else {

                        }
                        listAdapter.notifyDataSetChanged();
                    }
                });
    }
}