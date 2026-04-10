package com.example.baifirebase.models;

public class Showtime {
    private String id;
    private String movieId;
    private String theaterId;
    private String time;
    private double price;

    public Showtime() {}

    public Showtime(String id, String movieId, String theaterId, String time, double price) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.time = time;
        this.price = price;
    }

    public String getId() { return id; }
    public String getMovieId() { return movieId; }
    public String getTheaterId() { return theaterId; }
    public String getTime() { return time; }
    public double getPrice() { return price; }
}