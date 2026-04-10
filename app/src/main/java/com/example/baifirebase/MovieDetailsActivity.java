package com.example.baifirebase;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.baifirebase.models.Movie;
import com.example.baifirebase.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView ivPoster;
    private TextView tvTitle, tvGenre, tvDescription;
    private Spinner spinnerShowtimes;
    private Button btnConfirm;
    private Movie movie;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private List<String> showtimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ivPoster = findViewById(R.id.ivDetailPoster);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvGenre = findViewById(R.id.tvDetailGenre);
        tvDescription = findViewById(R.id.tvDetailDescription);
        spinnerShowtimes = findViewById(R.id.spinnerShowtimes);
        btnConfirm = findViewById(R.id.btnConfirmBooking);

        movie = (Movie) getIntent().getSerializableExtra("movie");

        if (movie != null) {
            tvTitle.setText(movie.getTitle());
            tvGenre.setText(movie.getGenre());
            tvDescription.setText(movie.getDescription());
            Glide.with(this).load(movie.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(ivPoster);
        }

        showtimes.add("10:00 AM - Theater A");
        showtimes.add("01:30 PM - Theater B");
        showtimes.add("04:45 PM - Theater A");
        showtimes.add("08:00 PM - Theater C");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, showtimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShowtimes.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> bookTicket());
    }

    private void bookTicket() {
        if (mAuth.getCurrentUser() == null) return;

        String ticketId = UUID.randomUUID().toString();
        String userId = mAuth.getCurrentUser().getUid();
        String selectedShowtime = spinnerShowtimes.getSelectedItem().toString();

        Ticket ticket = new Ticket(
                ticketId,
                userId,
                movie.getId(),
                movie.getTitle(),
                selectedShowtime.split(" - ")[1],
                selectedShowtime.split(" - ")[0],
                10.0
        );

        mDatabase.child("tickets").child(ticketId).setValue(ticket)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MovieDetailsActivity.this, "Ticket Booked Successfully!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MovieDetailsActivity.this, "Booking Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}