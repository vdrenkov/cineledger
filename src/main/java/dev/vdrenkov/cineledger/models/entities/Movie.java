package dev.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column(name = "average_rating")
    private double averageRating;

    @Column
    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column
    private Duration runtime;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Movie(String title, String description, LocalDate releaseDate, Duration runtime, Category category) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.category = category;
    }

    public Movie(int id, String title, String description, LocalDate releaseDate, Duration runtime, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.category = category;
    }

    public Movie() {}

    public Movie(final int id, final String title, final double averageRating, final String description, final LocalDate releaseDate, final Duration runtime, final Category category) {
        this.id = id;
        this.title = title;
        this.averageRating = averageRating;
        this.description = description;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(final double averageRating) {
        this.averageRating = averageRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Duration getRuntime() {
        return runtime;
    }

    public void setRuntime(final Duration runtime) {
        this.runtime = runtime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof Movie other && id != 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Movie.class.hashCode();
    }
}
