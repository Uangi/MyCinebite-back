package com.cine.back.recommendation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cine.back.recommendation.dto.FavoriteRequestDto;
import com.cine.back.recommendation.dto.FavoriteResponseDto;
import com.cine.back.recommendation.entity.UserFavorite;
import com.cine.back.recommendation.repository.UserFavoriteRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserFavoriteService {
    
    @Autowired
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserFavoriteMapper userFavoriteMapper;

    public UserFavoriteService(UserFavoriteRepository userFavoriteRepository, UserFavoriteMapper userFavoriteMapper) {
        this.userFavoriteRepository = userFavoriteRepository;
        this.userFavoriteMapper = userFavoriteMapper;
    }

    @Transactional
    public FavoriteResponseDto addFavorite(FavoriteRequestDto favoriteDto) {
        try {
            UserFavorite userFavorite = userFavoriteMapper.toUserFavorite(favoriteDto);
            Optional<UserFavorite> existingFavorite = userFavoriteRepository.findByUserIdAndMovieId(favoriteDto);
        existingFavorite.ifPresent(existing -> {
            log.info("이미 찜한 영화이기 때문에 취소합니다. / 취소한 영화 : {}", existing);
            userFavoriteRepository.deleteByUserIdAndMovieId(favoriteDto);
            throw new RuntimeException("이미 찜한 영화를 취소하였습니다.");
        });

        if (!existingFavorite.isPresent()) {
            UserFavorite saveFavorite = userFavoriteRepository.save(userFavorite);
            log.info("찜하기 성공 / 찜목록에 추가한 영화 : {}", userFavorite);
            return userFavoriteMapper.toResponseDto(saveFavorite);
        }
    } catch (Exception e) {
        log.error("잘못된 요청 : {}", e);
        throw e;
    }
    throw new RuntimeException("찜하기 처리 중 오류가 발생하였습니다.");
}

    @Transactional
    public void deleteFavorite(int movieId) throws IOException{
        userFavoriteRepository.deleteByMovieId(movieId);
        log.info("찜목록에서 삭제 성공 : {}", movieId);
    }

    public List<FavoriteResponseDto> favoriteList() {
            List<UserFavorite> userFavorites = userFavoriteRepository.findAll();
            List<FavoriteResponseDto> favoriteResponse = userFavoriteMapper.toResponseDtos(userFavorites);
            log.info("찜목록 조회 성공", favoriteResponse.size());
            return favoriteResponse;
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // public void addFavorite(String userId, int movieId) {
    //     try {
    //         if (!userFavoriteRepository.existsByUserIdAndMovieId(userId, movieId)) { // 찜한 상태가 아니라면

    //             UserFavorite userFavorite = new UserFavorite("jyp423",123);
    //             userFavorite.setUserId(userId);
    //             userFavorite.setMovieId(movieId);

    //             userFavoriteRepository.save(userFavorite);

    //             log.info("찜목록에 성공적으로 추가한 영화 : {}", userFavorite);
    //         }
    //     } catch (Exception e) {
    //         log.error("찜목록에 영화 추가 실패 : {}", e);
    //         throw e;
    //     }
    // }
    
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @Transactional
    // public void deleteFavorite(FavoriteRequestDto favoriteDto) throws IOException{
    //     userFavoriteRepository.deleteByUserIdAndMovieId(favoriteDto);
    //     log.info("찜 삭제 성공 : {}", favoriteDto);
    // }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // public Optional<List<UserFavorite>> favoriteList(String userId) {
    //     try {
    //         Optional<List<UserFavorite>> userFavorites = userFavoriteRepository.findByUserId(userId);
    //         log.info("찜목록 조회 성공", userFavorites.get());
    //         return userFavorites;
    //     } catch (Exception e) {
    //         log.error("찜목록 조회 실패 : ", e);
    //         return Optional.empty();
    //     }
    // }
}
