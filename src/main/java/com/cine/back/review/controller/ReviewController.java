// package com.cine.back.review.controller;

// import com.cine.back.review.entity.Review;
// import com.cine.back.review.service.ReviewService;
// import com.cine.back.test.MovieService;
// import com.cine.back.test.UserService;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/reviews")
// public class ReviewController {

//     private final ReviewService reviewService;
//     private final MovieService movieService;
//     private final UserService userService;

//     public ReviewController(ReviewService reviewService, MovieService movieService, UserService userService) {
//         this.reviewService = reviewService;
//         this.movieService = movieService;
//         this.userService = userService;
//     }

//     @PostMapping("/add")
//     public ResponseEntity<String> addReview(@RequestBody Review review) {
//         if (userService.existsById(review.getUser().getId()) && movieService.existsById(review.getMovie().getId())) {
//             reviewService.addReview(review);
//             return ResponseEntity.ok("Review added successfully");
//         } else {
//             return ResponseEntity.status(400).body("Invalid user or movie ID");
//         }
//     }

//     @GetMapping("/movie/{movieId}")
//     public ResponseEntity<List<Review>> getReviewsByMovie(@PathVariable("movieId") Long movieId) {
//         List<Review> reviews = reviewService.getReviewsByMovie(movieId);
//         return ResponseEntity.ok(reviews);
//     }

//     @GetMapping("/user/{userId}")
//     public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable("userId") Long userId) {
//         List<Review> reviews = reviewService.getReviewsByUser(userId);
//         return ResponseEntity.ok(reviews);
//     }
// }
