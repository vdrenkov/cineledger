package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column private double rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @ManyToOne private Movie movie;
    @ManyToOne private Cinema cinema;
    @ManyToOne private User user;

    public Review(
            double rating, String reviewText, LocalDate dateModified, Movie movie, User user) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.dateModified = dateModified;
        this.movie = movie;
        this.user = user;
    }

    public Review(
            double rating, String reviewText, LocalDate dateModified, Cinema cinema, User user) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.dateModified = dateModified;
        this.cinema = cinema;
        this.user = user;
    }

    public Review() {}

    public Review(
            final int id,
            final double rating,
            final String reviewText,
            final LocalDate dateModified,
            final Movie movie,
            final Cinema cinema,
            final User user) {
        this.id = id;
        this.rating = rating;
        this.reviewText = reviewText;
        this.dateModified = dateModified;
        this.movie = movie;
        this.cinema = cinema;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(final String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(final LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(final Movie movie) {
        this.movie = movie;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(final Cinema cinema) {
        this.cinema = cinema;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Review other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Review.class.hashCode();
    }
}
