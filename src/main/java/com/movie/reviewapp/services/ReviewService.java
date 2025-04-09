package com.movie.reviewapp.services;

import com.movie.reviewapp.models.Movie;
import com.movie.reviewapp.models.User;
import com.movie.reviewapp.models.Review;
import com.movie.reviewapp.repositories.MovieRepository;
import com.movie.reviewapp.repositories.UserRepository;
import com.movie.reviewapp.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Review> getReviewsByMovie(Long movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    public Review addReview(Review review) {
        // Get movie and user IDs from the incoming review object
        Long movieId = review.getMovie().getId();
        Long userId = review.getUser().getId();

        // Fetch the actual Movie and User from the database
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + movieId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Assign the full entities before saving
        review.setMovie(movie);
        review.setUser(user);

        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
