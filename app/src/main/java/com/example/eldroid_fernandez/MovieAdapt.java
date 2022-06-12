package com.example.eldroid_fernandez;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MovieAdapt extends BaseAdapter {

    List<Movie> movieList;
    Context context;


    public MovieAdapt(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup vGroup) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        view = LayoutInflater.from(context).inflate(R.layout.movielistitem, null);
        ImageView image;
        Button editIB, deleteIB;
        TextView movTitle, movYear, movCount, movReleasedD, movRunT, movLanguage;
        editIB = view.findViewById(R.id.editIB);
        deleteIB = view.findViewById(R.id.deleteIB);
        movTitle = view.findViewById(R.id.movTitle);
        movYear = view.findViewById(R.id.movYear);
        movCount = view.findViewById(R.id.movCount);
        movReleasedD = view.findViewById(R.id.movReleasedD);
        movRunT = view.findViewById(R.id.movRunT);
        movLanguage = view.findViewById(R.id.movLanguage);
        image = view.findViewById(R.id.image);
        Glide.with(context).load(movieList.get(i).getImage()).into(image);
        movTitle.setText("Title: " + movieList.get(i).getMovTit());
        movReleasedD.setText("Released Date: " + movieList.get(i)!=null?dateFormat.format(movieList.get(i).getMovReleaseD().toDate()):"");
        movYear.setText("Year: " + movieList.get(i).getMovYear());
        movCount.setText("Country: " + movieList.get(i).getMovCount());
        movRunT.setText("Running Time: " + movieList.get(i).getMovRunT());
        movLanguage.setText("Language: " + movieList.get(i).getMovLanguage());

        editIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateMovie.class);
                intent.putExtra("Movie", (Parcelable) movieList.get(i));
                context.startActivity(intent);
            }
        });

        deleteIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setCancelable(false);
                alert.setTitle("Delete movie record");
                alert.setMessage("Are you sure to delete this movie record?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int o) {

                        db.collection("Movie").document(movieList.get(i).getMovID())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        movieList.remove(i);
                                        notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
            }
        });

        return view;
    }
}
