/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.controller.dto;

import com.aw.MovieDatabase.dao.GenreDao;
import com.aw.MovieDatabase.dto.Genre;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Alex Wood
 */
@Controller
public class GenreController {
    @Autowired
    JdbcTemplate jdbc;
    
    @Autowired
    GenreDao genreDao;
    
    Set<ConstraintViolation<Genre>> violations = new HashSet<>();
    
    @GetMapping("genres")
    public String displayGenres(Model model){
        List<Genre> genres = genreDao.getGenres();
        model.addAttribute("genres",genres);
        model.addAttribute("errors",violations);
        violations = new HashSet<>();
        return "genres";
    }


    @PostMapping("addGenre")
    public String addGenre(Genre genre){
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(genre);
        Genre findGenre = genreDao.getGenreByName(genre.getName());

        
        if(violations.isEmpty()&&findGenre==null){
            genreDao.addGenre(genre);
        }
        return "redirect:/genres";
    }
    
    @GetMapping("editGenre")
    public String editGenre(Integer id, Model model){
        Genre genre = genreDao.getGenreById(id);
        model.addAttribute("genre",genre);
        return "editGenre";
    }
    
    @PostMapping("editGenre")
    public String editGenre(@Valid Genre genre, BindingResult result, Model model){
        
        if(result.hasErrors()){
            model.addAttribute("genre",genre);
            return "editGenre";
        }
        genreDao.updateGenre(genre);
        return "redirect:/genres";
    }
    
    @PostMapping("removeGenre")
    public String removeGenre(Integer id){
        genreDao.removeGenreById(id);
        return "redirect:/genres";
    }
    
    
    
    
}

