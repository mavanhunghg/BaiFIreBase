package com.example.baifirebase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baifirebase.adapters.MovieAdapter;
import com.example.baifirebase.models.Movie;
import com.example.baifirebase.models.Theater;
import com.example.baifirebase.models.Showtime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String DB_URL = "https://movieticketapp-2cd47-default-rtdb.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        }

        mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();

        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);
        rvMovies.setAdapter(movieAdapter);

        loadMovies();
        askNotificationPermission();
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void loadMovies() {
        mDatabase.child("movies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movieList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Movie movie = postSnapshot.getValue(Movie.class);
                        if (movie != null) movieList.add(movie);
                    }
                    movieAdapter.notifyDataSetChanged();
                    
                    // Nếu số lượng phim ít hơn 4, tự động chạy seedData để bổ sung
                    if (snapshot.getChildrenCount() < 4) {
                        seedData();
                    }
                } else {
                    seedData();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void seedData() {
        Movie movie1 = new Movie("1", "Inception", "Kẻ đánh cắp giấc mơ.", "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg", "Sci-Fi", 148);
        mDatabase.child("movies").child("1").setValue(movie1);

        Movie movie2 = new Movie("2", "The Dark Knight", "Hiệp sĩ bóng đêm.", "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg", "Action", 152);
        mDatabase.child("movies").child("2").setValue(movie2);

        Movie movie3 = new Movie("3", "Interstellar", "Hố đen tử thần.", "https://m.media-amazon.com/images/M/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg", "Sci-Fi", 169);
        mDatabase.child("movies").child("3").setValue(movie3);

        Movie movie4 = new Movie("4", "Spider-Man: No Way Home", "Người Nhện: Không còn đường về.", "https://m.media-amazon.com/images/M/MV5BZWMyYzFjYTYtNTRjYi00OGExLWE2YzgtOGRmYjAxZTU3NzBiXkEyXkFqcGdeQXVyMzQ0MzA0NTM@._V1_.jpg", "Action", 148);
        mDatabase.child("movies").child("4").setValue(movie4);

        Theater t1 = new Theater("t1", "CGV Vincom", "Hà Nội");
        mDatabase.child("theaters").child("t1").setValue(t1);

        Showtime s1 = new Showtime("s1", "1", "t1", "19:00", 90000);
        mDatabase.child("showtimes").child("s1").setValue(s1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}