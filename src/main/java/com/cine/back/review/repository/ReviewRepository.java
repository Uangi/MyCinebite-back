package com.cine.back.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.back.review.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovieId(int movieId);

    List<Review> findByUserId(String userId);
}
