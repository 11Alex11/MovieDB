/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dao;

import com.aw.MovieDatabase.dao.CastMemberDaoDB.CastMemberMapper;
import com.aw.MovieDatabase.dao.GenreDaoDB.GenreMapper;
import com.aw.MovieDatabase.dto.CastMember;
import com.aw.MovieDatabase.dto.Genre;
import com.aw.MovieDatabase.dto.Movie;
import com.aw.MovieDatabase.dto.MovieCastMember;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Alex Wood
 */
@Repository
public class MovieDaoDB implements MovieDao{
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    @Transactional
    public Movie getMovieById(int movieId) {
        String sql = "SELECT * FROM Movie WHERE id = ?;";
        Movie movie;
        try{
            movie =  jdbc.queryForObject(sql,new MovieMapper(),movieId);
        }
        catch(DataAccessException e){
            return null;
        }
        
        addCastMembersToMovie(movie);
        addGenresToMovie(movie);
        return movie;
    }

    @Override
    @Transactional
    public List<Movie> getAllMovies() {
        String sql = "SELECT * FROM Movie;";
        List<Movie> movies =  jdbc.query(sql,new MovieMapper());
        for(Movie m: movies){
            addCastMembersToMovie(m);
            addGenresToMovie(m);
        }
        return movies;
    }

    @Override
    @Transactional
    public Movie addMovie(Movie movie) {
        String sql = "INSERT INTO Movie"
                + "(imdbId, title, description, plot, releaseDate, contentRating, length, userRating, popularity, imageUrl, videoUrl) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?);";
        jdbc.update(sql,movie.getImdbId(), movie.getTitle(), movie.getDescription(), movie.getPlot(),
                movie.getReleaseDate(), movie.getContentRating(), movie.getLength(), movie.getUserRating(),
                movie.getPopularity(), movie.getImageUrl(), movie.getVideoUrl());
        int movieId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        movie.setId(movieId);
        addMovieCastMembers(movie);
        addMovieGenres(movie);
        return movie;
    }
    
    @Override
    @Transactional
    public void updateMovie(Movie movie) {
        String sql = "UPDATE Movie SET imdbId = ?, title = ?, description = ?, plot = ?, releaseDate = ?, "
                + "contentRating = ?, length = ?, userRating = ?, popularity = ?, imageUrl = ?, videoUrl = ? WHERE id = ?;";
        jdbc.update(sql,movie.getImdbId(), movie.getTitle(), movie.getDescription(), movie.getPlot(),
        movie.getReleaseDate(), movie.getContentRating(), movie.getLength(), movie.getUserRating(),
        movie.getPopularity(), movie.getImageUrl(), movie.getVideoUrl(), movie.getId());
        // Update bridge table
        removeMovieCastMembers(movie.getId());
        addMovieCastMembers(movie);
        // Update genre bridge table
        removeMovieGenres(movie.getId());
        addMovieGenres(movie);
    }

    @Override
    @Transactional
    public void removeMovieById(int movieId) {
        String sql = "DELETE FROM Movie WHERE id = ?;";
        //Delete Genres bridge
        removeMovieGenres(movieId);
        // Delete cast members bridge
        removeMovieCastMembers(movieId);
        //Finally remove movie
        jdbc.update(sql,movieId);
    }
    
    
    @Override
    @Transactional
    public List<Movie> getMoviesCastMemberIn(int castId) {
        String sql = "SELECT DISTINCT Movie.* FROM Movie JOIN MovieCastMember ON Movie.id = MovieCastMember.movieId WHERE MovieCastMember.castMemberId = ?;";
        List<Movie> movies = jdbc.query(sql, new MovieMapper(),castId);
        for(Movie m: movies){
            addCastMembersToMovie(m);
            addGenresToMovie(m);
        }
        return movies;
    }
    
    @Override
    @Transactional
    public List<Movie> searchMoviesByTitle(String searchText){
        String sql = "SELECT * FROM Movie WHERE title LIKE ?;";
        List<Movie> movies = jdbc.query(sql, new MovieMapper(),"%" + searchText + "%");
        for(Movie m: movies){
            addCastMembersToMovie(m);
            addGenresToMovie(m);
        }
        return movies;
    }
    
    @Override
    @Transactional
    public List<Movie> getTopUserRatedMovies(int numMovies){
        String sql = "SELECT * FROM Movie ORDER BY userRating DESC LIMIT ?;";
        List<Movie> movies = jdbc.query(sql, new MovieMapper(), numMovies);
        for (Movie m : movies) {
            addCastMembersToMovie(m);
            addGenresToMovie(m);
        }
        return movies;
    }
    
    private void addMovieCastMembers(Movie movie){
        String sql = "INSERT INTO MovieCastMember(movieId, castMemberId, role, roleRelevance) VALUES (?,?,?,?);";
        for(MovieCastMember m: movie.getDirectors()){
            jdbc.update(sql,movie.getId(), m.getCastMember().getId(),m.getRole(),m.getRoleRelevance());
        }
        for(MovieCastMember m: movie.getWriters()){
            jdbc.update(sql,movie.getId(), m.getCastMember().getId(),m.getRole(),m.getRoleRelevance());
        }
        for(MovieCastMember m: movie.getActors()){
            jdbc.update(sql,movie.getId(), m.getCastMember().getId(),m.getRole(),m.getRoleRelevance());
        }
    }
    
    private void addMovieGenres(Movie movie){
        String sql = "INSERT INTO MovieGenre(movieId, genreId) VALUES(?,?);";
        for(Genre g : movie.getGenres()){
            jdbc.update(sql,movie.getId(),g.getId());
        }
    }
    
    private void addCastMembersToMovie(Movie movie){
        String directorsSql = "SELECT MovieCastMember.* FROM MovieCastMember "
                + "JOIN Movie ON Movie.id = MovieCastMember.movieId "
                + "WHERE MovieCastMember.role = 'Director' AND Movie.id = ?;";
        
        String writersSql = "SELECT MovieCastMember.* FROM MovieCastMember "
                + "JOIN Movie ON Movie.id = MovieCastMember.movieId "
                + "WHERE MovieCastMember.role = 'Writer' AND Movie.id = ?;";
        
        String actorsSql = "SELECT MovieCastMember.* FROM MovieCastMember "
                + "JOIN Movie ON Movie.id = MovieCastMember.movieId "
                + "WHERE MovieCastMember.role NOT IN ('Director','Writer') AND Movie.id = ?;";
        
        List<MovieCastMember> directors = jdbc.query(directorsSql, new MovieCastMemberMapper(),movie.getId());
        List<MovieCastMember> writers = jdbc.query(writersSql, new MovieCastMemberMapper(),movie.getId());
        List<MovieCastMember> actors = jdbc.query(actorsSql, new MovieCastMemberMapper(),movie.getId());

        for(MovieCastMember d: directors){
            d.setMovie(movie);
            addCastMemberToMovieCastMember(d);
        }
        for(MovieCastMember w: writers){
            w.setMovie(movie);
            addCastMemberToMovieCastMember(w);
        }
        for(MovieCastMember a: actors){
            a.setMovie(movie);
            addCastMemberToMovieCastMember(a);
        }
        
        movie.setDirectors(directors);
        movie.setWriters(writers);
        movie.setActors(actors);
    }
    
    
    private void removeMovieCastMembers(int movieId){
        String sql = "DELETE FROM MovieCastMember WHERE movieId = ?;";
        jdbc.update(sql, movieId);
    }
    
    private void removeMovieGenres(int movieId){
        String sql = "DELETE FROM MovieGenre WHERE movieId = ?;";
        jdbc.update(sql, movieId);
    }
    
    private void addCastMemberToMovieCastMember(MovieCastMember castMember){
        String sql = "SELECT CastMember.* FROM CastMember JOIN MovieCastMember "
                + "ON MovieCastMember.castMemberId = CastMember.id WHERE MovieCastMember.castMemberId = ? AND MovieCastMember.movieId = ? AND MovieCastMember.role = ?;";
        try{
            
            castMember.setCastMember(jdbc.queryForObject(sql, new CastMemberMapper(),castMember.getCastMember().getId(), castMember.getMovie().getId(),castMember.getRole()));
        }
        catch(DataAccessException e){
            System.out.println("Cast Member data was null!");
        }
        
    }
    
    private void addGenresToMovie(Movie movie){
        String sql = "SELECT Genre.* FROM Genre JOIN MovieGenre ON Genre.id = MovieGenre.genreId "
                + "WHERE MovieGenre.movieId = ?;";
        List<Genre> genres = jdbc.query(sql, new GenreMapper(),movie.getId());
        movie.setGenres(genres);
    }

    @Override
    public Movie getMovieByNameAndReleaseDate(String name, LocalDate release) {
        String sql = " SELECT * FROM Movie WHERE title = ? AND releaseDate = ?;";
        try{
            Movie movie = jdbc.queryForObject(sql, new MovieMapper(), name, release);
            addCastMembersToMovie(movie);
            addGenresToMovie(movie);
            return movie;
        }
        catch(DataAccessException e){
            return null;
        }
    }

    
    public static class MovieMapper implements RowMapper<Movie>{

        @Override
        public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
            Movie movie = new Movie();
            movie.setId(rs.getInt("id"));
            movie.setImdbId(rs.getString("imdbId"));
            movie.setTitle(rs.getString("title"));
            movie.setDescription(rs.getString("description"));
            movie.setPlot(rs.getString("plot"));
            Date date = rs.getDate("releaseDate");
            if(date!=null){
                movie.setReleaseDate(date.toLocalDate());
            }
            else{
                movie.setReleaseDate(null);
            }
            movie.setContentRating(rs.getString("contentRating"));
            movie.setLength(rs.getInt("length"));
            movie.setUserRating(rs.getFloat("userRating"));
            movie.setPopularity(rs.getInt("popularity"));
            movie.setImageUrl(rs.getString("imageUrl"));
            movie.setVideoUrl("videoUrl");
            return movie;
        }
        
    }
    
    public static class MovieCastMemberMapper implements RowMapper<MovieCastMember>{

        @Override
        public MovieCastMember mapRow(ResultSet rs, int rowNum) throws SQLException {
            MovieCastMember castMember = new MovieCastMember();
            CastMember member = new CastMember();
            member.setId(rs.getInt("castMemberId"));
            
            castMember.setCastMember(member);
            castMember.setRole(rs.getString("role"));
            castMember.setRoleRelevance(rs.getInt("roleRelevance"));
           
            return castMember;
        }
        
    }
}
