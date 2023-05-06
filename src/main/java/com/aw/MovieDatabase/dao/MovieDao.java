/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dao;

import com.aw.MovieDatabase.dto.Movie;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Alex Wood
 */
public interface MovieDao{
    Movie getMovieById(int movieId);
    List<Movie> getAllMovies();
    Movie addMovie(Movie movie);
    void updateMovie(Movie movie);
    void removeMovieById(int movieId);
    
    Movie getMovieByNameAndReleaseDate(String name, LocalDate release);
    List<Movie> getMoviesCastMemberIn(int castId);
    List<Movie> searchMoviesByTitle(String searchText);
    List<Movie> getTopUserRatedMovies(int numMovies);
}
