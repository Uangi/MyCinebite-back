package com.cine.back.review.controller;

import com.cine.back.movieList.service.MovieDetailService;
import com.cine.back.review.entity.Review;
import com.cine.back.review.repository.ReviewRepository;
import com.cine.back.review.service.ReviewService;
import com.cine.back.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final MovieDetailService movieService;
    private final ReviewRepository reviewRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody Review review) {
        // if (userService.existsById(review.getUserId()) && movieService.existsById(review.getMovieId().getUserId())) {
            reviewService.addReview(review);
            log.info("리뷰 정보 : {}", review);
            return ResponseEntity.ok("리뷰 달기 성공@");
        // } else {
        //     return ResponseEntity.status(400).body("Invalid user or movie ID");
        // }
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Review>> getReviewsByMovie(@PathVariable("movieId") int movieId) {
        List<Review> reviews = reviewService.getReviewsByMovie(movieId);
        log.info("영화 번호 : {} 에 대한 리뷰", movieId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@RequestParam(value = "userId") String userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        log.info("사용자 : {} 에 대한 리뷰", userId);
        return ResponseEntity.ok(reviews);
    }
}
