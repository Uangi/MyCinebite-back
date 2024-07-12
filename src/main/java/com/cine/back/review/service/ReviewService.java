package com.cine.back.review.service;

import org.springframework.stereotype.Service;

import com.cine.back.movieList.repository.MovieDetailRepository;
import com.cine.back.review.entity.Review;
import com.cine.back.review.repository.ReviewRepository;
import com.cine.back.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByMovie(int movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    public List<Review> getReviewsByUser(String userId) {
        return reviewRepository.findByUserId(userId);
    }
}
