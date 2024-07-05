package com.cine.back.movieList.request;

import java.time.LocalDate;

public record UserRevalueRequest( 
    int movieId,
    String userId
    ) {}
