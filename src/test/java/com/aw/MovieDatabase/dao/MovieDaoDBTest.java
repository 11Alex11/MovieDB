/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dao;

import com.aw.MovieDatabase.dto.CastMember;
import com.aw.MovieDatabase.dto.Genre;
import com.aw.MovieDatabase.dto.Movie;
import com.aw.MovieDatabase.dto.MovieCastMember;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Alex Wood
 */
@SpringBootTest
public class MovieDaoDBTest {
    @Autowired
    private MovieDao movieDao;
    
    @Autowired
    private CastMemberDao castDao;
    
    @Autowired
    private GenreDao genreDao;
    
    
    public MovieDaoDBTest() {
        
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        List<Genre> genres = genreDao.getGenres();
        for(Genre g: genres){
            genreDao.removeGenreById(g.getId());
        }
        
        List<Movie> movies = movieDao.getAllMovies();
        for(Movie m : movies){
            movieDao.removeMovieById(m.getId());
        }
        
        List<CastMember> castMembers = castDao.getCastMembers();
        for(CastMember c: castMembers){
            castDao.removeCastMemberById(c.getId());
        }
    }
    
    @AfterEach
    public void tearDown() {
                List<Genre> genres = genreDao.getGenres();
        for(Genre g: genres){
            genreDao.removeGenreById(g.getId());
        }
        
        List<Movie> movies = movieDao.getAllMovies();
        for(Movie m : movies){
            movieDao.removeMovieById(m.getId());
        }
        
        List<CastMember> castMembers = castDao.getCastMembers();
        for(CastMember c: castMembers){
            castDao.removeCastMemberById(c.getId());
        }
    }

    @Test
    public void someTests() {
        Genre genre = new Genre();
        genre.setName("Action");
        genre = genreDao.addGenre(genre);
        Genre getGenre = genreDao.getGenreById(genre.getId());
        
        assertTrue(genre.equals(getGenre),"Genres should be equal");
        
        CastMember castMember = new CastMember();
        castMember.setName("Name");
        castMember = castDao.addCastMember(castMember);
        
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        List<MovieCastMember> actors = new ArrayList<>();
        List<MovieCastMember> writers = new ArrayList<>();
        List<MovieCastMember> directors = new ArrayList<>();

        Movie movie = new Movie();
        movie.setTitle("Movie");
        movie.setImageUrl("Test");
         movie.setDescription("Test");
         movie.setPlot("Test");
         movie.setPopularity(1);
         movie.setContentRating("G");
         movie.setGenres(genres);
         movie.setUserRating(1.1f);
         movie.setVideoUrl("Test");
         movie.setImdbId("Test");
        MovieCastMember movieCastMember = new MovieCastMember();
        movieCastMember.setRole("Role");
        movieCastMember.setCastMember(castMember);
        movieCastMember.setRoleRelevance(1);
        actors.add(movieCastMember);
        
        movie.setReleaseDate(LocalDate.now());
        
        
        movie.setActors(actors);
        movie.setWriters(writers);
        movie.setDirectors(directors);
        movie = movieDao.addMovie(movie);
        
        
        Movie getMovie = movieDao.getMovieById(movie.getId());
        
        assertTrue(movie.equals(getMovie),"Movie should be equal");
    }
    
}
