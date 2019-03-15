package com.katiforis.assignment.model;

import java.time.LocalDate;

public class LogEntry {
    String name;
    LocalDate date;
    int  minutesWatched;
    Movie movie;

    public LogEntry(String name, String year, String days , String month, String title, String movieLength, String minutesWatched, String genre) {
        this.name = name.replace(".txt", "");
        this.date = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(days));
        this.minutesWatched = Integer.valueOf(minutesWatched);
        movie = new Movie(title, movieLength, genre);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getMinutesWatched() {
        return minutesWatched;
    }

    public void setMinutesWatched(int minutesWatched) {
        this.minutesWatched = minutesWatched;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", minutesWatched=" + minutesWatched +
                ", movie=" + movie +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntry logEntry = (LogEntry) o;

        if (minutesWatched != logEntry.minutesWatched) return false;
        if (name != null ? !name.equals(logEntry.name) : logEntry.name != null) return false;
        if (date != null ? !date.equals(logEntry.date) : logEntry.date != null) return false;
        return movie != null ? movie.equals(logEntry.movie) : logEntry.movie == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + minutesWatched;
        result = 31 * result + (movie != null ? movie.hashCode() : 0);
        return result;
    }
}
