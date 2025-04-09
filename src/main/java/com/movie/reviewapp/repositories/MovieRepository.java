package com.movie.reviewapp.repositories;

import com.movie.reviewapp.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {}
