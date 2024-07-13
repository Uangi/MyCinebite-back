package com.cine.back.recommendation.service;

import com.cine.back.favorite.entity.UserFavorite;
import com.cine.back.favorite.repository.UserFavoriteRepository;
import com.cine.back.recommendation.dto.RecommendationRequest;
import com.cine.back.movieList.entity.MovieDetailEntity;
import com.cine.back.movieList.repository.MovieDetailRepository;
import com.cine.back.movieList.exception.MovieNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class RecommendationService {

    private final UserFavoriteRepository userFavoriteRepository;
    private final MovieDetailRepository movieDetailRepository;

    /**
     * 추천 영화 목록을 페이지로 반환합니다.
     *
     * @param userId   현재 사용자 ID
     * @param pageable 페이징 정보
     * @return 페이징된 추천 영화 목록
     */
    public Page<RecommendationRequest> recommendMovies(String userId, Pageable pageable) {
        // 현재 사용자의 찜 목록을 가져오기
        List<UserFavorite> currentUserFavorites = userFavoriteRepository.findByUserId(userId).orElse(Collections.emptyList());

        // 현재 사용자의 찜 목록에서 영화 ID 추출
        Set<Integer> currentUserMovieIds = extractMovieIds(currentUserFavorites);

        // 모든 사용자들의 찜 목록을 가져오기
        Map<String, List<UserFavorite>> allUserFavorites = getAllUserFavorites();
        log.info("# [GET][/recommendations] 서비스 - 다른 사용자들의 찜목록 : {} ", allUserFavorites);

        // 현재 사용자와 다른 사용자 간의 찜 목록 유사도 계산
        Map<String, Double> similarityScores = calculateSimilarityScores(userId, currentUserMovieIds, allUserFavorites);

        // 추천 영화 목록 생성
        List<RecommendationRequest> recommendedMovies = generateRecommendations(currentUserMovieIds, similarityScores, allUserFavorites);

        // 추천 영화 목록을 페이지로 변환
        return paginateRecommendations(recommendedMovies, pageable);
    }

    
    // 찜 목록에서 영화 ID만 추출합니다.
    private Set<Integer> extractMovieIds(List<UserFavorite> favorites) {
        return favorites.stream()
                .map(UserFavorite::getMovieId)
                .collect(Collectors.toSet());
    }

    // 모든 사용자들의 찜 목록 조회
    private Map<String, List<UserFavorite>> getAllUserFavorites() {
        List<UserFavorite> allFavorites = userFavoriteRepository.findAll();
        return allFavorites.stream().collect(Collectors.groupingBy(UserFavorite::getUserId));
    }

    /**
     * 현재 사용자와 다른 사용자 간의 유사도 점수를 계산합니다.
     *
     * @param userId              현재 사용자 ID
     * @param currentUserMovieIds 현재 사용자의 영화 ID 세트
     * @param allUserFavorites    모든 사용자들의 찜 목록
     * @return 사용자별 유사도 점수 맵
     */

    private Map<String, Double> calculateSimilarityScores(
        String userId, // 현재 사용자 ID
        Set<Integer> currentUserMovieIds, // 현재 사용자의 영화 ID set
        Map<String, List<UserFavorite>> allUserFavorites) {

        Map<String, Double> similarityScores = new HashMap<>();

        for (Map.Entry<String, List<UserFavorite>> entry : allUserFavorites.entrySet()) {
            String otherUserId = entry.getKey();

            if (!otherUserId.equals(userId)) {
                Set<Integer> otherUserMovieIds = extractMovieIds(entry.getValue());
                double similarity = calculateJaccardSimilarity(currentUserMovieIds, otherUserMovieIds);
                similarityScores.put(otherUserId, similarity);
            }
        }
        return similarityScores;
    }

    /**
     * 추천 영화 목록을 생성합니다.
     *
     * @param currentUserMovieIds 현재 사용자가 찜한 영화 ID 세트
     * @param similarityScores    유사 사용자 유사도 점수 맵
     * @param allUserFavorites    모든 사용자들의 찜 목록
     * @return 추천 영화 목록
     */
    private List<RecommendationRequest> generateRecommendations(
                    Set<Integer> currentUserMovieIds, // 현재 사용자가 찜한 영화 ID 세트
                    Map<String, Double> similarityScores, // 유사 사용자 유사도 점수 맵
                    Map<String, List<UserFavorite>> allUserFavorites)
                    {
        Set<Integer> recommendedMovieIds = new HashSet<>();
        List<RecommendationRequest> recommendedMovies = new ArrayList<>();

        similarityScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5) // 상위 5명의 유사한 사용자들로 제한
                .forEach(entry -> {
                    List<UserFavorite> similarUserFavorites = allUserFavorites.get(entry.getKey());
                    similarUserFavorites.stream()
                            .map(UserFavorite::getMovieId)
                            .filter(movieId -> !currentUserMovieIds.contains(movieId))
                            .filter(movieId -> !recommendedMovieIds.contains(movieId))
                            .map(this::findMovieById) // 영화 정보 가져오기
                            .map(this::convertToDto) // 가져오고 싶은 데이터(DTO)로 변환
                            .forEach(movie -> {
                                recommendedMovies.add(movie);
                                recommendedMovieIds.add(movie.movieId());
                            });
                });

        return recommendedMovies;
    }

    /**
     * 추천 영화 목록을 페이지로 변환합니다.
     *
     * @param recommendedMovies 추천 영화 목록
     * @param pageable          페이징 정보
     * @return 페이징된 추천 영화 목록
     */
    private Page<RecommendationRequest> paginateRecommendations(List<RecommendationRequest> recommendedMovies, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), recommendedMovies.size());
        return new PageImpl<>(recommendedMovies.subList(start, end), pageable, recommendedMovies.size());
    }

    /**
     * Jaccard 유사도를 계산합니다.
     *
     * @param set1 첫 번째 영화 ID 세트
     * @param set2 두 번째 영화 ID 세트
     * @return Jaccard 유사도 점수
     */
    private double calculateJaccardSimilarity(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<Integer> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return (double) intersection.size() / union.size();
    }

    /**
     * 영화 ID로 영화 정보를 조회합니다.
     *
     * @param movieId 영화 ID
     * @return 영화 정보 엔티티
     * @throws MovieNotFoundException 영화 정보를 찾을 수 없는 경우 예외 발생
     */
    private MovieDetailEntity findMovieById(int movieId) {
        // DB 쿼리: 영화 ID로 영화 정보 조회
        return movieDetailRepository.findByMovieId(movieId)
                .orElseThrow(MovieNotFoundException::new);
    }

    /**
     * 영화 엔티티를 DTO로 변환합니다.
     *
     * @param movie 영화 엔티티
     * @return 영화 DTO
     */
    private RecommendationRequest convertToDto(MovieDetailEntity movie) {
        return new RecommendationRequest(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getPosterPath(),
            movie.getTomatoScore()
        );
    }
}
