// package com.cine.back.review.service;

// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class ReviewService {

//     private final ReviewRepository reviewRepository;
//     private final MovieRepository movieRepository;
//     private final UserRepository userRepository;

//     public ReviewService(ReviewRepository reviewRepository, MovieRepository movieRepository,
//             UserRepository userRepository) {
//         this.reviewRepository = reviewRepository;
//         this.movieRepository = movieRepository;
//         this.userRepository = userRepository;
//     }

//     public void addReview(Review review) {
//         reviewRepository.save(review);
//     }

//     public List<Review> getReviewsByMovie(Long movieId) {
//         return reviewRepository.findByMovieId(movieId);
//     }

//     public List<Review> getReviewsByUser(Long userId) {
//         return reviewRepository.findByUserId(userId);
//     }
// }
