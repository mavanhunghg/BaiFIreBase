package com.example.baifirebase.models;

public class Ticket {
    private String id;
    private String userId;
    private String movieId;
    private String movieTitle;
    private String theaterName;
    private String showtime;
    private double price;

    public Ticket() {}

    public Ticket(String id, String userId, String movieId, String movieTitle, String theaterName, String showtime, double price) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.theaterName = theaterName;
        this.showtime = showtime;
        this.price = price;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getMovieId() { return movieId; }
    public String getMovieTitle() { return movieTitle; }
    public String getTheaterName() { return theaterName; }
    public String getShowtime() { return showtime; }
    public double getPrice() { return price; }
}