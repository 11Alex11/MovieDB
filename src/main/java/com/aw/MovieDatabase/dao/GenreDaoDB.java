/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dao;

import com.aw.MovieDatabase.dto.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class GenreDaoDB implements GenreDao{
    @Autowired
    private JdbcTemplate jdbc;
    
    
    @Override
    public Genre getGenreById(int genreId) {
        String sql = "SELECT * FROM Genre WHERE id = ?;";
        try{
            return jdbc.queryForObject(sql, new GenreMapper(), genreId);
        }
        catch(DataAccessException e){
            return null;
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM Genre";
        return jdbc.query(sql, new GenreMapper());
    }

    @Override
    @Transactional
    public Genre addGenre(Genre genre) {
        String sql = "INSERT INTO Genre(name) VALUES(?);";
        jdbc.update(sql, genre.getName());
        int genreId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        genre.setId(genreId);
        return genre;
    }

    @Override
    @Transactional
    public void updateGenre(Genre genre) {
        String sql = "UPDATE Genre SET name = ? WHERE id = ? ;";
        jdbc.update(sql, genre.getName(), genre.getId());
    }

    @Override
    @Transactional
    public void removeGenreById(int genreId) {
        String sql = "DELETE FROM Genre WHERE id = ?;";
        // Remove entries from MovieGenre bridge first
        removeMovieGenre(genreId);
        jdbc.update(sql,genreId);
    }
    
    private void removeMovieGenre(int genreId){
        String sql = "DELETE FROM MovieGenre WHERE genreId = ?;";
        jdbc.update(sql,genreId);
    }

    @Override
    public Genre getGenreByName(String name) {
        String sql = "SELECT * FROM Genre WHERE name = ?;";
        try{
            return jdbc.queryForObject(sql, new GenreMapper(), name);
        }
        catch(DataAccessException e){
            return null;
        }
    }
    
    public static class GenreMapper implements RowMapper<Genre>{
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }
        
    }
    
}
