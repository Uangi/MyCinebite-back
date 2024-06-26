package com.cine.back.board.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cine.back.board.comment.entity.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByPost_PostNo(Long postNo);

}
