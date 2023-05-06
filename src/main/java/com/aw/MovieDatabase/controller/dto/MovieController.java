/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.controller.dto;

import com.aw.MovieDatabase.dao.CastMemberDao;
import com.aw.MovieDatabase.dao.GenreDao;
import com.aw.MovieDatabase.dao.MovieDao;
import com.aw.MovieDatabase.dto.CastMember;
import com.aw.MovieDatabase.dto.Genre;
import com.aw.MovieDatabase.dto.Movie;
import com.aw.MovieDatabase.dto.MovieCastMember;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Alex Wood
 */
@Controller
public class MovieController {
    @Autowired
    MovieDao movieDao;
    
    @Autowired
    GenreDao genreDao;
    
    @Autowired
    CastMemberDao castDao;
    
    
    Set<ConstraintViolation<Movie>> violations = new HashSet<>();

    
    @GetMapping("movies")
    public String displayMovies(Model model){
        List<Movie> movies = movieDao.getAllMovies();
        model.addAttribute("movies",movies);
        return "movies";
    }
    
    @GetMapping("movieSearch")
    public String displaySearch(String searchText, Model model){
        List<Movie> movies = movieDao.searchMoviesByTitle(searchText);
        model.addAttribute("movies",movies);
        return "movies";
    }
    
    @GetMapping("addMovie")
    public String addMovie(Model model){
        List<CastMember> castMembers = castDao.getCastMembers();
        List<Genre> genres = genreDao.getGenres();
        model.addAttribute("castMembers",castMembers);
        model.addAttribute("genres",genres);
        model.addAttribute("errors",violations);
        violations = new HashSet<>();
        return "addMovie";
    }
    
    @PostMapping("addMovie")
    public String addMovie(@Valid Movie movie, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes){
        List<Genre> genres = new ArrayList<>();
        List<MovieCastMember> directors = new ArrayList<>();
        List<MovieCastMember> writers = new ArrayList<>();
        List<MovieCastMember> actors = new ArrayList<>();
        String[] genreIds = request.getParameterValues("genreIds");
        String[] castIds = request.getParameterValues("castId");
        String[] roles = request.getParameterValues("castRole");
        String[] relevance = request.getParameterValues("castRelevance");
        
        
        if(castIds!=null){
            for(int i =0; i < castIds.length; i++){
                MovieCastMember member = new MovieCastMember();
                member.setRole(roles[i]);
                if(!relevance[i].isEmpty()){
                    member.setRoleRelevance(Integer.parseInt(relevance[i]));
                }
                member.setMovie(movie);
                member.setCastMember(castDao.getCastMemberById(Integer.parseInt(castIds[i])));
                if(roles[i].toLowerCase().contains("director")){
                    directors.add(member);
                }
                else if(roles[i].toLowerCase().contains("writer")){
                    writers.add(member);
                }
                else{
                    actors.add(member);
                }
            }
        }
        
        /*for(String s : castIds){
            System.out.println(s);
        }
        
        for(String s : roles){
            System.out.println(s);
        }
        for(String s : relevance){
            System.out.println(s);
        }*/
        if(genreIds!=null){
            for(String genreId: genreIds){
                Genre genre = genreDao.getGenreById(Integer.parseInt(genreId));
                genres.add(genre);
            }
        }
        movie.setGenres(genres);
        movie.setDirectors(directors);
        movie.setWriters(writers);
        movie.setActors(actors);
        
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(movie);
        
        if(violations.isEmpty()&&!result.hasErrors()){
            movie = movieDao.addMovie(movie);
            redirectAttributes.addAttribute("id",movie.getId());
            return "redirect:/movieInfo";
        }
        
        
        return "redirect:/addMovie";
        
    }
    
    @GetMapping("movieInfo")
    public String movieInfo(Integer id, Model model){
        Movie movie = movieDao.getMovieById(id);
        model.addAttribute("movie", movie);
        return "movieInfo";
    }
    
    @PostMapping("removeMovie")
    public String removeMovie(Integer id){
        movieDao.removeMovieById(id);
        return "redirect:/movies";
    }
    
    @GetMapping("editMovie")
    public String editMovie(Integer id, Model model){
        Movie movie = movieDao.getMovieById(id);
        List<CastMember> castMembers = castDao.getCastMembers();
        List<Genre> genres = genreDao.getGenres();
        List<MovieCastMember> movieCastMembers = Stream.of(movie.getDirectors(), movie.getWriters(), movie.getActors()).flatMap(Collection::stream).collect(Collectors.toList());
        model.addAttribute("castMembers",castMembers);
        model.addAttribute("movieCastMembers",movieCastMembers);
        
        model.addAttribute("movie",movie);
        model.addAttribute("genres",genres);
        model.addAttribute("castMembers",castMembers);
        
        return "editMovie";
    }
    
    @PostMapping("editMovie")
    public String editMovie(@Valid Movie movie, BindingResult result, Model model, HttpServletRequest request){
        List<Genre> genres = new ArrayList<>();
        List<MovieCastMember> directors = new ArrayList<>();
        List<MovieCastMember> writers = new ArrayList<>();
        List<MovieCastMember> actors = new ArrayList<>();
        String[] genreIds = request.getParameterValues("genreIds");
        String[] castIds = request.getParameterValues("castId");
        String[] roles = request.getParameterValues("castRole");
        String[] relevance = request.getParameterValues("castRelevance");
        
        if(castIds!=null){
            for(int i =0; i < castIds.length; i++){
                MovieCastMember member = new MovieCastMember();
                member.setRole(roles[i]);
                member.setRoleRelevance(Integer.parseInt(relevance[i]));
                member.setMovie(movie);
                member.setCastMember(castDao.getCastMemberById(Integer.parseInt(castIds[i])));
                if(roles[i].toLowerCase().contains("director")){
                    directors.add(member);
                }
                else if(roles[i].toLowerCase().contains("writer")){
                    writers.add(member);
                }
                else{
                    actors.add(member);
                }
            }
        }
        
        if(genreIds!=null){
            for(String genreId: genreIds){
                Genre genre = genreDao.getGenreById(Integer.parseInt(genreId));
                genres.add(genre);
            }
        }
        movie.setGenres(genres);
        movie.setDirectors(directors);
        movie.setWriters(writers);
        movie.setActors(actors);
        
        if(result.hasErrors()){
            model.addAttribute("movie", movie);
            return "editMovie";
        }
        
        movieDao.updateMovie(movie);
        return "redirect:/movieInfo?id="+movie.getId();
    }
    
    
}
