package com.cine.back.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import com.cine.back.movieList.entity.MovieDetailEntity;
import com.cine.back.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "Review_Info")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review")
    private Long reviewNo;

    // 제목, 내용
    @NotNull
    @Column(name = "review_title", length = 500)
    private String reviewTitle;

    @NotNull
    @Column(name = "review_content", length = 3000)
    private String reviewContent;

    // 작성자
    @NotNull
    @Column(name = "user_id", length = 100)
    private String userId;

    // 영화번호
    @Column
    private int movieId;

    // 작성일, 수정일
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;


    // private int rating;
    // private int likes;
    // private int dislikes;
    // private boolean reported;

    // Getters and setters

}
