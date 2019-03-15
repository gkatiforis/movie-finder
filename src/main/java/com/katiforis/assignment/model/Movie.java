package com.katiforis.assignment.model;

public class Movie {
    String title;
    int movieLength;
    String genre;

    public Movie(String title, String movieLength, String genre) {
        this.title = title;
        this.movieLength = Integer.valueOf(movieLength);
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return title;
    }
}
