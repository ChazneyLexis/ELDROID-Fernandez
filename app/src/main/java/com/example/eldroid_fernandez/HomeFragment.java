package com.example.eldroid_fernandez;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    ListView movies_lv;
    MovieAdapt movieAdapt;
    List<Movie> movieList;
    RelativeLayout emptyView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        movieList =  new ArrayList<>();
        emptyView = view.findViewById(R.id.empty_rl);
        movieAdapt = new MovieAdapt(movieList,getActivity());
        movies_lv = view.findViewById(R.id.movies_lv);
        movies_lv.setAdapter(movieAdapt);
        movies_lv.setEmptyView(emptyView);
        getMovies();
        return view;
    }

    public void getMovies(){
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
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setCancelable(false);
                            alert.setTitle("Error");
                            alert.setMessage(task.getException().getLocalizedMessage());
                            alert.setPositiveButton("Okay",null);
                            alert.show();
                        }
                        movieAdapt.notifyDataSetChanged();

                    }
                });
    }
}