package bg.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private double rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @ManyToOne
    private Movie movie;
    @ManyToOne
    private Cinema cinema;
    @ManyToOne
    private User user;

    public Review(double rating, String reviewText, LocalDate dateModified, Movie movie, User user) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.dateModified = dateModified;
        this.movie = movie;
        this.user = user;
    }

    public Review(double rating, String reviewText, LocalDate dateModified, Cinema cinema, User user) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.dateModified = dateModified;
        this.cinema = cinema;
        this.user = user;
    }
}



