package dev.vdrenkov.cineledger.models.requests;

public record ReviewRequest(
    double rating,
    String reviewText
) {}
