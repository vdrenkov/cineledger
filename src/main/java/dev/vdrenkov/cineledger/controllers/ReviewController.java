package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.models.entities.Review;
import dev.vdrenkov.cineledger.models.requests.ReviewRequest;
import dev.vdrenkov.cineledger.services.ReviewService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(URIConstants.CINEMAS_ID_REVIEWS_PATH)
    public ResponseEntity<Void> addCinemaReview(@RequestBody @Valid ReviewRequest request, @PathVariable int id) {
        Review review = this.reviewService.addCinemaReview(request, id);
        log.info("A request for a cinema review to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.REVIEWS_ID_PATH)
            .buildAndExpand(review.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping(URIConstants.MOVIES_ID_REVIEWS_PATH)
    public ResponseEntity<Void> addMovieReview(@RequestBody @Valid ReviewRequest request, @PathVariable int id) {
        Review review = this.reviewService.addMovieReview(request, id);
        log.info("A request for a movie review to be added has been submitted");

        URI location = UriComponentsBuilder
            .fromUriString(URIConstants.REVIEWS_ID_PATH)
            .buildAndExpand(review.getId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = URIConstants.MOVIES_ID_REVIEWS_PATH)
    public ResponseEntity<List<ReviewDto>> getReviewsByMovieId(@PathVariable int id) {
        List<ReviewDto> reviewDtos = this.reviewService.getReviewsByMovieId(id);
        log.info("All reviews by movie title were requested from the database");

        return ResponseEntity.ok(reviewDtos);
    }

    @GetMapping(URIConstants.CINEMAS_ID_REVIEWS_PATH)
    public ResponseEntity<List<ReviewDto>> getReviewsByCinemaId(@PathVariable int id) {
        List<ReviewDto> reviewDtos = this.reviewService.getReviewsByCinemaId(id);
        log.info("All reviews by cinema id were requested from the database");

        return ResponseEntity.ok(reviewDtos);
    }

    @GetMapping(URIConstants.USERS_ID_MOVIES_REVIEWS_PATH)
    public ResponseEntity<List<ReviewDto>> getMovieReviewsByUserId(@PathVariable int id) {
        List<ReviewDto> reviewDtos = this.reviewService.getMovieReviewsByUserId(id);
        log.info("All movie reviews by user id were requested from the database");

        return ResponseEntity.ok(reviewDtos);
    }

    @GetMapping(URIConstants.USERS_ID_CINEMAS_REVIEWS_PATH)
    public ResponseEntity<List<ReviewDto>> getCinemaReviewsByUserId(@PathVariable int id) {
        List<ReviewDto> reviewDtos = this.reviewService.getCinemaReviewsByUserId(id);
        log.info("All cinema reviews by user id were requested from the database");

        return ResponseEntity.ok(reviewDtos);
    }

    @PutMapping(URIConstants.REVIEWS_ID_PATH)
    public ResponseEntity<ReviewDto> updateReview(@RequestBody @Valid ReviewRequest request, @PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        ReviewDto reviewDto = this.reviewService.updateReview(request, id);
        log.info(String.format("Review with id %d was updated", id));

        return returnOld ? ResponseEntity.ok(reviewDto) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(URIConstants.REVIEWS_ID_PATH)
    public ResponseEntity<ReviewDto> deleteReview(@PathVariable int id,
        @RequestParam(required = false) boolean returnOld) {

        ReviewDto reviewDto = this.reviewService.deleteReview(id);
        log.info(String.format("Review with id %d was deleted", id));

        return returnOld ? ResponseEntity.ok(reviewDto) : ResponseEntity.noContent().build();
    }
}



