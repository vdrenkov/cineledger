package bg.vdrenkov.cineledger.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
@Data
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
}



