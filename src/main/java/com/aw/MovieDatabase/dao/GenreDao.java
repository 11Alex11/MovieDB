/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dao;

import com.aw.MovieDatabase.dto.Genre;
import java.util.List;

/**
 *
 * @author Alex Wood
 */
public interface GenreDao {
    Genre getGenreById(int genreId);
    List<Genre> getGenres();
    Genre addGenre(Genre genre);
    void updateGenre(Genre genre);
    void removeGenreById(int genreId); 
    
    Genre getGenreByName(String name);
}
